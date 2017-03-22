package cn.java.generic;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

public class Test {

	public static void main(String[] args) {
		test1();
		
		System.out.println();
		System.out.println("------------------------getName/getSimpleName/getCanonicalName----------------------------");
		System.out.println("--------getClass().getName() : "+String.class.getName()+"--------------");
		System.out.println("--------getClass().getSimpleName() : "+String.class.getSimpleName()+"--------------");
		System.out.println("--------getClass().getCanonicalName() : "+String.class.getCanonicalName()+"--------------");
		
		System.out.println();
		System.out.println("------------------------getName/getSimpleName/getCanonicalName----------------------------");
		System.out.println("--------getClass().getName() : "+String[].class.getName()+"--------------");
		System.out.println("--------getClass().getSimpleName() : "+String[].class.getSimpleName()+"--------------");
		System.out.println("--------getClass().getCanonicalName() : "+String[].class.getCanonicalName()+"--------------");

		System.out.println();
		System.out.println("------------------------GenericImplLevel1----------------------------");
		GenericImplLevel1 mGenericImpl1 = new GenericImplLevel1();
		Method[] methods1 = mGenericImpl1.getClass().getMethods();
		System.out.println("--------getClass().getName() : "+mGenericImpl1.getClass().getName()+"--------------");
		System.out.println("--------getClass().getSimpleName() : "+mGenericImpl1.getClass().getSimpleName()+"--------------");
		System.out.println("--------getClass().getCanonicalName() : "+mGenericImpl1.getClass().getCanonicalName()+"--------------");
		resolveMethod(methods1);

		System.out.println();
		System.out.println("------------------------GenericImplLevel2----------------------------");
		GenericImplLevel2 mGenericImpl2 = new GenericImplLevel2();
		
		Method[] methods2 = mGenericImpl2.getClass().getMethods();
		System.out.println("--------getClass().getName() : "+mGenericImpl1.getClass().getName()+"--------------");
		System.out.println("--------getClass().getSimpleName() : "+mGenericImpl1.getClass().getSimpleName()+"--------------");
		System.out.println("--------getClass().getCanonicalName() : "+mGenericImpl1.getClass().getCanonicalName()+"--------------");
		resolveMethod(methods2);
		
	}

	public static void resolveMethod(Method[] methods) {
		for(Method method: methods){
			/*
			 	GenericArrayType  === Class <? extends String>[]
				ParameterizedType ===== Class <? extends String,Integer>
				TypeVariable ===== <T4> T4
			 */
			Type returnType = method.getGenericReturnType();
			Class<?> declaringClass = method.getDeclaringClass(); // 最后实现在哪里
			System.out.println("------");
			System.out.println("methodName : " + method.getName());
			System.out.println("declaringClass : " + declaringClass);
			System.out.println("returnType : " + returnType);
			if (returnType instanceof TypeVariable) { // 各种类型变量的公共父接口
				System.out.println("returnType instanceof TypeVariable");
				TypeVariable<?> componentType = (TypeVariable<?>) returnType;
				System.out.println("\t"+componentType);
		    } else if (returnType instanceof ParameterizedType) { // 一种参数化的类型，比如Collection,无论<>中有几层<>嵌套，这个方法仅仅脱去最外层的<>之后剩下的内容就作为这个方法的返回值。 
		    	System.out.println("returnType instanceof ParameterizedType");
		    	ParameterizedType parameterizedType = (ParameterizedType) returnType;
		    	Type[] typeArgs = parameterizedType.getActualTypeArguments();
		    	System.out.println("getRawType : " + (Class<?>) parameterizedType.getRawType());
		    	for (int i = 0; i < typeArgs.length; i++) {
		    		System.out.println("\t"+typeArgs[i]);
		    	}
		    } else if (returnType instanceof GenericArrayType) { // 一种元素类型是参数化类型或者类型变量的数组类型,无论从左向右有几个[]并列，这个方法仅仅脱去最右边的[]之后剩下的内容就作为这个方法的返回值。
		    	System.out.println("returnType instanceof GenericArrayType");
		    	GenericArrayType genericArrayType = (GenericArrayType) returnType;
		    	Type componentType = genericArrayType.getGenericComponentType();
		    	System.out.println("\t"+componentType);
		    } else if (returnType instanceof WildcardType) { // 代表一种通配符类型表达式
		    	System.out.println("returnType instanceof WildcardType");
		    } else {
		    	
		    }
			
		}
	}

	public static void test1() {
		GenericImplLevel1 genericImpl1 = new GenericImplLevel1();
		genericImpl1.method1();
		genericImpl1.method2();
		genericImpl1.method3("method3_param1");
		genericImpl1.method4("method4_param1");
		genericImpl1.method7("method7_param1");
		genericImpl1.method8("method8_param1");
		genericImpl1.method9("method9_param1");
		genericImpl1.method10("method10_param1");
		genericImpl1.method11("method11_param1");
		genericImpl1.method12(String.class);
		genericImpl1.method13(10);
		genericImpl1.method14(String.class);
	}

}
