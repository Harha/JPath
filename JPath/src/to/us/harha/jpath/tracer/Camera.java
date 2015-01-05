package to.us.harha.jpath.tracer;

import to.us.harha.jpath.util.math.Ray;
import to.us.harha.jpath.util.math.Vec3f;

public class Camera
{

	private Vec3f m_pos;
	private Vec3f m_forward;
	private Vec3f m_up;
	private Vec3f m_right;

	public Camera(Vec3f pos, Vec3f forward, Vec3f up)
	{
		m_pos = pos;
		m_forward = forward;
		m_up = up;

		calcDirections();
	}

	public void calcDirections()
	{
		m_forward = Vec3f.normalize(m_forward);
		m_up = Vec3f.normalize(m_up);
		m_right = Vec3f.normalize(Vec3f.cross(m_forward, m_up));
	}

	public Vec3f getPos()
	{
		return m_pos;
	}

	public Vec3f getForward()
	{
		return m_forward;
	}

	public Vec3f getBack()
	{
		return Vec3f.negate(m_forward);
	}

	public Vec3f getUp()
	{
		return m_up;
	}

	public Vec3f getDown()
	{
		return Vec3f.negate(m_up);
	}

	public Vec3f getRight()
	{
		return m_right;
	}

	public Vec3f getLeft()
	{
		return Vec3f.negate(m_right);
	}

}
