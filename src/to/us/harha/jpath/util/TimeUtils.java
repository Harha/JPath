package to.us.harha.jpath.util;

public class TimeUtils
{

	public static final long g_second  = 1000000L;
	public static final long g_frame   = 1000L;
	public static final long g_timeres = 1L;

	private static long      g_currentTime;
	private static long      g_lastTime;
	private static int       g_time;
	private static double    g_delta;
	private static double    g_fps;

	public static void init()
	{
		g_lastTime = getTime();
		updateDelta();
	}

	public static void updateDelta()
	{
		g_currentTime = getTime();
		g_delta = g_delta * 0.9 + (double) (g_currentTime - g_lastTime) * 0.1;
		g_lastTime = g_currentTime;
	}

	public static void updateFPS()
	{
		g_time++;

		if (g_time >= g_timeres)
		{
			if (Double.isInfinite(g_fps))
				g_fps = 0.0;
			g_fps = g_fps * 0.9 + (g_timeres / g_delta) * 0.1;
			g_time -= g_timeres;
		}
	}

	public static long getTime()
	{
		return System.nanoTime() / g_second;
	}

	public static double getDelta()
	{
		return g_delta;
	}

	public static double getFPS()
	{
		return g_frame * g_fps;
	}

}
