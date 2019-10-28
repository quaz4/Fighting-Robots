package art.willstew.ai;

import art.willstew.robots.RobotAI;
import art.willstew.robots.RobotControl;

public class NativeAI implements RobotAI  {

    private Thread thread = null;
    private RobotControl rc;

    static {
        try {
            System.load("/Users/stewwf/Projects/Fighting-Robots/native_ai/build/libs/nativeimp/shared/libnativeimp.dylib");
        } catch (Error e) {
            System.out.println(e);
        }
    }

    @Override
    public void runAI(RobotControl rc) {
        //Throw an exception if the thread is already running
        if (this.thread != null) {
            throw new IllegalStateException("AI is already running, so can't be started");
        }
        
        this.rc = rc;

        // Start the ai on its own thread
        Runnable ai = new Runnable() {
        
            @Override
            public void run() {
                logic(rc);
            }

        };

        this.thread = new Thread(ai, rc.getRobot().getName());
        this.thread.start();
    }

    private native void logic(RobotControl rc);

    public void stop() {
        // Throw an exception if the thread isn't running
        if(this.thread == null) {
            throw new IllegalStateException("Thread isn't running, so it can't be stopped");
        }

        // Interrupt the thread, it will stop after the next blocking call
        this.thread.interrupt();
        this.thread = null;
    }
}