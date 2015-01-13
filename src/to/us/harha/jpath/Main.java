package to.us.harha.jpath;

import java.util.Locale;

import to.us.harha.jpath.util.Logger;
import to.us.harha.jpath.util.math.Quaternion;
import to.us.harha.jpath.util.math.Vec3f;

public class Main
{

	// Program window's title String
	public static final String TITLE   = "Path Tracer | 1 = raytracer, 2 = pathtracer, wasd = move";

	// Main EPSILON value, used for floating point value comparison
	public static final float  EPSILON = 1e-3f;

	// Main Display object
	private static Display     display;

	// Main Engine object
	private static Engine      engine;

	// Main Logger object
	public static final Logger LOG     = new Logger(Main.class.getName());

	/*
	 * Main method
	 */
	public static void main(String[] args)
	{
		// Load an existing config file or create one with default values
		Config.initConfig();

		// Create a display object to draw things on
		display = new Display(Config.window_width, Config.window_height, Config.window_scale, TITLE);
		display.create();

		// Create an engine object
		engine = new Engine(display);
		engine.start();
	}
	
	// Testing quaternions
	/*
	public static void main(String[] args)
	{
		Locale.setDefault(new Locale("en", "UK"));

		
		Quaternion q1 = new Quaternion(new Vec3f(0, 1, 0), 22.0f);
		Quaternion q2 = new Quaternion(new Vec3f(1, 0, 0), 45.0f);
		Quaternion q2q1 = Quaternion.mul(q2, q1);
		q2q1 = Quaternion.normalize(q2q1);
		Vec3f v1 = new Vec3f(1.0f, 0.0f, 0.0f);
		Vec3f q2q1v1 = Quaternion.mul(q2q1, v1);
		LOG.printMsg("q1: " + q1.toString() + " Length: " + Quaternion.length(q1));
		LOG.printMsg("q2: " + q2.toString() + " Length: " + Quaternion.length(q2));
		LOG.printMsg("q1 * q2:" + q2q1.toString() + " Length: " + Quaternion.length(q2q1));
		LOG.printMsg("v1: " + v1.toString());
		LOG.printMsg("q2q1v1: " + q2q1v1.toString());
		Quaternion q = new Quaternion(new Vec3f(1.0f, 0.0f, 0.0f), 90.0f);
		Vec3f v = new Vec3f(0.0f, 0.0f, -5.0f);
		Vec3f qResult = Quaternion.mul(q, v);
		LOG.printMsg("qResult: " + qResult.toString());
		*/
		/*
		Quaternion q = new Quaternion().createFromAxisAngle(1, 0, 0, 90);
		Quaternion w = new Quaternion(0.0f, 0.0f, 0.0f, -5.0f);
		Quaternion q_inv = Quaternion.conjugate(q);
		Quaternion result = Quaternion.normalize(Quaternion.mul(Quaternion.mul(q, w), q_inv));

		LOG.printMsg(result.toString() + " " + Quaternion.length(result));
	}
	*/

}
