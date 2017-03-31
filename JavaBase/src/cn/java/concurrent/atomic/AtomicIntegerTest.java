package cn.java.concurrent.atomic;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicIntegerTest {
	
	public static void main(String[] args){
		AtomicInteger atomicInteger = new AtomicInteger(0);
		System.out.println(atomicInteger.get());
		Integer oldInteger = atomicInteger.getAndAdd(1);
		System.out.println(oldInteger);
		System.out.println(atomicInteger.get());
	}
}
