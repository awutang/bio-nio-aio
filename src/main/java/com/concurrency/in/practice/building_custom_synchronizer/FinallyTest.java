/**
 * Author: Tang Yuqian
 * Date: 2020/10/15
 */
package com.concurrency.in.practice.building_custom_synchronizer;

public class FinallyTest {


    public static void main(String[] args) throws Exception {
        try {
//            System.out.println("try");
//            return; // 会执行到finally

            throw new Exception(); // 会执行到finally
        } catch (Exception e) {
            System.out.println("Exception");
        }finally {
            System.out.println("finally");
        }
    }
}
