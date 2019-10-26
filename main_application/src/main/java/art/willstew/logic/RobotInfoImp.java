package art.willstew.logic;

import art.willstew.robots.RobotInfo;

public class RobotInfoImp implements RobotInfo {

    /**
     * Monitor is used as a lock to make the class thread safe
     */
    Object monitor = new Object();

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
    public RobotInfoImp(String name, int x, int y, float health) {
        this.setName(name);
        this.setX(x);
        this.setY(y);
        this.setHealth(health);
    }

    public void setName(String name) {
        // Name must be a non empty string
        if (name.length() == 0) {
            throw new IllegalArgumentException("Name must be a non empty string");
        }

        synchronized(this.monitor) {
            this.name = name;
        }
    }

    public String getName() {
        synchronized(this.monitor) {
            return this.name;
        }
        
    }

    public void setX(int x) {
        if (x < 0) {
            throw new IllegalArgumentException("X must be an int greater than or equal to 0");
        }

        synchronized(this.monitor) {
            this.x = x;
        }
    }

    public int getX() {
        synchronized(this.monitor) {
            return this.x;
        }
    }

    public void setY(int y) {
        if (y < 0) {
            throw new IllegalArgumentException("Y must be an int greater than or equal to 0");
        }

        synchronized(this.monitor) {
            this.y = y;
        }
    }

    public int getY() {
        synchronized(this.monitor) {
            return this.y;
        }
    }

    public void setHealth(float health) {
        // Compare will return 0 if health is less than 0.0f
        if (Float.compare(health, 0.0f) <= 0) {
            // throw new IllegalArgumentException("Health must be an int greater than or equal to 0");
            health = 0.0f;
        }

        synchronized(this.monitor) {
            this.health = health;
        }

        // System.out.println(this.name + "'s health is now " + this.health);
    }

    // public void takeHit() {
    //     synchronized(this.monitor) {
    //         this.health -= 35.0f;
    //     }
    // }

    public float getHealth() {
        synchronized(this.monitor) {
            return this.health;
        }
    }

    public String toString() {
        synchronized(this.monitor) {
            return this.name + " (" + Math.round(this.health / 100 * 100) + "%)";
        }
    }
}