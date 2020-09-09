/**
 * Author: Tang Yuqian
 * Date: 2020/8/31
 */
package com.concurrency.in.practice.explict_locks;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

/**
 * DeadlockAvoidance
 * <p/>
 * Avoiding lock-ordering deadlock using tryLock
 *
 * @author Brian Goetz and Tim Peierls
 */
public class DeadlockAvoidance {
    private static Random rnd = new Random();

    public boolean transferMoney(Account fromAcct,
                                 Account toAcct,
                                 DollarAmount amount,
                                 long timeout,
                                 TimeUnit unit)
            throws InsufficientFundsException, InterruptedException {
        long fixedDelay = getFixedDelayComponentNanos(timeout, unit);
        long randMod = getRandomDelayModulusNanos(timeout, unit);
        long stopTime = System.nanoTime() + unit.toNanos(timeout);

        while (true) {
            // 如果有ThreadA和threadB的fromAcct、toAcct正好相反且同时进入该方法，则toAcct.lock.tryLock()处会返回false,并且休眠随机秒,之后再重新获取锁
            // 如果某一时间内未成功获取两把锁，则返回false
            if (fromAcct.lock.tryLock()) {
                try {
                    if (toAcct.lock.tryLock()) {
                        try {
                            if (fromAcct.getBalance().compareTo(amount) < 0)
                                throw new InsufficientFundsException();
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
        public int compareTo(DollarAmount other) {
            return 0;
        }


        private int dollars;

        DollarAmount(int dollars) {
            this.dollars = dollars;
        }

        public void add(DollarAmount dollars) {
            this.dollars += dollars.getDollars();
        }

        public void subtract(DollarAmount dollars) {
            this.dollars -= dollars.getDollars();
        }

        public int getDollars() {
            return dollars;
        }

        public void setDollars(int dollars) {
            this.dollars = dollars;
        }
    }

    static class Account {
        public Lock lock;

        void debit(DollarAmount d) {
        }

        void credit(DollarAmount d) {
        }

        DollarAmount getBalance() {
            return null;
        }
    }

    static class BankCardAccount extends Account {
        private DollarAmount balance;

        public BankCardAccount(DollarAmount balance, Lock lock) {
            this.balance = balance;
            this.lock = lock;
        }

        void debit(DollarAmount d) {
            balance.add(d);
            super.debit(d);
        }

        @Override
        void credit(DollarAmount d) {
            super.credit(d);
            balance.subtract(d);
        }

        @Override
        DollarAmount getBalance() {
            return super.getBalance();
        }


    }

    class InsufficientFundsException extends Exception {
    }


    public static void main(String[] args) {

        Account fromAcct = new BankCardAccount(new DollarAmount(3), new ReentrantLock());
        Account toAcct = new BankCardAccount(new DollarAmount(5), new ReentrantLock());
        try {
            new DeadlockAvoidance().transferMoney(fromAcct, toAcct, new DollarAmount(1), 1000, TimeUnit.MILLISECONDS);
        } catch (InsufficientFundsException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

