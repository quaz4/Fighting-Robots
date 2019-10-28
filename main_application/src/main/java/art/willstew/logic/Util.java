package art.willstew.logic;

/**
 * Class for utility functions
 */
public class Util {
    
    static final float EPSILON = 0.001f;

    public static boolean equals (final float a, final float b) {
        if (a==b) return true;
        return Math.abs(a - b) < EPSILON;
    }

    public static int compare (final float a, final float b) {
        return equals(a, b) ? 0 : (a < b) ? -1 : +1;
    }
}