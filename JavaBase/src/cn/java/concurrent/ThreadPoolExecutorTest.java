package cn.java.concurrent;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * ThreadPoolExecutor的机制是：
 * 		当线程数小于corePoolSize的数量时，每向ThreadPoolExecutor分配一个任务，那么ThreadPoolExecutor会回调threadFactory创建一个线程X，
 * 			线程X执行完毕自己的任务的时候，不是立刻结束，而是从ThreadPoolExecutor的workQueue队列循环获取任务，然后执行。
 * 		当线程数大于corePoolSize的数量时，会把任务放入workQueue，等待被其他线程拿去执行。
 * 好处是：
 * 		减少了创建线程的开销
 * @author Administrator
 *
 */
public class ThreadPoolExecutorTest {
	public static void main(String[] args) {
		int corePoolSize = 10;
        int maximumPoolSize = 20;
        long keepAliveTime = 10;
        TimeUnit unit = TimeUnit.SECONDS;
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue();
		ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize,maximumPoolSize,keepAliveTime,unit,workQueue);
		threadPoolExecutor.execute(new Runnable() {
			@Override
			public void run() {
				
			}
		});
	}
}
