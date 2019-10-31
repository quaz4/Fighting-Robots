package art.willstew.ai;

import art.willstew.robots.RobotAI;
import art.willstew.robots.RobotControl;

public class NativeAI implements RobotAI  {

    private Thread thread = null;
    private RobotControl rc;

    // Load the nativelibrary before using the class
    static {
        try {
            System.loadLibrary("nativeimp");
        } catch (UnsatisfiedLinkError e) {
            System.err.println("Could not load native library in NativeAI");
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
                try {
                    logic(rc);
                } catch (InterruptedException e) {
                    thread = null;
                }
                
            }

        };

        this.thread = new Thread(ai, rc.getRobot().getName());
        this.thread.start();
    }

    // Declaration for native method
    private native void logic(RobotControl rc) throws InterruptedException;

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