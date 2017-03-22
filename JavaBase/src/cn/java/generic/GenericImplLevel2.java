package cn.java.generic;

public  class GenericImplLevel2<T0, T1, T2> extends GenericImplLevel1<T0, T1, T2> {

	@Override
	public void method1() {
		System.out.println("---method1---");
	}

	@Override
	public T0 method2() {
		System.out.println("---method2---");
		return null;
	}

	@Override
	public void method3(T0 param1) {
		System.out.println("---method3---"+param1);
	}

	@Override
	public T0 method4(T0 param1) {
		System.out.println("---method4---"+param1);
		return null;
	}


	@Override
	public void method7(String param1) {
		System.out.println("---method7---");
	}
	
	
	


}
