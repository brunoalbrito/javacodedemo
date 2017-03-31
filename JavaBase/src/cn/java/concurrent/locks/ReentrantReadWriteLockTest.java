package cn.java.concurrent.locks;

import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

public class ReentrantReadWriteLockTest {

	public static void main(String[] args) {
		ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();

		Thread thread1 = new Thread(new WriteRunnable("runnableName1", reentrantReadWriteLock));
		thread1.start();

		Thread thread2 = new Thread(new ReadRunnable("runnableName2", reentrantReadWriteLock));
		thread2.start();
	}

	private static class ReadRunnable implements Runnable {
		private String runnableName;
		private ReentrantReadWriteLock reentrantReadWriteLock;

		public ReadRunnable(String runnableName, ReentrantReadWriteLock reentrantReadWriteLock) {
			this.runnableName = runnableName;
			this.reentrantReadWriteLock = reentrantReadWriteLock;
		}

		public void run() {
			ReadLock readLock = this.reentrantReadWriteLock.readLock();
			try {
				readLock.lock();
			} catch (Exception e) {
			} finally {
				readLock.unlock();
			}
		}
	}

	private static class WriteRunnable implements Runnable {
		private String runnableName;
		private ReentrantReadWriteLock reentrantReadWriteLock;

		public WriteRunnable(String runnableName, ReentrantReadWriteLock reentrantReadWriteLock) {
			this.runnableName = runnableName;
			this.reentrantReadWriteLock = reentrantReadWriteLock;
		}

		public void run() {
			WriteLock writeLock = this.reentrantReadWriteLock.writeLock();
			try {
				writeLock.lock();
			} catch (Exception e) {
			} finally {
				writeLock.unlock();
			}
		}
	}

}
