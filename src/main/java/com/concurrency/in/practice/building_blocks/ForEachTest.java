/**
 * Author: Tang Yuqian
 * Date: 2020/7/5
 */
package com.concurrency.in.practice.building_blocks;

import java.util.ArrayList;
import java.util.List;

public class ForEachTest {
    /**
     * 1.foreach编译后底层是Iterator
     */

    public static void main(String[] args) {
//        List<Integer> list = new ArrayList<>();
////        list.add(1);
////        for (Integer integer : list) {
////            list.remove(integer);
////            System.out.println(integer);
////        }

        new ForEachTest().testFor();
    }

    public void testFor() {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        for (int i = 0; i < list.size(); i++) {
            list.remove(i);
            // System.out.println(list.get(i));
        }
    }


}
