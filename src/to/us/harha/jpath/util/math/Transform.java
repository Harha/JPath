package to.us.harha.jpath.util.math;

public class Transform
{

	private Vec3f      m_position;
	private Quaternion m_rotation;
	private Vec3f      m_scale;

	public Transform()
	{
		m_position = new Vec3f(0.0f);
		m_rotation = new Quaternion().initIdentity();
		m_scale = new Vec3f(1.0f);
	}

	public Transform(Vec3f position, Quaternion rotation, Vec3f scale)
	{
		m_position = position;
		m_rotation = rotation;
		m_scale = scale;
	}

	public Mat4f getTransformation()
	{
		Mat4f translationMatrix = new Mat4f();
		Mat4f rotationMatrix = new Mat4f();
		Mat4f scaleMatrix = new Mat4f();

		translationMatrix.initTranslation(m_position.x, m_position.y, m_position.z);
		rotationMatrix.initRotation(m_rotation);
		scaleMatrix.initScale(m_scale.x, m_scale.y, m_scale.z);

		return Mat4f.mul(translationMatrix, Mat4f.mul(rotationMatrix, scaleMatrix));
	}

	public Vec3f getPos()
	{
		return m_position;
	}

	public Quaternion getRot()
	{
		return m_rotation;
	}

	public Vec3f getScale()
	{
		return m_scale;
	}

}
