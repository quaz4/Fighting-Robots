package art.willstew.logic;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

/**
 * Simple class to make logging thread safe
 * It does this by only logging on on the GUI thread
 */
public class Logger {
    TextArea textArea;

    /**
     * Initialises the Logger by setting the TextArea
     * @param textArea Text area where the messages will appear
     */
    public Logger(TextArea textArea) {
        this.textArea = textArea;
    }

    /**
     * Output a formatted log message that includes the date at the start
     * e.g. 09:52:39: Game stopped
     * Where "Game stopped is the message param"
     * @param message
     */
    public void log(String message) {
        Platform.runLater(() -> {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");  
            LocalDateTime now = LocalDateTime.now();
            this.textArea.appendText(dtf.format(now) + ": " + message);
        });
    }
}