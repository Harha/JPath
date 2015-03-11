package to.us.harha.jpath.tracer.object;

import java.util.ArrayList;

import to.us.harha.jpath.util.math.Mat4f;
import to.us.harha.jpath.util.math.Primitive;
import to.us.harha.jpath.util.math.Transform;
import to.us.harha.jpath.util.math.Vec3f;

public class TracerObject
{
	private Mesh      m_mesh;
	private Material  m_material;
	private Transform m_transform;

	public TracerObject(Mesh mesh, Material material, Transform transform)
	{
		m_mesh = mesh;
		m_material = material;
		m_transform = transform;
		updateTransform();
	}

	public TracerObject(Material material)
	{
		m_mesh = new Mesh();
		m_material = material;
		m_transform = new Transform();
	}

	public TracerObject()
	{
		m_mesh = new Mesh();
		m_material = null;
		m_transform = new Transform();
	}

	public void updateTransform()
	{
		for (Primitive p : m_mesh.getPrimitives())
		{
			p.mulVertices(m_transform.getTransformation());
		}
	}

	public ArrayList<Primitive> getPrimitives()
	{
		return m_mesh.getPrimitives();
	}

	public Primitive getPrimitive(int index)
	{
		return m_mesh.getPrimitives().get(index);
	}

	public Material getMaterial()
	{
		return m_material;
	}

	public void addPrimitive(Primitive p)
	{
		m_mesh.getPrimitives().add(p);
	}

	public void addPrimitives(ArrayList<Primitive> m_primitives)
	{
		this.m_mesh.getPrimitives().addAll(m_primitives);
	}

	public void setMaterial(Material m_material)
	{
		this.m_material = m_material;
	}

}
