package to.us.harha.jpath.tracer.object;

import java.util.ArrayList;

import to.us.harha.jpath.util.math.Mat4f;
import to.us.harha.jpath.util.math.Primitive;
import to.us.harha.jpath.util.math.Transform;

public class TracerObject
{
	private ArrayList<Primitive> m_primitives;
	private Material             m_material;
	private Transform            m_transform;

	public TracerObject(ArrayList<Primitive> primitives, Material material, Transform transform)
	{
		m_primitives = primitives;
		m_material = material;
		m_transform = transform;
	}

	public TracerObject(Material material)
	{
		m_primitives = new ArrayList<Primitive>();
		m_material = material;
		m_transform = new Transform();
	}

	public TracerObject()
	{
		m_primitives = new ArrayList<Primitive>();
		m_material = null;
		m_transform = new Transform();
	}

	public void updateTransform()
	{
		for (Primitive p : m_primitives)
		{
			p.setTransformedPos(Mat4f.mul(m_transform.getTransformation(), p.getPos(), 1.0f));
		}
	}

	public ArrayList<Primitive> getPrimitives()
	{
		return m_primitives;
	}

	public Primitive getPrimitive(int index)
	{
		return m_primitives.get(index);
	}

	public Material getMaterial()
	{
		return m_material;
	}

	public void addPrimitive(Primitive p)
	{
		m_primitives.add(p);
	}

	public void addPrimitives(ArrayList<Primitive> m_primitives)
	{
		this.m_primitives.addAll(m_primitives);
	}

	public void setMaterial(Material m_material)
	{
		this.m_material = m_material;
	}

}
