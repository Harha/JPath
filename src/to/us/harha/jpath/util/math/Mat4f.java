package to.us.harha.jpath.util.math;

public class Mat4f
{

	// Matrix row & column values as in i,j
	public float[][]	m;

	/*
	 * Constructor Mat4f()
	 * @info: Rows and columns are 0 by default
	 */
	public Mat4f()
	{
		m = new float[4][4];
	}
	
	public void initIdentity()
	{
		m[0][0] = 1;		m[0][1] = 0;		m[0][2] = 0;		m[0][3] = 0;
		m[1][0] = 0;		m[1][1] = 1;		m[1][2] = 0;		m[1][3] = 0;
		m[2][0] = 0;		m[2][1] = 0;		m[2][2] = 1;		m[2][3] = 0;
		m[3][0] = 0;		m[3][1] = 0;		m[3][2] = 0;		m[3][3] = 1;
	}
	
	public void initTranslation(float x, float y, float z)
	{
		m[0][0] = 1;		m[0][1] = 0;		m[0][2] = 0;		m[0][3] = x;
		m[1][0] = 0;		m[1][1] = 1;		m[1][2] = 0;		m[1][3] = y;
		m[2][0] = 0;		m[2][1] = 0;		m[2][2] = 1;		m[2][3] = z;
		m[3][0] = 0;		m[3][1] = 0;		m[3][2] = 0;		m[3][3] = 1;
	}
	
	public void initScale(float x, float y, float z)
	{
		m[0][0] = x;		m[0][1] = 0;		m[0][2] = 0;		m[0][3] = 0;
		m[1][0] = 0;		m[1][1] = y;		m[1][2] = 0;		m[1][3] = 0;
		m[2][0] = 0;		m[2][1] = 0;		m[2][2] = z;		m[2][3] = 0;
		m[3][0] = 0;		m[3][1] = 0;		m[3][2] = 0;		m[3][3] = 1;
	}
	
	public void initRotation(Quaternion q)
	{
		Vec3f f = q.getForwardVector();
		Vec3f u = q.getUpVector();
		Vec3f r = q.getRightVector();
		
		m[0][0] = r.x;		m[0][1] = r.y;		m[0][2] = r.z;		m[0][3] = 0;
		m[1][0] = u.x;		m[1][1] = u.y;		m[1][2] = u.z;		m[1][3] = 0;
		m[2][0] = f.x;		m[2][1] = f.y;		m[2][2] = f.z;		m[2][3] = 0;
		m[3][0] = 0;		m[3][1] = 0;		m[3][2] = 0;		m[3][3] = 1;
	}
	
	/*
	 * Ugly euler angles, I hate them.
	 * Luckily all my rotations are now using Quaternions, which are beautiful and elegant.
	 */
	public void initRotation(float x, float y, float z)
	{
		Mat4f Mx = new Mat4f();
		Mat4f My = new Mat4f();
		Mat4f Mz = new Mat4f();
		
		Mx.initIdentity();
		My.initIdentity();
		Mz.initIdentity();
		
		Mx.m[1][1] = (float) Math.cos(Math.toRadians(x));
		Mx.m[1][2] = (float) Math.sin(Math.toRadians(x));
		Mx.m[2][1] = (float) -Math.sin(Math.toRadians(x));
		Mx.m[2][2] = (float) Math.cos(Math.toRadians(x));
		
		My.m[0][0] = (float) Math.cos(Math.toRadians(y));
		My.m[0][2] = (float) -Math.sin(Math.toRadians(y));
		My.m[2][0] = (float) Math.sin(Math.toRadians(y));
		My.m[2][2] = (float) Math.cos(Math.toRadians(y));
		
		Mz.m[0][0] = (float) Math.cos(Math.toRadians(z));
		Mz.m[0][1] = (float) Math.sin(Math.toRadians(z));
		Mz.m[1][0] = (float) -Math.sin(Math.toRadians(z));
		Mz.m[1][1] = (float) Math.cos(Math.toRadians(z));
		
		m = Mat4f.mul(Mz, Mat4f.mul(My, Mx)).getM();
	}
	
	public static Mat4f mul(Mat4f left, Mat4f right)
	{
		Mat4f data = new Mat4f();
		
		for (int i = 0; i < 4; i++)
		{
			for (int j = 0; j < 4; j++)
			{
				data.m[i][j] = left.m[i][0] * right.m[0][j] +
							   left.m[i][1] * right.m[1][j] +
							   left.m[i][2] * right.m[2][j] +
							   left.m[i][3] * right.m[3][j];
			}
		}
		
		return data;
	}
	
	public static Vec3f mul(Mat4f left, Vec3f right, float w)
	{
		Vec3f result = new Vec3f();
		
		for (int i = 0; i < 4; i++)
		{
			float value = 0.0f;
			for (int j = 0; j < 4; j++)
			{
				value += left.m[i][j] * right.getComponent(j, w);
			}
			result.setComponent(i, value);
		}
		
		return result;
	}
	
	public float[][] getM()
	{
		float[][] data = new float[4][4];
		
		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 4; j++)
				data[i][j] = m[i][j];
		
		return data;
	}

}