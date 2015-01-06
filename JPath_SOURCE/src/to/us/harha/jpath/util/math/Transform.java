package to.us.harha.jpath.util.math;

public class Transform
{

	private Vec3f m_position;
	private Vec3f m_rotation;
	private Vec3f m_scale;

	public Transform()
	{
		m_position = new Vec3f(0.0f);
		m_rotation = new Vec3f();
		m_scale = new Vec3f(1.0f);
	}

	public Transform(Vec3f pos, Vec3f rot, Vec3f scale)
	{
		m_position = pos;
		m_rotation = rot;
		m_scale = scale;
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
