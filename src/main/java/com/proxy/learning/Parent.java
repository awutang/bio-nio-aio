/**
 * Author: Tang Yuqian
 * Date: 2020/7/31
 */
package com.proxy.learning;

public class Parent {

    public void eat() {
        System.out.println("parent eat");
        drink();

    }

    public void drink() {
        System.out.println("parent drink");
    }


}
