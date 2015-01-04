package to.us.harha.jpath.util.math;

import java.util.concurrent.ThreadLocalRandom;

import to.us.harha.jpath.Main;

public class Vec3f
{

    // Vector x-coordinate
    public float x;
    // Vector y-coordinate
    public float y;
    // Vector z-coordinate
    public float z;

    /*
     * Constructor Vec3f(float x, float y, float z);
     * @param x: Vector x-coordinate
     * @param y: Vector y-coordinate
     * @param z: Vector z-coordinate
     */
    public Vec3f(float x, float y, float z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /*
     * Constructor Vec3f(float f);
     * @@param f: Sets the initial value for all vector components
     */
    public Vec3f(float f)
    {
        x = f;
        y = f;
        z = f;
    }

    /*
     * Constructor Vec3f();
     * @info: Blank vector constructor, all components are equal to 0.0f
     */
    public Vec3f()
    {
        x = 0.0f;
        y = 0.0f;
        z = 0.0f;
    }

    @Override
    public String toString()
    {
        return "Vec2f[" + x + ", " + y + ", " + z + "]";
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

    public static Vec3f reflect(Vec3f I, Vec3f N)
    {
        return sub(I, Vec3f.scale(scale(N, dot(N, I)), 2.0f));
    }

    public static Vec3f refract(Vec3f I, Vec3f N, float i1, float i2)
    {
        float NdotI, n1, n2;

        NdotI = dot(N, I);

        if (NdotI > 0.0)
        {
            n1 = i2;
            n2 = i1;
        } else
        {
            n1 = i1;
            n2 = i2;
        }

        float eta = n1 / n2;
        float k = 1.0f - eta * eta * (1.0f - NdotI * NdotI);

        if (k < 0.0f)
            return new Vec3f(0.0f);

        Vec3f temp_a = scale(I, eta);
        Vec3f temp_b = scale(N, eta * NdotI + (float) Math.sqrt(k));

        return normalize(sub(temp_a, temp_b));
    }

    public static Vec3f randomHemisphere(Vec3f N)
    {
        Vec3f R;

        do
        {
            R = normalize(new Vec3f(2.0f * ThreadLocalRandom.current().nextFloat() - 1.0f, 2.0f * ThreadLocalRandom.current().nextFloat() - 1.0f, 2.0f * ThreadLocalRandom.current().nextFloat() - 1.0f));
        } while (dot(N, R) <= 0.0f);

        return R;
    }

}