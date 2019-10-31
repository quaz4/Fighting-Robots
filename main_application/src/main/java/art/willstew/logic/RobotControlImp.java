package art.willstew.logic;

import art.willstew.robots.RobotControl;
import art.willstew.robots.RobotInfo;

/**
 * An implementation of the RobotControl api
 * AIs can be passed an instance of this object to pass on commands to the game
 */
public class RobotControlImp implements RobotControl {

    private Game game; // Reference to the game object to pass on calls
    private RobotInfo robot; // A reference to the robot this controller controls
    private NotificationManager nm;

    // Constants defining how long each robot should sleep
    private final int MOVE_SLEEP = 1000;
    private final int FIRE_SLEEP = 500;

    /**
     * Implements th
     * @param game Reference to the game object that this api allows access to
     * @param robot Reference to the robot that this controller is assigned to
     * @param nm Notification manager to allow notifcations to be sent
     */
    public RobotControlImp(Game game, RobotInfo robot, NotificationManager nm) {
        this.game = game;
        this.robot = robot;
        this.nm = nm;

        // Register for notications
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
        Thread.sleep(MOVE_SLEEP);
        return this.game.move((RobotInfoImp)this.getRobot(), 0, -1);
    }

    @Override
    public boolean moveEast() throws InterruptedException {
        Thread.sleep(MOVE_SLEEP);
        return this.game.move((RobotInfoImp)this.getRobot(), 1, 0);
    }

    @Override
    public boolean moveSouth() throws InterruptedException {
        Thread.sleep(MOVE_SLEEP);
        return this.game.move((RobotInfoImp)this.getRobot(), 0, 1);
    }

    @Override
    public boolean moveWest() throws InterruptedException {
        Thread.sleep(MOVE_SLEEP);
        return this.game.move((RobotInfoImp)this.getRobot(), -1, 0);
    }

    @Override
    public boolean fire(int x, int y) throws InterruptedException {
        Thread.sleep(FIRE_SLEEP);
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
