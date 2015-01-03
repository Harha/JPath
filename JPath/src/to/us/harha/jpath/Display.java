package to.us.harha.jpath;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Arrays;

import javax.swing.JFrame;

import to.us.harha.jpath.util.math.Vec3f;

public class Display extends Canvas
{
	private String			m_title;
	private int				m_width;
	private int				m_height;
	private int				m_scale;
	private int[]			m_pixels;
	private BufferedImage	m_image;
	private Dimension		m_dimension;
	private JFrame			m_jframe;

	public Display(int width, int height, int scale, String title)
	{
		m_title = title;
		m_width = width;
		m_height = height;
		m_scale = scale;
	}

	public void create()
	{
		// Create the bitmap
		if (m_image == null)
		{
			m_image = new BufferedImage(m_width, m_height, BufferedImage.TYPE_INT_RGB);
			m_pixels = ((DataBufferInt) m_image.getRaster().getDataBuffer()).getData();
		}

		// Create the jframe
		if (m_jframe == null)
		{
			m_dimension = new Dimension(m_width * m_scale, m_height * m_scale);
			setPreferredSize(m_dimension);
			m_jframe = new JFrame();
			m_jframe.setResizable(false);
			m_jframe.setTitle(m_title);
			m_jframe.add(this);
			m_jframe.pack();
			m_jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			m_jframe.setLocationRelativeTo(null);
			m_jframe.setVisible(true);
		}
	}

	public void clear()
	{
		Arrays.fill(m_pixels, 0x000000);
	}

	public void drawPixelVec3f(int x, int y, Vec3f v)
	{
		if (x < 0 || x > m_width || y < 0 || y > m_height)
			return;

		// Get the 2D index in the 1D array
		int index = x + y * m_width;

		// Calculate the hexadecimal color from the vector parameters
		long red = (long) (v.x * 255.0f);
		long green = (long) (v.y * 255.0f);
		long blue = (long) (v.z * 255.0f);
		long hex_value = ((red << 16) | (green << 8) | blue);

		m_pixels[index] = (int) hex_value;
	}

	public void drawPixelInt(int x, int y, int color)
	{
		if (x < 0 || x >= m_width || y < 0 || y >= m_height)
			return;
		m_pixels[x + y * m_width] = color;
	}

	public int getWidth()
	{
		return m_width;
	}

	public int getHeight()
	{
		return m_height;
	}

	public int[] getPixels()
	{
		return m_pixels;
	}

	public BufferedImage getImage()
	{
		return m_image;
	}

	public Dimension getDimension()
	{
		return m_dimension;
	}

	public JFrame getJFrame()
	{
		return m_jframe;
	}

	public float getAR()
	{
		return (float) m_width / (float) m_height;
	}
	
	public int getScale()
	{
		return m_scale;
	}

	public void setWidth(int m_width)
	{
		this.m_width = m_width;
	}

	public void setHeight(int m_height)
	{
		this.m_height = m_height;
	}

	public void setPixels(int[] m_pixels)
	{
		this.m_pixels = m_pixels;
	}

	public void setImage(BufferedImage m_image)
	{
		this.m_image = m_image;
	}

	public void setDimension(Dimension m_dimension)
	{
		this.m_dimension = m_dimension;
	}

	public void setJFrame(JFrame m_jframe)
	{
		this.m_jframe = m_jframe;
	}

	public void setScale(int m_scale)
	{
		this.m_scale = m_scale;
	}

}
