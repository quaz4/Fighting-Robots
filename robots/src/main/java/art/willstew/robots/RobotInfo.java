package art.willstew.robots;

import java.lang.Float;

public class RobotInfo {

    /**
     * Each robots name must be a unique non empty string
     */
    private String name;

    /**
     * Represents the robots x coordinate in the arena
     * Must be >= 0
     */
    private int x;

    /**
     * Represents the robots y coordinate in the arena
     * Must be >= 0
     */
    private int y;

    /**
     * Each robot starts with 100.0 units of “health”, and loses 35.0 units whenever it getshit, down to a minimum of 0.0.
     */
    private float health;

    /**
     * RobotInfo object holds information about the robot, including
     * its name, location and health.
     */
    public RobotInfo(String name, int x, int y, float health) {
        this.setName(name);
        this.setX(x);
        this.setY(y);
        this.setHealth(health);
    }

    private void setName(String name) {
         // Name must be a non empty string
        if (name.length() == 0) {
            throw new IllegalArgumentException("Name must be a non empty string");
        }

        this.name = name;
    }

    private void setX(int x) {
        if (x < 0) {
            throw new IllegalArgumentException("X must be an int greater than or equal to 0");
        }

        this.x = x;
    }

    private void setY(int y) {
        if (y < 0) {
            throw new IllegalArgumentException("Y must be an int greater than or equal to 0");
        }

        this.y = y;
    }

    private void setHealth(float health) {
        // Compare will return 0 if health is less than 0.0f
        if (Float.compare(health, 0.0f) == 0) {
            throw new IllegalArgumentException("Health must be an int greater than or equal to 0");
        }

        this.health = health;
    }
    
}