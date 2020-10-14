/**
 * Author: Tang Yuqian
 * Date: 2020/9/18
 */
package com.concurrency.in.practice.building_custom_synchronizer;

import net.jcip.annotations.*;

/**
 * BoundedBuffer
 * <p/>
 * Bounded buffer using condition queues
 *
 * @author Brian Goetz and Tim Peierls
 */
@ThreadSafe
public class BoundedBuffer <V> extends BaseBoundedBuffer<V> {
    // CONDITION PREDICATE: not-full (!isFull())
    // CONDITION PREDICATE: not-empty (!isEmpty())
    public BoundedBuffer() {
        this(100);
    }

    public BoundedBuffer(int size) {
        super(size);
    }

    // BLOCKS-UNTIL: not-full
    public synchronized void put(V v) throws InterruptedException {
        while (isFull())
            wait();
        // 如果从非空的一个节点增加到了两个节点，take的线程其实此时并没有阻塞，没必要notifyAll,因此可以优化为alternatePut
        doPut(v);
        notifyAll();
    }

    // BLOCKS-UNTIL: not-empty
    public synchronized V take() throws InterruptedException {
        // 不满足条件
        while (isEmpty())
            // 则执行wait放弃锁，从而进入条件队列进行阻塞;当其他线程执行notify时，从等待队列中出队，竞争获取锁，获取到锁后从wait返回
        /**
         * This method causes the current thread (call it <var>T</var>) to
         *      * place itself in the wait set for this object and then to relinquish
         *      * any and all synchronization claims on this object.
         * The thread <var>T</var> is then removed from the wait set for this
         *      * object and re-enabled for thread scheduling. It then competes in the
         *      * usual manner with other threads for the right to synchronize on the
         *      * object; once it has gained control of the object, all its
         *      * synchronization claims on the object are restored to the status quo
         *      * ante - that is, to the situation as of the time that the {@code wait}
         *      * method was invoked. Thread <var>T</var> then returns from the
         *      * invocation of the {@code wait} method. Thus, on return from the
         *      * {@code wait} method, the synchronization state of the object and of
         *      * thread {@code T} is exactly as it was when the {@code wait} method
         *      * was invoked.
         */
            wait();
        V v = doTake();
        notifyAll();
        return v;
    }

    // BLOCKS-UNTIL: not-full
    // Alternate form of put() using conditional notification
    public synchronized void alternatePut(V v) throws InterruptedException {
        while (isFull())
            wait();
        // 条件通知，从空变为非空时，通知
        boolean wasEmpty = isEmpty();
        doPut(v);
        if (wasEmpty)
            notifyAll();
    }
}
