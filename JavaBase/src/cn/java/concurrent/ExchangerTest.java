package cn.java.concurrent;

import java.util.concurrent.Exchanger;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 交换器
 * @author zhouzhian
 *
 */
public class ExchangerTest {

	public static void main(String[] args) {
		Exchanger exchanger = new Exchanger();
		new Thread(new MyRunnable("MyRunnable1",exchanger)).start();
		new Thread(new MyRunnable("MyRunnable2",exchanger)).start();
	}

	public static class MyRunnable implements Runnable{
		private String  runnableName = null;
		private Exchanger<String> exchanger;
		public MyRunnable(String runnableName,Exchanger  exchanger){
			this.runnableName = runnableName;
			this.exchanger = exchanger;
		}

		@Override
		public void run() {
			try {
				System.out.println("runnableName = " + runnableName);
				Thread.sleep(ThreadLocalRandom.current().nextInt(1, 3)*1000);
				System.out.println("runnableName = " + runnableName);
				String otherRunnableName = exchanger.exchange(this.runnableName);
				System.out.println("runnableName = " + runnableName + ",otherRunnableName = "+otherRunnableName);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}
