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

        P = Vec3f.sub(m_pos, r.getPos());
        d = Vec3f.dot(m_norm, r.getDir());
        t = Vec3f.dot(P, m_norm) / d;

        if (t < Main.EPSILON)
            return null;

        Intersection x = new Intersection();
        x.setPos(Vec3f.add(Vec3f.scale(r.getDir(), t), r.getPos()));
        x.setNorm(Vec3f.normalize(m_norm));
        x.setT(t);

        return x;
    }

}
