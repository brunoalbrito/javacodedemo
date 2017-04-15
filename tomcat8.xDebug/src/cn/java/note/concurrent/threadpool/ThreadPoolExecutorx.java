package cn.java.note.concurrent.threadpool;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadPoolExecutorx {
	private int startStopThreads = 1;
	protected ThreadPoolExecutor startStopExecutor;
	
	/**
	 * 当前程序的名称
	 */
	protected String name = null;
	
	/**
	 * 初始化线程池执行器
	 */
	public void init() {
		BlockingQueue<Runnable> startStopQueue = new LinkedBlockingQueue<>();
		startStopExecutor = new ThreadPoolExecutor(
				getStartStopThreadsInternal(),
				getStartStopThreadsInternal(), 10, TimeUnit.SECONDS,
				startStopQueue,
				new StartStopThreadFactory(getName() + "-startStop-"));
		startStopExecutor.allowCoreThreadTimeOut(true);
	}
	
	
	public String getName() {
		return (name);
	}
	
	public int getStartStopThreads() {
		return startStopThreads;
	}
	
	/**
	 * Handles the special values.
	 */
	private int getStartStopThreadsInternal() {
		int result = getStartStopThreads();

		// Positive values are unchanged
		if (result > 0) {
			return result;
		}

		// Zero == Runtime.getRuntime().availableProcessors()
		// -ve  == Runtime.getRuntime().availableProcessors() + value
		// These two are the same
		result = Runtime.getRuntime().availableProcessors() + result;
		if (result < 1) {
			result = 1;
		}
		return result;
	}

	/**
	 * 线程工厂
	 * @author Administrator
	 *
	 */
	private static class StartStopThreadFactory implements ThreadFactory {
		private final ThreadGroup group;
		private final AtomicInteger threadNumber = new AtomicInteger(1);
		private final String namePrefix;

		public StartStopThreadFactory(String namePrefix) {
			SecurityManager s = System.getSecurityManager();
			group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
			this.namePrefix = namePrefix;
		}

		@Override
		public Thread newThread(Runnable r) {
			Thread thread = new Thread(group, r, namePrefix + threadNumber.getAndIncrement());
			thread.setDaemon(true);
			return thread;
		}
	}
}
