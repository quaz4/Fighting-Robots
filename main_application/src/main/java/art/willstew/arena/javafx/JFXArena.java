package art.willstew.arena.javafx;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import art.willstew.ai.AIOne;
import art.willstew.ai.AITwo;
import art.willstew.ai.NativeAI;
import art.willstew.logic.LaserBeam;
import art.willstew.logic.MovementManager;
import art.willstew.logic.NotificationManager;
import art.willstew.logic.RobotControlImp;
import art.willstew.logic.RobotInfoImp;
import art.willstew.logic.Util;
import art.willstew.robots.RobotAI;
import art.willstew.robots.RobotControl;
import art.willstew.robots.RobotInfo;
import javafx.application.Platform;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

/**
 * A JavaFX GUI element that displays a grid on which you can draw images, text and lines.
 * It also
 */
public class JFXArena extends Pane {
    static final int xRange = 20;
    static final int yRange = 20;


    // Represents the image to draw. You can modify this to introduce multiple images.
    private static final String IMAGE_FILE = "1554047213.png";
    private Image robot1;

    private MovementManager movementManager;
    private NotificationManager nm;
    // private ArrayList<RobotAI> ais;
    private Hashtable<String, RobotAI> ais;
    private ArrayList<RobotInfo> robotInfo;
    // private ArrayList<RobotControl> robotControls;
    private Hashtable<String, RobotControl> robotControls;
    private ArrayList<LaserBeam> lasers;
    
    // The following values are arbitrary, and you may need to modify them according to the 
    // requirements of your application.
    private int gridWidth = 12;
    private int gridHeight = 8;
    private double gridSquareSize; // Auto-calculated
    private Canvas canvas; // Used to provide a 'drawing surface'.

    private TextArea logger;
    private ScheduledExecutorService executor;

    /**
     * Creates a new arena object, loading the robot image and initialising a drawing surface.
     */
    public JFXArena(TextArea logger) {

        this.logger = logger;

        this.movementManager = new MovementManager(gridWidth, gridHeight);
        this.nm = new NotificationManager();

        this.ais = new Hashtable<String, RobotAI>();
        this.robotInfo = new ArrayList<RobotInfo>();
        this.robotControls = new Hashtable<String, RobotControl>();

        this.lasers = new ArrayList<LaserBeam>();

        try {
            RobotInfoImp robotOne = new RobotInfoImp("Izzy", 2, 2, 100.0f);
            this.addRobot(robotOne, new AIOne());

            RobotInfoImp robotTwo = new RobotInfoImp("Archie", 11, 0, 100.0f);
            this.addRobot(robotTwo, new AIOne());

            RobotInfoImp robotThree = new RobotInfoImp("Remus", 2, 4, 100.0f);
            this.addRobot(robotThree, new AITwo());

            RobotInfoImp robotFour = new RobotInfoImp("Bogart", 5, 2, 100.0f);
            this.addRobot(robotFour, new AITwo());

            RobotInfoImp robotFive = new RobotInfoImp("Juno", 11, 7, 100.0f);
            this.addRobot(robotFive, new NativeAI());

            RobotInfoImp robotSix = new RobotInfoImp("Bonnie", 2, 1, 100.0f);
            this.addRobot(robotSix, new NativeAI());  
        } catch (IllegalStateException e) {
            // Ignore, try and run the program with however many robots are there
        }


        // Here's how you get an Image object from an image file (which you provide in the 
        // 'resources/' directory.
        robot1 = new Image(getClass().getClassLoader().getResourceAsStream(IMAGE_FILE));
        
        // You will get an exception here if the specified image file cannot be found.
        
        canvas = new Canvas();
        canvas.widthProperty().bind(widthProperty());
        canvas.heightProperty().bind(heightProperty());
        getChildren().add(canvas);

        this.requestLayout();

        this.executor = Executors.newScheduledThreadPool(this.robotInfo.size());
    }

    // Setup each robot by registering it with specic lists and managers
    public void addRobot(RobotInfo robot, RobotAI ai) {
        // Ensure that no robot has a duplicate name
        for (RobotInfo info : this.robotInfo) {
            if (robot.getName().equals(info.getName())) {
                throw new IllegalStateException("Cannot have a robot with duplicate names");
            }
        }

        this.ais.put(robot.getName(), ai);
        this.robotInfo.add(robot);
        this.movementManager.add(robot);
        this.robotControls.put(robot.getName(), new RobotControlImp(this, robot, this.nm));
    }

    public void start() {
        for (Map.Entry<String,RobotAI> set : this.ais.entrySet()) {
            set.getValue().runAI(this.robotControls.get(set.getKey()));
        }
    }

    public void stop() {
        for (RobotAI ai : this.ais.values()) {
            try {
                ai.stop();
            } catch (IllegalStateException e) {
                // Safe to ignore
            }
            
        }
    }

    public RobotInfo[] getAllRobots() {
		return this.robotInfo.toArray(new RobotInfo[this.robotInfo.size()]);
    }
    
    public void killRobot(String name) {
        // Remove robot from robotInfo list
        for (RobotInfo robot : this.robotInfo.toArray(new RobotInfo[this.robotInfo.size()])) {
            if (robot.getName().equals(name)) {
                this.ais.get(robot.getName()).stop();
                logger.appendText(robot.getName() + " is now dead\n");
                
                this.checkEndGame();
                break;
            }
        }
    }

    /**
     * Checks to see if the game has ended by counting the number of
     * robots left alive.
     * 
     * Usually called after a robot has been killed
     */
    public void checkEndGame() {
        int alive = 0;
        RobotInfo lastRobot = null;
        for (RobotInfo robot : this.robotInfo.toArray(new RobotInfo[this.robotInfo.size()])) {
            if(Util.compare(robot.getHealth(), 0.01f) == 1) {
                alive++;
                lastRobot = robot;
            }
        }

        if(alive == 1) {
           this.stop();
           this.logger.appendText(lastRobot.getName() + " is the winner\n"); 
        }
    }

    /**
     * Runs calls to check and move the specific robot in the GUI thread
     * Uses a completable future to block until the GUI thread runs the code
     */
    public boolean move(RobotInfoImp robot, int deltaX, int deltaY) {
        CompletableFuture<Boolean> movedFuture = new CompletableFuture<Boolean>();

        Platform.runLater(() -> {
            boolean moved = this.movementManager.move(robot, deltaX, deltaY);
            
            if (moved) {
                this.requestLayout();
            }

            // System.out.println(Thread.currentThread().getName());

            movedFuture.complete(Boolean.valueOf(moved));
        });

        boolean rval = false;

        try {
            rval = movedFuture.get().booleanValue();
        } catch (CancellationException | ExecutionException | InterruptedException e) {
            // Return false as doing nothing is always safe
            return false;
        }
        
        return rval;
    }
        
    /**
     * This method is called in order to redraw the screen, either because the user is manipulating 
     * the window, OR because you've called 'requestLayout()'.
     */
    @Override
    public void layoutChildren() {
        super.layoutChildren(); 
        GraphicsContext gfx = canvas.getGraphicsContext2D();
        gfx.clearRect(0.0, 0.0, canvas.getWidth(), canvas.getHeight());
        
        // First, calculate how big each grid cell should be, in pixels. (We do need to do this
        // every time we repaint the arena, because the size can change.)
        gridSquareSize = Math.min(
            getWidth() / (double) gridWidth,
            getHeight() / (double) gridHeight);
            
        double arenaPixelWidth = gridWidth * gridSquareSize;
        double arenaPixelHeight = gridHeight * gridSquareSize;
            
            
        // Draw the arena grid lines. This may help for debugging purposes, and just generally
        // to see what's going on.
        gfx.setStroke(Color.DARKGREY);
        gfx.strokeRect(0.0, 0.0, arenaPixelWidth - 1.0, arenaPixelHeight - 1.0); // Outer edge

        for(int gridX = 1; gridX < gridWidth; gridX++) { // Internal vertical grid lines
            double x = (double) gridX * gridSquareSize;
            gfx.strokeLine(x, 0.0, x, arenaPixelHeight);
        }
        
        for(int gridY = 1; gridY < gridHeight; gridY++) { // Internal horizontal grid lines
            double y = (double) gridY * gridSquareSize;
            gfx.strokeLine(0.0, y, arenaPixelWidth, y);
        }

        // Draw robots and labels
        for(RobotInfo robot : this.robotInfo) {
            drawImage(gfx, robot1, robot.getX(), robot.getY());
            drawLabel(gfx, robot.toString(), robot.getX(), robot.getY());
        }

        // Draw lines for lasers
        for(LaserBeam laser : this.lasers) {
            drawLine(gfx, laser.getStartX(), laser.getStartY(), laser.getEndX(), laser.getEndY());
        }
    }
    
    
    /** 
     * Draw an image in a specific grid location. *Only* call this from within layoutChildren(). 
     *
     * Note that the grid location can be fractional, so that (for instance), you can draw an image 
     * at location (3.5,4), and it will appear on the boundary between grid cells (3,4) and (4,4).
     *     
     * You shouldn't need to modify this method.
     */
    private void drawImage(GraphicsContext gfx, Image image, double gridX, double gridY) {
        // Get the pixel coordinates representing the centre of where the image is to be drawn. 
        double x = (gridX + 0.5) * gridSquareSize;
        double y = (gridY + 0.5) * gridSquareSize;
        
        // We also need to know how "big" to make the image. The image file has a natural width 
        // and height, but that's not necessarily the size we want to draw it on the screen. We 
        // do, however, want to preserve its aspect ratio.
        double fullSizePixelWidth = robot1.getWidth();
        double fullSizePixelHeight = robot1.getHeight();
        
        double displayedPixelWidth, displayedPixelHeight;
        if(fullSizePixelWidth > fullSizePixelHeight) {
            // Here, the image is wider than it is high, so we'll display it such that it's as 
            // wide as a full grid cell, and the height will be set to preserve the aspect 
            // ratio.
            displayedPixelWidth = gridSquareSize;
            displayedPixelHeight = gridSquareSize * fullSizePixelHeight / fullSizePixelWidth;
        } else {
            // Otherwise, it's the other way around -- full height, and width is set to 
            // preserve the aspect ratio.
            displayedPixelHeight = gridSquareSize;
            displayedPixelWidth = gridSquareSize * fullSizePixelWidth / fullSizePixelHeight;
        }

        // Actually put the image on the screen.
        gfx.drawImage(image,
            x - displayedPixelWidth / 2.0,  // Top-left pixel coordinates.
            y - displayedPixelHeight / 2.0, 
            displayedPixelWidth,              // Size of displayed image.
            displayedPixelHeight);
    }
    
    
    /**
     * Displays a string of text underneath a specific grid location. *Only* call this from within 
     * layoutChildren(). 
     *
     * You shouldn't need to modify this method.
     */
    private void drawLabel(GraphicsContext gfx, String label, double gridX, double gridY) {
        gfx.setTextAlign(TextAlignment.CENTER);
        gfx.setTextBaseline(VPos.TOP);
        gfx.setStroke(Color.BLUE);
        gfx.strokeText(label, (gridX + 0.5) * gridSquareSize, (gridY + 1.0) * gridSquareSize);
    }
    
    /** 
     * Draws a (slightly clipped) line between two grid coordinates. 
     *
     * You shouldn't need to modify this method.
     */
    private void drawLine(GraphicsContext gfx, double gridX1, double gridY1, 
                                               double gridX2, double gridY2) {
        gfx.setStroke(Color.RED);
        
        // Recalculate the starting coordinate to be one unit closer to the destination, so that it
        // doesn't overlap with any image appearing in the starting grid cell.
        final double radius = 0.5;
        double angle = Math.atan2(gridY2 - gridY1, gridX2 - gridX1);
        double clippedGridX1 = gridX1 + Math.cos(angle) * radius;
        double clippedGridY1 = gridY1 + Math.sin(angle) * radius;
        
        gfx.strokeLine((clippedGridX1 + 0.5) * gridSquareSize, 
                       (clippedGridY1 + 0.5) * gridSquareSize, 
                       (gridX2 + 0.5) * gridSquareSize, 
                       (gridY2 + 0.5) * gridSquareSize);
    }

    /**
     * Fires a shot from x,y to x2,y2
     * Performs logic on the GUI thread to avoid race conditions
     */
	public boolean fire(int x, int y, int x2, int y2) {

        CompletableFuture<Boolean> shootFuture = new CompletableFuture<Boolean>();

        Platform.runLater(() -> {
            RobotInfo shooter = this.movementManager.getRobot(x, y);
            RobotInfo target = this.movementManager.getRobot(x2, y2);

            // Check that shooting robot isn't dead
            if (Util.compare(shooter.getHealth(), 0.01f) == -1) {
                shootFuture.complete(Boolean.valueOf(false));
                return;
            }

            // Return false if target is alreday dead
            if (target != null && Util.compare(target.getHealth(), 0.01f) == -1) {
                shootFuture.complete(Boolean.valueOf(false));
                return;
            }

            // System.out.println(Thread.currentThread().getName());
            LaserBeam laser = new LaserBeam(x, y, x2, y2);

            // Add laser to list to be rendered
            this.lasers.add(laser);

            // Subtract health if robot was hit
            if(this.movementManager.occupied(x2, y2)) {
                RobotInfo robot = this.movementManager.getRobot(x2, y2);
                robot.setHealth(robot.getHealth() - 35.0f);

                logger.appendText(shooter.getName() + " hit " + target.getName() + "\n");

                if (Util.compare(robot.getHealth(), 0.01f) == -1) {
                    // System.out.println("Stopping AI " + ((AIOne)this.ais.get(robot.getName())).getName());
                    // logger.appendText("Stopping AI " + ((AIOne)this.ais.get(robot.getName())).getName() + "\n");

                    this.killRobot(robot.getName());
                }

                this.nm.notification(target, shooter);
            }
 
            this.requestLayout();

            // Define how to remove the laser from the screen
            Runnable task = () -> {
                Platform.runLater(() -> {
                    this.lasers.remove(laser);
                    this.requestLayout();
                });
            };

            // Remove the laser from the screen in 250ms
            executor.schedule(task, 250, TimeUnit.MILLISECONDS);

            shootFuture.complete(Boolean.valueOf(true));
        });

        boolean rval = false;

        try {
            rval = shootFuture.get().booleanValue();
        } catch (CancellationException | ExecutionException | InterruptedException e) {
            // Return false as doing nothing is always safe
            return false;
        }
        
        return rval;
	}
}
