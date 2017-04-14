package cn.java.thread.data_access_controller;

/**
 * @author zhouzhian
 *
 */
public class 介绍 {
	public static void main(String[] args) {
		/*
		 	ThreadLocal：
		 		简介：采用了“以空间换时间”的方式，每个线程有一份副本。
		 			读：读到独立的副本的数据
		 			写：写到独立的副本的数据
		 		应用场景：适合在多读零写的多线程编程中
		 	Synchronized：
		 		简介：同步机制采用了“以时间换空间”的方式
		 			读：保证得到并发控制
		 			写：保证得到并发控制
		 		应用场景：适合在多读多写的多线程编程中
		 	Volatile ：
		 		简介：
		 			读：可以保证读取到最新数据
		 			写：不能得到并发控制
		 		应用场景：适合在多读一写的多线程编程中（读操作远远大于写操作）
		 */
	}
}
