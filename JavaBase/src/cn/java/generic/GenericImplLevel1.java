package cn.java.generic;

import java.util.List;

public  class GenericImplLevel1<T0, T1, T2> implements GenericInterface1<T0, T1, T2> {

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

	@Override
	public <T3> void method8(T3 param1) {
		System.out.println("---method8---");
	}

	@Override
	public <T4> T4 method9(String param1) {
		System.out.println("---method9---");
		return (T4) param1;
	}

	@Override
	public <T5> T5 method10(T5 param1) {
		System.out.println("---method10---");
		return (T5) param1;
	}

	@Override
	public <T6 extends String, Integer> T6 method11(String param1) {
		System.out.println("---method11---");
		return (T6) param1;
	}

	@Override
	public void method12(Class<? extends String> param1) {
		System.out.println("---method12---");
	}

	@Override
	public Class<? extends String> method13(Integer param1) {
		System.out.println("---method13---");
		return String.class;
	}

	@Override
	public Class<? extends String> method14(Class<? extends String> param1) {
		System.out.println("---method14---");
		return String.class;
	}

	@Override
	public Class<? extends String>[] method15(String param1) {
		System.out.println("---method15---");
		return null;
	}




}
