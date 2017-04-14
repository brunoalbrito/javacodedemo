package cn.java.test.aop.beans;

import cn.java.test.aop.api.HelloService;

public class HelloServiceImpl2 implements HelloService {

	@Override
	public void method1() {
		System.out.println("HelloServiceImpl1::method1()");
	}

	@Override
	public void method2() {
		System.out.println("HelloServiceImpl1::method2()");
	}  
}  