package art.willstew.arena.javafx;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import art.willstew.logic.*;
import art.willstew.robots.*;
import javafx.application.Platform;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

/**
 * A JavaFX GUI element that displays a grid on which you can draw images, text and lines.
 * This class contains no game logic, it is just used for representing the game state
 */
public class JFXArena extends Pane {
    // Represents the image to draw. You can modify this to introduce multiple images.
    private static final String IMAGE_FILE = "1554047213.png";
    private Image robotImage;
    private int gridWidth;
    private int gridHeight;
    private double gridSquareSize; // Auto-calculated
    private Canvas canvas; // Used to provide a 'drawing surface'.
    private List<LaserBeam> lasers; // List to keep track of the lasers to draw
    private Game game;

    private ScheduledExecutorService executor;

    /**
     * Creates a new arena object, loading the robot image and initialising a drawing surface.
     * @param gridWidth The width of the arena grid
     * @param gridHeight The height of the arena grid
     */
    public JFXArena(int gridWidth, int gridHeight) {
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;

        // Here's how you get an Image object from an image file (which you provide in the 
        // 'resources/' directory.
        robotImage = new Image(getClass().getClassLoader().getResourceAsStream(IMAGE_FILE));
        canvas = new Canvas();
        canvas.widthProperty().bind(widthProperty());
        canvas.heightProperty().bind(heightProperty());
        getChildren().add(canvas);

        this.lasers = Collections.synchronizedList(new ArrayList<LaserBeam>());

        this.requestLayout();
    }

    /**
     * This method is needed to start the executor service for removing the lasers from the screen
     * It can't be in the constructor as the game object needs to be initialised first, to set the size
     * The number of threads in the pool is the number of robots in the game, as that is the maximum number
     * of shots that could be fired at any time
     */
    public void start() {
        this.executor = Executors.newScheduledThreadPool(this.game.getAllRobots().length);
    }

    /**
     * Wrapper to request an update to the GUI
     */
    public void update() {
        Platform.runLater(() -> {
            this.requestLayout();
        });
    }

    /**
     * Allows us to link the arena and the game, its needed to resolve a circular dep
     * @param game Used to get information about the game state
     */
    public void registerGame(Game game) {
        this.game = game;
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
        for(RobotInfo robot : this.game.getAllRobots()) {
            drawImage(gfx, robotImage, robot.getX(), robot.getY());
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
     * @param gfx Used to update the canvas
     * @param image The image to be rendered
     * @param gridX X coordinate location for image
     * @param gridY Y coordinate location for image
     */
    private void drawImage(GraphicsContext gfx, Image image, double gridX, double gridY) {
        // Get the pixel coordinates representing the centre of where the image is to be drawn. 
        double x = (gridX + 0.5) * gridSquareSize;
        double y = (gridY + 0.5) * gridSquareSize;
        
        // We also need to know how "big" to make the image. The image file has a natural width 
        // and height, but that's not necessarily the size we want to draw it on the screen. We 
        // do, however, want to preserve its aspect ratio.
        double fullSizePixelWidth = robotImage.getWidth();
        double fullSizePixelHeight = robotImage.getHeight();
        
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
     * @param gfx Used to update the canvas
     * @param label The text to be rendered under the grid location
     * @param gridX X coordinate location for label
     * @param gridY Y coordinate location for label
     */
    private void drawLabel(GraphicsContext gfx, String label, double gridX, double gridY) {
        gfx.setTextAlign(TextAlignment.CENTER);
        gfx.setTextBaseline(VPos.TOP);
        gfx.setStroke(Color.BLUE);
        gfx.strokeText(label, (gridX + 0.5) * gridSquareSize, (gridY + 1.0) * gridSquareSize);
    }
    
    /** 
     * Draws a (slightly clipped) line between two grid coordinates. 
     * @param gfx Used to update the canvas
     * @param gridX1 X coordinate location for line start
     * @param gridY1 Y coordinate location for line start
     * @param gridX2 X coordinate location for line end
     * @param gridY2 Y coordinate location for line end
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
     * @param laser LaserBeam object that specifies the start and end point for a laser
     */
	public void fire(LaserBeam laser) {
        this.lasers.add(laser);
        this.update();

        // Define how to remove the laser from the screen
        Runnable removeLaser = () -> {
            Platform.runLater(() -> {
                this.lasers.remove(laser);
                this.requestLayout();
            });
        };

        // Remove the laser from the screen in 250ms
        executor.schedule(removeLaser, 250, TimeUnit.MILLISECONDS);
    }

    /**
     * Cleanup the executor
     * Needs to be called otherwise threads will be left running
     */
    public void stop() {
        this.executor.shutdown();
    }
}
