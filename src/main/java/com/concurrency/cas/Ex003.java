/**
 * Author: Tang Yuqian
 * Date: 2020/10/30
 */
package com.concurrency.cas;

// import com.zhongan.xiaoying.core.util.LogUtils;
import lombok.extern.slf4j.Slf4j;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

@Slf4j
public class Ex003 {
    int flag=1;
    static Unsafe unsafe;
    static long fieldOffset;
    static {
        Class<Unsafe> unsafeClass = Unsafe.class;
        // Unsafe.getUnsafe();
        try {
            //通过反射获取unsafe
            Field field = unsafeClass.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            unsafe=(Unsafe) field.get(null);

            fieldOffset = unsafe.objectFieldOffset(Ex003.class.getDeclaredField("flag"));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws InterruptedException {
        Ex003 ex003 = new Ex003();
        /*new Thread(new Runnable() {
            int i=0;
            @Override
            public void run() {
                System.out.println("thread1:" + ex003.flag);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("thread1:" + ex003.flag);
            }
        }).start();
        Thread.sleep(1000);*/
        // ex003.changFlag();

        new Thread(new Runnable() {
            int i=0;
            @Override
            public void run() {
                // 在main线程CAS后为啥debug的时候异步线程中读到的flag是最新的，而在run时确实旧的？
                // --这应该是idea debug机制使得线程读取到的是更新后的值--其实是因为debug所以循环只执行了一两次，没触发JIT编译，那也就不会优化代码了
                // 再研究下CAS是否自带内存屏障吧--通过实验是不带的--实验是因为即时编译器优化代码的原因所以才表现出不可见的
                // unsafe.compareAndSwapInt(this, fieldOffset, 1, 0)
                while (ex003.flag==1){
                    // println方法中包含synchronized,会对JIT优化有影响(debug中也有synchronized关键字)
                    // System.out.println(i);
                    // LogUtils.projectInfoLogger.debug("" + i);
                }
            }
        }).start();

        // 因为休眠一秒，所以线程循环了足够多次，触发了JIT，然后JIT对while进行了优化
        Thread.sleep(1000);
        ex003.changFlag();
    }
    void changFlag(){
        boolean b = unsafe.compareAndSwapInt(this, fieldOffset, 1, 0);
        // System.out.println(b+" "+this.flag);
    }




}

