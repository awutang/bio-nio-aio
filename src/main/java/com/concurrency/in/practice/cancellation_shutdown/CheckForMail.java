/**
 * Author: Tang Yuqian
 * Date: 2020/8/2
 */
package com.concurrency.in.practice.cancellation_shutdown;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

/**
 * CheckForMail
 * <p/>
 * Using a private \Executor whose lifetime is bounded by a method call
 *
 * @author Brian Goetz and Tim Peierls
 */
public class CheckForMail {
    public boolean checkMail(Set<String> hosts, long timeout, TimeUnit unit)
            throws InterruptedException {
        ExecutorService exec = Executors.newCachedThreadPool();
        final AtomicBoolean hasNewMail = new AtomicBoolean(false);
        try {
            for (final String host : hosts)
                exec.execute(new Runnable() {
                    public void run() {
                        if (checkMail(host))
                            hasNewMail.set(true);
                    }
                });
        } finally {
            // 如上的批量任务，每个执行一次就可以了
            exec.shutdown();
            exec.awaitTermination(timeout, unit);
        }
        return hasNewMail.get();
    }

    private boolean checkMail(String host) {
        // Check for mail
        return false;
    }
}
