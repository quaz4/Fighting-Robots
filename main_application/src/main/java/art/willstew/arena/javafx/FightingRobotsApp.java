package art.willstew.arena.javafx;

import java.security.InvalidParameterException;

import art.willstew.ai.*;
import art.willstew.logic.*;
import art.willstew.robots.RobotInfo;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * JavaFX application class
 * This class creates the application window and initialises the game objects
 */
public class FightingRobotsApp extends Application {
    private boolean started = false; // Has the fight been started?

    public static void main(String[] args) {
        launch();
    }
    
    /**
     * @param stage Top level JavaFX container
     * Initialises the game objects defines the app layout
     */
    @Override
    public void start(Stage stage) {
        stage.setTitle("Fighting Robots");

        // Grid height and width that the robots fight in
        final int gridWidth = 10;
        final int gridHeight = 10;

        TextArea logText = new TextArea();
        Logger logger = new Logger(logText);
        JFXArena arena = new JFXArena(gridWidth, gridHeight);
        MovementManager mm = new MovementManager(gridWidth, gridHeight);
        NotificationManager nm = new NotificationManager();

        // Create game object using dependency injection
        Game game = new Game(arena, logger, mm, nm, gridWidth, gridHeight);
        arena.registerGame(game); // Add a reference to game object
        arena.start(); // Needed to start the executor

        // Specify each robot here
        try {
            RobotInfo robotOne = new RobotInfoImp("Izzy", 2, 2, 100.0f);
            game.addRobot(robotOne, new AIOne());

            RobotInfo robotTwo = new RobotInfoImp("Archie", 9, 0, 100.0f);
            game.addRobot(robotTwo, new AIOne());

            RobotInfo robotThree = new RobotInfoImp("Remus", 2, 8, 100.0f);
            game.addRobot(robotThree, new AITwo());

            RobotInfo robotFour = new RobotInfoImp("Bogart", 5, 2, 100.0f);
            game.addRobot(robotFour, new AITwo());

            RobotInfoImp robotFive = new RobotInfoImp("Juno", 9, 7, 100.0f);
            game.addRobot(robotFive, new NativeAI());

            RobotInfoImp robotSix = new RobotInfoImp("Bonnie", 9, 1, 100.0f);
            game.addRobot(robotSix, new NativeAI());
        } catch (IllegalStateException e) {
            // Ignore, try and run the program with however many robots are there
        } catch (InvalidParameterException e) {
            // Ignore, try and run the program with however many robots are there
        }

        // Build toolbar at the top of the app and add the buttons
        ToolBar toolbar = new ToolBar();
        Button btn1 = new Button("Start");
        Button btn2 = new Button("Stop");
        toolbar.getItems().addAll(btn1, btn2);
        
        // Specify callback for Start button
        btn1.setOnAction((event) -> {
            // Prevent from being called twice
            if (this.started) {
                return;
            } else {
                this.started = true;
            }

            try {
                game.start();
            } catch (IllegalStateException e) {
                // Ignore, game is already running
            }
            
        });

        // Specify callback for Stop button
        btn2.setOnAction((event) -> {
            // Prevent from being called before the game has started
            if (!this.started) {
                return;
            }

            try {
                game.stop();
            } catch (IllegalStateException e) {
                // Game is not running, ignore
            }

        });
        
        // Split the application in two, on the left is the arena
        // on the right is the the logger
        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(arena, logText);
        arena.setMinWidth(300.0);
        
        BorderPane contentPane = new BorderPane();
        contentPane.setTop(toolbar);
        contentPane.setCenter(splitPane);
        
        Scene scene = new Scene(contentPane, 800, 800);
        stage.setScene(scene);
        stage.show();

        // Add callback for application close to handle cleanup
        // Ensures all threads end when the window closes
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent we) {
                Platform.exit();

                try {
                    game.stop();
                } catch(IllegalStateException e) {
                    // Safe to ignore
                }
            }
        });
    }
}
