/**
 * Author: Tang Yuqian
 * Date: 2020/7/5
 */
package com.concurrency.in.practice.building_blocks;

import java.util.*;

import net.jcip.annotations.*;

/**
 * HiddenIterator
 * <p/>
 * Iteration hidden within string concatenation
 *
 * @author Brian Goetz and Tim Peierls
 */
public class HiddenIterator {
    @GuardedBy("this") private final Set<Integer> set = new HashSet<Integer>();

    public synchronized void add(Integer i) {
        set.add(i);
    }

    public synchronized void remove(Integer i) {
        set.remove(i);
    }

    public void addTenThings() {
        Random r = new Random();
        for (int i = 0; i < 10; i++)
            add(r.nextInt());

        // 由于+操作，会将set转换成String，会调用Set.toString(),toString会迭代Set中的每个元素append到StringBuilder中
        // 由于也使用到了迭代，因此在多线程下可能会发生ConcurrentModification.采用工厂方法包装成synchronizedSet可以实现同步操作
        System.out.println("DEBUG: added ten elements to " + set);
    }

    public static void main(String[] args) {
        new HiddenIterator().addTenThings();
    }
}
