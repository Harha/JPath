package to.us.harha.jpath.tracer;

import java.util.ArrayList;

import to.us.harha.jpath.tracer.object.Material;
import to.us.harha.jpath.tracer.object.Mesh;
import to.us.harha.jpath.tracer.object.TracerObject;
import to.us.harha.jpath.util.math.Plane;
import to.us.harha.jpath.util.math.Primitive;
import to.us.harha.jpath.util.math.Quaternion;
import to.us.harha.jpath.util.math.Sphere;
import to.us.harha.jpath.util.math.Transform;
import to.us.harha.jpath.util.math.Vec3f;

public class Scene
{

	private ArrayList<TracerObject> m_objects;
	private ArrayList<Camera>       m_cameras;

	public Scene()
	{
		m_objects = new ArrayList<TracerObject>();
		m_cameras = new ArrayList<Camera>();

		m_cameras.add(new Camera(new Vec3f(0, 1.0f, 5.0f), new Quaternion().initIdentity().mul(new Quaternion().createFromAxisAngle(1, 0, 0, 5)), 0.0025f, 0.1f));

		Material mat_diffuse_white = new Material(new Vec3f(0.0f), new Vec3f(1.0f));
		Material mat_diffuse_blue = new Material(new Vec3f(0.0f), new Vec3f(0.0f, 0.0f, 1.0f));
		Material mat_refractive_glass = new Material(new Vec3f(0.0f), 0.0f, 1.0f, 1.52f, 0.0f);
		Material mat_mirror = new Material(new Vec3f(0.0f, 1.0f, 0.0f), 1.0f, 0.0f, 1.0f, 0.0f);
		Material mat_light_white = new Material(new Vec3f(12.5f), new Vec3f(0.0f));

		Primitive plane_floor = new Plane(new Vec3f(0.0f, 0.0f, 0.0f), new Vec3f(0.0f, 1.0f, 0.0f));
		Primitive plane_ceiling = new Plane(new Vec3f(0.0f, 4.0f, 0.0f), new Vec3f(0.0f, -1.0f, 0.0f));
		Primitive plane_left = new Plane(new Vec3f(-2.5f, 0.0f, 0.0f), new Vec3f(1.0f, 0.0f, 0.0f));
		Primitive plane_right = new Plane(new Vec3f(2.5f, 0.0f, 0.0f), new Vec3f(-1.0f, 0.0f, 0.0f));
		Primitive plane_forward = new Plane(new Vec3f(0.0f, 0.0f, 4.0f), new Vec3f(0.0f, 0.0f, -1.0f));
		Primitive plane_back = new Plane(new Vec3f(0.0f, 0.0f, -4.0f), new Vec3f(0.0f, 0.0f, 1.0f));

		TracerObject obj_lamp_0 = new TracerObject(new Mesh("lamp.obj"), mat_light_white, new Transform(new Vec3f(0.0f, 4.0f, 0.0f), new Quaternion().initIdentity(), new Vec3f(0.5f, 1.0f, 0.5f)));
		TracerObject obj_cube_0 = new TracerObject(new Mesh("cube.obj"), mat_diffuse_white, new Transform(new Vec3f(1.0f, 0.25f, 2.0f), new Quaternion().initIdentity().mul(new Quaternion().createFromAxisAngle(0, 1, 0, 45)), new Vec3f(0.25f)));
		TracerObject obj_diffuse_white = new TracerObject(mat_diffuse_white);
		TracerObject obj_diffuse_blue = new TracerObject(mat_diffuse_blue);
		TracerObject obj_refractive_glass = new TracerObject(mat_refractive_glass);
		TracerObject obj_mirror = new TracerObject(mat_mirror);
		TracerObject obj_light_white = new TracerObject(mat_light_white);

		obj_diffuse_white.addPrimitive(plane_floor);
		obj_diffuse_white.addPrimitive(plane_ceiling);
		obj_diffuse_white.addPrimitive(plane_left);
		obj_diffuse_blue.addPrimitive(plane_right);
		obj_diffuse_white.addPrimitive(plane_forward);
		obj_diffuse_white.addPrimitive(plane_back);

		obj_refractive_glass.addPrimitive(new Sphere(new Vec3f(-0.75f, 0.6f, 0.5f), 0.5f));
		obj_mirror.addPrimitive(new Sphere(new Vec3f(0.75f, 0.6f, 0.5f), 0.5f));

		m_objects.add(obj_refractive_glass);
		m_objects.add(obj_mirror);
		m_objects.add(obj_lamp_0);
		m_objects.add(obj_cube_0);
		m_objects.add(obj_diffuse_white);
		m_objects.add(obj_diffuse_blue);
		m_objects.add(obj_light_white);
	}

	public ArrayList<TracerObject> getObjects()
	{
		return m_objects;
	}

	public ArrayList<Camera> getCameras()
	{
		return m_cameras;
	}

	public void setObjects(ArrayList<TracerObject> m_objects)
	{
		this.m_objects = m_objects;
	}

	public void setCameras(ArrayList<Camera> m_cameras)
	{
		this.m_cameras = m_cameras;
	}

}
