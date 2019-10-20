package art.willstew.logic;

import art.willstew.robots.*;
import art.willstew.logic.*;

import java.util.*;

/** 
 * Class for managing the movement of robots around the grid
 */

public class MovementManager {

    private int x;
    private int y;
    private Hashtable<String, RobotInfoImp> grid;

    public MovementManager(int x, int y) {
        // TODO Update this to fetch from config object
        this.x = x;
        this.y = y;

        this.grid = new Hashtable<String, RobotInfoImp>();
    }

    public void add(RobotInfoImp robot) {
        String hash = robot.getX() + ":" + robot.getY();

        this.grid.put(hash, robot);
    }

    /**
     * Move the robot provided, deltaX and deltaY can be positive or negative
     */
    public boolean move(RobotInfoImp robot, int deltaX, int deltaY) {
        // Calculate the new positions
        int newX = robot.getX() + deltaX;
        int newY = robot.getY() + deltaY;

        // Invalid move if outside the grid
        if (newX > this.x || newX < 0 || newY > this.y || newY < 0) {
            return false;
        }

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

        return true;
    }
}