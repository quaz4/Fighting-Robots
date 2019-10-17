package art.willstew.robots;

public interface RobotInfo {

    /**
     * Each robots name must be a unique non empty string
     */
    public String name = "";

    /**
     * Represents the robots x coordinate in the arena
     * Must be >= 0
     */
    public int x = 0;

    /**
     * Represents the robots y coordinate in the arena
     * Must be >= 0
     */
    public int y = 0;

    /**
     * Each robot starts with 100.0 units of “health”, and loses 35.0 units whenever it getshit, down to a minimum of 0.0.
     */
    public float health = 100.0f;

    public void setName(String name);

    public String getName();

    public void setX(int x);

    public int getX();

    public void setY(int y);

    public int getY();

    public void setHealth(float health);

    public float getHealth();
    
}