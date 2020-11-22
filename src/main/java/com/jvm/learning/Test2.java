/**
 * Author: Tang Yuqian
 * Date: 2020/11/1
 */
package com.jvm.learning;

/**
 * 用来解析字节码文件的类
 */
public class Test2 {

    private int age = 999;

    public static void main(String[] args) {
        Class<Test2> test2Class = Test2.class;
        Test2 obj = new Test2();
        System.out.println("hello");
    }
}
