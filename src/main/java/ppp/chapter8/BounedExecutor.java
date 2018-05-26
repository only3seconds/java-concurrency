package ppp.chapter8;

import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.Semaphore;

/**
 * 使用 Semaphore 来控制任务的提交速率
 */
public class BounedExecutor {
    private final Executor exec;
    private final Semaphore semaphore;

    public BounedExecutor(Executor exec, int bound) {
        this.exec = exec;
        this.semaphore = new Semaphore(bound);
    }

    public void submitTask(final Runnable command) throws InterruptedException {
        semaphore.acquire();
        try {
            exec.execute(new Runnable() {
                public void run() {
                   try {
                       command.run();
                   } finally {
                       semaphore.release();
                   }
                }
            });
        } catch (RejectedExecutionException e) {
            semaphore.release();
        }

    }
}
