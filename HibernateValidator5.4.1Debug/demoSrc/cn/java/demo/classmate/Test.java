package cn.java.demo.classmate;

import java.lang.reflect.Type;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.TypeResolver;

public class Test {

	public static void main(String[] args) {
		Class<?> target = FooClass0.class; // 父类
		Type subType = FooClass1.class; // 子类
		
		{
			TypeResolver typeResolver = new TypeResolver();
			ResolvedType resolvedType = typeResolver.resolve(subType); // 解析子类
			Class<?> clazz = resolvedType.typeParametersFor(target).get(0).getErasedType(); // 获取实现泛型类“指定可变参数实际值”
			System.out.println(resolvedType);
			System.out.println("类的可变参数是：" + clazz.getName());
		}
	}

	

}
