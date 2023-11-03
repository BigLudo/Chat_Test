package main.java;

public class Logger implements ILog{

    private  boolean dbg;
    public Logger (boolean debug) {
        dbg = debug;
    }
    @Override
    public void info(String message) {
        System.out.println("Info: " + message);
    }

    @Override
    public void debug(String message) {
        if (dbg) {
            System.out.println("Debug: " + message);
        }
    }
}
