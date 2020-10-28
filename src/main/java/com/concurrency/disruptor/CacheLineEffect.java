/**
 * Author: Tang Yuqian
 * Date: 2020/10/24
 */
package com.concurrency.disruptor;

/**
 * @author gongming
 * @description
 * @date 16/6/4
 */
public class CacheLineEffect {
    // CPU从主存中读取数据时，会将目标数据的相邻也读入同一个cacheLine.因此当你读取数组中的一个long时，会将其他临近的7个long也读入
    // 考虑一般缓存行大小是64字节，一个 long 类型占8字节
    static  long[][] arr;

    public static void main(String[] args) {
        arr = new long[1024 * 1024][];
        // 1024行8列的所有数据被设置为0
        for (int i = 0; i < 1024 * 1024; i++) {
            arr[i] = new long[8];
            for (int j = 0; j < 8; j++) {
                arr[i][j] = 0L;
            }
        }
        long sum = 0L;
        long marked = System.currentTimeMillis();
        for (int i = 0; i < 1024 * 1024; i+=1) {
            // 每一行的8列数据。每一行的这8列元素是相邻的，因此读arr[0][0]时，arr[0][0]-arr[0][7]的8个long都被读入了
            // 所以CPU与主存只需要交互1024次，但是下面读取逻辑CPU可能与主存交互1024*8次，所以本逻辑利用了cacheLine可以提高加载速度。
            for(int j =0; j< 8;j++){
                sum = arr[i][j];
            }
        }
        System.out.println("Loop times:" + (System.currentTimeMillis() - marked) + "ms");

        marked = System.currentTimeMillis();
        for (int i = 0; i < 8; i+=1) {
            // 每一列的1024行数据
            for(int j =0; j< 1024 * 1024;j++){
                sum = arr[j][i];
            }
        }
        System.out.println("Loop times:" + (System.currentTimeMillis() - marked) + "ms");
    }
}
