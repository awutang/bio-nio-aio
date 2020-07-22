/**
 * Author: Tang Yuqian
 * Date: 2020/7/12
 */
package com.concurrency.in.practice.building_blocks;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExchangeDemo {
    private static Exchanger<String> exch = new Exchanger<>();

    private static ExecutorService pool = Executors.newFixedThreadPool(2);

    // 用来保证线程池在两个线程执行完之后再关闭
    private static CountDownLatch latch = new CountDownLatch(2);

    public static void main(String[] args) {
        pool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String data = "第一个线程的结果";
                    Thread.sleep(100);
                    String res = exch.exchange(data);
                    System.out.println("我是第一个线程，我收到另一个线程的结果为：" + res);
                    latch.countDown();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        pool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String data = "第二个线程的结果";
                    Thread.sleep(1000);
                    String res = exch.exchange(data);
                    System.out.println("我是第二个线程，我收到另一个线程的结果为：" + res);
                    latch.countDown();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        try {
            latch.await();  // 等待两线程执行完，然后关闭线程池
        } catch (Exception e) {
            e.printStackTrace();
        }
        pool.shutdown();
    }
}
