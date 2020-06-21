/**
 * Author: Tang Yuqian
 * Date: 2020/6/16
 */
package com.reflection.learning;

import java.lang.reflect.Method;

public class TestClassLoad {
    public static void main(String[] args) throws Exception {
        long nanos = System.nanoTime();
        Class<?> clz = Class.forName("com.reflection.learning.ATest");
        System.out.println("forName:" + String.valueOf(System.nanoTime() - nanos));

        nanos = System.nanoTime();
        Object o = clz.newInstance();
        System.out.println("newInstance:" + String.valueOf(System.nanoTime() - nanos));

        nanos = System.nanoTime();
        Method m = clz.getMethod("foo", String.class);
        System.out.println("getMethod:" + String.valueOf(System.nanoTime() - nanos));

        nanos = System.nanoTime();
        // for (int i = 0; i < 16; i++) {
            m.invoke(o, Integer.toString(2));
        // }
        System.out.println("invoke:" + String.valueOf(System.nanoTime() - nanos));


        nanos = System.nanoTime();
        ATest aTest = new ATest();
        System.out.println("new:" + String.valueOf(System.nanoTime() - nanos));

        nanos = System.nanoTime();
        aTest.foo(Integer.toString(2));
        System.out.println("foo:" + String.valueOf(System.nanoTime() - nanos));


    }
}



