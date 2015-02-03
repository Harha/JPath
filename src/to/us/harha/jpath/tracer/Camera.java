package to.us.harha.jpath.tracer;

import to.us.harha.jpath.Input;
import to.us.harha.jpath.util.math.Mat4f;
import to.us.harha.jpath.util.math.Quaternion;
import to.us.harha.jpath.util.math.Vec3f;

public class Camera
{

	private Vec3f      m_pos;
	private Quaternion m_rot;
	private float      m_speed;
	private float      m_sensitivity;

	public Camera(Vec3f pos, Quaternion rot, float speed, float sensitivity)
	{
		m_pos = pos;
		m_rot = rot;
		m_speed = speed;
		m_sensitivity = sensitivity;
	}

	public void update(float delta, Input input)
	{
		// Camera movement
		if (input.getKey(Input.KEY_W))
		{
			move(getForward(), m_speed * delta);
		} else if (input.getKey(Input.KEY_S))
		{
			move(getForward().negate(), m_speed * delta);
		}
		if (input.getKey(Input.KEY_A))
		{
			move(getRight().negate(), m_speed * delta);
		} else if (input.getKey(Input.KEY_D))
		{
			move(getRight(), m_speed * delta);
		}
		if (input.getKey(Input.KEY_R))
		{
			move(getUp(), m_speed * delta);
		} else if (input.getKey(Input.KEY_F))
		{
			move(getUp().negate(), m_speed * delta);
		}

		// Camera rotation
		if (input.getKey(Input.KEY_RIGHT))
			rotate(getUp(), m_sensitivity * delta);
		if (input.getKey(Input.KEY_LEFT))
			rotate(getUp(), -m_sensitivity * delta);
		if (input.getKey(Input.KEY_UP))
			rotate(getRight(), -m_sensitivity * delta);
		if (input.getKey(Input.KEY_DOWN))
			rotate(getRight(), m_sensitivity * delta);
		if (input.getKey(Input.KEY_E))
			rotate(getForward(), m_sensitivity * delta);
		if (input.getKey(Input.KEY_Q))
			rotate(getForward(), -m_sensitivity * delta);
	}

	public void move(Vec3f direction, float amount)
	{
		m_pos.set(m_pos.add(direction.scale(amount)));
	}

	public void rotate(Vec3f axis, float theta)
	{
		Quaternion rotation = new Quaternion().createFromAxisAngle(axis.x, axis.y, axis.z, theta);
		m_rot = rotation.mul(m_rot).normalize();
	}

	public Vec3f getPos()
	{
		return m_pos;
	}

	public Quaternion getRot()
	{
		return m_rot;
	}

	public Vec3f getForward()
	{
		return m_rot.getForwardVector();
	}

	public Vec3f getRight()
	{
		return m_rot.getRightVector();
	}

	public Vec3f getUp()
	{
		return m_rot.getUpVector();
	}

}