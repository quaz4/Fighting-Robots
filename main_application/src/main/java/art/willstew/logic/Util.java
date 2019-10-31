package art.willstew.logic;

/**
 * Class for utility functions
 */
public class Util {
    
    static final float EPSILON = 0.001f;

    /**
     * Compare if two floats are equal
     * @param a First float to compare
     * @param b Second float to compare
     * @return Returns true if they are equal, false if not
     */
    public static boolean equals (final float a, final float b) {
        if (a==b) return true;
        return Math.abs(a - b) < EPSILON;
    }

    /**
     * Compares two floats and returns if a is larger/smaller than b, or if they are equal
     * @param a First float
     * @param b Second float
     * @return Returns 0 if equal, -1 if a<b and +1 if a>b
     */
    public static int compare (final float a, final float b) {
        if (equals(a, b)) {
            return 0;
        } else {
            if ((a < b)) {
                return -1;
            } else {
                return 1;
            }
        }
    }
}