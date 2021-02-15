/**
 * Author: Tang Yuqian
 * Date: 2021/1/2
 */
package com.jvm.learning;

import lombok.AllArgsConstructor;
import lombok.Data;
import sun.misc.Cleaner;

/**
 * 当cleaner被从pending链表中取出调用clean()时，referent指向已经置为null了吗？
 */
public class CleanerTest {
    public static void main(String[] args) throws Exception {
        int index = 0;
        while (true) {
            Thread.sleep(1000);
            // 提醒 GC 去进行垃圾收集了
            System.gc();

            // 该对象不断重新指向其他地方，那么原先指针指向的对象的就属于需要回收的数据
            DemoObject obj = new DemoObject("demo01");
            /*
                 增加 obj 的虚引用,定义清理的接口 DoSomethingThread
                 第一个参数：需要监控的堆内存对象
                 第二个参数：程序释放资源前的回调。
             */
            Cleaner.create(obj, new DoSomethingThread("thread_" + index++));
        }
    }

    static class DoSomethingThread implements Runnable {
        private String name;

        public DoSomethingThread(String name) {
            this.name = name;
        }

        // do something before gc
        @Override
        public void run() {
            System.out.println(name + " running DoSomething ...");
        }
    }

    @Data
    @AllArgsConstructor
    static class DemoObject{
        private String name;
    }
}

