package to.us.harha.jpath.util.math;

public class Primitive
{

    protected Vec3f m_pos;

    public Primitive(Vec3f pos)
    {
        m_pos = pos;
    }

    public Intersection intersect(Ray r)
    {
        return null;
    }

    public Vec3f getPos()
    {
        return m_pos;
    }

    public void setPos(Vec3f m_pos)
    {
        this.m_pos = m_pos;
    }

}
