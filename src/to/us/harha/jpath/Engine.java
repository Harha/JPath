package to.us.harha.jpath;

import java.awt.Color;
import java.awt.Font;
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
	// Engine variables and objects
	private double                   m_frameTime;
	private boolean                  m_isRunning;
	private boolean                  m_raytrace_enabled;
	private Display                  m_display;
	private Logger                   m_log;
	private Tracer                   m_tracer;
	private Input                    m_input;
	private Font                     m_font;

	// Multithreading
	private int                      m_cpu_cores;
	private int                      m_thread_amount;
	private ScheduledExecutorService m_eService;
	private boolean[]                m_executors_finished;

	/*
	 * Engine constructor
	 * Display display: The chosen display
	 */
	public Engine(Display display)
	{
		m_isRunning = false;
		m_raytrace_enabled = true;
		m_log = new Logger(this.getClass().getName());
		m_display = display;
		m_frameTime = 1.0 / Config.max_frames_per_second;
		m_cpu_cores = Runtime.getRuntime().availableProcessors();
		m_thread_amount = ((Config.mt_amount < 0) ? m_cpu_cores : Config.mt_amount);

		m_log.printMsg("# of Available CPU Cores: " + m_cpu_cores + " | Using a maximum of " + m_thread_amount + " threads for rendering.");

		// Create the executor for each thread
		m_eService = Executors.newScheduledThreadPool(m_thread_amount);

		if (m_thread_amount >= 2)
			m_executors_finished = new boolean[(m_thread_amount / 2) * (m_thread_amount / 2)];
		else
			m_executors_finished = new boolean[1];

		Arrays.fill(m_executors_finished, true);

		// Create input stuff
		m_input = new Input();
		m_display.addKeyListener(m_input);

		// Create random stuff
		m_font = new Font("Terminus", Font.BOLD, 12);

		// Create the final tracer object
		m_tracer = new Tracer(m_thread_amount, m_display.getWidth() * m_display.getHeight());
	}

	/*
	 * Start the program
	 */
	public void start()
	{
		if (m_isRunning)
			return;

		m_log.printMsg("Engine instance has been started!");
		m_isRunning = true;
		run();
	}

	/*
	 * Stop the program
	 */
	public void stop()
	{
		if (!m_isRunning)
			return;

		m_log.printMsg("Engine instance has stopped!");
		m_isRunning = false;
		m_eService.shutdown();
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
			// If saving the image is enabled and we have gathered enough samples, save the image and close the program
			if (m_tracer.getSamplesPerPixel(0) > Config.max_samples_per_pixel && Config.saving_enabled)
				stop();

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

		// Save the final rendered image
		m_display.saveBitmapToFile("JPathRender_SPP" + Config.max_samples_per_pixel + "_SS_" + Config.ss_enabled + "_SSAMOUNT_" + Config.ss_amount);
		System.exit(0);
	}

	/*
	 * Main update method
	 */
	private void update(float delta)
	{
		m_tracer.update(delta, m_input);

		if (m_input.getKey(Input.KEY_1))
		{
			m_tracer.clearSamples();
			m_raytrace_enabled = true;
		} else if (m_input.getKey(Input.KEY_2))
		{
			m_tracer.clearSamples();
			m_raytrace_enabled = false;
		}

		if (m_input.getKey(Input.KEY_3))
		{
			Config.debug_enabled = (Config.debug_enabled == false) ? true : false;
		}
	}

	/*
	 * Main render method
	 */
	private void render()
	{
		BufferStrategy bs = m_display.getBufferStrategy();

		if (bs == null)
		{
			m_display.createBufferStrategy(3);
			return;
		}

		if (m_raytrace_enabled == false)
		{
			// Only use multithreaded rendering if the amount of CPU cores is greater than 4
			// This could and will be improved, my current algorithms just won't split the screen
			// correctly for lower than 4 cores
			if (m_thread_amount >= 4)
			{
				// Get the state of all executor threads, only continue rendering if they are all finished
				if (getExecutorsState() == true)
				{
					// Iterate through the horizontal column of cells
					for (int j = 0; j < m_thread_amount / 2; j++)
					{
						// Iterate through the vertical row of cells
						for (int i = 0; i < m_thread_amount / 2; i++)
						{
							int x = i;
							int y = j;

							// Get the 1D index of the current chosen cell
							int t = i + j * (m_thread_amount / 2);

							// Execute a render task with a thread for a chosen cell
							m_eService.execute(new Runnable()
							{
								@Override
								public void run()
								{
									setExecutorState(t, false);
									m_tracer.renderMultiThreaded(m_display, x, y);
									setExecutorState(t, true);
								}
							});
						}
					}
				}
			} else
			{
				m_tracer.renderSingleThreaded(m_display);
			}
		} else
		{
			m_tracer.renderRayTraced(m_display);
		}

		Graphics g = bs.getDrawGraphics();
		g.drawImage(m_display.getImage(), 0, 0, m_display.getWidth() * m_display.getScale(), m_display.getHeight() * m_display.getScale(), null);

		// Render debug information if Config.debug_enabled is true
		if (Config.debug_enabled)
		{
			g.setColor(Color.WHITE);
			g.setFont(m_font);
			g.drawString("Camera Rotation: " + m_tracer.getCurrentCamera().getRot().toString(), 16, 16);
			g.drawString("Camera Forward: " + m_tracer.getCurrentCamera().getRot().getForwardVector().toString(), 16, 32);
			g.drawString("Camera Right: " + m_tracer.getCurrentCamera().getRot().getRightVector().toString(), 16, 48);
			g.drawString("Camera Up: " + m_tracer.getCurrentCamera().getRot().getUpVector().toString(), 16, 64);
			g.drawString("Camera Position: " + m_tracer.getCurrentCamera().getPos().toString(), 16, 80);
		}

		g.dispose();
		bs.show();
	}

	/*
	 * Set the state of a cell @ index
	 */
	public void setExecutorState(int index, boolean state)
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