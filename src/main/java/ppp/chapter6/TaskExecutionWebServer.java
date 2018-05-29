package ppp.chapter6;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 基于线程池的web服务器
 */
public class TaskExecutionWebServer {
    private static final int NTHREADS = 100;
    private static final Executor exec = Executors.newFixedThreadPool(NTHREADS);
}
