/**
 * Author: Tang Yuqian
 * Date: 2020/6/14
 */
package com.concurrency.reentrance;

public class LoggingWidget extends Widget{
    public synchronized void doSomething() {
        System.out.println(toString() + ": calling doSomething");
        super.doSomething();
    }

    public static void main(String[] args) {
        LoggingWidget loggingWidget = new LoggingWidget();
        loggingWidget.doSomething();
    }
}


class Widget {
    public synchronized void doSomething() {
        // do somethig here...
        System.out.println("parent do something");
    }
}


