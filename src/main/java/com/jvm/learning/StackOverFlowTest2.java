/**
 * Author: Tang Yuqian
 * Date: 2020/11/1
 */
package com.jvm.learning;

public class StackOverFlowTest2 {


    private int deep = 0;

    public void test() {
        deep++;
        test();
    }
    public static void main(String[] args) {

        StackOverFlowTest2 stackOverFlowTest2 = new StackOverFlowTest2();

        try {
            stackOverFlowTest2.test();
        } catch (Throwable e) {
            // e.printStackTrace();
            System.out.println(stackOverFlowTest2.deep); // 这个是调用链的深度,所以栈帧的大小=160KB/772
        }



    }


}
