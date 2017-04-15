package cn.java.note.concurrent.countdownLatch;

import java.util.concurrent.CountDownLatch;



public class Test {

	public static void main() {
		int pollerThreadCount = Math.min(2,Runtime.getRuntime().availableProcessors());
		
		CountDownLatch stopLatch = null;
		stopLatch = new CountDownLatch(pollerThreadCount);
	}

}
