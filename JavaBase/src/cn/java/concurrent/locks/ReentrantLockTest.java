package cn.java.concurrent.locks;

import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockTest {

	public static void main(String[] args) {
		ReentrantLock reentrantLock = new ReentrantLock();
		try {
			reentrantLock.lock();
		} catch (Exception e) {
		}
		finally {
			reentrantLock.unlock();
		}
	}

}
