/**
 * Author: Tang Yuqian
 * Date: 2020/8/19
 */
package com.concurrency.in.practice.avoiding_liveness_hazards;

import java.util.*;

import com.concurrency.in.practice.composing_objects.Point;
import net.jcip.annotations.*;

/**
 * CooperatingDeadlock
 * <p/>
 * Lock-ordering deadlock between cooperating objects
 *
 * @author Brian Goetz and Tim Peierls
 */
public class CooperatingDeadlock {
    // Warning: deadlock-prone!
    class Taxi {
        @GuardedBy("this") private Point location, destination;
        private final Dispatcher dispatcher;

        public Taxi(Dispatcher dispatcher) {
            this.dispatcher = dispatcher;
        }

        public synchronized Point getLocation() {
            return location;
        }

        // 先获取taxi锁并且在持有taxi锁的同时再获取dispatcher锁，与getImage（)会发生死锁
        public synchronized void setLocation(Point location) {
            this.location = location;
            if (location.equals(destination))
                // 再获取dispatcher锁
                dispatcher.notifyAvailable(this);
        }

        public synchronized Point getDestination() {
            return destination;
        }

        public synchronized void setDestination(Point destination) {
            this.destination = destination;
        }
    }

    class Dispatcher {
        @GuardedBy("this") private final Set<Taxi> taxis;
        @GuardedBy("this") private final Set<Taxi> availableTaxis;

        public Dispatcher() {
            taxis = new HashSet<Taxi>();
            availableTaxis = new HashSet<Taxi>();
        }

        public synchronized void notifyAvailable(Taxi taxi) {
            availableTaxis.add(taxi);
        }

        // 先获取dispatcher锁再获取taxi，此时调用时notifyAvailable已执行完并且在还未执行到getLocation()时没有另一个线程正在执行setLocation
        // ，否则就会发生死锁，因此多个现场之间只能串行执行getImage与setLocation，因此getImage中获取的是最新taxi列表
        public synchronized Image getImage() {
            Image image = new Image();
            for (Taxi t : taxis)
                // 再获取taxi锁
                image.drawMarker(t.getLocation());
            return image;
        }
    }

    class Image {
        public void drawMarker(Point p) {
        }
    }
}
