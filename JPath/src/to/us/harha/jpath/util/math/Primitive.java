package to.us.harha.jpath.util.math;

public class Primitive
{

	protected Vec3f m_pos;
	protected Vec3f m_transformed_pos;

	public Primitive(Vec3f pos)
	{
		m_pos = pos;
		m_transformed_pos = m_pos;
	}

	public Intersection intersect(Ray r)
	{
		return null;
	}

	public Vec3f getPos()
	{
		return m_pos;
	}

	public Vec3f getTransformedPos()
	{
		return m_transformed_pos;
	}

	public void setPos(Vec3f m_pos)
	{
		this.m_pos = m_pos;
	}

	public void setTransformedPos(Vec3f m_transformed_pos)
	{
		this.m_transformed_pos = m_transformed_pos;
	}

}
