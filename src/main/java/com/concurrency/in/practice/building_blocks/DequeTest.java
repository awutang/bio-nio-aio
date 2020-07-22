/**
 * Author: Tang Yuqian
 * Date: 2020/7/11
 */
package com.concurrency.in.practice.building_blocks;


import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

public class DequeTest {

    static class LinkedBlockingDequeTest {

        private LinkedBlockingDeque<String> linkedBlockingDeque;

        public LinkedBlockingDequeTest(LinkedBlockingDeque<String> linkedBlockingDeque) {
            this.linkedBlockingDeque = linkedBlockingDeque;
        }

        public void operation(String item) {

            // linkedBlocking中的Node是有pre与next指针的，并且整个队列中有first、last引用，这样才使得任意一头都可以出队与入队
            linkedBlockingDeque.addFirst(item);
            linkedBlockingDeque.addLast(item);

            linkedBlockingDeque.pollFirst();
            linkedBlockingDeque.pollLast();
        }
    }

    public static void main(String[] args) {
        LinkedBlockingDeque<String> linkedBlockingDeque = new LinkedBlockingDeque<>();

        new LinkedBlockingDequeTest(linkedBlockingDeque).operation("sss");
    }
}
