/**
 * Author: Tang Yuqian
 * Date: 2020/6/21
 */
package com.concurrency.in.practice;


import java.util.EventListener;

public class ThisEscape {
    private int value;

    public static void main(String[] args) {
        // EventSource source = new EventSourceImpl();
        ThisEscape thisEscape = new ThisEscape();



    }

    public ThisEscape() {

        // source.registerListener(
            new EventListener(){
                public void onEvent(){
                    // 此处在ThisEscape对象还未实例化完成之前就调用了它的doSomething方法并且访问了它的value成员变量
                    // 但是成员变量还未初始化，所以会存在安全问题
                    doSomething();
                    // System.out.println(ThisEscape.this.value);
                }
            }.onEvent();
        // );


        //一些初始化工作
        value = 7;
    }

    public void doSomething(){
        System.out.println(value);
    }


}
