package cn.java.dubbo.note.proxy;

import java.lang.reflect.InvocationHandler;

import com.alibaba.dubbo.common.bytecode.Proxy;

/**
 * 生成的代码
 * @author zhouzhian
 */
public class Proxy0 extends Proxy{

	public Proxy0(){
		
	}
	
	@Override
	public Object newInstance(InvocationHandler handler) {
		return new InstanceProxy0(handler);
	}

}
