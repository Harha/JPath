package to.us.harha.jpath.tracer;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

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

	private int							m_maxrecursion;
	private Vec3f[]						m_samples;
	private Scene						m_scene;
	private Logger						m_log;

	public static final AtomicInteger	SAMPLESTAKEN	= new AtomicInteger();

	public Tracer(int maxrecursion)
	{
		m_maxrecursion = maxrecursion;
		m_samples = new Vec3f[Main.WIDTH * Main.HEIGHT];
		m_scene = new Scene();
		m_log = new Logger(this.getClass().getName());

		clearSamples();
	}

	public void update(float delta)
	{

	}

	public void render(Display display)
	{
		SAMPLESTAKEN.incrementAndGet();

		float width = display.getWidth();
		float height = display.getHeight();

		Ray ray = new Ray(new Vec3f(0.0f, 2.5f, 13.0f), new Vec3f(0.0f, 0.0f, -1.0f));
		for (int y = 0; y < display.getHeight(); y++)
		{
			for (int x = 0; x < display.getWidth(); x++)
			{
				int index = x + y * display.getWidth();

				float x_norm = (x - width * 0.5f) / width * display.getAR();
				float y_norm = (height * 0.5f - y) / height;
				ray.setDir(Vec3f.normalize(new Vec3f(x_norm, y_norm, -1.0f)));

				m_samples[index] = Vec3f.add(m_samples[index], pathTrace(ray, 0));

				Vec3f color_averaged = MathUtils.clamp(Vec3f.divide(m_samples[index], SAMPLESTAKEN.get()), 0.0f, 1.0f);

				display.drawPixelVec3f(x, y, color_averaged);
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
		if (n > m_maxrecursion)
			return new Vec3f();

		// Initialize some objects and variables
		Vec3f color_final = new Vec3f();
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
			return new Vec3f();

		// Get the object's surface material
		Material M = OBJECT.getMaterial();

		// If the object is a light source, return it's emittance
		if (Vec3f.length(M.getEmittance()) > 0.0f)
			return M.getEmittance();

		// If the object is reflective like a mirror, reflect a ray
		if (M.getReflectivity() > 0.0f)
		{
			Ray newRay;
			if (M.getGlossiness() > 0.0f)
				newRay = new Ray(iSectionFinal.getPos(), Vec3f.normalize(Vec3f.add(Vec3f.reflect(ray.getDir(), iSectionFinal.getNorm()), Vec3f.scale(Vec3f.randomHemisphere(iSectionFinal.getNorm()), M.getGlossiness()))));
			else
				newRay = new Ray(iSectionFinal.getPos(), Vec3f.normalize(Vec3f.reflect(ray.getDir(), iSectionFinal.getNorm())));
			color_final = Vec3f.add(color_final, Vec3f.scale(pathTrace(newRay, n + 1), M.getReflectivity()));
		}

		// If the object is refractive like glass, refract the ray
		if (M.getRefractivity() > 0.0f)
		{
			Ray newRay;
			if (M.getGlossiness() > 0.0f)
				newRay = new Ray(iSectionFinal.getPos(), Vec3f.normalize(Vec3f.add(Vec3f.refract(ray.getDir(), iSectionFinal.getNorm(), 1.0f, M.getRefractivityIndex()), Vec3f.scale(Vec3f.randomHemisphere(iSectionFinal.getNorm()), M.getGlossiness()))));
			else
				newRay = new Ray(iSectionFinal.getPos(), Vec3f.normalize(Vec3f.refract(ray.getDir(), iSectionFinal.getNorm(), 1.0f, M.getRefractivityIndex())));
			color_final = Vec3f.add(color_final, Vec3f.scale(pathTrace(newRay, n + 1), M.getRefractivity()));
		}

		// Calculate the diffuse lighting if reflectance is greater than 0.0
		if (Vec3f.length(M.getReflectance()) > 0.0f)
		{
			Ray newRay = new Ray(iSectionFinal.getPos(), Vec3f.normalize(Vec3f.randomHemisphere(iSectionFinal.getNorm())));

			float NdotD = Math.abs(Vec3f.dot(iSectionFinal.getNorm(), newRay.getDir()));
			Vec3f BRDF = Vec3f.scale(M.getReflectance(), 1.0f * NdotD);
			Vec3f REFLECTED = pathTrace(newRay, n + 1);

			color_final = Vec3f.add(color_final, Vec3f.add(M.getEmittance(), Vec3f.scale(BRDF, REFLECTED)));
		}

		return color_final;
	}

	public void clearSamples()
	{
		Arrays.fill(m_samples, new Vec3f(0.0f));
		SAMPLESTAKEN.set(0);
	}

}
