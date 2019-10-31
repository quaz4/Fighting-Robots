package art.willstew.ai;

import java.util.concurrent.ThreadLocalRandom;

import art.willstew.robots.RobotAI;
import art.willstew.robots.RobotControl;
import art.willstew.robots.RobotInfo;

/**
 * An implementation of RobotAI.
 * This AI will move towards a randomly selected target.
 * It will also move towards the last robot to shoot it.
 */
public class AITwo implements RobotAI  {

    private Thread thread = null;
    private RobotControl rc;
    private RobotInfo target = null;

    @Override
    public void runAI(RobotControl rc) {
        //Throw an exception if the thread is already running
        if (this.thread != null) {
            throw new IllegalStateException("AI is already running, so can't be started");
        }
        
        this.rc = rc;

        // Start the ai on its own thread
        Runnable ai = new Runnable() {
        
            @Override
            public void run() {
                logic();
            }

        };

        // Start a thread with the name of the robot
        this.thread = new Thread(ai, rc.getRobot().getName());
        this.thread.start();
    }

    // Specifies how the AI behaves
    private void logic() {
        RobotInfo me = null;

        try {
            String direction = "north";
            me = this.rc.getRobot();

            target = this.randomTarget();

            // While the thread hasn't been stopped
            while(this.thread != null) {
    
                RobotInfo notification = this.rc.hitTaken();

                if (notification != null) {
                    this.target = notification;
                }

                // Find a random target that isn't dead
                while(target.getHealth() < 0.0001f || target == me) {
                    this.target = this.randomTarget();
                }   

                // Itterate over all the other robots in the game
                for (RobotInfo robot : this.rc.getAllRobots()) {

                    // Skip the robot controlled by this ai
                    // Skip any robot that is out of range
                    if(!robot.getName().equals(me.getName())
                        && (Math.abs(me.getX() - robot.getX()) <= 2)
                        && (Math.abs(me.getY() - robot.getY()) <= 2)
                        && robot.getHealth() > 0.0f
                        ) {

                        this.rc.fire(robot.getX(), robot.getY());
                    
                        break;
                    }

                }

                if (this.thread == null) {
                    throw new InterruptedException();
                }

               // Try and move towards the target
                if (target != null) {
                    if(me.getX() > target.getX()) {
                        direction = "west";
                    } else if(me.getX() < target.getX()) {
                        direction = "east";
                    }

                    if(me.getY() > target.getY()) {
                        direction = "north";
                    } else if(me.getY() < target.getY()) {
                        direction = "south";
                    }
                }

                // Try and move, if it fails, try the next direction
                switch (direction) {
                    case "north":
                        if(!this.rc.moveNorth()) {
                            direction = "east";
                        } else {
                            break;
                        }
                    case "east":
                        if(!this.rc.moveEast()) {
                            direction = "south";
                        } else {
                            break;
                        }
                    case "south":
                        if(!this.rc.moveSouth()) {
                            direction = "west";
                        } else {
                            break;
                        }
                    case "west":
                        if(!this.rc.moveWest()) {
                            direction = "north";
                        } else {
                            break;
                        }
                }
            }

            throw new InterruptedException();

        } catch(InterruptedException e) {
            // Thread has been interrupted, stopping
        }
    }

    private RobotInfo randomTarget() {
        // Pick a random target to move towards
        int numRobots = this.rc.getAllRobots().length;
        int randomNum = ThreadLocalRandom.current().nextInt(0, numRobots);
        return this.rc.getAllRobots()[randomNum];
    }

    public void stop() {
        // Throw an exception if the thread isn't running
        if(this.thread == null) {
            throw new IllegalStateException("Thread isn't running, so it can't be stopped");
        }

        // Interrupt the thread, it will stop after the next blocking call
        this.thread.interrupt();
        this.thread = null;
    }
}