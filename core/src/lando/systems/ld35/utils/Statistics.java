package lando.systems.ld35.utils;

/**
 * Brian Ploeckelman created on 4/18/2016.
 */
public class Statistics {

    public static int numDeaths = 0;
    public static int numResets = 0;
    public static int numShapeShifts = 0;
    public static int numLevelsCompleted = 0;
    public static long endTime = 0L;

    public static void reset() {
        numDeaths = 0;
        numResets = 0;
        numShapeShifts = 0;
        numLevelsCompleted = 0;
        endTime = 0L;
    }

}
