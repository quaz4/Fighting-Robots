package art.willstew.arena.javafx;

import art.willstew.ai.*;
import art.willstew.logic.*;
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

public class ExampleJFXApp extends Application {
    public static void main(String[] args) {
        launch();
    }
    
    @Override
    public void start(Stage stage) {
        stage.setTitle("Fighting Robots");

        int gridWidth = 12;
        int gridHeight = 8;

        TextArea logger = new TextArea();
        JFXArena arena = new JFXArena();
        MovementManager mm = new MovementManager(gridWidth, gridHeight);
        NotificationManager nm = new NotificationManager();

        Game game = new Game(arena, logger, mm, nm);
        arena.registerGame(game);

        try {
            RobotInfoImp robotOne = new RobotInfoImp("Izzy", 2, 2, 100.0f);
            game.addRobot(robotOne, new AIOne());

            RobotInfoImp robotTwo = new RobotInfoImp("Archie", 11, 0, 100.0f);
            game.addRobot(robotTwo, new AIOne());

            RobotInfoImp robotThree = new RobotInfoImp("Remus", 2, 4, 100.0f);
            game.addRobot(robotThree, new AITwo());

            RobotInfoImp robotFour = new RobotInfoImp("Bogart", 5, 2, 100.0f);
            game.addRobot(robotFour, new AITwo());

            // RobotInfoImp robotFive = new RobotInfoImp("Juno", 11, 7, 100.0f);
            // game.addRobot(robotFive, new NativeAI());

            // RobotInfoImp robotSix = new RobotInfoImp("Bonnie", 2, 1, 100.0f);
            // game.addRobot(robotSix, new NativeAI());  
        } catch (IllegalStateException e) {
            // Ignore, try and run the program with however many robots are there
        }

        ToolBar toolbar = new ToolBar();
        Button btn1 = new Button("Start");
        Button btn2 = new Button("Stop");
        toolbar.getItems().addAll(btn1, btn2);
        
        btn1.setOnAction((event) -> {
            System.out.println("Game started");
            logger.appendText("Game started\n");
            // arena.start();
            game.start();
        });

        btn2.setOnAction((event) -> {
            System.out.println("Stopping game\n");
            logger.appendText("Stopping game\n");
            // arena.stop();
            game.stop();
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
                    // arena.stop();
                    game.stop();
                } catch(IllegalStateException e) {
                    // Safe to ignore
                }
            }
        });
    }
}
