package art.willstew.logic;

import art.willstew.robots.RobotInfo;

public class Hit {

    private RobotInfo target;
    private RobotInfo shooter;

    public Hit(RobotInfo target, RobotInfo shooter) {
        this.target = target;
        this.shooter = shooter;
    }

    public RobotInfo getShooter() {
        return this.shooter;
    }

    public RobotInfo getTarget() {
        return this.target;
    }
}
