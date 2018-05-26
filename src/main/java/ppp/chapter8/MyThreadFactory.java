package ppp.chapter8;

import java.util.concurrent.ThreadFactory;

/**
 * 自定义的线程工厂
 */
public class MyThreadFactory implements ThreadFactory{
    private final String poolName;

    public MyThreadFactory(String poolName) {
        this.poolName = poolName;
    }

    @Override
    public Thread newThread(Runnable runnable) {
        return new MyAppThread(runnable, poolName); //可以在线程转储和错误日志信息中区分来自不同线程池的线程
    }
}
