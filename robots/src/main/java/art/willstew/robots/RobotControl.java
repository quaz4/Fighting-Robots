package art.willstew.robots;

public interface RobotControl {
 
    public RobotInfo getRobot();

    public RobotInfo[] getAllRobots();

    public boolean moveNorth() throws InterruptedException;

    public boolean moveEast() throws InterruptedException;

    public boolean moveSouth() throws InterruptedException;

    public boolean moveWest() throws InterruptedException;

    public boolean fire(int x, int y) throws InterruptedException;

    public RobotInfo hitMade();

    public RobotInfo hitTaken();
}
