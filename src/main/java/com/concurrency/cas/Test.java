/**
 * Author: Tang Yuqian
 * Date: 2020/11/9
 */
package com.concurrency.cas;

public class Test {

    boolean running = true;

    void m() {
        System.out.println("m start");


        if (running) {
            while (true) {

            }
        }

        while (running) {

        }
        System.out.println("m end");
    }

    void p() {
        System.out.println("print running value=" + running);
    }

    public static void main(String[] args) throws Exception {
        Test volatileDemo = new Test();
        new Thread(volatileDemo::m, "t1").start();
        // new Thread(volatileDemo::p, "t2").start();
        Thread.sleep(1000);
        volatileDemo.running = false;
    }
}
