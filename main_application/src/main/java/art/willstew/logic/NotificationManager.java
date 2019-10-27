package art.willstew.logic;

import java.util.Hashtable;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import art.willstew.robots.RobotInfo;

public class NotificationManager {

    // The queues for targets getting told they have been shot
    Hashtable<String, BlockingQueue<RobotInfo>> targetQueues = new Hashtable<>();
    // The queues for shooters getting told they have made a hit
    Hashtable<String, BlockingQueue<RobotInfo>> shooterQueues = new Hashtable<>();

    public NotificationManager() {}

    public void registerTarget(String name) {
        this.targetQueues.put(name, new LinkedBlockingQueue<RobotInfo>());
    }

    public void registerShooter(String name) {
        this.shooterQueues.put(name, new LinkedBlockingQueue<RobotInfo>());
    }

    public RobotInfo getTargetNotification(String name) {
        return this.targetQueues.get(name).poll();
    }

    public RobotInfo getShooterNotification(String name) {
        return this.shooterQueues.get(name).poll();
    }

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














// package art.willstew.logic;

// import java.util.concurrent.BlockingQueue;
// import java.util.concurrent.LinkedBlockingQueue;

// import art.willstew.robots.RobotInfo;
// import javafx.concurrent.Task;

// @Deprecated
// public class NotificationManager {

//     private Thread thread = null;
//     // private Hashtable

//     // private BlockingQueue<RobotInfo> bitMade = new LinkedBlockingQueue<RobotInfo>();
//     // private BlockingQueue<RobotInfo> hitTaken = new LinkedBlockingQueue<RobotInfo>();
//     private BlockingQueue<Hit> hit = new LinkedBlockingQueue<Hit>();

//     public NotificationManager() { 

//     }

//     public void start() {
//         //Throw an exception if the thread is already running
//         if (this.thread != null) {
//             throw new IllegalStateException("Notification Manager is already running, so can't be started");
//         }

//         // Start the manager on its own thread
//         Runnable manager = new Runnable() {
        
//             @Override
//             public void run() {
//                 while(true) {
                    
//                 }
//             }

//         };

//         this.thread = new Thread(manager, "Notification Manager");
//         this.thread.start();
//     }

//     public void registerHitMade(String name, Task task) {
//         // Can get robot through rc I guess...
//     }

//     public void registerHitTaken(String name, Task task) {
//         // Can get robot through rc I guess...
//     }

//     public void notifification(RobotInfo firedBy, RobotInfo hit) {
//         // Notify firedBy
//         // Notify hit
//     }

//     public void stop() {
//         // Throw an exception if the thread isn't running
//         if(this.thread == null) {
//             throw new IllegalStateException("Thread isn't running, so it can't be stopped");
//         }

//         // Interrupt the thread, it will stop after the next blocking call
//         this.thread.interrupt();
//         this.thread = null;
//     }
// }
