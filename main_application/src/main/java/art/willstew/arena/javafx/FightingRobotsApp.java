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

public class FightingRobotsApp extends Application {
    private boolean started = false;

    public static void main(String[] args) {
        launch();
    }
    
    @Override
    public void start(Stage stage) {
        stage.setTitle("Fighting Robots");

        final int gridWidth = 10;
        final int gridHeight = 10;

        TextArea logger = new TextArea();
        JFXArena arena = new JFXArena(gridWidth, gridHeight);
        MovementManager mm = new MovementManager(gridWidth, gridHeight);
        NotificationManager nm = new NotificationManager();

        Game game = new Game(arena, logger, mm, nm, gridWidth, gridHeight);
        arena.registerGame(game); // Add a reference to game

        try {
            RobotInfo robotOne = new RobotInfoImp("Izzy", 2, 2, 100.0f);
            game.addRobot(robotOne, new AIOne());

            RobotInfo robotTwo = new RobotInfoImp("Archie", 9, 0, 100.0f);
            game.addRobot(robotTwo, new AIOne());

            RobotInfo robotThree = new RobotInfoImp("Remus", 2, 8, 100.0f);
            game.addRobot(robotThree, new AITwo());

            RobotInfo robotFour = new RobotInfoImp("Bogart", 5, 2, 100.0f);
            game.addRobot(robotFour, new AITwo());

            // RobotInfoImp robotFive = new RobotInfoImp("Juno", 11, 7, 100.0f);
            // game.addRobot(robotFive, new NativeAI());

            // RobotInfoImp robotSix = new RobotInfoImp("Bonnie", 2, 1, 100.0f);
            // game.addRobot(robotSix, new NativeAI());  
        } catch (IllegalStateException e) {
            // Ignore, try and run the program with however many robots are there
        } catch (InvalidParameterException e) {
            // Ignore, try and run the program with however many robots are there
        }

        ToolBar toolbar = new ToolBar();
        Button btn1 = new Button("Start");
        Button btn2 = new Button("Stop");
        toolbar.getItems().addAll(btn1, btn2);
        
        btn1.setOnAction((event) -> {
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

        btn2.setOnAction((event) -> {
            if (!this.started) {
                return;
            }

            try {
                game.stop();
            } catch (IllegalStateException e) {
                // Game is not running, ignore
            }

        });
        
        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(arena, logger);
        arena.setMinWidth(300.0);
        
        BorderPane contentPane = new BorderPane();
        contentPane.setTop(toolbar);
        contentPane.setCenter(splitPane);
        
        Scene scene = new Scene(contentPane, 800, 800);
        stage.setScene(scene);
        stage.show();

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
