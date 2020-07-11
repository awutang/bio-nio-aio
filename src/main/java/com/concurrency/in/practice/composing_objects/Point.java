/**
 * Author: Tang Yuqian
 * Date: 2020/6/28
 */
package com.concurrency.in.practice.composing_objects;


import jdk.nashorn.internal.ir.annotations.Immutable;

/**
 * Point
 * <p/>
 * Immutable Point class used by DelegatingVehicleTracker
 *
 * @author Brian Goetz and Tim Peierls
 */
@Immutable
public class Point {
    public final int x, y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
