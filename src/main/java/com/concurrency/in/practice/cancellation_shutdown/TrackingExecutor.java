/**
 * Author: Tang Yuqian
 * Date: 2020/8/2
 */
package com.concurrency.in.practice.cancellation_shutdown;

import java.util.*;
import java.util.concurrent.*;

/**
 * TrackingExecutor
 * <p/>
 * ExecutorService that keeps track of cancelled tasks after shutdown
 *
 * @author Brian Goetz and Tim Peierls
 */
public class TrackingExecutor extends AbstractExecutorService {
    private final ExecutorService exec;
    private final Set<Runnable> tasksCancelledAtShutdown =
            Collections.synchronizedSet(new HashSet<Runnable>());

    public TrackingExecutor(ExecutorService exec) {
        this.exec = exec;
    }

    public void shutdown() {
        exec.shutdown();
    }

    public List<Runnable> shutdownNow() {
        return exec.shutdownNow();
    }

    public boolean isShutdown() {
        return exec.isShutdown();
    }

    public boolean isTerminated() {
        return exec.isTerminated();
    }

    public boolean awaitTermination(long timeout, TimeUnit unit)
            throws InterruptedException {
        return exec.awaitTermination(timeout, unit);
    }

    // 已经开始却没有正常结束的任务
    public List<Runnable> getCancelledTasks() {
        // 需要等线程池状态变为TERMINATED，线程池中已经空,才能获取到进行中被取消的任务
        if (!exec.isTerminated())
            throw new IllegalStateException(/*...*/);
        return new ArrayList<Runnable>(tasksCancelledAtShutdown);
    }

    public void execute(final Runnable runnable) {
        exec.execute(new Runnable() {
            public void run() {
                try {
                    runnable.run();
                } finally {
                    // 当前执行线程必须在任务返回时保存线程的中断状态

                    // 如果从runnable.run()返回时才将线程池shutdownNow（会将所有工作线程interrupt)，所以此时两个条件都满足，假阳性
                    if (isShutdown()
                            && Thread.currentThread().isInterrupted()/*当前执行线程必须在任务返回时保存线程的中断状态*/)
                        tasksCancelledAtShutdown.add(runnable);
                }
            }
        });
    }
}
