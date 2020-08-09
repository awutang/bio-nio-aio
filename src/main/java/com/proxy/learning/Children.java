/**
 * Author: Tang Yuqian
 * Date: 2020/7/31
 */
package com.proxy.learning;

public class Children extends Parent {

    @Override
    public void eat() {
        System.out.println("children eat");
        super.eat();
    }

    @Override
    public void drink() {
        System.out.println("children eat");
        super.drink();
    }

    public static void main(String[] args) {
        Parent parent = new Children();
        parent.eat();
    }
}
