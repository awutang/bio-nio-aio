/**
 * Author: Tang Yuqian
 * Date: 2020/11/8
 */
package com.jvm.learning;

public class TestMarkCompact {

    private static String callableStr;

    public static void main(String[] args) {

        callableStr = new String();
        callableStr = new String();
        callableStr = new String();
        callableStr = new String();

        String ss = new String();
        System.out.println(System.identityHashCode(ss));
        // 现在都是g1算法，调用的不一定是标记整理算法
        System.gc();
        System.out.println(System.identityHashCode(ss));
    }
}
