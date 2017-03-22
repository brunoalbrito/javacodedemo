package cn.java.generic;

public interface GenericInterface1<T0,T1, T2> {
	
	/*
	 	GenericArrayType  === Class <? extends String>[]
		ParameterizedType ===== Class <? extends String,Integer>
		TypeVariable ===== <T4> T4
	 */
	
	// -----------普通方法---------
	public void method1();
	public void method7(String param1);
	
	// ----------泛型类的方法---------- 要添加 T0,T1,T2 到类声明里面
	public T0 method2(); // 作为返回值,
	public void method3(T0 param1); // 作为参数
	public T0 method4(T0 param1); // 作为返回值+参数
	
	// ----------泛型方法---------- 不用添加T3到类声明里面 
	public <T3> void method8(T3 param1); // 作为参数  
	public <T4> T4 method9(String param1); // 作为返回值
	public <T5> T5 method10(T5 param1); // 作为返回值+参数
	public <T6 extends String,Integer> T6 method11(String param1); // 有界的类型参数
	
	// ----------类型通配符 ---------- 
	public void method12(Class <? extends String> param1); // 作为参数
	public Class <? extends String> method13(Integer param1);  // 作为返回值   
	public Class <? extends String> method14(Class <? extends String> param1); // 作为返回值+参数
	
	public Class <? extends String>[] method15(String param1); // 作为返回值+参数  
	

}
