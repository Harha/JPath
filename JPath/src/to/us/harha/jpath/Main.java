package to.us.harha.jpath;

import to.us.harha.jpath.util.Logger;

public class Main
{

	public static final String TITLE   = "Path Tracer";
	public static final int    WIDTH   = 1280 / 2;
	public static final int    HEIGHT  = 720 / 2;
	public static final int    SCALE   = 1;
	public static final float  EPSILON = 1e-3f;

	private static Display     display;
	private static Engine      engine;

	public static final Logger LOG     = new Logger(Main.class.getName());

	public static void main(String[] args)
	{
		display = new Display(WIDTH, HEIGHT, SCALE, TITLE);
		display.create();
		engine = new Engine(display, 8, 1000.0, false, 4);
		engine.start();
	}

}
