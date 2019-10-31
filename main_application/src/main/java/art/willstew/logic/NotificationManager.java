package art.willstew.logic;

import java.util.Hashtable;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import art.willstew.robots.RobotInfo;

/**
 * Notification manager handles the blocking queues that allow ais to receive notifications.
 * Hash tables are used to make each notification queue accessable by name.
 */
public class NotificationManager {
    // The queues for targets getting told they have been shot
    Hashtable<String, BlockingQueue<RobotInfo>> targetQueues = new Hashtable<>();
    // The queues for shooters getting told they have made a hit
    Hashtable<String, BlockingQueue<RobotInfo>> shooterQueues = new Hashtable<>();

    public NotificationManager() {}

    /**
     * Register a target to receive notifications
     * @param name Robots unique name
     */
    public void registerTarget(String name) {
        this.targetQueues.put(name, new LinkedBlockingQueue<RobotInfo>());
    }

    /**
     * Register a shooter to receive notifications
     * @param name Robots unique name
     */
    public void registerShooter(String name) {
        this.shooterQueues.put(name, new LinkedBlockingQueue<RobotInfo>());
    }

    /**
     * Get the next notification from the queue if it exists.
     * Uses the poll method so that it doesn't block the robot.
     * @param name The name of the target you want to get info from
     * @return The info of the shooting robot
     */
    public RobotInfo getTargetNotification(String name) {
        return this.targetQueues.get(name).poll();
    }

    /**
     * Get the next notification from the queue if it exists.
     * Uses the poll method so that it doesn't block the robot.
     * @param name The name of the shooter you want to get info from
     * @return The info of the target robot
     */
    public RobotInfo getShooterNotification(String name) {
        return this.shooterQueues.get(name).poll();
    }

    /**
     * Make a new notification to be added to the shooters and targets queues
     * @param target The robot making the shot
     * @param shooter The robot taking the shot
     */
    public void notification(RobotInfo target, RobotInfo shooter) {
        String targetName = target.getName();
        String shooterName = shooter.getName();

        try {
            this.targetQueues.get(targetName).add(shooter);
        } catch(NullPointerException e) {
            // Do nothing
        }

        try {
            this.shooterQueues.get(shooterName).add(target);
        } catch(NullPointerException e) {
            // Do nothing
        }
    }
}