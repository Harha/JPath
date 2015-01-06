package to.us.harha.jpath;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Input implements KeyListener
{

	public boolean[]        m_keyboard_keys;

	public static final int KEY_UP    = 38;
	public static final int KEY_DOWN  = 40;
	public static final int KEY_LEFT  = 37;
	public static final int KEY_RIGHT = 39;
	public static final int KEY_W     = 87;
	public static final int KEY_S     = 83;
	public static final int KEY_A     = 65;
	public static final int KEY_D     = 68;
	public static final int KEY_1     = 49;
	public static final int KEY_2     = 50;
	public static final int KEY_R     = 82;
	public static final int KEY_F     = 70;

	public Input()
	{
		m_keyboard_keys = new boolean[255];
	}

	@Override
	public void keyTyped(KeyEvent e)
	{

	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		m_keyboard_keys[e.getKeyCode()] = true;

		if (Config.debug_enabled)
		{
			Main.LOG.printMsg("Pressed key: " + e.getKeyCode());
		}
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		m_keyboard_keys[e.getKeyCode()] = false;

		if (Config.debug_enabled)
		{
			Main.LOG.printMsg("Released key: " + e.getKeyCode());
		}
	}

	public boolean getKey(int k)
	{
		return m_keyboard_keys[k];
	}

}
