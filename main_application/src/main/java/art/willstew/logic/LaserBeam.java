package art.willstew.logic;

public class LaserBeam {

    private int startX;
    private int startY;

    private int endX;
    private int endY;

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

    public String toString() {
        return this.startX + ":" + this.startY + " -> " + this.endX + ":" + this.endY;
    }
}