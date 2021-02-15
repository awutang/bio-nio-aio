/**
 * Author: Tang Yuqian
 * Date: 2020/10/24
 */
package com.concurrency.disruptor;

/**
 * 伪共享:不同线程需要修改的不同变量处于一个cacheLine中，导致任何一个线程修改数据导致另一个线程的缓存行失效
 * 增大数组元素的间隔使得由不同线程存取的元素位于不同的缓存行上，以空间换时间
 */
public class FalseSharing implements Runnable{
    public final static long ITERATIONS = 500L * 1000L * 100L;
    private int arrayIndex = 0;

    private static ValuePadding[] longs;
    public FalseSharing(final int arrayIndex) {
        this.arrayIndex = arrayIndex;
    }

    public static void main(final String[] args) throws Exception {
        for(int i=1;i<10;i++){
            System.gc();
            final long start = System.currentTimeMillis();
            runTest(i);
            System.out.println("Thread num "+i+" duration = " + (System.currentTimeMillis() - start));
        }

    }

    private static void runTest(int NUM_THREADS) throws InterruptedException {
        Thread[] threads = new Thread[NUM_THREADS];
        longs = new ValuePadding[NUM_THREADS];
        for (int i = 0; i < longs.length; i++) {
            // 一个ValuePadding实例的value字段左右有七个long，因此对value的读会将左或右的七个long读入，
            // 不会读成其他longs[i]（其他threads[i])的数据
            longs[i] = new ValuePadding();
        }
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(new FalseSharing(i));
        }

        for (Thread t : threads) {
            t.start();
        }

        // main线程等着t执行完才返回
        for (Thread t : threads) {
            t.join();
        }
    }

    public void run() {
        long i = ITERATIONS + 1;
        // 每个线程的value赋值ITERATIONS次，根据缓存一致性协议MESI,修改值时，会使得其他加载了这个值的缓存行无效
        // 比如longs[0]、longs[1]在不同的线程中，如果采用ValueNoPadding，longs[0]、longs[1]的value可能被threads[0]、threads[1]加载
        // 同一个缓存行中（不同线程的工作内存的缓存行中)，当longs[0].value修改时，threads[1]的工作内存的缓存行会无效，因此
        // threads[1]如果想读取longs[1].value则需要重新从主存中读取。这个过程就是伪共享。
        // 因为一个线程修改数据，另一个线程下次肯定要从主存中获取数据，所以通过在value前后pad数据将缓存行空间占满，就可以让另一个线程的工作内存的缓存行
        // 中不再含有其他线程的value,这样线程只会在第一次读取数据时与主存交互，其他读取都会在缓存行中找得到。
        // 这就是以空间换取缓存行不再被置为无效，节省时间

        // TODO：有个疑问，这里是数据修改也涉及到了读取数据吗？如果没有读取value那也不会与主存交互吧，时间也不会有浪费
        // --写的话需要线程发送request for owner请求得到L3Cache上的所有权并将其他线程的缓存行设置为invalid,这些操作消耗性能。https://www.cnblogs.com/cyfonly/p/5800758.html
        // 总而言之，结合上文，伪共享不仅在读取时因为一定要重新去主存读取数据导致速度变慢，而且在写入时由于竞争也会带来性能消耗。
        while (0 != --i) {
            longs[arrayIndex].value = 0L;
        }
    }

    /**
     * Thread num 1 duration = 349
     * Thread num 2 duration = 353
     * Thread num 3 duration = 482
     * Thread num 4 duration = 456
     * Thread num 5 duration = 471
     * Thread num 6 duration = 524
     * Thread num 7 duration = 584
     * Thread num 8 duration = 638
     * Thread num 9 duration = 722
     */
    public final static class ValuePadding {
        protected long p1, p2, p3, p4, p5, p6, p7;
        protected volatile long value = 0L;
        protected long p9, p10, p11, p12, p13, p14;
        protected long p15;
    }

    /**
     * Thread num 1 duration = 346
     * Thread num 2 duration = 1660
     * Thread num 3 duration = 2123
     * Thread num 4 duration = 2396
     * Thread num 5 duration = 3301
     * Thread num 6 duration = 3527
     * Thread num 7 duration = 3197
     * Thread num 8 duration = 3270
     * Thread num 9 duration = 2609
     */
    public final static class ValueNoPadding {
        // protected long p1, p2, p3, p4, p5, p6, p7;
        protected volatile long value = 0L;
        // protected long p9, p10, p11, p12, p13, p14, p15;
    }
}
