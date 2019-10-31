package art.willstew.robots;

/**
 * Defines the methods each implementation of an AI should have
 */
public interface RobotAI {
    public void runAI(RobotControl rc);
    public void stop();
}