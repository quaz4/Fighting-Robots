package art.willstew.robots;

import java.util.ArrayList;

public class RobotControl {

    private ArrayList<RobotInfo> robots; // TODO Maybe make thread safe? and unmodifiable?
    private String name;
    private RobotInfo me;

    public RobotControl(ArrayList<RobotInfo> robots, String name) {
        this.robots = robots;
        this.name = name;

        for (RobotInfo robot : this.robots) {
            // Find our info and keep it someone easy to access
            if(this.me.getName().equals(robot.getName())) {
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
