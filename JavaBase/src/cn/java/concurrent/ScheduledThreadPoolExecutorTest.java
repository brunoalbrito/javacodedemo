package cn.java.concurrent;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ScheduledThreadPoolExecutorTest {

	public static void main(String[] args) {
		ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(10);
		Runnable callable = new Runnable() {
			@Override
			public void run() {
				System.out.println("--------Runnable----------");
			}
		};
        long delay = 2;
        TimeUnit unit = TimeUnit.SECONDS;
		scheduledThreadPoolExecutor.schedule(callable, delay, unit);
	}

}
