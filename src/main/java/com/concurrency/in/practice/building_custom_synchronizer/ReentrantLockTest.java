/**
 * Author: Tang Yuqian
 * Date: 2020/10/12
 */
package com.concurrency.in.practice.building_custom_synchronizer;

import sun.awt.windows.ThemeReader;

import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockTest {

    private static ReentrantLock reentrantLock = new ReentrantLock();

    public static void main(String[] args) {

        // 测试interrupt status
        Thread.currentThread().interrupt();
        System.out.println("isInterrupted:" + Thread.currentThread().isInterrupted());
        LockSupport.park();
        System.out.println("isInterrupted:" + Thread.currentThread().isInterrupted());
        System.out.println("interrupted:" + Thread.interrupted());

        System.out.println("isInterrupted:" + Thread.currentThread().isInterrupted());
        LockSupport.park();
        System.out.println("interrupted:" + Thread.interrupted());

        System.out.println("isInterrupted:" + Thread.currentThread().isInterrupted());
        LockSupport.park();



        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                testSync();
            }
        });
        t1.setName("t1");

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                testSync();
            }
        });
        t2.setName("t2");

        t1.start();
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        t2.start();
        try {
            Thread.sleep(2000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("main");
        t2.interrupt();
    }

    public static void testSync() {
        String threadName = Thread.currentThread().getName();
        // 不可中断
        if (threadName.equals("t2")) {
            Thread.currentThread().interrupt();
        }
        reentrantLock.lock();


//        try {
////            // park可以被中断
////            reentrantLock.lockInterruptibly();
////        } catch (InterruptedException e) {
////            System.out.println("xxxxxxxxxx");
////            e.printStackTrace();
////        }
        System.out.println(threadName);
        try {
            Thread.sleep(20000000000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(threadName);
        reentrantLock.unlock();
    }
}
