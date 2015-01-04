package to.us.harha.jpath.util;

public class TimeUtils
{

    private static final long SECOND = 1000000000L;

    public static double getTime()
    {
        return (double) System.nanoTime() / (double) SECOND;
    }

}
