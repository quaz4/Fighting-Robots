package art.willstew.logic;

import java.util.ArrayList;

import art.willstew.robots.RobotInfo;

public class RobotControlImp {

    private ArrayList<RobotInfo> robots; // TODO Maybe make thread safe? and unmodifiable?
    private String name;
    private RobotInfo me;

    public RobotControlImp(ArrayList<RobotInfo> robots, String name) {
        this.robots = robots;
        this.name = name;

        for (RobotInfo robot : this.robots) {
            // Find our info and keep it someone easy to access
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
        // TODO
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
