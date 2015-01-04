package to.us.harha.jpath;

import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import to.us.harha.jpath.tracer.Tracer;
import to.us.harha.jpath.util.Logger;
import to.us.harha.jpath.util.TimeUtils;

public class Engine
{
    private int                      m_cpu_cores;
    private double                   m_frameTime;
    private boolean                  m_debug;
    private boolean                  m_isRunning;
    private Display                  m_display;
    private Tracer                   m_tracer;
    private Logger                   m_log;
    private ScheduledExecutorService m_eService;
    private boolean[]                m_executors_finished;

    /*
     * Engine constructor
     * Display display: The chosen display
     * double frameTime: Maximum frames per second
     */
    public Engine(Display display, double frameTime, boolean debug)
    {
        m_cpu_cores = Runtime.getRuntime().availableProcessors() * 2;
        m_log = new Logger(this.getClass().getName());
        m_log.printMsg("Engine instance has been started! # of Available CPU Cores: " + m_cpu_cores);
        m_frameTime = 1.0 / frameTime;
        m_debug = debug;
        m_isRunning = false;
        m_display = display;
        m_tracer = new Tracer(m_display, 2, m_cpu_cores, m_debug);
        m_eService = Executors.newScheduledThreadPool(m_cpu_cores);
        if (m_cpu_cores >= 2)
            m_executors_finished = new boolean[(m_cpu_cores / 2) * (m_cpu_cores / 2)];
        else
            m_executors_finished = new boolean[1];
        Arrays.fill(m_executors_finished, true);
    }

    public void start()
    {
        if (m_isRunning)
            return;

        m_isRunning = true;
        run();
    }

    public void stop()
    {
        if (!m_isRunning)
            return;

        m_isRunning = false;
    }

    /*
     * Main run loop
     */
    private void run()
    {
        int frames = 0;
        double frameCounter = 0;

        double lastTime = TimeUtils.getTime();
        double unprocessedTime = 0.0;

        while (m_isRunning)
        {
            boolean render = false;

            double startTime = TimeUtils.getTime();
            double passedTime = startTime - lastTime;
            lastTime = startTime;

            unprocessedTime += passedTime;
            frameCounter += passedTime;

            while (unprocessedTime > m_frameTime)
            {
                render = true;
                unprocessedTime -= m_frameTime;

                update((float) m_frameTime);

                if (frameCounter >= 1.0)
                {
                    String eServiceInfo = m_eService.toString().replace("java.util.concurrent.ScheduledThreadPoolExecutor@", "ThreadExecutor @ ");
                    String cellInfo = m_tracer.getSamplesPerPixel().toString();
                    m_log.printMsg("# of samples taken / px per cell: " + cellInfo);
                    m_log.printMsg(eServiceInfo);
                    frames = 0;
                    frameCounter = 0;
                }
            }

            if (render)
            {
                render();
                frames++;
            } else
            {
                try
                {
                    Thread.sleep(1);
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    /*
     * Main update method
     */
    private void update(float delta)
    {
        m_tracer.update(delta);
    }

    /*
     * Main render method
     */
    private void render()
    {
        BufferStrategy bs = m_display.getBufferStrategy();
        if (bs == null)
        {
            m_display.createBufferStrategy(2);
            return;
        }

        // Only use multithreaded rendering if the amount of CPU cores is greater than 4
        // This could and will be improved, my current algorithms just won't split the screen
        // correctly for lower than 4 cores
        if (m_cpu_cores >= 4)
        {
            // Get the state of all executor threads, only continue rendering if they are all finished
            if (getExecutorsState() == true)
            {
                // Iterate through the horizontal column of cells
                for (int j = 0; j < m_cpu_cores / 2; j++)
                {
                    // Iterate through the vertical row of cells
                    for (int i = 0; i < m_cpu_cores / 2; i++)
                    {
                        int x = i;
                        int y = j;

                        // Get the 1D index of the current chosen cell
                        int t = i + j * (m_cpu_cores / 2);

                        // Execute a render task with a thread for a chosen cell
                        m_eService.execute(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                setExecutorState(t, false);
                                m_tracer.renderPortion(m_display, x, y);
                                setExecutorState(t, true);
                            }
                        });
                    }
                }
            }
        } else
        {
            m_tracer.render(m_display);
        }

        Graphics g = bs.getDrawGraphics();
        g.drawImage(m_display.getImage(), 0, 0, m_display.getWidth() * m_display.getScale(), m_display.getHeight() * m_display.getScale(), null);
        g.dispose();
        bs.show();
    }

    /*
     * Set the state of a cell @ index
     */
    public synchronized void setExecutorState(int index, boolean state)
    {
        m_executors_finished[index] = state;
    }

    /*
     * Get the state of a cell @ index
     */
    public boolean getExecutorState(int index)
    {
        return m_executors_finished[index];
    }

    /*
     * Get the state of all cells as a whole
     * Return true if all cells have been rendered
     * Otherwise, return false
     */
    public boolean getExecutorsState()
    {
        for (boolean b : m_executors_finished)
            if (!b)
                return false;
        return true;
    }

}