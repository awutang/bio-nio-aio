/**
 * Author: Tang Yuqian
 * Date: 2020/7/27
 */
package com.concurrency.in.practice.cancellation_shutdown;


import java.sql.Time;
import java.util.concurrent.*;

import static com.concurrency.in.practice.building_blocks.LaunderThrowable.launderThrowable;
import static java.util.concurrent.Executors.newScheduledThreadPool;

/**
 * TimedRun2
 * <p/>
 * Interrupting a task in a dedicated thread
 *
 * @author Brian Goetz and Tim Peierls
 */
public class TimedRun2 {
    private static final ScheduledExecutorService cancelExec = newScheduledThreadPool(1);

    public static void timedRun(final Runnable r,
                                long timeout, TimeUnit unit)
            throws InterruptedException {
        class RethrowableTask implements Runnable {

            // volatile多线程之间内存可见
            private volatile Throwable t;

            public void run() {
                try {
                    r.run();
                } catch (Throwable t) {
                    // catch未经检查的异常
                    this.t = t;
                }
            }

            // 对未经检查的异常进行处理
            void rethrow() {
                if (t != null)
                    throw launderThrowable(t);
            }
        }

        RethrowableTask task = new RethrowableTask();
        final Thread taskThread = new Thread(task);
        taskThread.start();
        cancelExec.schedule(new Runnable() {
            public void run() {
                taskThread.interrupt();
            }
        }, timeout, unit);
        // 当前main线程等待，但当前线程必须有taskThread的monitor wait timeout
        taskThread.join(unit.toMillis(timeout));
        task.rethrow();
    }

    public static void main(String[] args) {


        try {
            TimedRun2.timedRun(new Runnable() {
                @Override
                public void run() {
                    System.out.println("run");
                }
            }, 5000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
