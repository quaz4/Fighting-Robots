package art.willstew.logic;

import java.util.Hashtable;

import art.willstew.robots.RobotInfo;

/** 
 * Class for managing the movement of robots around the grid
 * 
 * It uses a Hashtable for quick access to where each of the robots is
 * Each robot is keyed to its x value and y value, seperated by :
 * e.g. x = 10 and y = 2 would have a key of 10:2
 */
public class MovementManager {

    private Object monitor = new Object();

    private int x;
    private int y;
    private Hashtable<String, RobotInfo> grid;

    /**
     * Init the hash table and grid size
     * @param x Max grid x size
     * @param y Max grid y size
     */
    public MovementManager(int x, int y) {
        this.x = x;
        this.y = y;

        this.grid = new Hashtable<String, RobotInfo>();
    }

    // Add a new robot to the grid
    public void add(RobotInfo robot) {
        synchronized(monitor) {
            String hash = robot.getX() + ":" + robot.getY();
            this.grid.put(hash, robot);
        }
    }

    /**
     * Move the robot provided, deltaX and deltaY can be positive or negative
     * and specify how far to move in each direction
     * @param robot The robot to move
     * @param deltaX The relative distance to move in the x direction
     * @param deltaY The relative distance to move in the y direction
     * @return Retuns if the move was valid
     */
    public boolean move(RobotInfo robot, int deltaX, int deltaY) {
        // Calculate the new positions
        int newX = robot.getX() + deltaX;
        int newY = robot.getY() + deltaY;

        // Invalid move if outside the grid
        if (newX >= this.x || newX < 0 || newY >= this.y || newY < 0) {
            return false;
        }

        synchronized(monitor) {
            // Invalid move if grid is occupied
            String hash = newX + ":" + newY;
            if (this.grid.containsKey(hash)) {
                return false;
            }

            // Remove from old position
            this.grid.remove(robot.getX() + ":" + robot.getY());

            robot.setX(newX);
            robot.setY(newY);

            // Return to new position
            this.grid.put(hash, robot);
        }

        return true;
    }

    /**
     * Tests if the location in the grid is occupied
     * @param x X coordinate to test
     * @param y Y coordinate to test
     * @return Returns if the location is occupied
     */
    public boolean occupied(int x, int y) {
        synchronized(monitor) {
            String hash = x + ":" + y;
            if (this.grid.containsKey(hash)) {
                return true;
            }
    
            return false;
        }
    }

    /**
     * Gets the robot that is at the specified location, or returns null
     * @param x X coordinate of the robot
     * @param y Y coordinate of the robot
     * @return Returns the robot or null
     */
    public RobotInfo getRobot(int x, int y) {
        synchronized(monitor) {
            String hash = x + ":" + y;
            return this.grid.get(hash);
        }
    }
}