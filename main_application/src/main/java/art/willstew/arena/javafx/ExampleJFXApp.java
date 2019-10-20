package art.willstew.arena.javafx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ExampleJFXApp extends Application {
    public static void main(String[] args) {
        launch();
    }
    
    @Override
    public void start(Stage stage) {
        stage.setTitle("Fighting Robots");

        // TODO Initialise robots and pass to arena

        JFXArena arena = new JFXArena(null);
        
        ToolBar toolbar = new ToolBar();
        Button btn1 = new Button("Start");
        Button btn2 = new Button("Stop");
        toolbar.getItems().addAll(btn1, btn2);
        
        btn1.setOnAction((event) -> {
            System.out.println("Starting...");
            arena.start();
        });

        btn2.setOnAction((event) -> {
            System.out.println("Stopping...");
            arena.stop();
        });
                    
        TextArea logger = new TextArea();
        logger.appendText("Hello\n");
        logger.appendText("World\n");
        
        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(arena, logger);
        arena.setMinWidth(300.0);
        
        BorderPane contentPane = new BorderPane();
        contentPane.setTop(toolbar);
        contentPane.setCenter(splitPane);
        
        Scene scene = new Scene(contentPane, 800, 800);
        stage.setScene(scene);
        stage.show();
    }

    public void log(String message) {
        
    }
}
