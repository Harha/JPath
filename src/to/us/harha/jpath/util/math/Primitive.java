package to.us.harha.jpath.util.math;

import java.util.Arrays;

import to.us.harha.jpath.Main;

public class Primitive
{

	protected Vec3f[] m_vertices;

	public Primitive(Vec3f pos)
	{
		m_vertices = new Vec3f[1];
		m_vertices[0] = pos;
	}

	public Primitive(Vec3f[] vertices)
	{
		m_vertices = vertices;
	}

	public Intersection intersect(Ray r)
	{
		return null;
	}

	public void calcNormal()
	{

	}

	public Vec3f[] getVertices()
	{
		return m_vertices;
	}

	public Vec3f getVertex(int i)
	{
		return m_vertices[i];
	}

	public void setVertices(Vec3f[] m_vertices)
	{
		this.m_vertices = m_vertices;
	}

	public void setVertex(int i, Vec3f vertex)
	{
		m_vertices[i] = vertex;
	}

	public void mulVertices(Mat4f m)
	{
		for (int i = 0; i < m_vertices.length; i++)
		{
			m_vertices[i] = Mat4f.mul(m, m_vertices[i], 1.0f);
		}
		calcNormal();
	}

	public Primitive copy()
	{
		Vec3f[] tempVerts = new Vec3f[m_vertices.length];
		Arrays.fill(tempVerts, new Vec3f(0.0f));
		
		for (int i = 0; i < tempVerts.length; i++)
		{
			tempVerts[i].x = m_vertices[i].x;
			tempVerts[i].y = m_vertices[i].y;
			tempVerts[i].z = m_vertices[i].z;
		}

		return new Primitive(tempVerts);
	}

}
