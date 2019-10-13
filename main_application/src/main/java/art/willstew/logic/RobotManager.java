package art.willstew.logic;

import java.util.ArrayList;

import art.willstew.robots.*;

/** Class for managing the robots in the game */
@Deprecated
public class RobotManager {

    ArrayList<Robot> robots = new ArrayList<Robot>();// TODO Maybe make thread safe?

    public RobotManager() {}

    public void add(RobotControl rc, RobotAI ai, RobotInfo info) {
        this.robots.add(new Robot(rc, ai, info));
    }
}