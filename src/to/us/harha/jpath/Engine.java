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
	private Logger                   m_log;
	private boolean                  m_isRunning;
	private Display                  m_display;
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
		m_log = new Logger(this.getClass().getName());
		m_isRunning = false;
		m_display = display;
		m_cpu_cores = Runtime.getRuntime().availableProcessors();
		m_thread_amount = ((Config.mt_amount <= 0) ? m_cpu_cores : Config.mt_amount);

		m_log.printMsg("# of Available CPU Cores: " + m_cpu_cores + " | Using a maximum of " + m_thread_amount + " threads for rendering.");

		// Create the executor for each thread
		m_eService = Executors.newScheduledThreadPool(m_thread_amount);
		m_executors_finished = new boolean[(m_thread_amount) * (m_thread_amount)];
		Arrays.fill(m_executors_finished, true);

		// Initialize other important stuff...
		m_input = new Input();
		m_display.addKeyListener(m_input);
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

		// Initialize TimeUtils to calculate FPS and delta-time accurately
		TimeUtils.init();
		TimeUtils.updateDelta();
		TimeUtils.updateFPS();

		while (m_isRunning)
		{
			// If saving the image is enabled and we have gathered enough samples, save the image and close the program
			if (m_tracer.getSamplesPerPixel(0) > Config.max_samples_per_pixel && Config.saving_enabled)
				stop();

			// Update delta and fps
			TimeUtils.updateDelta();
			TimeUtils.updateFPS();

			// Update info on the window title
			m_display.setTitle(String.format("Workers: %02d Samples taken/px: %02d DeltaTime: %2.2f FPS: %2.2f/s Eye: %s Eye_length: %2.2f", m_thread_amount, m_tracer.getSamplesPerPixel(0), TimeUtils.getDelta(), TimeUtils.getFPS(), m_tracer.getCurrentCamera().getRot().toString(), m_tracer.getCurrentCamera().getRot().length()));

			update((float) TimeUtils.getDelta());
			render();
			m_display.render();
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
		}
		
		if (m_input.getKey(Input.KEY_3))
		{
			Config.debug_enabled = (Config.debug_enabled == false) ? true : false;
		}

		if (m_tracer.getCurrentCamera().isMoving() && m_tracer.getSamplesPerPixel(0) > 4)
		{
			m_tracer.clearSamples();
		}
	}

	/*
	 * Main render method
	 */
	private void render()
	{
		// Get the state of all executor threads, only continue rendering if they are all finished
		if (getExecutorsState() == true)
		{
			// Divide the horizontal and vertical cell amount correctly
			int divider = ((2 % m_thread_amount) == 2 ? 2 : 1);

			// Iterate through the horizontal column of cells
			for (int j = 0; j < m_thread_amount; j++)
			{
				// Iterate through the vertical row of cells
				for (int i = 0; i < m_thread_amount; i++)
				{
					int x = i;
					int y = j;

					// Get the 1D index of the current chosen cell
					int t = i + j * m_thread_amount;

					// Execute a render task with a thread for a chosen cell
					m_eService.execute(new Runnable()
					{
						@Override
						public void run()
						{
							setExecutorState(t, false);
							m_tracer.render(m_display, x, y);
							setExecutorState(t, true);
						}
					});
				}
			}
		}
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