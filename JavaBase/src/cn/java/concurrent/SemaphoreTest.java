package cn.java.concurrent;

import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;

/**
 *  Java 并发库 的Semaphore 可以很轻松完成信号量控制，Semaphore可以控制某个资源可被同时访问的个数，
 *  	通过 acquire() 获取一个许可，如果没有就等待，而 release() 释放一个许可。
 *  	比如在Windows下可以设置共享文件的最大客户端访问个数。
 *  单个信号量的Semaphore对象可以实现互斥锁的功能，并且可以是由一个线程获得了“锁”，
 *  	再由另一个线程释放“锁”，这可应用于死锁恢复的一些场合。
 * @author zhouzhian
 *
 */
public class SemaphoreTest {

	public static void main(String[] args) {
		releasAllSemaphore();
		nonReleasAllSemaphore();
	}

	public static void releasAllSemaphore() {
		Semaphore semaphore = new Semaphore(1); // 某个资源最多只能同时让1个用户访问
		new Thread(new RunnableImpl("RunnableImpl1",semaphore)).start();
		new Thread(new RunnableImpl("RunnableImpl2",semaphore)).start();
	}
	
	public static void nonReleasAllSemaphore() {
		Semaphore semaphore = new Semaphore(2); // 某个资源最多只能同时让2个用户访问
		new Thread(new RunnableImpl("RunnableImpl1",semaphore)).start();
		new Thread(new RunnableImpl("RunnableImpl2",semaphore)).start();
	}


	public static class RunnableImpl implements Runnable {
		private String runnableName;
		private Semaphore semaphore;

		public RunnableImpl(String runnableName,Semaphore semaphore) {
			this.runnableName = runnableName;
			this.semaphore = semaphore;
		}

		@Override
		public void run() {
			System.out.println("AwaitRunnableImpl : " + this.runnableName + ", before semaphore acquire");
			try {
				semaphore.acquire(); // 获取信号量
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			try {
				Thread.sleep(ThreadLocalRandom.current().nextInt(4, 5)*1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			semaphore.release(); // 释放信号量
			System.out.println("AwaitRunnableImpl : " + this.runnableName + ", after semaphore acquire");
		}
	}
}
