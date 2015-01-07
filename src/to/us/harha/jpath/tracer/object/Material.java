package to.us.harha.jpath.tracer.object;

import to.us.harha.jpath.util.math.Vec3f;

public class Material
{

    private Vec3f m_emittance;
    private Vec3f m_reflectance;
    private float m_reflectivity;
    private float m_refractivity;
    private float m_refractivityIndex;
    private float m_glossiness;

    public Material(Vec3f emittance, Vec3f reflectance, float reflectivity, float refractivity, float refractivityIndex, float glossiness)
    {
        m_emittance = emittance;
        m_reflectance = reflectance;
        m_reflectivity = reflectivity;
        m_refractivity = refractivity;
        m_refractivityIndex = refractivityIndex;
        m_glossiness = glossiness;
    }

    public Material(Vec3f reflectance, float reflectivity, float refractivity, float refractivityIndex, float glossiness)
    {
        m_emittance = new Vec3f();
        m_reflectance = reflectance;
        m_reflectivity = reflectivity;
        m_glossiness = glossiness;
        m_refractivity = refractivity;
        m_refractivityIndex = refractivityIndex;
    }

    public Material(Vec3f emittance, Vec3f reflectance)
    {
        m_emittance = emittance;
        m_reflectance = reflectance;
        m_reflectivity = 0.0f;
        m_refractivity = 0.0f;
        m_refractivityIndex = 0.0f;
        m_glossiness = 0.0f;
    }

    public Vec3f getEmittance()
    {
        return m_emittance;
    }

    public Vec3f getReflectance()
    {
        return m_reflectance;
    }

    public float getReflectivity()
    {
        return m_reflectivity;
    }

    public float getRefractivity()
    {
        return m_refractivity;
    }

    public float getRefractivityIndex()
    {
        return m_refractivityIndex;
    }

    public float getGlossiness()
    {
        return m_glossiness;
    }

    public void setEmittance(Vec3f m_emittance)
    {
        this.m_emittance = m_emittance;
    }

    public void setReflectance(Vec3f m_reflectance)
    {
        this.m_reflectance = m_reflectance;
    }

    public void setReflectivity(float m_reflectivity)
    {
        this.m_reflectivity = m_reflectivity;
    }

    public void setRefractivity(float m_refractivity)
    {
        this.m_refractivity = m_refractivity;
    }

    public void setRefractivityIndex(float m_refractivityIndex)
    {
        this.m_refractivityIndex = m_refractivityIndex;
    }

    public void setGlossiness(float m_glossiness)
    {
        this.m_glossiness = m_glossiness;
    }

}
