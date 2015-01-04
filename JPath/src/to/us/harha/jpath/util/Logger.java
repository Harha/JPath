package to.us.harha.jpath.util;

public class Logger
{

    private String m_prefix;

    public Logger(String prefix)
    {
        m_prefix = prefix;
        printMsg("Logger has started!");
    }

    public void printMsg(String msg)
    {
        System.out.println("[" + m_prefix + "]: " + msg);
    }

    public void printErr(String msg)
    {
        System.err.println("[" + m_prefix + "]: ERROR: " + msg);
    }

}
