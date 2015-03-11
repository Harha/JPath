package to.us.harha.jpath.util.math;

import java.util.concurrent.ThreadLocalRandom;

import to.us.harha.jpath.tracer.Camera;

public class Ray
{

	private Vec3f m_pos;
	private Vec3f m_dir;
	private float m_currentMediumIOR;

	public Ray(Vec3f pos, Vec3f dir)
	{
		m_pos = pos;
		m_dir = dir.normalize();
		m_currentMediumIOR = 1.0f;
	}

	public Ray()
	{
		m_pos = new Vec3f();
		m_dir = new Vec3f();
		m_currentMediumIOR = 1.0f;
	}

	public static Ray calcCameraRay(Camera c, int w, int h, float ar, int x, int y)
	{
		float x_norm = (x - w * 0.5f) / w * ar;
		float y_norm = (h * 0.5f - y) / h;

		Vec3f forward = c.getForward();
		Vec3f up = c.getUp();
		Vec3f right = c.getRight();

		Vec3f image_point = right.scale(x_norm).add(up.scale(y_norm)).add(c.getPos().add(forward));
		Vec3f ray_direction = image_point.sub(c.getPos());

		return new Ray(c.getPos(), ray_direction);
	}

	public static Ray calcSupersampledCameraRay(Camera c, int w, int h, float ar, int x, int y, float jitter)
	{
		float x_norm = (x - w * 0.5f) / w * ar;
		float y_norm = (h * 0.5f - y) / h;

		Vec3f forward = c.getForward();
		Vec3f up = c.getUp();
		Vec3f right = c.getRight();

		Vec3f image_point = right.scale(x_norm).add(up.scale(y_norm)).add(c.getPos().add(forward));
		image_point = image_point.add(new Vec3f(jitter * ThreadLocalRandom.current().nextFloat() - (jitter / 2.0f), jitter * ThreadLocalRandom.current().nextFloat() - (jitter / 2.0f), 0.0f));
		Vec3f ray_direction = image_point.sub(c.getPos());

		return new Ray(c.getPos(), ray_direction);
	}

	public Vec3f getPos()
	{
		return m_pos;
	}

	public Vec3f getDir()
	{
		return m_dir;
	}

	public float getCurrentMediumIOR()
	{
		return m_currentMediumIOR;
	}

	public void setPos(Vec3f pos)
	{
		m_pos.set(pos);
	}

	public void setDir(Vec3f dir)
	{
		m_dir.set(dir);
	}

	public void setCurrentMediumIOR(float IOR)
	{
		m_currentMediumIOR = IOR;
	}

}
