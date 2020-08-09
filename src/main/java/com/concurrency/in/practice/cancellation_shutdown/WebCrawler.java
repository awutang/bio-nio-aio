/**
 * Author: Tang Yuqian
 * Date: 2020/8/3
 */
package com.concurrency.in.practice.cancellation_shutdown;

import java.net.URL;
import java.util.*;
import java.util.concurrent.*;

import net.jcip.annotations.*;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * WebCrawler
 * <p/>
 * Using TrackingExecutorService to save unfinished tasks for later execution
 *
 * @author Brian Goetz and Tim Peierls
 */
public abstract class WebCrawler {
    private volatile TrackingExecutor exec;
    @GuardedBy("this") private final Set<URL> urlsToCrawl = new HashSet<URL>();

    protected final ConcurrentMap<URL, Boolean> seen = new ConcurrentHashMap<URL, Boolean>();
    private static final long TIMEOUT = 500;
    private static final TimeUnit UNIT = MILLISECONDS;

    public WebCrawler(URL startUrl) {
        urlsToCrawl.add(startUrl);
    }

    public synchronized void start() {
        exec = new TrackingExecutor(Executors.newCachedThreadPool());

        // urlsToCrawl中包括所有未执行或上次执行中未正常结束的url,因此重新启动后可以恢复上次的场景
        for (URL url : urlsToCrawl) submitCrawlTask(url);
        urlsToCrawl.clear();
    }

    public synchronized void stop() throws InterruptedException {
        try {
            saveUncrawled(exec.shutdownNow());

            // 等待500ms就返回，获取中断过程已开始且未正常完结的任务
            if (exec.awaitTermination(TIMEOUT, UNIT))
                saveUncrawled(exec.getCancelledTasks());
        } finally {
            exec = null;
        }
    }

    protected abstract List<URL> processPage(URL url);

    private void saveUncrawled(List<Runnable> uncrawled) {
        for (Runnable task : uncrawled)
            urlsToCrawl.add(((CrawlTask) task).getPage());
    }

    private void submitCrawlTask(URL u) {
        exec.execute(new CrawlTask(u));
    }

    private class CrawlTask implements Runnable {
        private final URL url;

        CrawlTask(URL url) {
            this.url = url;
        }

        private int count = 1;

        boolean alreadyCrawled() {
            return seen.putIfAbsent(url, true) != null;
        }

        void markUncrawled() {
            seen.remove(url);
            System.out.printf("marking %s uncrawled%n", url);
        }

        public void run() {
            for (URL link : processPage(url)) {
                // 判断如果被中断
                if (Thread.currentThread().isInterrupted())
                    return;
                submitCrawlTask(link);
                // 将已处理的任务去掉，防止假阳性导致的重复执行
                if (alreadyCrawled()) {
                    urlsToCrawl.remove(url);
                }
            }
        }

        public URL getPage() {
            return url;
        }
    }
}
