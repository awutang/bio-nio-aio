/**
 * Author: Tang Yuqian
 * Date: 2020/9/8
 */
package com.concurrency.in.practice.explict_locks;

import java.util.*;
import java.util.concurrent.locks.*;

/**
 * ReadWriteMap
 * <p/>
 * Wrapping a Map with a read-write lock
 *
 * @author Brian Goetz and Tim Peierls
 */
public class ReadWriteMap <K,V> {
    private final Map<K, V> map;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock r = lock.readLock();
    private final Lock w = lock.writeLock();

    public ReadWriteMap(Map<K, V> map) {
        this.map = map;
    }

    public V put(K key, V value) {
        // 写锁获取时会判断是否有线程已经持有了读锁，若有（即使持有读锁的是当前线程)则等待
        w.lock();
        try {
            return map.put(key, value);
        } finally {
            w.unlock();
        }
    }

    public V remove(Object key) {
        w.lock();
        try {
            return map.remove(key);
        } finally {
            w.unlock();
        }
    }

    public void putAll(Map<? extends K, ? extends V> m) {
        w.lock();
        try {
            map.putAll(m);
        } finally {
            w.unlock();
        }
    }

    public void clear() {
        w.lock();
        try {
            map.clear();
        } finally {
            w.unlock();
        }
    }

    public V get(Object key) {
        // 也判断了写锁是否存在，若存在且被其他线程占有，则当前线程等待
        r.lock();
        try {
            return map.get(key);
        } finally {
            r.unlock();
        }
    }

    public int size() {
        r.lock();
        try {
            return map.size();
        } finally {
            r.unlock();
        }
    }

    public boolean isEmpty() {
        r.lock();
        try {
            return map.isEmpty();
        } finally {
            r.unlock();
        }
    }

    public boolean containsKey(Object key) {
        r.lock();
        try {
            return map.containsKey(key);
        } finally {
            r.unlock();
        }
    }

    public boolean containsValue(Object value) {
        r.lock();
        try {
            return map.containsValue(value);
        } finally {
            r.unlock();
        }
    }
}
