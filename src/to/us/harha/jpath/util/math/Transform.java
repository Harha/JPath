package to.us.harha.jpath.util.math;

public class Transform
{

	private Vec3f      m_position;
	//private Quaternion m_rotation;
	private Vec3f	   m_rotation;
	private Vec3f      m_scale;

	private Vec3f      m_oldPosition;
	private Quaternion m_oldRotation;
	private Vec3f      m_oldScale;

	public Transform()
	{
		m_position = new Vec3f(0.0f);
		m_rotation = new Vec3f();
		m_scale = new Vec3f(1.0f);
	}

	public Transform(Vec3f position, Vec3f rotation, Vec3f scale)
	{
		m_position = position;
		m_rotation = rotation;
		m_scale = scale;
	}

	public void update()
	{
		setOldComponents();
	}
	
	public void setOldComponents()
	{
		if (m_oldPosition == null)
		{
			m_oldPosition.set(m_position);
		}
		if (m_oldRotation == null)
		{
			//Vec3f.set(m_oldRotation, m_rotation);
		}
		if (m_oldScale == null)
		{
			m_oldScale.set(m_scale);
		}
	}

	public boolean hasChanged()
	{
		if (!m_position.equals(m_oldPosition))
			return true;

		if (!m_rotation.equals(m_oldRotation))
			return true;

		if (!m_scale.equals(m_oldScale))
			return true;

		return false;
	}

	public Mat4f getTransformation()
	{
		Mat4f translationMatrix = new Mat4f();
		Mat4f rotationMatrix = new Mat4f();
		Mat4f scaleMatrix = new Mat4f();

		translationMatrix.initTranslation(m_position.x, m_position.y, m_position.z);
		rotationMatrix.initRotation(m_rotation.x, m_rotation.y, m_rotation.z);
		scaleMatrix.initScale(m_scale.x, m_scale.y, m_scale.z);

		return Mat4f.mul(translationMatrix, Mat4f.mul(rotationMatrix, scaleMatrix));
	}

}
