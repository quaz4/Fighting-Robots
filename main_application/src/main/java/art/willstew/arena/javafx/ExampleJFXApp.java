package art.willstew.arena.javafx;

import art.willstew.logic.Game;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class ExampleJFXApp extends Application {
    public static void main(String[] args) {
        
        Game game = new Game(null, null);
        game.toString();

        launch();
    }
    
    @Override
    public void start(Stage stage) {
        stage.setTitle("Fighting Robots");
        JFXArena arena = new JFXArena();
        
        ToolBar toolbar = new ToolBar();
        Button btn1 = new Button("Start");
        Button btn2 = new Button("Stop");
        toolbar.getItems().addAll(btn1, btn2);
        
        btn1.setOnAction((event) -> {
            System.out.println("Start");
        });

        btn2.setOnAction((event) -> {
            System.out.println("Stop");
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
}
