package cn.java.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ExecutorServiceTest {
	public static void main(String[] args) throws Exception  {
		ExecutorServiceTest.testNewWorkStealingPool();
		ExecutorServiceTest.testNewScheduledThreadPool();
		ExecutorServiceTest.testNewSingleThreadExecutor();
		ExecutorServiceTest.testNewFixedThreadPool();
		ExecutorServiceTest.testNewCachedThreadPool();
	}

	public static void testNewWorkStealingPool() throws Exception {
		System.out.println("----------newWorkStealingPool-----------------");
		ExecutorService executor = Executors.newWorkStealingPool(10);
		Future<Integer> result = executor.submit(new CallableImpl()); // 阻塞等待返回
		executor.shutdown();
		Thread.sleep(1000);
		System.out.println("主线程在执行任务");
		System.out.println("运行结果 ：" + result.get());
		System.out.println("所有任务执行完毕");
	}
	
	public static void testNewScheduledThreadPool() throws Exception {
		System.out.println("----------newScheduledThreadPool-----------------");
		ExecutorService executor = Executors.newScheduledThreadPool(10);
		Future<Integer> result = executor.submit(new CallableImpl()); // 阻塞等待返回
		executor.shutdown();
		Thread.sleep(1000);
		System.out.println("主线程在执行任务");
		System.out.println("运行结果 ：" + result.get());
		System.out.println("所有任务执行完毕");
	}
	
	public static void testNewSingleThreadExecutor() throws Exception {
		System.out.println("----------newSingleThreadExecutor-----------------");
		ExecutorService executor = Executors.newSingleThreadExecutor();
		Future<Integer> result = executor.submit(new CallableImpl()); // 阻塞等待返回
		executor.shutdown();
		Thread.sleep(1000);
		System.out.println("主线程在执行任务");
		System.out.println("运行结果 ：" + result.get());
		System.out.println("所有任务执行完毕");
	}
	
	public static void testNewFixedThreadPool() throws Exception {
		System.out.println("----------newFixedThreadPool-----------------");
		ExecutorService executor = Executors.newFixedThreadPool(10);
		Future<Integer> result = executor.submit(new CallableImpl()); // 阻塞等待返回
		executor.shutdown();
		Thread.sleep(1000);
		System.out.println("主线程在执行任务");
		System.out.println("运行结果 ：" + result.get());
		System.out.println("所有任务执行完毕");
	}
	
	public static void testNewCachedThreadPool() throws Exception {
		System.out.println("----------newCachedThreadPool-----------------");
		ExecutorService executor = Executors.newCachedThreadPool();
		Future<Integer> result = executor.submit(new CallableImpl()); // 阻塞等待返回
		executor.shutdown();
		Thread.sleep(1000);
		System.out.println("主线程在执行任务");
		System.out.println("运行结果 ：" + result.get());
		System.out.println("所有任务执行完毕");
	}

	private static class CallableImpl implements Callable<Integer> {
		@Override
		public Integer call() throws Exception {
			System.out.println("子线程在进行计算");
			Thread.sleep(3000);
			int sum = 0;
			for (int i = 0; i < 100; i++)
				sum += i;
			return sum;
		}
	}
}
