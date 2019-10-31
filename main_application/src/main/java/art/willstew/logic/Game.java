package art.willstew.logic;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import art.willstew.arena.javafx.JFXArena;
import art.willstew.robots.RobotInfo;
import art.willstew.robots.*;

/**
 * The Game class coordinates the running of the game logic
 * This includes, movement, shooting and starting/ending the game
 */
public class Game {

    private Object monitor = new Object();

    private JFXArena arena;
    private Logger logger;
    private MovementManager mm;
    private NotificationManager nm;

    private Hashtable<String, RobotAI> ais; // Access to ais by name
    private List<RobotInfo> ri; // List of all robots
    private Hashtable<String, RobotControl> rc; // Access to RobotContols by name

    private int gridWidth;
    private int gridHeight;

    public Game(JFXArena arena, Logger logger, MovementManager mm, NotificationManager nm, int x, int y) {
        this.arena = arena; // GUI Grid
        this.logger = logger; // Text output about game actions
        this.mm = mm; // MovementManager
        this.nm = nm; // NotificationManger

        this.ais = new Hashtable<String, RobotAI>();
        this.ri = Collections.synchronizedList(new ArrayList<RobotInfo>());
        this.rc = new Hashtable<String, RobotControl>(); // RobotControl

        this.gridWidth = x;
        this.gridHeight = y;
    }

    /**
     * Construct robot object and register with required lists
     * Prevent duplicate names from being added
     * @param robot Reference to the robot info object, should be unique
     * @param ai The AI that will be controlling this robot, should be unique
     */
    public void addRobot(RobotInfo robot, RobotAI ai) {
        synchronized(monitor) {
            // Ensure that no robot has a duplicate name
            for (RobotInfo info : this.ri) {
                if (robot.getName().equals(info.getName())) {
                    throw new IllegalStateException("Cannot have a robot with duplicate names");
                }
            }

            if(robot.getX() > this.gridWidth - 1 || robot.getY() > this.gridHeight - 1) {
                throw new InvalidParameterException("Robot not in grid");
            }

            this.ais.put(robot.getName(), ai); // Add robots ai to list
            this.ri.add(robot); // Add robot info to list
            this.mm.add(robot); // Register with movement manager
            this.rc.put(robot.getName(), new RobotControlImp(this, robot, this.nm)); // Register robot controller
        }
    }

    /**
     * Stops the named robots ai and logs that the robot is dead
     * @param name unique name of a robot
     */
    public void killRobot(String name) {
        synchronized(monitor) {
            // Get robot ai and stop it
            for (RobotInfo robot : this.ri.toArray(new RobotInfo[this.ri.size()])) {
                if (robot.getName().equals(name)) {
                    this.ais.get(robot.getName()).stop();
                    this.logger.log(robot.getName() + " is now dead\n");
                    
                    this.checkEndGame();
                    break; // No point continuing to search
                }
            }
        }
    }

    /**
     * Checks to see if the game has ended by counting the number of
     * robots left alive.
     * 
     * Usually called after a robot has been killed.
     */
    private void checkEndGame() {
        synchronized(monitor) {
            int alive = 0;
            RobotInfo lastRobot = null;
            // Loop through all robots in the game
            for (RobotInfo robot : this.ri.toArray(new RobotInfo[this.ri.size()])) {
                // Check if it is alive
                if(Util.compare(robot.getHealth(), 0.01f) == 1) {
                    alive++;
                    lastRobot = robot;
                }
            }
    
            // If only one robot is left alive, stop it and log that it won
            if(alive == 1) {
               this.stop();
               this.logger.log(lastRobot.getName() + " is the winner\n"); 
            }
        }
    }

    /**
     * Fires a shot from x,y to x2,y2
     * @param x x coordinate for shot start
     * @param y y coordinate for shot start
     * @param x2 x coordinate for shot end
     * @param y2 y coordinate for shot end
     * @return if the shot was valid, not if it has hit, a miss is still a valid shot
     */
    public boolean fire(int x, int y, int x2, int y2) {
        synchronized(monitor) {
            RobotInfo shooter = this.mm.getRobot(x, y);
            RobotInfo target = this.mm.getRobot(x2, y2);
    
            // Check that shooting robot isn't dead
            if (Util.compare(shooter.getHealth(), 0.01f) == -1) {
                return false;
            }
    
            // Return false if target is alreday dead
            if (target != null && Util.compare(target.getHealth(), 0.01f) == -1) {
                return false;
            }
    
            LaserBeam laser = new LaserBeam(x, y, x2, y2);
    
            this.arena.fire(laser);
    
            // Subtract health if robot was hit
            if(target != null) {
                target.setHealth(target.getHealth() - 35.0f);
    
                this.logger.log(shooter.getName() + " hit " + target.getName() + "\n");
    
                if (Util.compare(target.getHealth(), 0.01f) == -1) {
                    this.killRobot(target.getName());
                }
    
                this.nm.notification(target, shooter); // Notify target and shooter of the result
            }
    
            return true;
        }
	}

    /**
     * Runs calls to check and move the specific robot in the GUI thread
     * Uses a completable future to block until the GUI thread runs the code
     * @param robot The robot to move
     * @param deltaX The relative change in the x value
     * @param deltaY The relative change in the y value
     * @return If the move is valid
     */
    public boolean move(RobotInfo robot, int deltaX, int deltaY) {
        synchronized(monitor) {
            boolean moved = this.mm.move(robot, deltaX, deltaY);
            
            if (moved) {
                this.arena.update();
            }
            
            return moved;
        }
    }

    /**
     * Gets a reference to all robots
     * @return Array of RobotInfo objects
     */
    public RobotInfo[] getAllRobots() {
        synchronized(monitor) {
            return this.ri.toArray(new RobotInfo[this.ri.size()]);
        }
    }

    /**
     * Loop through the list of ais and call their start method
     */
    public void start() {
        synchronized(monitor) {
            logger.log("Game started\n");
            // Extract the keyvalue pairs from the list and itterate through them
            for (Map.Entry<String,RobotAI> set : this.ais.entrySet()) {
                // Get the specific RC object for this ai and call unAI
                set.getValue().runAI(this.rc.get(set.getKey()));
            }
        }
    }

    /**
     * Loop through the list of ais and call their stop method
     */
    public void stop() {
        synchronized(monitor) {
            logger.log("Game stopped\n");
            for (RobotAI ai : this.ais.values()) {
                try {
                    ai.stop();
                } catch (IllegalStateException e) {
                    // Safe to ignore
                }
            }

            this.arena.stop();
        }
    }
}