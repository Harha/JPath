package to.us.harha.jpath.util.math;

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

}
