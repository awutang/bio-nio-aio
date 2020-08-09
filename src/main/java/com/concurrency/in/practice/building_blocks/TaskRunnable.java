/**
 * Author: Tang Yuqian
 * Date: 2020/7/11
 */
package com.concurrency.in.practice.building_blocks;

import java.util.concurrent.BlockingQueue;

/**
 * TaskRunnable
 * <p/>
 * Restoring the interrupted status so as not to swallow the interrupt
 *
 * @author Brian Goetz and Tim Peierls
 */
public class TaskRunnable implements Runnable {
    BlockingQueue<Task> queue;

    public void run() {
        try {
            processTask(queue.take());
        } catch (InterruptedException e) {
            // restore interrupted status 从中断中恢复？由于抛出InterruptedException时的interrupted会清除interrupted status且Runnable的run方法不能继续抛出InterruptedException,所以为了让外层代码感知到threadA是已经被中断的，则需要再次将threadA.interrupt恢复interrupted status。
            // throw e;
            Thread.currentThread().interrupt();
        }
    }

    void processTask(Task task) {
        // Handle the task
    }

    interface Task {
    }
}
