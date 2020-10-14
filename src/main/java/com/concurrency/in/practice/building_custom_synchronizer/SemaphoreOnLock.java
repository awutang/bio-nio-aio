/**
 * Author: Tang Yuqian
 * Date: 2020/9/26
 */
package com.concurrency.in.practice.building_custom_synchronizer;

import java.util.concurrent.locks.*;

import net.jcip.annotations.*;

/**
 * SemaphoreOnLock
 * <p/>
 * Counting semaphore implemented using Lock
 * (Not really how java.util.concurrent.Semaphore is implemented)
 *
 * @author Brian Goetz and Tim Peierls
 */
@ThreadSafe
public class SemaphoreOnLock {
    private final Lock lock = new ReentrantLock();
    // CONDITION PREDICATE: permitsAvailable (permits > 0)
    private final Condition permitsAvailable = lock.newCondition();
    // 类似信号量Semaphore中的permits
    @GuardedBy("lock") private int permits;

    SemaphoreOnLock(int initialPermits) {
        // 但是Semaphore没有只允许一个线程获取锁的逻辑吧，Semaphore可以允许permits个线程一起执行任务的，所以信号量用lock实现的话是只能有一个允许么？
        // 是的，当permit被锁保护时会阻塞
        lock.lock();
        try {
            permits = initialPermits;
        } finally {
            lock.unlock();
        }
    }

    // BLOCKS-UNTIL: permitsAvailable
    public void acquire() throws InterruptedException {
        lock.lock();
        try {
            while (permits <= 0)
                permitsAvailable.await();
            --permits;
        } finally {
            lock.unlock();
        }
    }

    public void release() {
        lock.lock();
        try {
            ++permits;
            permitsAvailable.signal();
        } finally {
            lock.unlock();
        }
    }
}
