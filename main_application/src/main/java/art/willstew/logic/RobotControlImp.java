package art.willstew.logic;

import java.util.ArrayList;

import art.willstew.robots.RobotInfo;

public class RobotControlImp {

    private ArrayList<RobotInfo> robots; // TODO Maybe make thread safe? and unmodifiable?
    private String name; // The unique name of the robot this controller controls
    private RobotInfo me; // A reference to the robot this controller controls
    private Game game; // Reference to the game object that controls the game state TODO Rethink this...

    public RobotControlImp(ArrayList<RobotInfo> robots, String name, Game game) {
        this.robots = robots;
        this.name = name;

        // Find this robot and keep a reference it somewhere easy to access
        for (RobotInfo robot : this.robots) {
            if(this.me.name.equals(robot.name)) {
                this.me = robot;
                break;
            }
        }
    }

    public RobotInfo getRobot() {
        return this.me;
    }

    public RobotInfo[] getAllRobots() {
        return this.robots.toArray(new RobotInfo[this.robots.size()]);
    }

    public boolean moveNorth() {
        // Needs checks
        this.me.x++;
        return false;
    }

    public boolean moveEast() {
        // TODO
        return false;
    }

    public boolean moveSouth() {
        // TODO
        return false;
    }

    public boolean moveWest() {
        // TODO
        return false;
    }

    public boolean fire(int x, int y) {
        // TODO
        return false;
    }
}
