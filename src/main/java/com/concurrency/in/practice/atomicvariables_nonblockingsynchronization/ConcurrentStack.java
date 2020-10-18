/**
 * Author: Tang Yuqian
 * Date: 2020/10/18
 */
package com.concurrency.in.practice.atomicvariables_nonblockingsynchronization;

import java.util.concurrent.atomic.*;

import net.jcip.annotations.*;

/**
 * ConcurrentStack
 * <p/>
 * Nonblocking stack using Treiber's algorithm
 *
 * @author Brian Goetz and Tim Peierls
 */
@ThreadSafe
public class ConcurrentStack <E> {

    // 因为栈顶只有一个元素，因此用一个原子变量top表示栈顶即可。
    // 实际上其他元素（除了栈顶元素的next节点之外)不在ConcurrentStack中--其实不是的，Node通过next将所有节点都关联起来了，其实内部实现是一个单向链表
    AtomicReference<Node<E>> top = new AtomicReference<Node<E>>();

    public void push(E item) {
        Node<E> newHead = new Node<E>(item);
        Node<E> oldHead;
        do {
            oldHead = top.get();
            // 新的栈顶元素指向old,相当于oldHead已经是栈中栈顶的下一个元素
            newHead.next = oldHead;
        } while (!top.compareAndSet(oldHead, newHead));
    }

    public E pop() {
        Node<E> oldHead;
        Node<E> newHead;
        do {
            oldHead = top.get();
            if (oldHead == null)
                return null;
            // 新的栈顶元素是head的下一个元素，用newHead替代top则代表下一个元素成为栈顶元素，即出栈
            newHead = oldHead.next;
        } while (!top.compareAndSet(oldHead, newHead));
        return oldHead.item;
    }

    private static class Node <E> {
        public final E item;
        public Node<E> next;

        public Node(E item) {
            this.item = item;
        }
    }

    public static void main(String[] args) {
        ConcurrentStack stack = new ConcurrentStack<>();
        stack.push(1);
        stack.push(2);
        stack.push(3);
        System.out.println(stack.pop());
    }
}
