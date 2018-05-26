package ppp.chapter8;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 定制Thread基类
 */
public class MyAppThread extends Thread {
    public static final String DEFAULT_NAME = "MyAppThread";
    private static volatile boolean debugLifecycle = false;
    private static final AtomicInteger created = new AtomicInteger();
    private static final AtomicInteger alive = new AtomicInteger();
    private static final Logger log = Logger.getAnonymousLogger();

    public MyAppThread(Runnable r) {
        this(r, DEFAULT_NAME);
    }

    public MyAppThread(Runnable runnable, String name) {
        super(runnable, name + "_" + created.incrementAndGet());
        setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {

            @Override
            public void uncaughtException(Thread t, Throwable e) {
                log.log(Level.SEVERE, "UNCAUGHT in thread" + t.getName(), e);
            }
        });
    }

    public void run() {
        //复制debug标志以确定一致的值
        boolean debug = debugLifecycle;
        if(debug) log.log(Level.FINE, "Created" + getName());
    }

    public static int getThreadsCreated() {return  created.get(); }
    public static int getThreadsAlive() {return alive.get(); }
    public static boolean getDebugLifecycle() {return debugLifecycle; }
    public static void setDebugLifecycle(boolean b) {debugLifecycle = b; }
}
