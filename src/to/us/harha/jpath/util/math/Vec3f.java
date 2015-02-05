package to.us.harha.jpath.util.math;

import java.util.concurrent.ThreadLocalRandom;

public class Vec3f
{

	public float x;
	public float y;
	public float z;

	public Vec3f(float x, float y, float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vec3f(float f)
	{
		x = f;
		y = f;
		z = f;
	}

	public Vec3f()
	{
		x = 0.0f;
		y = 0.0f;
		z = 0.0f;
	}

	@Override
	public String toString()
	{
		return String.format("Vec3f[%.5f, %.5f, %.5f]", x, y, z);
	}

	public boolean equals(Vec3f v)
	{
		if (x == v.x && y == v.y && z == v.z)
			return true;
		else
			return false;
	}

	public Vec3f set(Vec3f v)
	{
		x = v.x;
		y = v.y;
		z = v.z;
		return this;
	}

	public Vec3f set(float x, float y, float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}

	public Vec3f add(Vec3f v)
	{
		return new Vec3f(x + v.x, y + v.y, z + v.z);
	}

	public Vec3f add(float f)
	{
		return new Vec3f(x + f, y + f, z + f);
	}

	public Vec3f sub(Vec3f v)
	{
		return new Vec3f(x - v.x, y - v.y, z - v.z);
	}

	public Vec3f sub(float f)
	{
		return new Vec3f(x - f, y - f, z - f);
	}

	public Vec3f scale(Vec3f v)
	{
		return new Vec3f(x * v.x, y * v.y, z * v.z);
	}

	public Vec3f scale(float f)
	{
		return new Vec3f(x * f, y * f, z * f);
	}

	public Vec3f divide(Vec3f v)
	{
		return new Vec3f(x / v.x, y / v.y, z / v.z);
	}

	public Vec3f divide(float f)
	{
		return new Vec3f(x / f, y / f, z / f);
	}

	public Vec3f cross(Vec3f v)
	{
		return new Vec3f(y * v.z - v.y * z, z * v.x - v.z * x, x * v.y - v.x * y);
	}

	public float dot(Vec3f v)
	{
		return x * v.x + y * v.y + z * v.z;
	}

	public Vec3f mul(Quaternion q)
	{
		Quaternion q_inv = q.conjugate();

		Quaternion w = q.mul(this).mul(q_inv);

		return new Vec3f(w.x, w.y, w.z);
	}

	public float length()
	{
		return (float) Math.sqrt(x * x + y * y + z * z);
	}

	public Vec3f normalize()
	{
		float length = length();
		return new Vec3f(x / length, y / length, z / length);
	}

	public Vec3f negate()
	{
		return new Vec3f(-x, -y, -z);
	}

	public Vec3f reflect(Vec3f N)
	{
		return this.sub(N.scale(2.0f * N.dot(this)));
	}

	public Vec3f refract(Vec3f N, float i1, float i2)
	{
		float NdotI = this.dot(N), n, n1, n2, fresnel;

		if (NdotI > 0.0f)
		{
			n1 = i1;
			n2 = i2;
			N = N.negate();
		} else
		{
			n1 = i2;
			n2 = i1;
			NdotI = -NdotI;
		}

		n = n2 / n1;

		float cos_t = n * n * (1.0f - NdotI * NdotI);

		if (cos_t > 1.0f)
		{
			return this.add(N.scale(2.0f * NdotI));
		}

		cos_t = (float) Math.sqrt(1.0 - cos_t);
		fresnel = ((float) Math.sqrt((n1 * NdotI - n2 * cos_t) / (n1 * NdotI + n2 * cos_t)) + (float) Math.sqrt((n2 * NdotI - n1 * cos_t) / (n2 * NdotI + n1 * cos_t))) * 0.5f;

		if (ThreadLocalRandom.current().nextFloat() <= fresnel)
		{
			return this.add(N.scale(2.0f * NdotI));
		}

		return this.scale(n).add(N.scale(n * NdotI - cos_t));
	}

	public Vec3f rotateTowards(Vec3f w)
	{
		float dot = w.z;

		// No rotation needed
		if (dot > 0.9999f)
			return new Vec3f(x, y, z);

		// Mirror along the z-axis
		if (dot < -0.9999f)
			return new Vec3f(x, y, -z);

		Vec3f up = new Vec3f(0.0f, 0.0f, 1.0f);
		Vec3f a1 = up.cross(w).normalize();
		Vec3f a2 = a1.cross(w).normalize();

		return a1.scale(x).add(a2.scale(y)).add(w.scale(z)).normalize();
	}

	public Vec3f randomHemisphere()
	{
		float phi = ThreadLocalRandom.current().nextFloat() * (float) (2.0 * Math.PI);
		float rq = ThreadLocalRandom.current().nextFloat();
		float r = (float) Math.sqrt(rq);

		Vec3f V = new Vec3f((float) Math.cos(phi) * r, (float) Math.sin(phi) * r, (float) Math.sqrt(1.0f - rq)).normalize();
		return V.rotateTowards(this);
	}

	public float getComponent(int i, float w)
	{
		if (i == 0)
			return this.x;
		else if (i == 1)
			return this.y;
		else if (i == 2)
			return this.z;
		else if (i == 3)
			return w;
		else
			return 0.0f;
	}

	public void setComponent(int i, float value)
	{
		if (i == 0)
			this.x = value;
		else if (i == 1)
			this.y = value;
		else if (i == 2)
			this.z = value;
	}

}