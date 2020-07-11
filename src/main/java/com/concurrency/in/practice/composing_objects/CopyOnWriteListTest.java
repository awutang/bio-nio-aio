/**
 * Author: Tang Yuqian
 * Date: 2020/6/28
 */
package com.concurrency.in.practice.composing_objects;

import java.util.concurrent.CopyOnWriteArrayList;

public class CopyOnWriteListTest {

    public static void main(String[] args) {
        CopyOnWriteArrayList<String> copyOnWriteArrayList = new CopyOnWriteArrayList<>();
        copyOnWriteArrayList.add("111");
        copyOnWriteArrayList.add("222");
        copyOnWriteArrayList.add("333");
        for (String str : copyOnWriteArrayList) {
            new Thread(() -> {
//                try {
//                    Thread.currentThread().sleep(200);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                copyOnWriteArrayList.clear();
            }).start();
            System.out.println(str);
        }
        System.out.println(copyOnWriteArrayList);
    }
}
