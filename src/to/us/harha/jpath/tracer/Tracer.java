package to.us.harha.jpath.tracer;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicIntegerArray;

import to.us.harha.jpath.Config;
import to.us.harha.jpath.Display;
import to.us.harha.jpath.Input;
import to.us.harha.jpath.Main;
import to.us.harha.jpath.tracer.object.Material;
import to.us.harha.jpath.tracer.object.TracerObject;
import to.us.harha.jpath.util.Logger;
import to.us.harha.jpath.util.MathUtils;
import to.us.harha.jpath.util.math.Intersection;
import to.us.harha.jpath.util.math.Primitive;
import to.us.harha.jpath.util.math.Ray;
import to.us.harha.jpath.util.math.Vec3f;

public class Tracer
{
	// Tracer variables and objects
	private Vec3f[]            m_samples;
	private Scene              m_scene;
	private Camera             m_camera;
	private Logger             m_log;
	private int                m_cameras;
	private int                m_camera_selected;

	// Multithreading
	private int                m_thread_amount;
	private AtomicIntegerArray m_samples_taken;

	// Static constant objects to minimize object creation during tracing
	private static final Vec3f COLOR_BLACK   = new Vec3f();
	private static final Vec3f COLOR_AMBIENT = new Vec3f(0.05f);
	private static final Vec3f COLOR_DEBUG   = new Vec3f(1.0f, 0.0f, 1.0f);

	public Tracer(int thread_amount, int resolution)
	{
		m_log = new Logger(this.getClass().getName());
		m_thread_amount = thread_amount;
		m_samples = new Vec3f[resolution];

		// Create the per pixel sample counter for each segment on the screen
		if (m_thread_amount > 1)
			m_samples_taken = new AtomicIntegerArray((m_thread_amount / 2) * (m_thread_amount / 2));
		else
			m_samples_taken = new AtomicIntegerArray(1);

		// Initially clear all samples
		clearSamples();

		// Initialize the scene and use the camera @ id 0 from the scene object
		m_scene = new Scene();
		m_cameras = m_scene.getCameras().size();
		m_camera_selected = 0;
		m_camera = m_scene.getCameras().get(m_camera_selected);

		m_log.printMsg("Tracer instance has been initalized, using " + m_thread_amount + " threads!");
		m_log.printMsg("The current scene has " + m_cameras + " cameras.");
	}

	/*
	 * Update the tracer
	 */
	public void update(float delta, Input input)
	{
		if (input.getKey(Input.KEY_PLUS))
		{
			if (m_camera_selected < m_cameras - 1)
			{
				m_camera_selected += 1;
				m_camera = m_scene.getCameras().get(m_camera_selected);
			}
		} else if (input.getKey(Input.KEY_MINUS))
		{
			if (m_camera_selected > 0)
			{
				m_camera_selected -= 1;
				m_camera = m_scene.getCameras().get(m_camera_selected);
			}
		}
		m_camera.update(delta, input);
	}

	/*
	 * Render the whole screen at once using simple raytracing
	 * For single-threaded rendering
	 */
	public void renderRayTraced(Display display)
	{
		for (int y = 0; y < display.getHeight(); y++)
		{
			for (int x = 0; x < display.getWidth(); x++)
			{
				// Calculate the primary ray
				Ray ray = Ray.calcCameraRay(m_camera, display.getWidth(), display.getHeight(), display.getAR(), x, y);

				// Do the ray tracing
				Vec3f color_raytraced = rayTrace(ray, 0);

				// Draw the pixel
				display.drawPixelVec3f(x, y, color_raytraced);
			}
		}
	}

	/*
	 * Render the whole screen at once
	 * For single-threaded rendering
	 */
	public void renderSingleThreaded(Display display)
	{
		incrementSampleCounter(0);

		for (int y = 0; y < display.getHeight(); y++)
		{
			for (int x = 0; x < display.getWidth(); x++)
			{
				int index = x + y * display.getWidth();

				// Calculate the primary ray
				Ray ray = Ray.calcCameraRay(m_camera, display.getWidth(), display.getHeight(), display.getAR(), x, y);

				// Do the path tracing
				m_samples[index] = m_samples[index].add(pathTrace(ray, 0, 1.0f));

				// Draw the pixel
				display.drawPixelVec3fAveraged(index, m_samples[index], m_samples_taken.get(0));
			}
		}
	}

	/*
	 * Render a chosen portion of the screen @ t1, t2
	 * The size of one portion is (m_cpu_cores)^2
	 * For multi-threaded rendering
	 */
	public void renderMultiThreaded(Display display, int t1, int t2)
	{
		int t = t1 + t2 * (m_thread_amount / 2);

		incrementSampleCounter(t);

		if (t1 >= (m_thread_amount / 2))
			t1 = (m_thread_amount / 2) - 1;
		if (t2 >= (m_thread_amount / 2))
			t2 = (m_thread_amount / 2) - 1;

		float width = display.getWidth();
		float height = display.getHeight();
		int width_portion = display.getWidth() / (m_thread_amount / 2);
		int height_portion = display.getHeight() / (m_thread_amount / 2);

		for (int y = height_portion * t2; y < (height_portion * t2) + height_portion; y++)
		{
			for (int x = width_portion * t1; x < (width_portion * t1) + width_portion; x++)
			{
				int xx = x - width_portion * t1;
				int yy = y - height_portion * t2;
				int index_screen = x + y * display.getWidth();
				int index_sample = xx + yy * width_portion;

				// Supersample each pixel if demanded
				if (Config.ss_enabled)
				{
					Vec3f sample = COLOR_BLACK;

					// Sample the pixels n times
					for (int i = 0; i < Config.ss_amount; i++)
					{
						// Calculate the randomized primary ray
						Ray ray = Ray.calcSupersampledCameraRay(m_camera, display.getWidth(), display.getHeight(), display.getAR(), x, y, Config.ss_jitter);

						// Do the path tracing
						sample = sample.add(pathTrace(ray, 0, 1.0f));
					}

					// Get the average color of the sample
					Vec3f sample_averaged = sample.divide(Config.ss_amount);

					// Add the averaged sample to the samples
					m_samples[index_screen] = m_samples[index_screen].add(sample_averaged);

				} else
				{
					// Calculate the primary ray
					Ray ray = Ray.calcCameraRay(m_camera, display.getWidth(), display.getHeight(), display.getAR(), x, y);

					// Do the path tracing
					m_samples[index_screen] = m_samples[index_screen].add(pathTrace(ray, 0, 1.0f));
				}

				// Draw the pixel
				display.drawPixelVec3fAveraged(index_screen, m_samples[index_screen], m_samples_taken.get(t));

				// Draw lines to separate each section, for debugging purposes
				if (Config.debug_enabled)
				{
					if (xx == 0 || xx == width_portion || yy == 0 || yy == width_portion)
					{
						display.drawPixelVec3f(x, y, COLOR_DEBUG);
					}
				}
			}
		}
	}

	/*
	 * Path tracing
	 * n = recursion level
	 */
	public Vec3f pathTrace(Ray ray, int n, float weight)
	{
		// Return if the max recursion depth is exceeded
		if (n > Config.max_recursion)
		{
			return COLOR_BLACK;
		}

		// Initialize some objects and variables required
		Intersection xInit = null;
		Intersection xFinal = null;
		TracerObject O = null;
		float t_init = Float.MAX_VALUE;

		// Find the intersection
		for (TracerObject o : m_scene.getObjects())
		{
			for (Primitive p : o.getPrimitives())
			{
				xInit = p.intersect(ray);
				if (xInit != null && xInit.getT() < t_init)
				{
					xFinal = xInit;
					t_init = xFinal.getT();
					O = o;
				}
			}
		}

		// Return if no intersection happened
		if (xFinal == null)
			return COLOR_BLACK;

		// If the ray hit a purely emissive surface, return the emittance
		if (O.getMaterial().getEmittance().length() > 0.0f && xFinal.getT() > Main.EPSILON)
			return O.getMaterial().getEmittance().scale(weight);

		// Store the required data into temp objects
		Material M = O.getMaterial();
		Vec3f P = xFinal.getPos();
		Vec3f N = xFinal.getNorm();
		Vec3f RO = ray.getPos();
		Vec3f RD = ray.getDir();

		// Initialize the total color vector
		Vec3f color = new Vec3f();

		// Calculate the russian roulette probabilities
		float Kd = M.getReflectance().length();
		float Ks = M.getReflectivity();
		float Pp = Kd / (Kd + Ks);
		float Pr = ThreadLocalRandom.current().nextFloat();

		// Refractive BRDF
		if (M.getRefractivity() > 0.0f)
		{
			weight *= M.getRefractivity();
			Ray refractedRay = new Ray(P, RD.refract(N, 1.0f, M.getIndexOfRefraction()).normalize());
			color = color.add(pathTrace(refractedRay, n + 1, weight));
		}

		if (Pr < Pp && Kd + Ks != 0.0f)
		// Choose diffuse BRDF with probability Pp
		{
			// Diffuse BRDF
			if (M.getReflectance().length() > 0.0f)
			{
				Ray randomRay = new Ray(P, N.randomHemisphere());
				float NdotRD = Math.abs(N.dot(randomRay.getDir()));
				Vec3f BRDF = M.getReflectance().scale(2.0f * NdotRD);
				Vec3f REFLECTED = pathTrace(randomRay, n + 1, weight);
				color = color.add(BRDF.scale(REFLECTED));
			}
		} else
		// Choose reflective BRDF with probability 1 - Pp
		{
			// Reflective BRDF
			if (M.getReflectivity() > 0.0f)
			{
				weight *= M.getReflectivity();
				Ray reflectedRay = new Ray(P, RD.reflect(N).normalize());
				color = color.add(pathTrace(reflectedRay, n + 1, weight));
			}
		}

		return MathUtils.clamp(color, 0.0f, Config.min_radiance);
	}

	/*
	 * Simple raytracing, no shading or anything
	 * Just for navigation, it's still slow but at least "real-time"
	 */
	public Vec3f rayTrace(Ray ray, int n)
	{
		// Return black if recursion goes over 3 iterations
		if (n > 3)
			return COLOR_BLACK;

		// Initialize some objects and variables
		Intersection iSection = null;
		Intersection iSectionFinal = null;
		TracerObject OBJECT = null;
		float t_init = Float.MAX_VALUE;

		// Intersect the initial ray against all scene objects and find the closest interestection to the ray origin
		for (TracerObject o : m_scene.getObjects())
		{
			for (Primitive p : o.getPrimitives())
			{
				iSection = p.intersect(ray);
				if (iSection != null)
				{
					if (iSection.getT() < t_init)
					{
						iSectionFinal = iSection;
						t_init = iSection.getT();
						OBJECT = o;
					}
				}
			}
		}

		// If no intersection happened at all, return black
		if (iSectionFinal == null)
			return COLOR_BLACK;

		// Get the object's surface material
		Material M = OBJECT.getMaterial();

		// If the object is a light source, return it's emittance
		if (M.getEmittance().length() > 0.0f && iSectionFinal.getT() > Main.EPSILON)
			return MathUtils.clamp(M.getEmittance(), 0.0f, 1.0f);

		// Get the intersection's info
		Vec3f P = iSectionFinal.getPos();
		Vec3f N = iSectionFinal.getNorm();

		// Get the info about the ray
		Vec3f RO = ray.getPos();
		Vec3f RD = ray.getDir();

		// Initialize the final color which will be returned in the end
		Vec3f color_final = new Vec3f();

		// Reflect
		if (M.getReflectivity() > 0.0f)
		{
			color_final = color_final.add(rayTrace(new Ray(iSectionFinal.getPos(), RD.reflect(N).normalize()), n + 1).scale(M.getReflectivity()));
		}

		// Refract
		if (M.getRefractivity() > 0.0f)
		{
			color_final = color_final.add(rayTrace(new Ray(iSectionFinal.getPos(), RD.refract(N, 1.0f, M.getIndexOfRefraction()).normalize()), n + 1).scale(M.getRefractivity()));
		}

		// Diffuse objects
		if (M.getReflectance().length() > 0.0f)
			color_final = color_final.add(M.getReflectance().scale(0.75f));

		// Clamp the final color
		return MathUtils.clamp(color_final, 0.0f, 1.0f);
	}

	/*
	 * Set the camera @ index to the primary camera
	 */
	public void setCamera(int index)
	{
		if (m_scene.getCameras().get(index) != null)
			m_camera = m_scene.getCameras().get(index);
	}

	/*
	 * Clear all taken samples by setting their value to [0.0, 0.0, 0.0]
	 */
	public void clearSamples()
	{
		Arrays.fill(m_samples, new Vec3f());
		for (int i = 0; i < m_samples_taken.length(); i++)
			m_samples_taken.set(i, 0);
	}

	/*
	 * Increment a chosen sample counter by 1 @ index
	 */
	public void incrementSampleCounter(int index)
	{
		m_samples_taken.incrementAndGet(index);
	}

	/*
	 * Get the current chosen camera object
	 */
	public Camera getCurrentCamera()
	{
		return m_camera;
	}

	/*
	 * Get the amount of samples taken per pixel of a chosen cell @ index
	 */
	public int getSamplesPerPixel(int index)
	{
		return m_samples_taken.get(index);
	}

	/*
	 * Get the samples per pixel as an array
	 */
	public AtomicIntegerArray getSamplesPerPixel()
	{
		return m_samples_taken;
	}

}
