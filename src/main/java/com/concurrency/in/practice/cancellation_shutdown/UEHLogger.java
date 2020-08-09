/**
 * Author: Tang Yuqian
 * Date: 2020/8/6
 */
package com.concurrency.in.practice.cancellation_shutdown;

import java.util.concurrent.ExecutorService;
import java.util.logging.*;

/**
 * UEHLogger
 * <p/>
 * UncaughtExceptionHandler that logs the exception
 * 需要将UncaughtExceptionHandler与Thread或threadGroup绑定
 * @author Brian Goetz and Tim Peierls
 */
public class UEHLogger implements Thread.UncaughtExceptionHandler {
    public void uncaughtException(Thread t, Throwable e) {
        Logger logger = Logger.getAnonymousLogger();
        logger.log(Level.SEVERE, "Thread terminated with exception: " + t.getName(), e);
    }

    /**
     *     只有通过 execute 提交的任务，才能将它抛出的异常交给未捕获异常处理器，
     *     而通过 submit 提交的任务，无论是抛出的未检查异常还是已检查异常，都将被认为是任务返回状态的一部分。如果一个由 submit 提交的任务由于抛出了异常而结束，那么这个异常将被 Future.get 封装在 ExecutionException 中重新抛出。
     */


}

class TestThread extends Thread {
    @Override
    public void run() {
        throw new RuntimeException();
    }

    public static void main(String[] args) {
        UncaughtExceptionHandler uncaughtExceptionHandler = new UEHLogger();
        Thread thread = new TestThread();
        // thread.setUncaughtExceptionHandler(uncaughtExceptionHandler);
        thread.start();

        // Runtime.getRuntime().addShutdownHook();
    }
}
