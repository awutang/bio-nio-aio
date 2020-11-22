package com.concurrency.disruptor;

import java.util.concurrent.CountDownLatch;

/**
 * @fileName: VolatileDemo.java
 * @author: laoMa
 * @date: 2020/11/2 11:35
 */
public class CachelinePaddingDemo2 {

	static long COUNT = 1000000000;

	private static int number;

	static CountDownLatch countDownLatch = new CountDownLatch(2);

	public static class T {
		//加volatile，执行时间20秒以上
		// public volatile long x = 0L;
		
		//不加volatile，同样的执行结果，执行时间差不多2秒以上
		public long x = 0L;
	}

	public static T[] arr = new T[2];

	static {
		arr[0] = new T();
		arr[1] = new T();
	}

	public static void main(String[] args) throws Exception {

		final long start = System.nanoTime();
		
		Thread thread1 = new Thread(()->{
			for (int i = 0; i < COUNT; i++) {
				arr[0].x = i;
			}
			//countDownLatch.countDown();
		});

		/*Thread thread2 = new Thread(()->{
			for (int i = 0; i < COUNT; i++) {
				arr[1].x = i;
			}
			//countDownLatch.countDown();
		});*/

		thread1.start();
		// thread2.start();

		//countDownLatch.await();
		while (arr[0].x != COUNT-1) {
//			Thread.sleep(10000);
//			arr[0].x = 55;
		}
		
		System.out.println((System.nanoTime() - start)/100_0000);
	}
}
