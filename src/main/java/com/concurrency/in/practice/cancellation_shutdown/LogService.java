/**
 * Author: Tang Yuqian
 * Date: 2020/8/2
 */
package com.concurrency.in.practice.cancellation_shutdown;

import java.io.*;
import java.util.concurrent.*;

import net.jcip.annotations.*;
import sun.rmi.runtime.Log;

/**
 * LogService
 * <p/>
 * Adding reliable cancellation to LogWriter
 *
 * @author Brian Goetz and Tim Peierls
 */
public class LogService {
    private final BlockingQueue<String> queue;
    private final LoggerThread loggerThread;
    private final PrintWriter writer;
    @GuardedBy("this") private boolean isShutdown;
    @GuardedBy("this") private int reservations;

    public LogService(Writer writer) {
        this.queue = new LinkedBlockingQueue<String>();
        this.loggerThread = new LoggerThread();
        this.writer = new PrintWriter(writer);
    }

    public void start() {
        loggerThread.start();
    }

    public void stop() {
        synchronized (this) {
            isShutdown = true;
        }
        loggerThread.interrupt();
    }

    public void log(String msg) throws InterruptedException {
        synchronized (this) {
            // 中断了生产者
            if (isShutdown)
                throw new IllegalStateException(/*...*/);
            ++reservations;
        }

        // 之所以不把put放入同步操作，是因为put如果阻塞则一直不会释放this内部锁（put内部await释放的是ReentrantLock的锁，不是内部锁synchronized)
        queue.put(msg);
    }

    private class LoggerThread extends Thread {
        public void run() {
            try {
                while (true) {
                    try {
                        synchronized (LogService.this) {
                            // 判断reservations == 0是为了让消费者被中断后将队列中还在等待的日志输出
                            if (isShutdown && reservations == 0)
                                break;
                        }
                        String msg = queue.take();
                        synchronized (LogService.this) {
                            --reservations;
                        }


                        // println的中断是重新interrupt了，并未抛出InterruptedException，如下的catch无法捕捉println的中断?
                        // 由于reservations的作用，若当前消费线程正在take阻塞，则可以catch异常，并判断是否还有未处理的消息，无则退出真正中断，有则继续take打印
                        // 如果当前消费线程不在take处阻塞,则下一次循环判断isShutdown && reservations == 0，继续如上的操作，从而实现将所有需要输出的log打印
                        // 因此，println即使被中断
                        writer.println(msg);
                    } catch (InterruptedException e) { /* retry */
                    }
                }
            } finally {
                writer.close();
            }
        }
    }

    private static class Producer extends Thread {

        private LogService logService;

        public Producer(LogService logService) {
            this.logService = logService;
        }

        @Override
        public void run() {
            try {
                logService.log("hello, I am a producer" + currentThread().getName());
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println("I am interrupted, help!");
            }
        }
    }

    public static void main(String[] args) throws IOException {

        Writer logWriter = new CharArrayWriter() {
            @Override
            public void flush() {
                System.out.println(this.toString());
            }
        };

        LogService logService = new LogService(logWriter);

        // 生产者两个，消费者一个
        new Producer(logService).start();
        new Producer(logService).start();
        logService.start();

        // 放弃cpu
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 中断
        logService.stop();

        logWriter.flush();
    }


}

