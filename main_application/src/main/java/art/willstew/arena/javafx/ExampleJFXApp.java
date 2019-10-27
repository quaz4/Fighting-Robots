package art.willstew.arena.javafx;

// import java.beans.EventHandler;

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

        TextArea logger = new TextArea();

        JFXArena arena = new JFXArena(logger);
        
        ToolBar toolbar = new ToolBar();
        Button btn1 = new Button("Start");
        Button btn2 = new Button("Stop");
        toolbar.getItems().addAll(btn1, btn2);
        
        btn1.setOnAction((event) -> {
            System.out.println("Game started");
            logger.appendText("Game started\n");
            arena.start();
        });

        btn2.setOnAction((event) -> {
            System.out.println("Stopping game\n");
            logger.appendText("Stopping game\n");
            arena.stop();
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
                    arena.stop();
                } catch(IllegalStateException e) {
                    // Safe to ignore
                }
            }
        });
    }

    public void log(String message) {
        
    }
}
