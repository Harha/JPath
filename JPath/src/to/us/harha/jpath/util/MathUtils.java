package to.us.harha.jpath.util;

import to.us.harha.jpath.util.math.Vec3f;

public class MathUtils
{

    private MathUtils()
    {

    }

    public static float max(Vec3f v)
    {
        return Math.max(v.x, Math.max(v.y, v.z));
    }

    public static float clamp(float f, float min, float max)
    {
        return Math.max(min, Math.min(f, max));
    }

    public static Vec3f clamp(Vec3f v, float min, float max)
    {
        float x = clamp(v.x, min, max);
        float y = clamp(v.y, min, max);
        float z = clamp(v.z, min, max);
        return new Vec3f(x, y, z);
    }

    public static float interpolate(float f, float min, float max)
    {
        return min + (max - min) * clamp(f, 0.0f, 1.0f);
    }

    public static Vec3f interpolate(Vec3f v, float min, float max)
    {
        float x = interpolate(v.x, min, max);
        float y = interpolate(v.y, min, max);
        float z = interpolate(v.z, min, max);
        return new Vec3f(x, y, z);
    }

    public static float smoothstep(float f, float min, float max)
    {
        return clamp((f - min) / (max - min), 0.0f, 1.0f);
    }

    public static Vec3f smoothstep(Vec3f v, float min, float max)
    {
        float x = smoothstep(v.x, min, max);
        float y = smoothstep(v.y, min, max);
        float z = smoothstep(v.z, min, max);
        return new Vec3f(x, y, z);
    }

}
