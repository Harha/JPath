package to.us.harha.jpath.util.math;

import to.us.harha.jpath.Main;

public class Plane extends Primitive
{

	private Vec3f m_norm;

	public Plane(Vec3f pos, Vec3f norm)
	{
		super(pos);
		m_norm = norm;
	}

	@Override
	public Intersection intersect(Ray r)
	{
		Vec3f P;
		float d, t;

		P = m_vertices[0].sub(r.getPos());
		d = m_norm.dot(r.getDir());

		if (d > 0.0f)
			return null;

		t = P.dot(m_norm) / d;

		if (t < Main.EPSILON)
			return null;

		Intersection x = new Intersection();
		x.setPos(r.getPos().add(r.getDir().scale(t)));
		x.setNorm(m_norm.normalize());
		x.setT(t);

		return x;
	}

}
