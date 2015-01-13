package to.us.harha.jpath.tracer;

import to.us.harha.jpath.Input;
import to.us.harha.jpath.util.math.Mat4f;
import to.us.harha.jpath.util.math.Quaternion;
import to.us.harha.jpath.util.math.Vec3f;

public class Camera
{

	private static final Vec3f YAXIS = new Vec3f(0.0f, 1.0f, 0.0f);

	private Vec3f              m_pos;
	private Quaternion         m_eye;
	private Vec3f              m_forward;
	private Vec3f              m_up;
	private Vec3f              m_right;
	private float              m_speed;
	private float              m_sensitivity;

	public Camera(Vec3f pos, Vec3f forward, Vec3f up, Vec3f right, float speed, float sensitivity)
	{
		m_pos = pos;
		m_eye = new Quaternion(0.0f, forward.x, forward.y, forward.z);
		m_forward = forward;
		m_up = up;
		m_right = right;
		m_speed = speed;
		m_sensitivity = sensitivity;
	}

	public void update(float delta, Input input)
	{
		// Camera movement
		if (input.getKey(Input.KEY_W))
		{
			setPos(Vec3f.add(getPos(), Vec3f.scale(getForward(), m_speed * delta)));
		} else if (input.getKey(Input.KEY_S))
		{
			setPos(Vec3f.add(getPos(), Vec3f.scale(getBack(), m_speed * delta)));
		}
		if (input.getKey(Input.KEY_D))
		{
			setPos(Vec3f.add(getPos(), Vec3f.scale(getRight(), m_speed * delta)));
		} else if (input.getKey(Input.KEY_A))
		{
			setPos(Vec3f.add(getPos(), Vec3f.scale(getLeft(), m_speed * delta)));
		}
		if (input.getKey(Input.KEY_R))
		{
			setPos(Vec3f.add(getPos(), Vec3f.scale(getUp(), m_speed * delta)));
		} else if (input.getKey(Input.KEY_F))
		{
			setPos(Vec3f.add(getPos(), Vec3f.scale(getDown(), m_speed * delta)));
		}

		// Camera eye vector rotation
		if (input.getKey(Input.KEY_UP))
		{
			rotate(m_right, m_sensitivity * delta);
		} else if (input.getKey(Input.KEY_DOWN))
		{
			rotate(m_right, -m_sensitivity * delta);
		}
		if (input.getKey(Input.KEY_RIGHT))
		{
			rotate(m_up, -m_sensitivity * delta);
		} else if (input.getKey(Input.KEY_LEFT))
		{
			rotate(m_up, m_sensitivity * delta);
		}
		if (input.getKey(Input.KEY_Q))
		{
			rotate(m_forward, m_sensitivity * delta);
		} else if (input.getKey(Input.KEY_E))
		{
			rotate(m_forward, -m_sensitivity * delta);
		}

		// Refresh all direction vectors
		recalcViewVectors();
	}

	public void move(Vec3f direction, float amount)
	{
		m_pos = Vec3f.add(m_pos, Vec3f.scale(direction, amount));
	}

	public void rotate(Vec3f axis, float theta)
	{
		Quaternion q = new Quaternion().createFromAxisAngle(axis.x, axis.y, axis.z, theta);
		Quaternion wForward = new Quaternion(0.0f, m_forward.x, m_forward.y, m_forward.z);
		Quaternion wUp = new Quaternion(0.0f, m_up.x, m_up.y, m_up.z);
		Quaternion wRight = new Quaternion(0.0f, m_right.x, m_right.y, m_right.z);
		Quaternion q_inv = Quaternion.conjugate(q);

		// Quaternion resultForward = Quaternion.normalize(Quaternion.mul(Quaternion.mul(q, wForward), q_inv));
		Quaternion resultUp = Quaternion.normalize(Quaternion.mul(Quaternion.mul(q, wUp), q_inv));
		Quaternion resultRight = Quaternion.normalize(Quaternion.mul(Quaternion.mul(q, wRight), q_inv));

		m_forward = Vec3f.normalize(Quaternion.mul(q, m_forward));
		m_up.x = resultUp.x;
		m_up.y = resultUp.y;
		m_up.z = resultUp.z;
		m_right.x = resultRight.x;
		m_right.y = resultRight.y;
		m_right.z = resultRight.z;
	}

	public void recalcViewVectors()
	{
		m_up = Vec3f.normalize(Vec3f.cross(m_right, m_forward));
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

	public float getSpeed()
	{
		return m_speed;
	}

	public float getSensitivity()
	{
		return m_sensitivity;
	}

	public void setPos(Vec3f pos)
	{
		m_pos = pos;
	}

	public void setForward(Vec3f forward)
	{
		m_forward = forward;
	}

	public void setSpeed(float m_speed)
	{
		this.m_speed = m_speed;
	}

	public void setSensitivity(float m_sensitivity)
	{
		this.m_sensitivity = m_sensitivity;
	}

}
