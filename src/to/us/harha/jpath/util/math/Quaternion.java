package to.us.harha.jpath.util.math;

public class Quaternion
{

	public float w;
	public float x;
	public float y;
	public float z;

	public Quaternion(float w, float x, float y, float z)
	{
		this.w = w;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Quaternion()
	{
		w = 0.0f;
		x = 0.0f;
		y = 0.0f;
		z = 0.0f;
	}

	public Quaternion set(Quaternion q)
	{
		this.w = q.w;
		this.x = q.x;
		this.y = q.y;
		this.z = q.z;

		return this;
	}

	public Quaternion createFromAxisAngle(float x, float y, float z, float theta)
	{
		theta = (float) Math.toRadians(theta);

		this.w = (float) Math.cos(theta / 2.0);
		this.x = x * (float) Math.sin(theta / 2.0);
		this.y = y * (float) Math.sin(theta / 2.0);
		this.z = z * (float) Math.sin(theta / 2.0);

		return this;
	}

	@Override
	public String toString()
	{
		return String.format("Quaternion[%.5f, %.5f, %.5f, %.5f]", w, x, y, z);
	}

	public boolean equals(Quaternion q)
	{
		if (this.w == q.w && this.x == q.x && this.y == q.y && this.z == q.z)
			return true;
		else
			return false;
	}

	public static Quaternion conjugate(Quaternion q)
	{
		Quaternion r = new Quaternion();

		r.w = q.w;
		r.x = -q.x;
		r.y = -q.y;
		r.z = -q.z;

		return r;
	}

	public static Quaternion mul(Quaternion q1, Quaternion q2)
	{
		Quaternion r = new Quaternion();

		r.w = q1.w * q2.w - q1.x * q2.x - q1.y * q2.y - q1.z * q2.z;
		r.x = q1.w * q2.x + q1.x * q2.w + q1.y * q2.z - q1.z * q2.y;
		r.y = q1.w * q2.y - q1.x * q2.z + q1.y * q2.w + q1.z * q2.x;
		r.z = q1.w * q2.z + q1.x * q2.y - q1.y * q2.x + q1.z * q2.w;

		return r;
	}

	public static Vec3f mul(Quaternion q, Vec3f v)
	{
		Quaternion r = new Quaternion();
		Quaternion w = new Quaternion(0.0f, v.x, v.y, v.z);
		Quaternion q_inv = conjugate(q);

		r = mul(mul(q, w), q_inv);

		return new Vec3f(r.x, r.y, r.z);
	}

	public static Quaternion normalize(Quaternion q)
	{
		Quaternion r = new Quaternion();

		float length = length(q);
		r.w = q.w / length;
		r.x = q.x / length;
		r.y = q.y / length;
		r.z = q.z / length;

		return r;
	}

	public static float length(Quaternion q)
	{
		return (float) Math.sqrt(q.w * q.w + q.x * q.x + q.y * q.y + q.z * q.z);
	}

}
