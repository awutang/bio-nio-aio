/**
 * Author: Tang Yuqian
 * Date: 2020/8/12
 */
package com.concurrency.in.practice.apply_threadpools;

import java.util.concurrent.*;

import net.jcip.annotations.*;

/**
 * BoundedExecutor
 * <p/>
 * Using a Semaphore to throttle task submission
 *
 * @author Brian Goetz and Tim Peierls
 */
@ThreadSafe
public class BoundedExecutor {
    private final Executor exec;
    private final Semaphore semaphore;

    public BoundedExecutor(Executor exec, int bound) {
        this.exec = exec;
        this.semaphore = new Semaphore(bound);
    }

    public void submitTask(final Runnable command)
            throws InterruptedException {
        // 利用semaphore控制需要提交的任务数量，包括当前执行的和等待在队列中的，
        // 任务执行完成或默认拒绝策略拒绝时才将semaphore的permits加1；提交任务之前将semaphore的permits加1
        // 这样就不会发生需要采用拒绝策略的场景了，自然而然可以不用设置abort policy
        semaphore.acquire();
        try {
            exec.execute(new Runnable() {
                public void run() {
                    try {
                        command.run();
                    } finally {
                        semaphore.release();
                    }
                }
            });
        } catch (RejectedExecutionException e) {
            semaphore.release();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executor = new ThreadPoolExecutor(1, 3,
                60L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>());

        BoundedExecutor boundedExecutor = new BoundedExecutor(executor, 4);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    // 为了让线程执行一段时间，模仿semaphore.acquire时阻塞的场景
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        for (int i = 0; i < 5; i++) {
            boundedExecutor.submitTask(runnable);
        }


    }
}
