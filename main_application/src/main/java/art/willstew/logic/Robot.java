/**
 * Consider moving this to the robots subproject
 */

package art.willstew.logic;

import art.willstew.robots.RobotControl;
import art.willstew.robots.RobotInfo;
import art.willstew.robots.RobotAI;

/**
 * Container class for holding all the parts of an individual robot
 */
 // TODO Maybe remove this
public class Robot {

    private RobotControl rc;
    private RobotAI ai;
    private RobotInfo info;

    public Robot(RobotControl rc, RobotAI ai, RobotInfo info) {
        this.rc = rc;
        this.ai = ai;
        this.info = info;
    }
}