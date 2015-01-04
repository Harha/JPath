package to.us.harha.jpath.util.math;

public class Ray
{

    private Vec3f m_pos;
    private Vec3f m_dir;

    public Ray(Vec3f pos, Vec3f dir)
    {
        m_pos = pos;
        m_dir = Vec3f.normalize(dir);
    }

    public Ray()
    {
        m_pos = new Vec3f();
        m_dir = new Vec3f();
    }

    public Vec3f getPos()
    {
        return m_pos;
    }

    public Vec3f getDir()
    {
        return m_dir;
    }

    public void setPos(Vec3f m_pos)
    {
        this.m_pos = m_pos;
    }

    public void setDir(Vec3f m_dir)
    {
        this.m_dir = m_dir;
    }

}
