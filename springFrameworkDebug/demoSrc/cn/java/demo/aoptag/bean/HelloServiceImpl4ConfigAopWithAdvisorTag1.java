package cn.java.demo.aoptag.bean;

import cn.java.demo.aoptag.api.HelloService2;

public class HelloServiceImpl4ConfigAopWithAdvisorTag1 implements HelloService2 {

	@Override
	public void method1() {
		System.out.println(" --- invoke method ---> " + this.getClass().getSimpleName()+":method1()");
	}

	@Override
	public void method2() {
		System.out.println(" --- invoke method ---> " + this.getClass().getSimpleName()+":method2()");
		throw new RuntimeException("这是异常信息....");
	} 
	
	@Override
	public String method3() {
		return " --- invoke method ---> " + this.getClass().getSimpleName()+":method3()";
	} 
	
}  