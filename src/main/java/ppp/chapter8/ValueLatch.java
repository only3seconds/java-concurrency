package ppp.chapter8;

import java.util.concurrent.CountDownLatch;

/**
 *
 * 由ConcurrentPuzzleSolver使用的携带结果的闭锁
 */
public class ValueLatch <T> {
    private T value = null;
    private final CountDownLatch done = new CountDownLatch(1);

    public boolean isSet() {
        return (done.getCount() == 0);
    }

    public synchronized void setValue(T newValue) {
        if (!isSet()) {
            value = newValue;
            done.countDown();
        }
    }

    public T getValue()throws InterruptedException {
        done.await();
        synchronized (this) {
            return value;
        }
    }
}
