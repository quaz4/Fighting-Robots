package art.willstew.logic;

import art.willstew.arena.javafx.JFXArena;
import art.willstew.robots.RobotControl;
import art.willstew.robots.RobotInfo;

public class RobotControlImp implements RobotControl {

    private JFXArena arena;
    private RobotInfo robot; // A reference to the robot this controller controls
    private NotificationManager nm;

    public RobotControlImp(JFXArena arena, RobotInfo robot, NotificationManager nm) {
        this.arena = arena;
        this.robot = robot;
        this.nm = nm;
    }

    @Override
    public RobotInfo getRobot() {
        return this.robot;
    }

    @Override
    public RobotInfo[] getAllRobots() {
        return this.arena.getAllRobots();
    }

    @Override
    public boolean moveNorth() {
        return this.arena.move((RobotInfoImp)this.getRobot(), 0, -1);
    }

    @Override
    public boolean moveEast() {
        return this.arena.move((RobotInfoImp)this.getRobot(), 1, 0);
    }

    @Override
    public boolean moveSouth() {
        return this.arena.move((RobotInfoImp)this.getRobot(), 0, 1);
    }

    @Override
    public boolean moveWest() {
        return this.arena.move((RobotInfoImp)this.getRobot(), -1, 0);
    }

    @Override
    public boolean fire(int x, int y) {
        return this.arena.fire(this.robot.getX(), this.robot.getY(), x, y);
    }

    @Override
    public RobotInfo hitMade() {
        return this.nm.getShooterNotification(this.robot.getName());
    }

    @Override
    public RobotInfo hitTaken() {
        return this.nm.getTargetNotification(this.robot.getName());
    }
}
