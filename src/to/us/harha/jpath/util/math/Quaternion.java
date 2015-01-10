package to.us.harha.jpath.util.math;

public class Quaternion
{

	private float w;
	private Vec3f v;

	public Quaternion(Vec3f n, float theta)
	{
		theta = (float) Math.toRadians(theta);

		w = (float) Math.cos(theta / 2.0);
		v = new Vec3f(n.x * (float) Math.sin(theta / 2.0), n.y * (float) Math.sin(theta / 2.0), n.z * (float) Math.sin(theta / 2.0));
	}

	public Quaternion()
	{
		w = 0.0f;
		v = new Vec3f();
	}

	@Override
	public String toString()
	{
		return String.format("Quaternion[%.5f, %.5f, %.5f, %.5f]", w, v.x, v.y, v.z);
	}

	public static Quaternion conjugate(Quaternion q)
	{
		Quaternion r = new Quaternion();

		r.w = q.w;
		r.v.x = -q.v.x;
		r.v.y = -q.v.y;
		r.v.z = -q.v.z;

		return r;
	}

	public static Quaternion mul(Quaternion q1, Quaternion q2)
	{
		Quaternion r = new Quaternion();

		r.w = q1.w * q2.w - Vec3f.dot(q1.v, q2.v);
		r.v = Vec3f.add(Vec3f.add(Vec3f.scale(q1.v, q2.w), Vec3f.scale(q2.v, q1.w)), Vec3f.cross(q1.v, q2.v));

		return r;
	}

	public static Vec3f mul(Quaternion q, Vec3f v)
	{
		Quaternion r = new Quaternion();

		r.w = 0.0f;
		r.v = new Vec3f(v.x, v.y, v.z);

		Vec3f vcV = Vec3f.cross(q.v, v);
		return Vec3f.add(Vec3f.add(v, Vec3f.scale(vcV, 2.0f * q.w)), Vec3f.scale(Vec3f.cross(q.v, vcV), 2.0f));
	}

}
