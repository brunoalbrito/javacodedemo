package cn.java.note;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.catalina.LifecycleException;

public class ThreadPoolExecutorNote {

	private int startStopThreads = 1;
	protected ThreadPoolExecutor mThreadPoolExecutor;


	public static void main(String[] args) {
		new ThreadPoolExecutorNote().run1();
	}

	public void run1() {
		BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue();
		mThreadPoolExecutor = new ThreadPoolExecutor(startStopThreads, startStopThreads, 10, TimeUnit.SECONDS, workQueue, new StartStopThreadFactory("-startStop-"));
		mThreadPoolExecutor.allowCoreThreadTimeOut(true);
		
		// -----------------1
		List<Future<Void>> results = new ArrayList<>();
		results.add(mThreadPoolExecutor.submit(new StartChild("children_0"))); // 提交异步任务
		
		boolean fail = false;
		for (Future<Void> result : results) {
			try {
				result.get();
			} catch (Exception e) {
				fail = true;
			}
		}
		System.out.println(fail);
		
		// -----------------2
		ExecutorService mExecutorService = mThreadPoolExecutor;
		List<Future<?>> results2 = new ArrayList<>();
		results2.add(mExecutorService.submit(new DeployDescriptor("DeployDescriptor")));
		for (Future<?> result : results2) {
            try {
                result.get();
            } catch (Exception e) {
            	e.printStackTrace();
            }
        }
	}


	/**
	 * 实现接口 Callable
	 * @author Administrator
	 *
	 */
	private static class StartChild implements Callable<Void> {

		private String child;

		public StartChild(String child) {
			this.child = child;
		}

		@Override
		public Void call() throws LifecycleException {
			System.out.println("StartChild::call() : " + this.child);
			return null;
		}
	}

	/**
	 * 实现接口 Runnable
	 * @author Administrator
	 *
	 */
	private static class DeployDescriptor implements Runnable {

		private String config;

		public DeployDescriptor(String config) {
			this.config = config;
		}

		@Override
		public void run() {
			System.out.println("DeployDescriptor::run() : " + this.config);
		}
	}

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
