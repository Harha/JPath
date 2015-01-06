package to.us.harha.jpath.tracer.object;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

import to.us.harha.jpath.Config;
import to.us.harha.jpath.Main;
import to.us.harha.jpath.util.DataUtils;
import to.us.harha.jpath.util.math.Primitive;
import to.us.harha.jpath.util.math.Triangle;
import to.us.harha.jpath.util.math.Vec3f;

public class Mesh
{

	private ArrayList<Primitive> m_primitives;

	public Mesh(String fileName)
	{
		m_primitives = new ArrayList<Primitive>();
		loadMeshFromObjFile(fileName);
	}

	private void loadMeshFromObjFile(String fileName)
	{
		String[] splitArray = fileName.split("\\.");
		String ext = splitArray[splitArray.length - 1];

		if (!ext.equals("obj"))
		{
			Main.LOG.printErr("Error: File format not supported for mesh data: " + ext);
			new Exception().printStackTrace();
			System.exit(1);
		}

		ArrayList<Float> vertices = new ArrayList<Float>();
		ArrayList<Integer> indices = new ArrayList<Integer>();

		BufferedReader meshReader = null;

		try
		{
			meshReader = new BufferedReader(new FileReader(Config.MODEL_PATH + fileName));
			String line;

			while ((line = meshReader.readLine()) != null)
			{
				String[] tokens = line.split(" ");
				tokens = DataUtils.removeEmptyStrings(tokens);

				if (tokens.length == 0 || tokens[0].equals("#"))
					continue;
				else if (tokens[0].equals("v"))
				{
					vertices.add((float) Double.parseDouble(tokens[1]));
					vertices.add((float) Double.parseDouble(tokens[2]));
					vertices.add((float) Double.parseDouble(tokens[3]));
				} else if (tokens[0].equals("f"))
				{
					indices.add(Integer.parseInt(tokens[1]));
					indices.add(Integer.parseInt(tokens[2]));
					indices.add(Integer.parseInt(tokens[3]));

					// Very simple triangulation if the mesh is in a quad form instead of triangles
					if (tokens.length > 4)
					{
						indices.add(Integer.parseInt(tokens[1]));
						indices.add(Integer.parseInt(tokens[3]));
						indices.add(Integer.parseInt(tokens[4]));
					}
				}
			}

			meshReader.close();

			for (int i = 0; i < indices.size(); i += 3)
			{
				int index_0 = indices.get(i) - 1;
				int index_1 = indices.get(i + 1) - 1;
				int index_2 = indices.get(i + 2) - 1;
				Vec3f vertex_1 = new Vec3f(vertices.get(0 + index_0 * 3), vertices.get(1 + index_0 * 3), vertices.get(2 + index_0 * 3));
				Vec3f vertex_2 = new Vec3f(vertices.get(0 + index_1 * 3), vertices.get(1 + index_1 * 3), vertices.get(2 + index_1 * 3));
				Vec3f vertex_3 = new Vec3f(vertices.get(0 + index_2 * 3), vertices.get(1 + index_2 * 3), vertices.get(2 + index_2 * 3));
				Vec3f[] verts = new Vec3f[] { vertex_1, vertex_2, vertex_3 };
				m_primitives.add(new Triangle(verts));
			}

			Main.LOG.printMsg("Mesh " + Config.MODEL_PATH + fileName + " loaded succesfully!");

		} catch (Exception e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}

	public ArrayList<Primitive> getPrimitives()
	{
		return m_primitives;
	}

}
