/**
 * Author: Tang Yuqian
 * Date: 2021/1/2
 */
package com.jvm.learning;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;

public class PhantomReferenceTest {

    public static void main(String[] args) {
        try {
            demo();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void demo() throws InterruptedException {
        Object obj = new Object();
        ReferenceQueue<Object> refQueue =new ReferenceQueue<>();
        PhantomReference<Object> phanRef =new PhantomReference<>(obj, refQueue);

        Object objg = phanRef.get();
        //这里拿到的是null
        System.out.println(objg);
        //让obj变成垃圾
        obj=null;
        System.gc();
        Thread.sleep(3000);
        //gc后会将phanRef加入到refQueue中,如果obj没有变成垃圾(referent指向的对象不可达)，phanRef不会加入到refQueue的
        // 并且虽然referent指向的对象不可达，但是找个对象并没有被回收！！！
        /**
         * 引用队列可以与软引用、弱引用以及虚引用一起配合使用，当垃圾回收器准备回收一个对象时，
         * 如果发现它还有引用，那么就会在回收对象之前，把这个引用加入到与之关联的引用队列中去。
         * 程序可以通过判断引用队列中是否已经加入了引用，来判断被引用的对象是否将要被垃圾回收，
         * 这样就可以在对象被回收之前采取一些必要的措施
         */
        Reference<? extends Object> phanRefP = refQueue.remove();
        //这里输出true
        System.out.println(phanRefP==phanRef);

        // 强行将referent=null,解除引用与对象之间的关系,那么下次gc时会回收对象
        phanRef.clear();
        System.gc();
        Thread.sleep(3000);
    }

}
