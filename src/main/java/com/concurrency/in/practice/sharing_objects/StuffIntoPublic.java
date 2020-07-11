/**
 * Author: Tang Yuqian
 * Date: 2020/6/26
 */
package com.concurrency.in.practice.sharing_objects;

/**
 * StuffIntoPublic
 * <p/>
 * Unsafe publication
 *
 * @author Brian Goetz and Tim Peierls
 * 3.14 Publishing an object without adequate synchronization.
 */
public class StuffIntoPublic {
    public Holder holder;

    public void initialize() {
        System.out.println(Thread.currentThread().getName() + "initialize");
        holder = new Holder(42);
    }

    public static void main(String[] args) throws InterruptedException {
        // 两个线程都是先判断holder是否已有值，没有则initialize，
        // 如果其中一个threadA正在初始化时，另一个threadB判断holder引用已有值（但是threadA还未将构造方法执行完成），threadB调用assertSanity会报错？
        // threadB执行assertSanity时会取两次n，第一次是0，第二次是42（threadA已将构造方法执行完成)，则不相等报错
        StuffIntoPublic stuffIntoPublic = new StuffIntoPublic();
        Thread threadA = new TestThread(stuffIntoPublic);
        threadA.start();
        // 休眠500ms，为了让threadA执行
        // Thread.sleep(500);
        Thread threadB = new TestThread(stuffIntoPublic);
        threadB.start();
    }
}

class TestThread extends Thread {
    private StuffIntoPublic stuffIntoPublic;

    public TestThread(StuffIntoPublic stuffIntoPublic) {
        this.stuffIntoPublic = stuffIntoPublic;
    }

    @Override
    public void run () {
        if (stuffIntoPublic.holder == null) {
            stuffIntoPublic.initialize();
        } else {
            stuffIntoPublic.holder.assertSanity();
        }

    }
}
