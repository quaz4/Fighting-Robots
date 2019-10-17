package art.willstew.ai;

import art.willstew.robots.*;

public class AIOne implements RobotAI  {

    private Thread thread = null;
    private RobotControl rc;

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

        this.thread = new Thread(ai, "AI One");
        this.thread.start();
    }

    private void logic() {
        try {
            String direction = "north";

            // While the thread hasn't been stopped
            while(!Thread.interrupted()) {
                RobotInfo me = this.rc.getRobot();
    
                // Itterate over all the other robots in the game
                for (RobotInfo robot : this.rc.getAllRobots()) {
    
                    // Skip the robot controlled by this ai
                    // Skip any robot that is out of range
                    if(!robot.name.equals(me.name)
                        && (Math.abs(me.x - robot.x) <= 2)
                        && (Math.abs(me.y - robot.y) <= 2)
                        ) {
                        
                        // Wait for 0.5s
                        Thread.sleep(500);
                        this.rc.fire(robot.x, robot.x);
                    }
                }

                /**
                 * Try and move, if it fails, try the next direction
                 */
                switch (direction) {
                    case "north":
                        if(!this.rc.moveNorth()) {
                            direction = "east";
                            break;
                        }
                    case "east":
                        if(!this.rc.moveEast()) {
                            direction = "south";
                            break;
                        }
                    case "south":
                        if(!this.rc.moveSouth()) {
                            direction = "west";
                            break;
                        }
                    case "west":
                        if(!this.rc.moveWest()) {
                            direction = "north";
                            break;
                        }
                }

                // Wait for 1s
                Thread.sleep(1000);
            }

            if(Thread.interrupted()) {
                throw new InterruptedException();
            }
        } catch(InterruptedException e) { // TODO Check if I need to remove this

        }
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