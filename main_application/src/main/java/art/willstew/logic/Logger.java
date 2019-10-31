package art.willstew.logic;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

/**
 * Simple class to make logging thread safe
 * It does this by only logging on on the GUI thread
 */
public class Logger {
    TextArea textArea;

    public Logger(TextArea textArea) {
        this.textArea = textArea;
    }

    public void log(String message) {
        Platform.runLater(() -> {
            this.textArea.appendText(message);
        });
    }
}