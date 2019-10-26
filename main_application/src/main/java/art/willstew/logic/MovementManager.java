package art.willstew.logic;

import java.util.Hashtable;

import art.willstew.robots.RobotInfo;

/** 
 * Class for managing the movement of robots around the grid
 */

public class MovementManager {

    private int x;
    private int y;
    private Hashtable<String, RobotInfo> grid;

    public MovementManager(int x, int y) {
        // TODO Update this to fetch from config object
        this.x = x;
        this.y = y;

        this.grid = new Hashtable<String, RobotInfo>();
    }

    public void add(RobotInfo robot) {
        String hash = robot.getX() + ":" + robot.getY();

        this.grid.put(hash, robot);
    }

    /**
     * Move the robot provided, deltaX and deltaY can be positive or negative
     */
    public boolean move(RobotInfo robot, int deltaX, int deltaY) {

        // System.out.println("Moving " + robot.getName());

        // Calculate the new positions
        int newX = robot.getX() + deltaX;
        int newY = robot.getY() + deltaY;

        // Invalid move if outside the grid
        // TODO Revisit when working on Config object
        if (newX >= this.x || newX < 0 || newY >= this.y || newY < 0) {
            return false;
        }

        // Invalid move if grid is occupied
        String hash = newX + ":" + newY;
        if (this.grid.containsKey(hash)) {
            return false;
        }

        // Don't move if robot is dead
        // float health = this.grid.get(robot.getX() + ":" + robot.getY()).getHealth();
        // if (Util.compare(health, 0.01f) == -1) {
        //     return false;
        // }

        // Remove from old position
        this.grid.remove(robot.getX() + ":" + robot.getY());

        robot.setX(newX);
        robot.setY(newY);

        // Return to new position
        this.grid.put(hash, robot);

        return true;
    }

    public boolean occupied(int x, int y) {
        String hash = x + ":" + y;
        if (this.grid.containsKey(hash)) {
            return true;
        }

        return false;
    }

    public RobotInfo getRobot(int x, int y) {
        String hash = x + ":" + y;
        return this.grid.get(hash);
    }
}