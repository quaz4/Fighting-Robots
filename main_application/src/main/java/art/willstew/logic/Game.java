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

    public void killRobot(String name) {
        synchronized(monitor) {
            // Remove robot from robotInfo list
            for (RobotInfo robot : this.ri.toArray(new RobotInfo[this.ri.size()])) {
                if (robot.getName().equals(name)) {
                    this.ais.get(robot.getName()).stop();
                    this.logger.log(robot.getName() + " is now dead\n");
                    
                    this.checkEndGame();
                    break;
                }
            }
        }
    }

    /**
     * Checks to see if the game has ended by counting the number of
     * robots left alive.
     * 
     * Usually called after a robot has been killed
     */
    private void checkEndGame() {
        synchronized(monitor) {
            int alive = 0;
            RobotInfo lastRobot = null;
            for (RobotInfo robot : this.ri.toArray(new RobotInfo[this.ri.size()])) {
                if(Util.compare(robot.getHealth(), 0.01f) == 1) {
                    alive++;
                    lastRobot = robot;
                }
            }
    
            if(alive == 1) {
               this.stop();
               this.logger.log(lastRobot.getName() + " is the winner\n"); 
            }
        }
    }

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
            if(this.mm.occupied(x2, y2)) {
                // TODO Review the use of the new Robot variable
                RobotInfo robot = this.mm.getRobot(x2, y2);
                robot.setHealth(robot.getHealth() - 35.0f);
    
                this.logger.log(shooter.getName() + " hit " + target.getName() + "\n");
    
                if (Util.compare(robot.getHealth(), 0.01f) == -1) {
                    this.killRobot(robot.getName());
                }
    
                this.nm.notification(target, shooter);
            }
    
            return true;
        }
	}

    /**
     * Runs calls to check and move the specific robot in the GUI thread
     * Uses a completable future to block until the GUI thread runs the code
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