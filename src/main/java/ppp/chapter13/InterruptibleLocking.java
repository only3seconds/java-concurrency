package ppp.chapter13;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 可中断的锁获取机制。
 * lockInterruptibly() 能够在获得锁的同时保持对中断的响应
 */
public class InterruptibleLocking {
    private Lock lock = new ReentrantLock();

    public boolean sendOnSharedLine(String message) throws InterruptedException {
        lock.lockInterruptibly(); //获得锁的同时保持对中断的响应
        try {
            return cancellableSendOnSharedLine(message);
        } finally {
            lock.unlock();
        }

    }


    private boolean cancellableSendOnSharedLine(String message) throws InterruptedException {
        /* send something */
        return true;
    }
}
