/**
 * Author: Tang Yuqian
 * Date: 2020/9/1
 */
package com.concurrency.in.practice.explict_locks;

import java.util.concurrent.locks.*;

/**
 * InterruptibleLocking
 *
 * @author Brian Goetz and Tim Peierls
 */
public class InterruptibleLocking {
    private Lock lock = new ReentrantLock();

    public boolean sendOnSharedLine(String message)
            throws InterruptedException {

        // 某一线程在在等待获取锁的过程中，可以被中断
        lock.lockInterruptibly();
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
