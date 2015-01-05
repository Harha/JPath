package to.us.harha.jpath.util.math;

import to.us.harha.jpath.Main;

public class Sphere extends Primitive
{

	private float m_radius;

	public Sphere(Vec3f pos, float radius)
	{
		super(pos);
		m_radius = radius;
	}

	@Override
	public Intersection intersect(Ray r)
	{
		Vec3f S;
		float b, c, h, t;

		S = Vec3f.sub(r.getPos(), m_transformed_pos);
		b = Vec3f.dot(S, r.getDir());
		c = Vec3f.dot(S, S) - (m_radius * m_radius);
		h = b * b - c;

		if (h < 0.0f)
			return null;

		t = -b - (float) Math.sqrt(h);

		if (t < Main.EPSILON)
			return null;

		Intersection x = new Intersection();
		x.setPos(Vec3f.add(r.getPos(), Vec3f.scale(r.getDir(), t)));
		x.setNorm(Vec3f.divide(Vec3f.sub(x.getPos(), m_transformed_pos), m_radius));
		x.setT(t);

		return x;
	}

}
