package cn.java.note.concurrent.atomicinteger;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicIntegerTest {

	public static void main(String[] args) {
		AtomicInteger accessCount = new AtomicInteger();
		accessCount.set(0);
		accessCount.incrementAndGet();
	}

}
