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

	public Quaternion set(float w, float x, float y, float z)
	{
		this.w = w;
		this.x = x;
		this.y = y;
		this.z = z;

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

	public Quaternion createFromAxisAngle(float x, float y, float z, float theta)
	{
		theta = (float) Math.toRadians(theta);

		this.w = (float) Math.cos(theta / 2.0);
		this.x = x * (float) Math.sin(theta / 2.0);
		this.y = y * (float) Math.sin(theta / 2.0);
		this.z = z * (float) Math.sin(theta / 2.0);

		return this;
	}

	public Quaternion mul(Quaternion q)
	{
		Quaternion r = new Quaternion();

		r.w = w * q.w - x * q.x - y * q.y - z * q.z;
		r.x = w * q.x + x * q.w + y * q.z - z * q.y;
		r.y = w * q.y - x * q.z + y * q.w + z * q.x;
		r.z = w * q.z + x * q.y - y * q.x + z * q.w;

		return r;
	}

	public Quaternion mul(Vec3f v)
	{
		Quaternion r = new Quaternion();

		r.w = -x * v.x - y * v.y - z * v.z;
		r.x = w * v.x + y * v.z - z * v.y;
		r.y = w * v.y + z * v.x - x * v.z;
		r.z = w * v.z + x * v.y - y * v.x;

		return r;
	}

	public Quaternion conjugate()
	{
		return new Quaternion(w, -x, -y, -z);
	}

	public Quaternion normalize()
	{
		float length = length();
		return new Quaternion(w / length, x / length, y / length, z / length);
	}

	public float length()
	{
		return (float) Math.sqrt(w * w + x * x + y * y + z * z);
	}

	public Vec3f getForwardVector()
	{
		return new Vec3f(0, 0, 1).mul(this);
	}

	public Vec3f getUpVector()
	{
		return new Vec3f(0, 1, 0).mul(this);
	}

	public Vec3f getRightVector()
	{
		return new Vec3f(1, 0, 0).mul(this);
	}

}