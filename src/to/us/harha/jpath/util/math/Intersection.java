package to.us.harha.jpath.util.math;

public class Intersection
{

    private Vec3f m_pos;
    private Vec3f m_norm;
    private float m_t;

    public Intersection(Vec3f pos, Vec3f norm, float t)
    {
        m_pos = pos;
        m_norm = norm;
        m_t = t;
    }

    public Intersection()
    {
        m_pos = new Vec3f();
        m_norm = new Vec3f();
        m_t = 0.0f;
    }

    public Vec3f getPos()
    {
        return m_pos;
    }

    public Vec3f getNorm()
    {
        return m_norm;
    }

    public float getT()
    {
        return m_t;
    }

    public void setPos(Vec3f m_pos)
    {
        this.m_pos = m_pos;
    }

    public void setNorm(Vec3f m_norm)
    {
        this.m_norm = m_norm;
    }

    public void setT(float m_t)
    {
        this.m_t = m_t;
    }

}
