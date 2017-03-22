package cn.java.dubbo.note.proxy;

import java.lang.reflect.InvocationHandler;

// cn.java.pkg.proxy0

/**
 * 生成的代码
 * @author zhouzhian
 */
public class InstanceProxy0 {
	public static java.lang.reflect.Method[] methods; // 目标实体的实际方法
	private InvocationHandler handler;

	public InstanceProxy0 (InvocationHandler arg0)  {
		handler = arg0;
	}

	public InstanceProxy0()  {
	}

	/**
	 * 方法一
	 * @return
	 */
	public Byte method0(int arg0) throws Throwable{
		Object[] args = new Object[3];
		args[0] = (int)arg0;
		Object ret = handler.invoke(this, methods[0], args);
		return ret == null ? (byte)0 : ((Byte)ret).byteValue();
	}
	
	/**
	 * 方法二
	 * @return
	 */
	public Byte method1(int arg0,String arg1) throws Throwable{
		Object[] args = new Object[3];
		args[0] = (int)arg0;
		args[1] = (String)arg1;
		Object ret = handler.invoke(this, methods[1], args);
		return ret == null ? (byte)0 : ((Byte)ret).byteValue();
	}
}

