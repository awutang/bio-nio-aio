/**
 * Author: Tang Yuqian
 * Date: 2020/7/12
 */
package com.concurrency.in.practice.building_blocks;

import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierDemo2 {
    static CyclicBarrier barrier = new CyclicBarrier(2, new After());

    public static void main(String[] args) {
        new Thread() {
            @Override
            public void run() {
                System.out.println("In thread");
                try {
                    // 所有线程到达屏障后直接返回，
                    // 否则会阻塞在Condition.await的LockSupport.park中，满足符合的条件后park会返回
                    barrier.await();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

        System.out.println("In main");
        try {
            barrier.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Finish.");
    }

    static class After implements Runnable {
        @Override
        public void run() {
            System.out.println("All reach barrier.");
        }
    }
}
