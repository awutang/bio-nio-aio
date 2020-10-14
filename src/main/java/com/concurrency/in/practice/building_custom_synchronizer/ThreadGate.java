/**
 * Author: Tang Yuqian
 * Date: 2020/9/23
 */
package com.concurrency.in.practice.building_custom_synchronizer;

import net.jcip.annotations.*;

/**
 * ThreadGate
 * <p/>
 * Recloseable gate using wait and notifyAll
 *
 * @author Brian Goetz and Tim Peierls
 */
@ThreadSafe
public class ThreadGate {
    // CONDITION-PREDICATE: opened-since(n) (isOpen || generation>n)
    @GuardedBy("this") private boolean isOpen;
    @GuardedBy("this") private int generation;

    public synchronized void close() {
        isOpen = false;
    }

    public synchronized void open() {
        ++generation;
        isOpen = true;
        notifyAll();
    }

    // BLOCKS-UNTIL: opened-since(generation on entry)
    public synchronized void await() throws InterruptedException {
        int arrivalGeneration = generation;

        while (!isOpen && arrivalGeneration == generation)
            // 等待的线程从唤醒、获取锁到从wait中返回时如果close了，那么isOpen赋值为false; 如果没有generation的判断则此线程会再次wait。
            // 因此为了避免这种情况，增加了计数器generation，当线程到达while判断处，如果阀门已经打开了(后续被关闭)，则不会进入wait
            wait();
    }
}
