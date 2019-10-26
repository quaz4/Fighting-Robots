package art.willstew.logic;

public class Util {
    
    static final float EPSILON = 0.001f;

    public static boolean equals (final float a, final float b) {
        if (a==b) return true;
        return Math.abs(a - b) < EPSILON;
    }

    // TODO Modify later
    public static int compare (final float a, final float b) {
        return equals(a, b) ? 0 : (a < b) ? -1 : +1;
    }
}