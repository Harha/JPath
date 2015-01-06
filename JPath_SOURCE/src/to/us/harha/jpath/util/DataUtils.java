package to.us.harha.jpath.util;

import java.util.ArrayList;

public class DataUtils
{

	private DataUtils()
	{

	}

	public static String[] removeEmptyStrings(String[] data)
	{
		ArrayList<String> result = new ArrayList<String>();

		for (int i = 0; i < data.length; i++)
			if (!data[i].equals(""))
				result.add(data[i]);

		String[] res = new String[result.size()];
		result.toArray(res);

		return res;
	}

}
