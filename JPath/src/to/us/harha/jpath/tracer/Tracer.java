package to.us.harha.jpath.tracer;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicIntegerArray;

import to.us.harha.jpath.Display;
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
	private int                m_max_recursion;
	private boolean            m_debug;
	private Vec3f[]            m_samples;
	private Scene              m_scene;
	private Camera             m_camera;
	private Logger             m_log;

	// Antialiasing
	private boolean            m_supersampling;
	private int                m_supersampling_amount;
	private float              m_supersampling_jitter;

	// Multithreading
	private int                m_thread_amount;
	private AtomicIntegerArray m_samples_taken;

	// Static constant objects to minimize object creation during tracing
	private static final Vec3f COLOR_BLACK = new Vec3f();
	private static final Vec3f COLOR_DEBUG = new Vec3f(1.0f, 0.0f, 1.0f);

	public Tracer(int thread_amount, int max_recursion, int sample_amount, boolean supersampling, int supersampling_amount, float supersampling_jitter, boolean debug)
	{
		m_log = new Logger(this.getClass().getName());
		m_thread_amount = thread_amount;
		m_max_recursion = max_recursion;
		m_samples = new Vec3f[sample_amount];
		m_supersampling = supersampling;
		m_supersampling_amount = supersampling_amount;
		m_supersampling_jitter = supersampling_jitter;
		m_debug = debug;

		if (m_thread_amount > 1)
			m_samples_taken = new AtomicIntegerArray((m_thread_amount / 2) * (m_thread_amount / 2));
		else
			m_samples_taken = new AtomicIntegerArray(1);

		clearSamples();

		m_scene = new Scene();
		m_camera = m_scene.getCameras().get(0);

		m_log.printMsg("Tracer instance has been initalized, using " + m_thread_amount + " threads!");
	}

	/*
	 * Update the scene
	 */
	public void update(float delta)
	{
		for (TracerObject o : m_scene.getObjects())
		{
			if (!o.getPrimitives().isEmpty())
				o.updateTransform();
		}
	}

	/*
	 * Render the whole screen at once
	 * For single-threaded rendering
	 */
	public void render(Display display)
	{
		incrementSampleCounter(0);

		float width = display.getWidth();
		float height = display.getHeight();

		for (int y = 0; y < display.getHeight(); y++)
		{
			for (int x = 0; x < display.getWidth(); x++)
			{
				int index = x + y * display.getWidth();

				// Calculate the primary ray
				Ray ray = Ray.calcCameraRay(m_camera, display.getWidth(), display.getHeight(), display.getAR(), x, y);

				// Do the path tracing
				m_samples[index] = Vec3f.add(m_samples[index], pathTrace(ray, 0));

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
	public void renderPortion(Display display, int t1, int t2)
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

				// Draw lines to separate each section, for debugging purposes
				if (m_debug)
				{
					if (xx == 0 || xx == width_portion || yy == 0 || yy == width_portion)
					{
						display.drawPixelVec3f(x, y, COLOR_DEBUG);
						continue;
					}
				}

				// Supersample each pixel if demanded
				if (m_supersampling)
				{
					Vec3f sample = COLOR_BLACK;

					// Sample the pixels n times
					for (int i = 0; i < m_supersampling_amount; i++)
					{
						// Calculate the randomized primary ray
						Ray ray = Ray.calcSupersampledCameraRay(m_camera, display.getWidth(), display.getHeight(), display.getAR(), x, y, m_supersampling_jitter);

						// Do the path tracing
						sample = Vec3f.add(sample, pathTrace(ray, 0));
					}

					// Get the average color of the sample
					Vec3f sample_averaged = Vec3f.divide(sample, m_supersampling_amount);

					// Add the averaged sample to the samples
					m_samples[index_screen] = Vec3f.add(m_samples[index_screen], sample_averaged);

				} else
				{
					// Calculate the primary ray
					Ray ray = Ray.calcCameraRay(m_camera, display.getWidth(), display.getHeight(), display.getAR(), x, y);

					// Do the path tracing
					m_samples[index_screen] = Vec3f.add(m_samples[index_screen], pathTrace(ray, 0));
				}

				// Draw the pixel
				display.drawPixelVec3fAveraged(index_screen, m_samples[index_screen], m_samples_taken.get(t));
			}
		}
	}

	/*
	 * Path tracing
	 * n = recursion level
	 */
	public Vec3f pathTrace(Ray ray, int n)
	{
		// Return black if max recursion depth has been exceeded
		if (n > m_max_recursion)
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
		if (Vec3f.length(M.getEmittance()) > 0.0f && iSectionFinal.getT() > Main.EPSILON)
			return M.getEmittance();

		// Get the intersection's info
		Vec3f P = iSectionFinal.getPos();
		Vec3f N = iSectionFinal.getNorm();

		// Get the info about the ray
		Vec3f O = ray.getPos();
		Vec3f V = ray.getDir();

		// Initialize the final color which will be returned in the end
		Vec3f color_final = new Vec3f();

		// If the object is reflective like a mirror, reflect a ray
		if (M.getReflectivity() > 0.0f)
		{
			Ray newRay;
			if (M.getGlossiness() > 0.0f)
				newRay = new Ray(P, Vec3f.normalize(Vec3f.add(Vec3f.reflect(V, N), Vec3f.scale(Vec3f.randomHemisphere(N), M.getGlossiness()))));
			else
				newRay = new Ray(P, Vec3f.normalize(Vec3f.reflect(V, N)));

			color_final = Vec3f.add(color_final, Vec3f.scale(pathTrace(newRay, n + 1), M.getReflectivity()));
		}

		// If the object is refractive like glass, refract the ray
		if (M.getRefractivity() > 0.0f)
		{
			Ray newRay;
			if (M.getGlossiness() > 0.0f)
				newRay = new Ray(P, Vec3f.normalize(Vec3f.add(Vec3f.refract(V, N, 1.0f, M.getRefractivityIndex()), Vec3f.scale(Vec3f.randomHemisphere(N), M.getGlossiness()))));
			else
				newRay = new Ray(P, Vec3f.normalize(Vec3f.refract(V, N, 1.0f, M.getRefractivityIndex())));

			color_final = Vec3f.add(color_final, Vec3f.scale(pathTrace(newRay, n + 1), M.getRefractivity()));
		}

		// Calculate the diffuse lighting if reflectance is greater than 0.0
		// NOTE: This could be improved / changed, it isn't physically correct at all atm and it's quite simple
		if (Vec3f.length(M.getReflectance()) > 0.0f)
		{
			Ray newRay = new Ray(P, Vec3f.randomHemisphere(N));

			float NdotD = Math.abs(Vec3f.dot(N, newRay.getDir()));
			Vec3f BRDF = Vec3f.scale(M.getReflectance(), 2.0f * NdotD);
			Vec3f REFLECTED = pathTrace(newRay, n + 1);

			color_final = Vec3f.add(color_final, Vec3f.add(M.getEmittance(), Vec3f.scale(BRDF, REFLECTED)));
		}

		// Simple radiance clamping to avoid fireflies
		return MathUtils.clamp(color_final, 0.0f, 10.0f);
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
