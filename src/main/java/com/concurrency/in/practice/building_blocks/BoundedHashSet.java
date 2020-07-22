/**
 * Author: Tang Yuqian
 * Date: 2020/7/11
 */
package com.concurrency.in.practice.building_blocks;

import java.util.*;
import java.util.concurrent.*;

/**
 * BoundedHashSet
 * <p/>
 * Using Semaphore to bound a collection
 *
 * @author Brian Goetz and Tim Peierls
 */
public class BoundedHashSet <T> {
    private final Set<T> set;
    private final Semaphore sem;

    public BoundedHashSet(int bound) {
        this.set = Collections.synchronizedSet(new HashSet<T>());
        sem = new Semaphore(bound);
    }


    public boolean add(T o) throws InterruptedException {
        // 可以多个线程同时到达并执行acquire，将state-1
        sem.acquire();
        boolean wasAdded = false;
        try {
            // 但是只允许一个线程执行
            wasAdded = set.add(o);
            return wasAdded;
        } finally {
            if (!wasAdded)
                // 如果set已有相同元素wasAdded为false,将state+1,给下一个线程机会去add元素
                // add成功后则加1
                sem.release();
        }
    }

    public boolean remove(Object o) {
        boolean wasRemoved = set.remove(o);
        if (wasRemoved)
            // 删除成功后加1，给add一次机会。
            // 因此此方法与上面add方法结合起来，实现了一个固定长度的阻塞容器的逻辑
            sem.release();
        return wasRemoved;
    }
}
