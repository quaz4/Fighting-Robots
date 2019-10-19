package art.willstew.logic;

import java.util.ArrayList;

import art.willstew.robots.RobotInfo;

public class RobotControlImp {

    private ArrayList<RobotInfoImp> robots; // TODO Maybe make thread safe? and unmodifiable?
    private String name; // The unique name of the robot this controller controls
    private RobotInfoImp me; // A reference to the robot this controller controls

    public RobotControlImp(ArrayList<RobotInfoImp> robots, String name) {
        this.robots = robots;
        this.name = name;

        // Find this robot and keep a reference to it somewhere easy to access
        for (RobotInfoImp robot : this.robots) {
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
        return false;
    }

    // Working on it
    public void registerMoveNorth() {
        
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
