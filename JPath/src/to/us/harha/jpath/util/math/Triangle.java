package to.us.harha.jpath.util.math;

import to.us.harha.jpath.Main;

public class Triangle extends Primitive
{

	private Vec3f[] m_vertices;
	private Vec3f   m_edge_a;
	private Vec3f   m_edge_b;
	private Vec3f   m_norm;

	public Triangle(Vec3f pos, Vec3f[] vertices)
	{
		super(pos);
		m_vertices = vertices;
		calcNormal();
	}

	public Intersection intersect(Ray r)
	{
		Vec3f P, Q, T;
		float d, inv_d, u, v, t;

		P = Vec3f.cross(r.getDir(), m_edge_b);
		d = Vec3f.dot(m_edge_a, P);

		if (d < 0.0f)
			return null;

		inv_d = 1.0f / d;
		T = Vec3f.sub(r.getPos(), Vec3f.add(m_transformed_pos, m_vertices[0]));
		u = Vec3f.dot(T, P) * inv_d;

		if (u < 0.0f || u > 1.0f)
			return null;

		Q = Vec3f.cross(T, m_edge_a);
		v = Vec3f.dot(r.getDir(), Q) * inv_d;

		if (v < 0.0f || u + v > 1.0f)
			return null;

		t = Vec3f.dot(m_edge_b, Q) * inv_d;

		if (t < Main.EPSILON)
			return null;

		Intersection x = new Intersection();
		x.setPos(Vec3f.add(r.getPos(), Vec3f.scale(r.getDir(), t)));
		x.setNorm(m_norm);
		x.setT(t);

		return x;
	}

	private void calcNormal()
	{
		m_edge_a = Vec3f.sub(m_vertices[1], m_vertices[0]);
		m_edge_b = Vec3f.sub(m_vertices[2], m_vertices[0]);
		m_norm = Vec3f.normalize(Vec3f.cross(m_edge_a, m_edge_b));
	}

}
