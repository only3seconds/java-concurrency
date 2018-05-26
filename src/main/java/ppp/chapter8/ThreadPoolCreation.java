package ppp.chapter8;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 创建一个固定大小的线程池，并采用有界队列以及"调用者运行"饱和策略
 */
public class ThreadPoolCreation {

    private static final int N_THREADS = 8;
    private static final int CAPACITY = 16;

    public static void main(String [] args) {

        ThreadPoolExecutor executor = new ThreadPoolExecutor(N_THREADS, N_THREADS,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingDeque<Runnable>(CAPACITY));

        executor.setRejectedExecutionHandler (
                new ThreadPoolExecutor.CallerRunsPolicy());

    }

}
