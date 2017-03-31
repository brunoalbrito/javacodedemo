package cn.java.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * @author zhouzhian
 */
public class FutureTaskAndCallableTest {

	public static void main(String[] args) {
		blockWithFuture();
		unBlockWithFutureTask();
	}

	/**
	 * 取得线程的返回值，立即阻塞等待返回
	 * 
	 * @param args
	 */
	public static void blockWithFuture() {
		System.out.println("---------blockWithFuture--------------");
		ExecutorService executor = Executors.newCachedThreadPool();
		Future<Integer> result = executor.submit(new CallableImpl()); // 阻塞等待返回
		executor.shutdown();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		System.out.println("主线程在执行任务.blockWithFuture");

		try {
			System.out.println("blockWithFuture 运行结果 ：" + result.get());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

		System.out.println("所有任务执行完毕.blockWithFuture");
	}

	/**
	 * 取得线程的返回值，不立即阻塞等待返回
	 * 
	 * @param args
	 */
	public static void unBlockWithFutureTask() {
		System.out.println("---------unBlockWithFutureTask--------------");
		// 第一种方式
		ExecutorService executor = Executors.newCachedThreadPool();
		FutureTask<Integer> futureTask = new FutureTask<Integer>(new CallableImpl());
		executor.submit(futureTask); // 不阻塞
		executor.shutdown();

		// 第二种方式，注意这种方式和第一种方式效果是类似的，只不过一个使用的是ExecutorService，一个使用的是Thread
		// FutureTask<Integer> futureTask = new FutureTask<Integer>(new
		// CallableClazz());
		// Thread thread = new Thread(futureTask);
		// thread.start();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		System.out.println("主线程在执行任务.unBlockWithFutureTask");

		try {
			System.out.println("unBlockWithFutureTask 运行结果 ：" + futureTask.get());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

		System.out.println("所有任务执行完毕.unBlockWithFutureTask");
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
