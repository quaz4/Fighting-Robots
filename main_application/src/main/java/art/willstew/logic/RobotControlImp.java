package art.willstew.logic;

import art.willstew.robots.*;
import art.willstew.logic.*;
import art.willstew.arena.javafx.JFXArena;

public class RobotControlImp implements RobotControl {

    private JFXArena arena;
    private RobotInfoImp robot; // A reference to the robot this controller controls

    public RobotControlImp(JFXArena arena, RobotInfoImp robot) {
        this.arena = arena;
        this.robot = robot;
    }

    public RobotInfo getRobot() {
        return this.robot;
    }

    public RobotInfo[] getAllRobots() {
        return this.arena.getAllRobots();
    }

    public boolean moveNorth() {
        return this.arena.move((RobotInfoImp)this.getRobot(), 0, -1);
    }

    public boolean moveEast() {
        return this.arena.move((RobotInfoImp)this.getRobot(), 1, 0);
    }

    public boolean moveSouth() {
        return this.arena.move((RobotInfoImp)this.getRobot(), 0, 1);
    }

    public boolean moveWest() {
        return this.arena.move((RobotInfoImp)this.getRobot(), -1, 0);
    }

    public boolean fire(int x, int y) {
        // TODO
        return false;
    }
}
