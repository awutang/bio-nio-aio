/**
 * Author: Tang Yuqian
 * Date: 2020/10/18
 */
package com.concurrency.in.practice.atomicvariables_nonblockingsynchronization;

import java.util.concurrent.atomic.*;

import net.jcip.annotations.*;

/**
 * LinkedQueue
 * <p/>
 * Insertion in the Michael-Scott nonblocking queue algorithm
 *
 * @author Brian Goetz and Tim Peierls
 */
@ThreadSafe
public class LinkedQueue <E> {

    private static class Node <E> {
        final E item;
        final AtomicReference<LinkedQueue.Node<E>> next;

        public Node(E item, LinkedQueue.Node<E> next) {
            this.item = item;
            this.next = new AtomicReference<LinkedQueue.Node<E>>(next);
        }
    }

    private final LinkedQueue.Node<E> dummy = new LinkedQueue.Node<E>(null, null);
    private final AtomicReference<LinkedQueue.Node<E>> head
            = new AtomicReference<LinkedQueue.Node<E>>(dummy);
    private final AtomicReference<LinkedQueue.Node<E>> tail
            = new AtomicReference<LinkedQueue.Node<E>>(dummy);

    public boolean put(E item) {
        LinkedQueue.Node<E> newNode = new LinkedQueue.Node<E>(item, null);
        while (true) {
            LinkedQueue.Node<E> curTail = tail.get();
            LinkedQueue.Node<E> tailNext = curTail.next.get();
            if (curTail == tail.get()) {
                if (tailNext != null) { // A
                    // Queue in intermediate（中间) state, advance tail
                    tail.compareAndSet(curTail, tailNext); // B
                } else {
                    // In quiescent(稳定) state, try inserting new node
                    if (curTail.next.compareAndSet(null, newNode)) {// C
                        // Insertion succeeded, try advancing tail
                        tail.compareAndSet(curTail, newNode);// D
                        return true;
                    }
                }
            }
        }
    }
}
