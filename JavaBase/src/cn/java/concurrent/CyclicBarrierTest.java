package cn.java.concurrent;

import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 模型： 一个等多个
 * @author zhouzhian
 *
 */
public class CyclicBarrierTest {

	public static void main(String[] args) {
		Integer threadCount = 3;
		ArrayBlockingQueue arrayBlockingQueue = new ArrayBlockingQueue(threadCount);
		// 等待者
		Runnable barrierAction = new Runnable() {
			@Override
			public void run() {
				StringBuilder stringBuilder = new StringBuilder();
				for (Iterator iterator = arrayBlockingQueue.iterator(); iterator.hasNext();) {
					stringBuilder.append((String) iterator.next());
					stringBuilder.append(";");
				}
				System.out.println(stringBuilder.toString()); // runnableName1;runnableName3;runnableName2;
			}
		};
		
		// 等待所有线程都进入一直的状态，执行回调
		CyclicBarrier cyclicBarrier = new CyclicBarrier(threadCount,barrierAction);
		new Thread(new WorkRunnable("runnableName1", cyclicBarrier, arrayBlockingQueue)).start();
		new Thread(new WorkRunnable("runnableName2", cyclicBarrier, arrayBlockingQueue)).start();
		new Thread(new WorkRunnable("runnableName3", cyclicBarrier, arrayBlockingQueue)).start();
	}

	private static class WorkRunnable implements Runnable {
		private String runnableName;
		private CyclicBarrier cyclicBarrier;
		private ArrayBlockingQueue<String> arrayBlockingQueue;

		public WorkRunnable(String runnableName, CyclicBarrier cyclicBarrier, ArrayBlockingQueue arrayBlockingQueue) {
			this.runnableName = runnableName;
			this.cyclicBarrier = cyclicBarrier;
			this.arrayBlockingQueue = arrayBlockingQueue;
		}

		@Override
		public void run() {
			try {
				// 随机睡眠
				Thread.sleep(ThreadLocalRandom.current().nextInt(0, 10) * 1000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			this.arrayBlockingQueue.add(this.runnableName);
			try {
				this.cyclicBarrier.await();
			} catch (InterruptedException | BrokenBarrierException e) {
				e.printStackTrace();
			}
		}

	}
}
