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
		Vec3f SP;
		float t, b, d;

		SP = m_vertices[0].sub(r.getPos());
		b = SP.dot(r.getDir());
		d = b * b - SP.dot(SP) + m_radius * m_radius;

		if (d < 0.0f)
			return null;

		d = (float) Math.sqrt(d);
		t = (t = b - d) > Main.EPSILON ? t : ((t = b + d) > Main.EPSILON ? t : -1.0f);

		if (t == -1.0f)
			return null;

		Intersection x = new Intersection();
		x.setPos(r.getPos().add(r.getDir().scale(t)));
		x.setNorm(x.getPos().sub(m_vertices[0]).divide(m_radius));
		x.setT(t);

		return x;
	}

}
