package to.us.harha.jpath;

import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.util.concurrent.atomic.AtomicInteger;

import to.us.harha.jpath.tracer.Tracer;
import to.us.harha.jpath.util.Logger;
import to.us.harha.jpath.util.TimeUtils;

public class Engine
{
	private double				m_frameTime;
	private boolean				m_isRunning;
	private Display				m_display;
	private Tracer				m_tracer;

	public static final Logger	LOG_CORE	= new Logger(Engine.class.getName());

	public Engine(Display display, double frameTime)
	{
		m_frameTime = 1.0 / frameTime;
		m_isRunning = false;
		m_display = display;
		m_tracer = new Tracer(4);
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
					LOG_CORE.printMsg("FPS: " + frames + " | # of Screens sampled: " + m_tracer.SAMPLESTAKEN.get() + " | Samples taken: " + m_tracer.SAMPLESTAKEN.get() * (m_display.getWidth() * m_display.getHeight()));
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

		m_tracer.render(m_display);

		Graphics g = bs.getDrawGraphics();
		g.drawImage(m_display.getImage(), 0, 0, m_display.getWidth() * m_display.getScale(), m_display.getHeight() * m_display.getScale(), null);
		g.dispose();
		bs.show();
	}

}