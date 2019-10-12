package art.willstew.ai;

import art.willstew.robots.*;

public class AIOne implements RobotAI  {

    private Thread thread;
    private RobotControl rc;

    @Override
    public void runAI(RobotControl rc) {
        this.rc = rc;

        Runnable ai = new Runnable() {
        
            @Override
            public void run() {
                logic();
            }
        };

        this.thread = new Thread(ai, "AI One");
        this.thread.start();
    }

    private void logic() {
        // TODO AI One logic
    }
}