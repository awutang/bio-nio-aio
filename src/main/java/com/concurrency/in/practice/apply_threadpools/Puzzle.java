/**
 * Copyright 2014-2016 www.fangdd.com All Rights Reserved.
 * Author: Tang Yuqian <tangyuqian-sz@fangdd.com>
 * Date: 2020/8/17
 */
package com.concurrency.in.practice.apply_threadpools;

import java.util.*;

/**
 * Puzzle
 * <p/>
 * Abstraction for puzzles like the 'sliding blocks puzzle'
 *
 * @author Brian Goetz and Tim Peierls
 */
public interface Puzzle <P, M> {
    P initialPosition();

    boolean isGoal(P position);

    Set<M> legalMoves(P position);

    P move(P position, M move);
}
