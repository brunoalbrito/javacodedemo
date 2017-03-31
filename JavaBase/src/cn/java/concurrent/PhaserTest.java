package cn.java.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.Phaser;
import java.util.concurrent.ThreadLocalRandom;

public class PhaserTest {

	public static void main(String[] args) {
		PhaserTest.releasAllPhaser();
		PhaserTest.nonReleasAllPhaser();
	}

	public static void releasAllPhaser() {
		Phaser phaser = new Phaser(2); // 计数器设置成 2，由于两个都到达了，所以会顺利执行
		new Thread(new ArriveRunnableImpl("ArriveRunnableImpl1",phaser)).start();
		new Thread(new ArriveRunnableImpl("ArriveRunnableImpl2",phaser)).start();

		new Thread(new AwaitRunnableImpl("AwaitRunnableImpl1",phaser)).start();
		new Thread(new AwaitRunnableImpl("AwaitRunnableImpl2",phaser)).start();
	}
	
	public static void nonReleasAllPhaser() {
		Phaser phaser = new Phaser(3); // 计数器设置成 3，由于只到达了两个，所以会一直阻塞
		new Thread(new ArriveRunnableImpl("ArriveRunnableImpl1",phaser)).start();
		new Thread(new ArriveRunnableImpl("ArriveRunnableImpl2",phaser)).start();

		new Thread(new AwaitRunnableImpl("AwaitRunnableImpl1",phaser)).start();
		new Thread(new AwaitRunnableImpl("AwaitRunnableImpl2",phaser)).start();
	}

	public static class ArriveRunnableImpl implements Runnable {
		private String runnableName;
		private Phaser phaser;

		public ArriveRunnableImpl(String runnableName,Phaser phaser) {
			this.runnableName = runnableName;
			this.phaser = phaser;
		}

		@Override
		public void run() {
			System.out.println("ArriveRunnableImpl : " + this.runnableName + ", before arrive");
			try {
				Thread.sleep(ThreadLocalRandom.current().nextInt(4, 5)*1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			phaser.arrive(); // !!!
			System.out.println("ArriveRunnableImpl : " + this.runnableName + ", after arrive");
		}
	}

	public static class AwaitRunnableImpl implements Runnable {
		private String runnableName;
		private Phaser phaser;

		public AwaitRunnableImpl(String runnableName,Phaser phaser) {
			this.runnableName = runnableName;
			this.phaser = phaser;
		}

		@Override
		public void run() {
			System.out.println("AwaitRunnableImpl : " + this.runnableName + ", before arriveAndAwaitAdvance");
			phaser.awaitAdvance(phaser.getPhase()); // !!!
			System.out.println("AwaitRunnableImpl : " + this.runnableName + ", after arriveAndAwaitAdvance");
		}
	}
}
