package to.us.harha.jpath.tracer.object;

import java.util.ArrayList;

import to.us.harha.jpath.util.math.Primitive;

public class TracerObject
{

    private ArrayList<Primitive> m_primitives;
    private Material             m_material;

    public TracerObject(ArrayList<Primitive> primitives, Material material)
    {
        m_primitives = primitives;
        m_material = material;
    }

    public TracerObject(Material material)
    {
        m_primitives = new ArrayList<Primitive>();
        m_material = material;
    }

    public TracerObject()
    {
        m_primitives = new ArrayList<Primitive>();
        m_material = null;
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

    public void setPrimitives(ArrayList<Primitive> m_primitives)
    {
        this.m_primitives = m_primitives;
    }

    public void addPrimitive(Primitive p)
    {
        m_primitives.add(p);
    }

    public void setMaterial(Material m_material)
    {
        this.m_material = m_material;
    }

}
