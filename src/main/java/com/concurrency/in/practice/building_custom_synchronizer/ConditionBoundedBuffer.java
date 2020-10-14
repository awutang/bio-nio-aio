/**
 * Author: Tang Yuqian
 * Date: 2020/9/25
 */
package com.concurrency.in.practice.building_custom_synchronizer;

import java.util.concurrent.locks.*;

import net.jcip.annotations.*;

/**
 * ConditionBoundedBuffer
 * <p/>
 * Bounded buffer using explicit condition variables
 *
 * @author Brian Goetz and Tim Peierls
 */

@ThreadSafe
public class ConditionBoundedBuffer <T> {
    protected final Lock lock = new ReentrantLock();
    // CONDITION PREDICATE: notFull (count < items.length)
    private final Condition notFull = lock.newCondition();
    // CONDITION PREDICATE: notEmpty (count > 0)
    private final Condition notEmpty = lock.newCondition();
    private static final int BUFFER_SIZE = 100;
    @GuardedBy("lock") private final T[] items = (T[]) new Object[BUFFER_SIZE];
    @GuardedBy("lock") private int tail, head, count;

    // BLOCKS-UNTIL: notFull
    public void put(T x) throws InterruptedException {
        lock.lock();
        try {
            while (count == items.length)
                notFull.await();
            items[tail] = x;
            if (++tail == items.length)
                tail = 0;
            ++count;
            // 唤醒在notEmpty.await()中等待的线程集中的一个，可以避免之前BoundedBuffer中使用notifyAll导致的等待非满条件的线程的误唤醒
            // （其实只需要唤醒等待非空的线程就够了，但是由于等待非空与非满的线程在一个队列中，所以都会唤醒)减少了线程上下文切换与锁竞争
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    // BLOCKS-UNTIL: notEmpty
    public T take() throws InterruptedException {
        // lock.lock();
        try {
            while (count == 0)
                // await signal signalAll都会校验当前线程是否为lock.lock()的独占线程，所以这三个操作都应该持有锁即执行lock.lock()
                notEmpty.await();
            T x = items[head];
            items[head] = null;
            if (++head == items.length)
                head = 0;
            --count;
            notFull.signal();
            return x;
        } finally {
            // lock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ConditionBoundedBuffer<String> conditionBoundedBuffer = new ConditionBoundedBuffer<String>();

        conditionBoundedBuffer.take();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    conditionBoundedBuffer.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    conditionBoundedBuffer.put("111");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }
}
