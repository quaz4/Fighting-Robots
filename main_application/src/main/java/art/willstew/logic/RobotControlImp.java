package art.willstew.logic;

import art.willstew.robots.RobotControl;
import art.willstew.robots.RobotInfo;

public class RobotControlImp implements RobotControl {

    private Game game; 
    private RobotInfo robot; // A reference to the robot this controller controls
    private NotificationManager nm;

    public RobotControlImp(Game game, RobotInfo robot, NotificationManager nm) {
        this.game = game;
        this.robot = robot;
        this.nm = nm;

        this.nm.registerShooter(robot.getName());
        this.nm.registerTarget(robot.getName());
    }

    @Override
    public RobotInfo getRobot() {
        return this.robot;
    }

    @Override
    public RobotInfo[] getAllRobots() {
        return this.game.getAllRobots();
    }

    @Override
    public boolean moveNorth() throws InterruptedException {
        Thread.sleep(1000);
        return this.game.move((RobotInfoImp)this.getRobot(), 0, -1);
    }

    @Override
    public boolean moveEast() throws InterruptedException {
        Thread.sleep(1000);
        return this.game.move((RobotInfoImp)this.getRobot(), 1, 0);
    }

    @Override
    public boolean moveSouth() throws InterruptedException {
        Thread.sleep(1000);
        return this.game.move((RobotInfoImp)this.getRobot(), 0, 1);
    }

    @Override
    public boolean moveWest() throws InterruptedException {
        Thread.sleep(1000);
        return this.game.move((RobotInfoImp)this.getRobot(), -1, 0);
    }

    @Override
    public boolean fire(int x, int y) throws InterruptedException {
        Thread.sleep(500);
        return this.game.fire(this.robot.getX(), this.robot.getY(), x, y);
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
