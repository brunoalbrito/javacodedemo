package cn.java.demo.aoptag.bean;

import cn.java.demo.aoptag.api.HelloService;

public class HelloServiceImpl4ConfigAopWithAdvisorTag implements HelloService {

	@Override
	public void method1() {
		System.out.println(" --- invoke method ---> " + this.getClass().getSimpleName()+":method1()");
	}

	@Override
	public void method2() {
		System.out.println(" --- invoke method ---> " + this.getClass().getSimpleName()+":method2()");
		throw new RuntimeException("这是异常信息....");
	}  
	
}  