/**
 * Author: Tang Yuqian
 * Date: 2020/8/17
 */
package com.concurrency.in.practice.apply_threadpools;

import java.util.*;

/**
 * SequentialPuzzleSolver
 * <p/>
 * Sequential puzzle solver
 *
 * @author Brian Goetz and Tim Peierls
 */

public class SequentialPuzzleSolver <P, M> {
    private final Puzzle<P, M> puzzle;
    private final Set<P> seen = new HashSet<P>();

    public SequentialPuzzleSolver(Puzzle<P, M> puzzle) {
        this.puzzle = puzzle;
    }

    public List<M> solve() {
        P pos = puzzle.initialPosition();
        return search(new PuzzleNode<P, M>(pos, null, null));
    }

    private List<M> search(PuzzleNode<P, M> node) {
        // 避免无限循环将已经遍历过的node放入Set中
        if (!seen.contains(node.pos)) {
            seen.add(node.pos);
            // 这个解答并不一定是最短路径
            if (puzzle.isGoal(node.pos))
                return node.asMoveList();
            // 当前点的所有下一步可移动方案
            for (M move : puzzle.legalMoves(node.pos)) {
                P pos = puzzle.move(node.pos, move);
                // search返回后才进行下次迭代即下一个move,所以是深度优先搜索，可能会受到方法调用栈的大小影响，如果调用栈过小，可能在递归调用到最底层之前就报错了OutOfMemoey
                PuzzleNode<P, M> child = new PuzzleNode<P, M>(pos, move, node);
                List<M> result = search(child);
                if (result != null)
                    // 在这里return表示没有找到global,只是把某个直至遍历到无child的move集合返回了
                    return result;
            }
        }
        return null;
    }
}
