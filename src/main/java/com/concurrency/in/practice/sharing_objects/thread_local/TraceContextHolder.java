package com.concurrency.in.practice.sharing_objects.thread_local;

import org.apache.commons.lang3.StringUtils;

import java.util.Random;

public class TraceContextHolder {

    static final ThreadLocal<TraceContext> threadLocal = new ThreadLocal();

    private TraceContextHolder(){}

    public static void setTraceContext(TraceContext context) {
        threadLocal.set(context);
    }

    public static TraceContext getTraceContext() {
        TraceContext traceContext = threadLocal.get();
        if(traceContext == null) {
            traceContext = new TraceContext();
            setTraceContext(traceContext);
        }

        return traceContext;
    }

    public static void removeTraceContext() {
        threadLocal.remove();
    }


//    public static void main(String[] args) {
//        // TraceContextHolder.removeTraceContext();
//        // 存储当前openid
//        TraceContext context = TraceContextHolder.getTraceContext();
//    }



    // 多个线程使用的threadLocal对象是同一个
    private static class TestClient extends Thread {
        // 线程产生序列号
        public void run() {
            String token = "xx" + new Random().nextDouble();
            if (StringUtils.isNotBlank(token)) {

                TraceContextHolder.removeTraceContext();

                // 存储当前openid
                TraceContext context = TraceContextHolder.getTraceContext();
                if (context == null) {
                    context = new TraceContext();
                    TraceContextHolder.setTraceContext(context);
                }
                // context是否是上一个线程的？

                context.setOpenid(token);
                // TraceContextHolder.removeTraceContext();
                Thread t = Thread.currentThread();
                // ThreadLocal.ThreadLocalMap map = getMap(t);
                System.out.println("thread[" + Thread.currentThread().getName()   + "] set[" + token + "]" + "threadLocal:" + threadLocal);
            }

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("thread[" + Thread.currentThread().getName()   + "] get[" + TraceContextHolder.getTraceContext().getOpenid() + "]");
        }
    }

    public static void main(String[] args) {

        Thread threadOne = new TestClient();
        threadOne.start();
        Thread threadTwo = new TestClient();
        threadTwo.start();

    }

}
