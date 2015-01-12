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
		if (this.x == v.x && this.y == v.y && this.z == v.z)
			return true;
		else
			return false;
	}

	public static void set(Vec3f v, Vec3f w)
	{
		v.x = w.x;
		v.y = w.y;
		v.z = w.z;
	}

	public static Vec3f lerp(Vec3f v, Vec3f u, float lerpFactor)
	{
		return Vec3f.add(Vec3f.scale(Vec3f.sub(v, u), lerpFactor), u);
	}

	public static Vec3f add(Vec3f left, Vec3f right)
	{
		return new Vec3f(left.x + right.x, left.y + right.y, left.z + right.z);
	}

	public static Vec3f add(Vec3f left, float f)
	{
		return new Vec3f(left.x + f, left.y + f, left.z + f);
	}

	public static Vec3f sub(Vec3f left, Vec3f right)
	{
		return new Vec3f(left.x - right.x, left.y - right.y, left.z - right.z);
	}

	public static Vec3f sub(Vec3f left, float f)
	{
		return new Vec3f(left.x - f, left.y - f, left.z - f);
	}

	public static Vec3f scale(Vec3f left, Vec3f right)
	{
		return new Vec3f(left.x * right.x, left.y * right.y, left.z * right.z);
	}

	public static Vec3f scale(Vec3f v, float f)
	{
		return new Vec3f(f * v.x, f * v.y, f * v.z);
	}

	public static Vec3f divide(Vec3f left, Vec3f right)
	{
		return new Vec3f(left.x / right.x, left.y / right.y, left.z / right.z);
	}

	public static Vec3f divide(Vec3f v, float f)
	{
		return new Vec3f(v.x / f, v.y / f, v.z / f);
	}

	public static Vec3f cross(Vec3f left, Vec3f right)
	{
		return new Vec3f(left.y * right.z - right.y * left.z, left.z * right.x - right.z * left.x, left.x * right.y - right.x * left.y);
	}

	public static float dot(Vec3f left, Vec3f right)
	{
		return left.x * right.x + left.y * right.y + left.z * right.z;
	}

	public static Vec3f normalize(Vec3f v)
	{
		float length = length(v);
		return new Vec3f(v.x / length, v.y / length, v.z / length);
	}

	public static float length(Vec3f v)
	{
		return (float) Math.sqrt(v.x * v.x + v.y * v.y + v.z * v.z);
	}

	public static Vec3f negate(Vec3f v)
	{
		return new Vec3f(-v.x, -v.y, -v.z);
	}

	public static Vec3f rotate(Vec3f v, Vec3f axis, float angle)
	{
		float sinAngle = (float) Math.sin(-angle);
		float cosAngle = (float) Math.cos(-angle);

		return Vec3f.add(Vec3f.add(Vec3f.cross(v, Vec3f.scale(axis, sinAngle)), Vec3f.scale(v, cosAngle)), Vec3f.scale(axis, Vec3f.dot(v, Vec3f.scale(axis, 1.0f - cosAngle))));
	}

	public static Vec3f rotateTowards(Vec3f v, Vec3f w)
	{
		float dot = w.z;

		// No rotation needed
		if (dot > 0.9999f)
			return v;

		// Mirror along the z-axis
		if (dot < -0.9999f)
			return new Vec3f(v.x, v.y, -v.z);

		Vec3f up = new Vec3f(0.0f, 0.0f, 1.0f);
		Vec3f a1 = normalize(cross(up, w));
		Vec3f a2 = normalize(cross(a1, w));

		return normalize(add(add(scale(a1, v.x), scale(a2, v.y)), scale(w, v.z)));
	}

	public static Vec3f reflect(Vec3f I, Vec3f N)
	{
		return sub(I, scale(scale(N, dot(N, I)), 2.0f));
	}

	public static Vec3f refract(Vec3f I, Vec3f N, float i1, float i2)
	{
		float NdotI = dot(I, N), n = (NdotI > 0.0f) ? i2 / i1 : i1 / i2;
		float cos_t = 1.0f - n * (1.0f - NdotI * NdotI);

		if (cos_t < 0.0f)
			return reflect(I, N);

		return add(scale(I, n), scale(N, n * NdotI - (float) Math.sqrt(cos_t)));
	}

	public static Vec3f randomHemisphere(Vec3f N)
	{
		float phi = ThreadLocalRandom.current().nextFloat() * (float) (2.0 * Math.PI);
		float rq = ThreadLocalRandom.current().nextFloat();
		float r = (float) Math.sqrt(rq);

		Vec3f V = normalize(new Vec3f((float) Math.cos(phi) * r, (float) Math.sin(phi) * r, (float) Math.sqrt((1.0f - rq))));

		return rotateTowards(V, N);
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