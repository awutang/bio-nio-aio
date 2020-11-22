/**
 * Author: Tang Yuqian
 * Date: 2020/10/31
 */
package com.concurrency.cas;

import java.util.concurrent.atomic.AtomicInteger;

public class CASVisibleTest {


    private static AtomicInteger sharedVariable = new AtomicInteger(0);
    private static final int MAX = 10;

    public static void main(String[] args) {
        new Thread(() -> {
            int oldValue = sharedVariable.get();
            while (sharedVariable.get() < MAX) {
                if (sharedVariable.get() != oldValue) {
                    System.out.println(Thread.currentThread().getName() + " watched the change : " + oldValue + "->" + sharedVariable);
                    oldValue = sharedVariable.get();
                }
            }
        }, "t1").start();

        new Thread(() -> {
            int oldValue = sharedVariable.get();
            while (sharedVariable.get() < MAX) {
                System.out.println(Thread.currentThread().getName() + " do the change : " + sharedVariable + "->" + (++oldValue));
                sharedVariable.set(oldValue);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "t2").start();

    }

}
