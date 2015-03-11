package to.us.harha.jpath.tracer;

import to.us.harha.jpath.Input;
import to.us.harha.jpath.util.math.Mat4f;
import to.us.harha.jpath.util.math.Quaternion;
import to.us.harha.jpath.util.math.Transform;
import to.us.harha.jpath.util.math.Vec3f;

public class Camera
{

	private Transform m_transform;
	private float     m_speed;
	private float     m_sensitivity;
	private boolean   m_moving;

	public Camera(Vec3f pos, Quaternion rot, float speed, float sensitivity)
	{
		m_transform = new Transform(pos, rot, new Vec3f(1.0f));
		m_speed = speed;
		m_sensitivity = sensitivity;
		m_moving = false;
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
		m_moving = true;
		m_transform.getPos().set(m_transform.getPos().add(direction.scale(amount)));
	}

	public void rotate(Vec3f axis, float theta)
	{
		m_moving = true;
		Quaternion rotation = new Quaternion().createFromAxisAngle(axis.x, axis.y, axis.z, theta);
		m_transform.getRot().set(rotation.mul(m_transform.getRot()).normalize());
	}

	public Vec3f getPos()
	{
		return m_transform.getPos();
	}

	public Quaternion getRot()
	{
		return m_transform.getRot();
	}

	public Vec3f getForward()
	{
		return m_transform.getRot().getForwardVector();
	}

	public Vec3f getRight()
	{
		return m_transform.getRot().getRightVector();
	}

	public Vec3f getUp()
	{
		return m_transform.getRot().getUpVector();
	}

	public boolean isMoving()
	{
		if (m_moving)
		{
			m_moving = false;
			return true;
		}
		return false;
	}

}