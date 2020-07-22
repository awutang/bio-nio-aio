/**
 * Author: Tang Yuqian
 * Date: 2020/7/13
 */
package com.concurrency.in.practice.building_blocks;

import org.apache.commons.lang3.concurrent.Computable;

import java.util.concurrent.*;

/**
 * Memoizer
 * <p/>
 * Final implementation of Memoizer
 *
 * @author Brian Goetz and Tim Peierls
 */
public class Memoizer <A, V> implements Computable<A, V> {
    private final ConcurrentMap<A, Future<V>> cache
            = new ConcurrentHashMap<A, Future<V>>();
    private final Computable<A, V> c;

    public Memoizer(Computable<A, V> c) {
        this.c = c;
    }

    public V compute(final A arg) throws InterruptedException {
        while (true) {
            Future<V> f = cache.get(arg);
            if (f == null) {
                Callable<V> eval = new Callable<V>() {
                    public V call() throws InterruptedException {
                        return c.compute(arg);
                    }
                };
                FutureTask<V> ft = new FutureTask<V>(eval);
                f = cache.putIfAbsent(arg, ft);
                if (f == null) {
                    f = ft;
                    ft.run();
                }
            }
            try {
                return f.get();
            } catch (CancellationException e) {
                // 如果计算被取消，则将future从map中删掉
                cache.remove(arg, f);
            } catch (ExecutionException e) {
                // 如果有异常，也需要在上层代码中从map中remove，这样才能使得同样的arg再次计算才不会失败
                // 但是仍有计算结果过期的问题。可以通过一个FutureTask的子类为结果关联一个超时时间，定时扫描，清除cache中的数据
                throw LaunderThrowable.launderThrowable(e.getCause());
            }
        }
    }
}
