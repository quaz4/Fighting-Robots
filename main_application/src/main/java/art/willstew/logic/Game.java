package art.willstew.logic;

import art.willstew.arena.javafx.JFXArena;
import art.willstew.config.*;
import art.willstew.robots.*;

/**
 * Game class controls the game logic
 */
public class Game {
    
    private JFXArena arena;
    private Configuration config;
    private RobotInfo[] robots; // TODO Check this later

    public Game(JFXArena arena, Configuration config) {
        this.arena = arena;
        this.config = config;
    }

    // public boolean fire(RobotInfo robot, int x, int y) {

    // }

    // public boolean move(RobotInfo robot, int x, int y) {
    //     // Ensure robot stays within the board
    //     // if () {

    //     // }
    // }
}
