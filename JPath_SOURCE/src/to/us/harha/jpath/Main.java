package to.us.harha.jpath;

import to.us.harha.jpath.util.Logger;

public class Main
{

	public static final String TITLE   = "Path Tracer | 1 = raytracer, 2 = pathtracer, wasd = move";
	public static final int    WIDTH   = 512 / 4;
	public static final int    HEIGHT  = 512 / 4;
	public static final int    SCALE   = 4;
	public static final float  EPSILON = 1e-3f;

	private static Display     display;
	private static Engine      engine;

	public static final Logger LOG     = new Logger(Main.class.getName());

	public static void main(String[] args)
	{
		Config.initConfig();

		display = new Display(WIDTH, HEIGHT, SCALE, TITLE);
		display.create();
		engine = new Engine(display);
		engine.start();
	}

}
