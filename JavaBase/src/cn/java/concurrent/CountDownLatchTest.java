package cn.java.concurrent;

import java.util.concurrent.CountDownLatch;

/**
 * 模型： 多个等一个
 * @author zhouzhian
 *
 */
public class CountDownLatchTest {

	public static void main(String[] args) {
		CountDownLatch countDownLatch = new CountDownLatch(2);
		new Thread(new WaitRunnable("WaitRunnable1",countDownLatch)).start();
		new Thread(new WaitRunnable("WaitRunnable2",countDownLatch)).start();
		
		new Thread(new ReleaseRunnable("ReleaseRunnable1",countDownLatch)).start();
		new Thread(new ReleaseRunnable("ReleaseRunnable2",countDownLatch)).start();
	}

	public static class ReleaseRunnable implements Runnable{
		private CountDownLatch  countDownLatch = null;
		private String  runnableName = null;
		public ReleaseRunnable(String runnableName,CountDownLatch  countDownLatch){
			this.runnableName = runnableName;
			this.countDownLatch = countDownLatch;
		}

		@Override
		public void run() {
			this.countDownLatch.countDown();
			System.out.println("runnableName : " + runnableName + ",do countDown");
		}
	}

	public static class WaitRunnable implements Runnable{
		private CountDownLatch  countDownLatch = null;
		private String  runnableName = null;
		public WaitRunnable(String runnableName,CountDownLatch  countDownLatch){
			this.runnableName = runnableName;
			this.countDownLatch = countDownLatch;
		}

		@Override
		public void run() {
			try {
				this.countDownLatch.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("runnableName : " + runnableName + ",do await");
		}
	}

}
