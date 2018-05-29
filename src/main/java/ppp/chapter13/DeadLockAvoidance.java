package ppp.chapter13;

import javax.naming.InsufficientResourcesException;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

/**
 *
 * 通过tryLock()来避免锁顺序死锁,使用tryLock来获取两个锁，如果不能同时获得，那么就回退并重新尝试；
 * 在休眠时间中包括固定部分和随机部分，从而降低活锁的可能性。
 *
 */
public class DeadLockAvoidance {
    private static Random rnd = new Random();

    public boolean transferMoney(Account fromAcct, Account toAcct, DollarAmount amount, long timeout, TimeUnit unit)
    throws InsufficientFoundException, InterruptedException {
        long fixedDelay = getFixedDelayComponentNanos(timeout, unit);
        long randMod = getRandomDelayModulusNanos(timeout, unit);
        long stopTime = System.nanoTime() + unit.toNanos(timeout);

        while (true) {
            if (fromAcct.lock.tryLock()) {
                try {
                    if (toAcct.lock.tryLock()) {
                        try {
                            if (fromAcct.getBalance().compareTo(amount) < 0)
                                throw new InsufficientFoundException();
                            else {
                                fromAcct.debit(amount);
                                toAcct.credit(amount);
                                return true;
                            }
                        } finally {
                            toAcct.lock.unlock();
                        }
                    }
                } finally {
                    fromAcct.lock.unlock();
                }
            }

            if (System.nanoTime() < stopTime)
                return false;
            NANOSECONDS.sleep(fixedDelay + rnd.nextLong() % randMod);
        }

    }

    private static final int DELAY_FIXED = 1;
    private static final int DELAY_RANDOM = 2;

    static long getFixedDelayComponentNanos(long timeout, TimeUnit unit) {
        return DELAY_FIXED;
    }

    static long getRandomDelayModulusNanos(long timeout, TimeUnit unit) {
        return DELAY_RANDOM;
    }


    static class DollarAmount implements Comparable<DollarAmount> {

        @Override
        public int compareTo(DollarAmount other) {
            return 0;
        }

        DollarAmount(int dollars) {

        }
    }

    class Account {
        public Lock lock;
        void debit(DollarAmount d) {

        }

        void credit(DollarAmount d) {

        }

        DollarAmount getBalance() {
            return null;
        }
    }

    class InsufficientFoundException extends Exception {

    }
}
