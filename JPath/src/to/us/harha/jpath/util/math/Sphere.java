package to.us.harha.jpath.util.math;

import to.us.harha.jpath.Main;

public class Sphere extends Primitive
{

    private float m_radius;

    public Sphere(Vec3f pos, float radius)
    {
        super(pos);
        m_radius = radius;
    }

    @Override
    public Intersection intersect(Ray r)
    {
        Vec3f S;
        float b, c, h, t;

        S = Vec3f.sub(r.getPos(), m_pos);
        b = Vec3f.dot(S, r.getDir());
        c = Vec3f.dot(S, S) - (m_radius * m_radius);
        h = b * b - c;

        if (h < 0.0f)
            return null;

        t = -b - (float) Math.sqrt(h);

        if (t < Main.EPSILON)
            return null;

        Intersection x = new Intersection();
        x.setPos(Vec3f.add(r.getPos(), Vec3f.scale(r.getDir(), t)));
        x.setNorm(Vec3f.divide(Vec3f.sub(x.getPos(), m_pos), m_radius));
        x.setT(t);

        return x;
    }

    /*
    @Override
    public Intersection intersect(Ray r)
    {
    	Vec3f S;
    	float dist_sq, b, d, t, t1, t2;

    	S = Vec3f.sub(r.getPos(), m_pos);
    	dist_sq = Vec3f.dot(S, S);

    	if (dist_sq <= m_radius)
    		return null;

    	b = Vec3f.dot(Vec3f.negate(S), r.getDir());
    	d = (b * b) - dist_sq + (m_radius * m_radius);

    	if (d < 0.0f)
    		return null;

    	d = (float) Math.sqrt(d);
    	t1 = b - d;
    	t2 = b + d;
    	t = Math.min(t1,  t2);

    	if (t < Main.EPSILON)
    		return null;

    	Intersection x = new Intersection();
    	x.setPos(Vec3f.add(Vec3f.scale(r.getDir(), t), r.getPos()));
    	x.setNorm(Vec3f.divide(Vec3f.sub(x.getPos(), m_pos), m_radius));
    	x.setT(t);

    	return x;
    }
    */
}
