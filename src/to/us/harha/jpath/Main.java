package to.us.harha.jpath;

import java.util.Locale;

import to.us.harha.jpath.util.Logger;

public class Main
{

	// Program window's title String
	public static final String TITLE   = "JPath";

	// Main EPSILON value, used for floating point value comparison
	public static final float  EPSILON = 1e-3f;

	// Main Display object
	private static Display     display;

	// Main Engine object
	private static Engine      engine;

	// Main's global Logger object
	public static final Logger LOG     = new Logger(Main.class.getName());

	/*
	 * Main method
	 */
	public static void main(String[] args)
	{
		// Set the locale to en, UK - because I just prefer it
		Locale.setDefault(new Locale("en", "UK"));

		// Load an existing config file or create one with default values
		Config.initConfig();

		// Create a display object to draw things on
		display = new Display(Config.window_width, Config.window_height, Config.window_scale, TITLE);
		display.create();

		// Create an engine object
		engine = new Engine(display);
		engine.start();
	}

}
