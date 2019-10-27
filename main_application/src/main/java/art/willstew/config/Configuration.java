package art.willstew.config;

public class Configuration {
    
    private static int x;
    private static int y;

    public Configuration(int xIn, int yIn) {
        x = xIn;
        y = yIn;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}