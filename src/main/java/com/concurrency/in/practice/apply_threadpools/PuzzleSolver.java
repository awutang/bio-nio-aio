/**
 * Author: Tang Yuqian
 * Date: 2020/8/17
 */
package com.concurrency.in.practice.apply_threadpools;

import java.util.concurrent.atomic.*;

/**
 * PuzzleSolver
 * <p/>
 * Solver that recognizes when no solution exists
 *
 * @author Brian Goetz and Tim Peierls
 */
public class PuzzleSolver <P,M> extends ConcurrentPuzzleSolver<P, M> {
    PuzzleSolver(Puzzle<P, M> puzzle) {
        super(puzzle);
    }

    private final AtomicInteger taskCount = new AtomicInteger(0);

    protected Runnable newTask(P p, M m, PuzzleNode<P, M> n) {
        return new CountingSolverTask(p, m, n);
    }

    class CountingSolverTask extends SolverTask {
        CountingSolverTask(P pos, M move, PuzzleNode<P, M> prev) {
            super(pos, move, prev);
            taskCount.incrementAndGet();
        }

        public void run() {
            try {
                super.run();
            } finally {
                // 一个线程执行完一个任务后就将taskCount减1并未0时设置CountDownLatch,解决了“如果solution永远不会找到则这里set永远不会执行，那调用者会一直等待”的问题
                if (taskCount.decrementAndGet() == 0)
                    solution.setValue(null);
            }
        }
    }
}
