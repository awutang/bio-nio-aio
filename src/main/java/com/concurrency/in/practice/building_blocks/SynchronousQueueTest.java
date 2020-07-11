/**
 * Author: Tang Yuqian
 * Date: 2020/7/9
 */
package com.concurrency.in.practice.building_blocks;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SynchronousQueueTest {

    public static void main(String[] args) {

        // 1.演示下SynchronousQueue的活动
        // 2.SynchronousQueue到底有没有维护一个元素？--没有，维护的是线程队列
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                System.out.println("hello start");
            }
        });

    }
}
