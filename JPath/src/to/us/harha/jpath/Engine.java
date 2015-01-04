package to.us.harha.jpath;

import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import to.us.harha.jpath.tracer.Tracer;
import to.us.harha.jpath.util.Logger;
import to.us.harha.jpath.util.TimeUtils;

public class Engine
{
	private int				m_cpu_cores;
	private double			m_frameTime;
	private boolean			m_isRunning;
	private Display			m_display;
	private Tracer			m_tracer;
	private Logger			m_log;
	private ExecutorService	m_eService;

	public Engine(Display display, double frameTime)
	{
		m_cpu_cores = Runtime.getRuntime().availableProcessors();

		m_log = new Logger(this.getClass().getName());
		m_log.printMsg("Engine instance has been started! # of Available CPU Cores: " + m_cpu_cores);

		m_frameTime = 1.0 / frameTime;
		m_isRunning = false;
		m_display = display;
		m_tracer = new Tracer(m_display, 4, m_cpu_cores);
		m_eService = Executors.newFixedThreadPool(m_cpu_cores);

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
					// FPS INFO HERE
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

	private void update(float delta)
	{
		m_tracer.update(delta);
	}

	private void render()
	{
		BufferStrategy bs = m_display.getBufferStrategy();
		if (bs == null)
		{
			m_display.createBufferStrategy(2);
			return;
		}

		m_tracer.incrementSampleCounter();

		// This is just some quick code, everything will be changed and fixed, it's just really late atm
		// and I want to get something that works at least in some way
		if (m_cpu_cores < 4)
		{
			m_tracer.render(m_display);
		} else
		{
			for (int j = 0; j < m_cpu_cores / 2; j++)
			{
				for (int i = 0; i < m_cpu_cores / 2; i++)
				{
					m_tracer.renderPortion(m_display, i, j);
				}
			}
		}

		Graphics g = bs.getDrawGraphics();
		g.drawImage(m_display.getImage(), 0, 0, m_display.getWidth() * m_display.getScale(), m_display.getHeight() * m_display.getScale(), null);
		g.dispose();
		bs.show();
	}

}