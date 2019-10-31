package art.willstew.logic;

/**
 * Container class for storing information about laser beams
 * The class doesn't do anything other than store the start and end point
 */
public class LaserBeam {

    // Start point of the laser
    private int startX;
    private int startY;

    // End point of the laser
    private int endX;
    private int endY;

    /**
     * Create a new laser beam object
     * @param startX Starting x coordinate
     * @param startY Starting y coordinate
     * @param endX Ending x coordinate
     * @param endY Ending y coordinate
     */
    public LaserBeam(int startX, int startY, int endX, int endY) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
    }

    public int getStartX() {
        return this.startX;
    }

    public int getStartY() {
        return this.startY;
    }

    public int getEndX() {
        return this.endX;
    }

    public int getEndY() {
        return this.endY;
    }
}