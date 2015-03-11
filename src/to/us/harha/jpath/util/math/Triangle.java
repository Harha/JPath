package to.us.harha.jpath.util.math;

import to.us.harha.jpath.Main;

public class Triangle extends Primitive
{
	private Vec3f m_edge_a;
	private Vec3f m_edge_b;
	private Vec3f m_norm;

	public Triangle(Vec3f[] vertices)
	{
		super(vertices);
		calcNormal();
	}

	public Intersection intersect(Ray r)
	{
		Vec3f P, Q, T;
		float d, inv_d, u, v, t;

		P = r.getDir().cross(m_edge_b);
		d = m_edge_a.dot(P);

		if (d > -Main.EPSILON && d < Main.EPSILON)
			return null;

		inv_d = 1.0f / d;
		T = r.getPos().sub(m_vertices[0]);
		u = T.dot(P) * inv_d;

		if (u < 0.0f || u > 1.0f)
			return null;

		Q = T.cross(m_edge_a);
		v = r.getDir().dot(Q) * inv_d;

		if (v < 0.0f || u + v > 1.0f)
			return null;

		t = m_edge_b.dot(Q) * inv_d;

		if (t < Main.EPSILON)
			return null;

		Intersection x = new Intersection();
		x.setPos(r.getPos().add(r.getDir().scale(t)));
		x.setNorm(m_norm);
		x.setT(t);

		return x;
	}

	@Override
	public void calcNormal()
	{
		m_edge_a = m_vertices[1].sub(m_vertices[0]);
		m_edge_b = m_vertices[2].sub(m_vertices[0]);
		m_norm = m_edge_a.cross(m_edge_b).normalize();
	}

}
