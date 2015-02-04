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

		m_cameras.add(new Camera(new Vec3f(0, 1.5f, 6), new Quaternion(0, 0, 1, 0), 5, 64));

		Mesh mesh_lamp_0 = new Mesh("lamp.obj");
		Mesh mesh_lamp_1 = new Mesh("lamp.obj");
		Mesh mesh_cube_0 = new Mesh("cube.obj");

		Material mat_white_diffuse = new Material(new Vec3f(0.0f), new Vec3f(1.0f), 0.0f, 0.0f, 0.0f, 0.0f);
		Material mat_red_diffuse = new Material(new Vec3f(0.0f), new Vec3f(1.0f, 0.0f, 0.0f));
		Material mat_green_diffuse = new Material(new Vec3f(0.0f), new Vec3f(0.0f, 1.0f, 0.0f));
		Material mat_blue_diffuse = new Material(new Vec3f(0.0f), new Vec3f(0.0f, 0.0f, 1.0f));
		Material mat_yellow_diffuse = new Material(new Vec3f(0.0f), new Vec3f(1.0f, 1.0f, 0.0f));
		Material mat_white_shiny = new Material(new Vec3f(0.0f), new Vec3f(1.0f), 0.5f, 0.0f, 0.0f, 0.0f);
		Material mat_red_shiny = new Material(new Vec3f(0.0f), new Vec3f(1.0f, 0.0f, 0.0f), 0.75f, 0.0f, 0.0f, 0.0f);
		Material mat_green_shiny = new Material(new Vec3f(0.0f), new Vec3f(0.0f, 1.0f, 0.0f), 1.0f, 0.0f, 0.0f, 0.0f);
		Material mat_blue_shiny = new Material(new Vec3f(0.0f), new Vec3f(0.0f, 0.0f, 1.0f), 0.9f, 0.0f, 0.0f, 0.0f);
		Material mat_yellow_shiny = new Material(new Vec3f(0.0f), new Vec3f(1.0f, 1.0f, 0.0f), 0.5f, 0.0f, 0.0f, 0.0f);
		Material mat_black_mirror = new Material(new Vec3f(0.0f), new Vec3f(0.0f), 1.0f, 0.0f, 0.0f, 0.0f);
		Material mat_black_glass_ri52 = new Material(new Vec3f(0.0f), new Vec3f(), 0.0f, 1.25f, 1.52f, 0.0f);
		Material mat_black_glass_ri37 = new Material(new Vec3f(0.0f), new Vec3f(), 0.0f, 1.25f, 1.37f, 0.0f);
		Material mat_white_light = new Material(new Vec3f(0.9568f, 1.0f, 0.9803f).scale(5.0f), new Vec3f());

		TracerObject obj_white_diffuse = new TracerObject(mat_white_diffuse);
		TracerObject obj_red_diffuse = new TracerObject(mat_red_diffuse);
		TracerObject obj_green_diffuse = new TracerObject(mat_green_diffuse);
		TracerObject obj_blue_diffuse = new TracerObject(mat_blue_diffuse);
		TracerObject obj_yellow_diffuse = new TracerObject(mat_yellow_diffuse);
		TracerObject obj_white_shiny = new TracerObject(mat_white_shiny);
		TracerObject obj_red_shiny = new TracerObject(mat_red_shiny);
		TracerObject obj_green_shiny = new TracerObject(mat_green_shiny);
		TracerObject obj_blue_shiny = new TracerObject(mat_blue_shiny);
		TracerObject obj_yellow_shiny = new TracerObject(mat_yellow_shiny);
		TracerObject obj_black_mirror = new TracerObject(mat_black_mirror);
		TracerObject obj_black_glass_ri52 = new TracerObject(mat_black_glass_ri52);
		TracerObject obj_black_glass_ri37 = new TracerObject(mat_black_glass_ri37);
		TracerObject obj_white_light = new TracerObject(mat_white_light);

		TracerObject obj_light_0 = new TracerObject(mesh_lamp_0.getPrimitives(), mat_white_light, new Transform(new Vec3f(0.0f, 4.0f, 0.0f), new Vec3f(0.0f, 0.0f, 0.0f), new Vec3f(0.6f)));
		TracerObject obj_cube_0 = new TracerObject(mesh_cube_0.getPrimitives(), mat_white_diffuse, new Transform(new Vec3f(-1.25f, 0.5f, 1.25f), new Vec3f(0.0f, 0.0f, 0.0f), new Vec3f(0.5f)));

		Primitive plane_floor = new Plane(new Vec3f(0.0f, 0.0f, 0.0f), new Vec3f(0.0f, 1.0f, 0.0f));
		Primitive plane_ceiling = new Plane(new Vec3f(0.0f, 4.0f, 0.0f), new Vec3f(0.0f, -1.0f, 0.0f));
		Primitive plane_left = new Plane(new Vec3f(-2.5f, 0.0f, 0.0f), new Vec3f(1.0f, 0.0f, 0.0f));
		Primitive plane_right = new Plane(new Vec3f(2.5f, 0.0f, 0.0f), new Vec3f(-1.0f, 0.0f, 0.0f));
		Primitive plane_forward = new Plane(new Vec3f(0.0f, 0.0f, 5.0f), new Vec3f(0.0f, 0.0f, -1.0f));
		Primitive plane_back = new Plane(new Vec3f(0.0f, 0.0f, -5.0f), new Vec3f(0.0f, 0.0f, 1.0f));

		Primitive sphere_refractive_0 = new Sphere(new Vec3f(1.25f, 0.5f, 0.0f), 0.5f);
		Primitive sphere_refractive_1 = new Sphere(new Vec3f(2.0f, 2.0f, -2.5f), 0.5f);
		Primitive sphere_reflective = new Sphere(new Vec3f(-0.25f, 0.5f, -1.0f), 0.5f);

		obj_white_diffuse.addPrimitive(plane_floor);
		obj_white_diffuse.addPrimitive(plane_ceiling);
		obj_blue_diffuse.addPrimitive(plane_left);
		obj_red_diffuse.addPrimitive(plane_right);
		obj_white_diffuse.addPrimitive(plane_forward);
		obj_white_diffuse.addPrimitive(plane_back);
		obj_black_glass_ri52.addPrimitive(sphere_refractive_0);
		obj_black_glass_ri37.addPrimitive(sphere_refractive_1);
		obj_black_mirror.addPrimitive(sphere_reflective);

		m_objects.add(obj_light_0);
		m_objects.add(obj_cube_0);
		m_objects.add(obj_white_diffuse);
		m_objects.add(obj_blue_diffuse);
		m_objects.add(obj_red_diffuse);
		m_objects.add(obj_black_glass_ri52);
		m_objects.add(obj_black_glass_ri37);
		m_objects.add(obj_black_mirror);
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
