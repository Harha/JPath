package to.us.harha.jpath;

import java.util.Random;

public class Main
{

	public static final String	TITLE	= "Path Tracer";
	public static final int		WIDTH	= 1280 / 4;
	public static final int		HEIGHT	= 720 / 4;
	public static final int		SCALE	= 2;

	public static final float	EPSILON	= 1e-3f;

	private static Display		display;
	private static Engine		engine;

	public static final Random	RNG		= new Random();

	public static void main(String[] args)
	{
		display = new Display(WIDTH, HEIGHT, SCALE, TITLE);
		display.create();
		engine = new Engine(display, 5000.0);
		engine.start();
	}

}
