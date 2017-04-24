package cn.java.demo.beantag.bean.methodreplacer;

import java.lang.reflect.Method;

import org.springframework.beans.factory.support.MethodReplacer;

import cn.java.demo.beantag.bean.lookupmethod.Property1;
import cn.java.demo.beantag.bean.lookupmethod.Property1Impl;

public class MethodReplacerImpl implements MethodReplacer{
	
	private Property1 property1;
	
	@Override
	public Object reimplement(Object obj, Method method, Object[] args) throws Throwable {
		if(method.getDeclaringClass() == Object.class){
			return method.invoke(obj, args);
		}
		else {
			System.out.println("-----------"+method.getName()+"------------");
			property1 = new Property1Impl((Integer)args[0],(String)args[1]+"(created in MethodReplacerImpl - 我是“劫持者”)");
			return true;
		}
	}

	public Property1 getProperty1() {
		return property1;
	}

}
