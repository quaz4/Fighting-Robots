package art.willstew.robots;

public interface RobotControl {
    public RobotInfo getRobot();

    public RobotInfo[] getAllRobots();

    public boolean moveNorth();

    public boolean moveEast();

    public boolean moveSouth();

    public boolean moveWest();

    public boolean fire(int x, int y);
}
