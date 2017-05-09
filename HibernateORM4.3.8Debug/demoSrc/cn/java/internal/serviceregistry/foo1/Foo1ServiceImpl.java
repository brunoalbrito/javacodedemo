package cn.java.internal.serviceregistry.foo1;

public class Foo1ServiceImpl implements Foo1Service {
	@Override
	public void method1() {
		System.out.println(this.getClass().getName() + ":method1()");
	}
}
