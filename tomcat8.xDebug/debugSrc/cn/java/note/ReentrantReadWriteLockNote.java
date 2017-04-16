package cn.java.note;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReentrantReadWriteLockNote {
	private final ReadWriteLock realmLock = new ReentrantReadWriteLock();

	public void test() {
		Lock l = realmLock.readLock();
		l.lock();
		try {
			System.out.println("...");
		} finally {
			l.unlock();
		}
	}

	public static void main(String[] args) {

	}

}
