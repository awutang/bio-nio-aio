/**
 * Author: Tang Yuqian
 * Date: 2020/8/9
 */
package com.concurrency.in.practice.apply_threadpools;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * ThreadDeadlock
 * <p/>
 * Task that deadlocks in a single-threaded Executor
 *
 * @author Brian Goetz and Tim Peierls
 */
public class ThreadDeadlock {
    static ExecutorService exec = Executors.newSingleThreadExecutor();

    public class LoadFileTask implements Callable<String> {
        private final String fileName;

        public LoadFileTask(String fileName) {
            this.fileName = fileName;
        }

        public String call() throws Exception {
            // Here's where we would actually read the file
            return "";
        }
    }

    public class RenderPageTask implements Callable<String> {
        public String call() throws Exception {
            Future<String> header, footer;
            header = exec.submit(new LoadFileTask("header.html"));
            footer = exec.submit(new LoadFileTask("footer.html"));
            String page = renderBody();
            // Will deadlock -- task waiting for result of subtask
            return header.get() + page + footer.get();
        }

        private String renderBody() {
            // Here's where we would actually render the page
            return "";
        }
    }

    public void main(String[] args) {

        // RenderPageTask需要wait header与footer。因为renderPageTask先进入线程池执行，header与
        // footer提交之后会进入阻塞队列一直不会被分配线程池去执行，因为renderPageTask会一直等待，并且
        // 阻塞队列中的任务也一直等着执行renderPageTask的线程释放从只来执行他们，因此renderPageTask与
        // 阻塞队列中的任务是一直相互等待的，从而死锁
        exec.submit(new RenderPageTask());


        // 这是个匿名内部类，LinkedHashMap是它的父类
        LinkedHashMap hashMap = new LinkedHashMap(){
            @Override
            protected boolean removeEldestEntry(Map.Entry eldest) {
                return 10 > 5;
            }
        };
    }
}
