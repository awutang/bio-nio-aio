/**
 * Author: Tang Yuqian
 * Date: 2020/8/17
 */
package com.concurrency.in.practice.apply_threadpools;

import java.util.*;
import java.util.concurrent.*;

/**
 * ConcurrentPuzzleSolver
 * <p/>
 * Concurrent version of puzzle solver
 *
 * @author Brian Goetz and Tim Peierls
 */
public class ConcurrentPuzzleSolver <P, M> {
    private final Puzzle<P, M> puzzle;
    private final ExecutorService exec;
    private final ConcurrentMap<P, Boolean> seen;
    protected final ValueLatch<PuzzleNode<P, M>> solution = new ValueLatch<PuzzleNode<P, M>>();

    public ConcurrentPuzzleSolver(Puzzle<P, M> puzzle) {
        this.puzzle = puzzle;
        this.exec = initThreadPool();
        this.seen = new ConcurrentHashMap<P, Boolean>();
        if (exec instanceof ThreadPoolExecutor) {
            ThreadPoolExecutor tpe = (ThreadPoolExecutor) exec;
            // 丢弃提交的线程
            tpe.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
        }
    }

    private ExecutorService initThreadPool() {
        return Executors.newCachedThreadPool();
    }

    public List<M> solve() throws InterruptedException {
        try {
            P p = puzzle.initialPosition();
            exec.execute(newTask(p, null, null));
            // block until solution found
            PuzzleNode<P, M> solnPuzzleNode = solution.getValue();
            return (solnPuzzleNode == null) ? null : solnPuzzleNode.asMoveList();
        } finally {
            // 当第一个线程solution found,调用者关闭线程池
            exec.shutdown();
        }
    }

    protected Runnable newTask(P p, M m, PuzzleNode<P, M> n) {
        return new SolverTask(p, m, n);
    }

    protected class SolverTask extends PuzzleNode<P, M> implements Runnable {
        SolverTask(P pos, M move, PuzzleNode<P, M> prev) {
            super(pos, move, prev);
        }

        public void run() {
            // 方案已找到或pos已处理过
            if (solution.isSet() || seen.putIfAbsent(pos, true) != null)
                return; // already solved or seen this position
            if (puzzle.isGoal(pos))
                // solution采用CountDownLatch，只接受第一个得到的解决方案
                // 如果solution永远不会找到则这里set永远不会执行，那调用者会一直等待
                solution.setValue(this);
            else
                for (M m : puzzle.legalMoves(pos))
                    // 可移动move对应的child全部开始执行，所以是广度优先搜索
                    exec.execute(newTask(puzzle.move(pos, m), m, this));
        }
    }
}
