package cn.java.concurrent;

import java.util.concurrent.ThreadLocalRandom;

public class ThreadLocalRandomTest {

	public static void main(String[] args) {
		ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
		threadLocalRandom.nextInt();
		threadLocalRandom.nextLong();
	}

}
