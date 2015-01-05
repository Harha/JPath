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
	
	public void initRotation(Vec3f forward, Vec3f up, Vec3f right)
	{
		Vec3f f = forward;
		Vec3f u = up;
		Vec3f r = right;
		
		m[0][0] = r.x;		m[0][1] = r.y;		m[0][2] = r.z;		m[0][3] = 0;
		m[1][0] = u.x;		m[1][1] = u.y;		m[1][2] = u.z;		m[1][3] = 0;
		m[2][0] = f.x;		m[2][1] = f.y;		m[2][2] = f.z;		m[2][3] = 0;
		m[3][0] = 0;		m[3][1] = 0;		m[3][2] = 0;		m[3][3] = 1;
	}
	
	public void initScale(float x, float y, float z)
	{
		m[0][0] = x;		m[0][1] = 0;		m[0][2] = 0;		m[0][3] = 0;
		m[1][0] = 0;		m[1][1] = y;		m[1][2] = 0;		m[1][3] = 0;
		m[2][0] = 0;		m[2][1] = 0;		m[2][2] = z;		m[2][3] = 0;
		m[3][0] = 0;		m[3][1] = 0;		m[3][2] = 0;		m[3][3] = 1;
	}
	
	public void initRotation(float x, float y, float z)
	{
		Mat4f rx = new Mat4f();
		Mat4f ry = new Mat4f();
		Mat4f rz = new Mat4f();
		
		x = (float) Math.toRadians(x);
		y = (float) Math.toRadians(y);
		z = (float) Math.toRadians(z);
		
		float cosx = (float) Math.cos(x);
		float cosy = (float) Math.cos(y);
		float cosz = (float) Math.cos(z);
		float sinx = (float) Math.sin(x);
		float siny = (float) Math.sin(y);
		float sinz = (float) Math.sin(z);
		
		rx.m[0][0] = cosz;		rx.m[0][1] = -sinz;		rx.m[0][2] = 0;			rx.m[0][3] = 0;
		rx.m[1][0] = sinz;		rx.m[1][1] = cosz;		rx.m[1][2] = 0;			rx.m[1][3] = 0;
		rx.m[2][0] = 0;			rx.m[2][1] = 0;			rx.m[2][2] = 1;			rx.m[2][3] = 0;
		rx.m[3][0] = 0;			rx.m[3][1] = 0;			rx.m[3][2] = 0;			rx.m[3][3] = 1;
		
		ry.m[0][0] = 1;			ry.m[0][1] = 0;			ry.m[0][2] = 0;			ry.m[0][3] = 0;
		ry.m[1][0] = 0;			ry.m[1][1] = cosx;		ry.m[1][2] = -sinx;		ry.m[1][3] = 0;
		ry.m[2][0] = 0;			ry.m[2][1] = sinx;		ry.m[2][2] = cosx;		ry.m[2][3] = 0;
		ry.m[3][0] = 0;			ry.m[3][1] = 0;			ry.m[3][2] = 0;			ry.m[3][3] = 1;
		
		rz.m[0][0] = cosy;		rz.m[0][1] = 0;			rz.m[0][2] = -siny;		rz.m[0][3] = 0;
		rz.m[1][0] = 0;			rz.m[1][1] = 1;			rz.m[1][2] = 0;			rz.m[1][3] = 0;
		rz.m[2][0] = siny;		rz.m[2][1] = 0;			rz.m[2][2] = cosy;		rz.m[2][3] = 0;
		rz.m[3][0] = 0;			rz.m[3][1] = 0;			rz.m[3][2] = 0;			rz.m[3][3] = 1;
		
		m = Mat4f.mul(rz, Mat4f.mul(ry, rx)).getM();
	}
	
	public void initRotation(Vec3f forward, Vec3f up)
	{
		Vec3f f = forward;
		f = Vec3f.normalize(f);
		
		Vec3f r = up;
		r = Vec3f.normalize(r);
		r = Vec3f.cross(r, f);
		
		Vec3f u = Vec3f.cross(f, r);
		
		Mat4f m = new Mat4f();
		m.initRotation(f, u, r);
		
		this.m = m.getM();
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