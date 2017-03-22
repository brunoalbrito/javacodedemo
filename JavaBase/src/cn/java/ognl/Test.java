package cn.java.ognl;

import java.util.ArrayList;
import java.util.HashMap;

public class Test {
	

	public static void main(String[] args) throws Exception {
		Object parameterObject = new HashMap();
		DynamicContext context = new DynamicContext(parameterObject);
		context.bind("strValue","key1Value");
		context.bind("listValue",new ArrayList(){
			{
				add("listItem_1");
				add("listItem_2");
				add("listItem_3");
			}
		});
		context.bind("mapValue",new HashMap(){
			{
				put("key1","key1_value");
				put("key2","key2_value");
				put("key3","key3_value");
			}
		});
		context.bind("objectValue",new Object(){
			private String property1 = "Property1Value";
			public String getProperty1(){
				return property1;
			}
			
			public String method1(){
				return "method1";
			}
		});
		
		System.out.println("-------- Normal -----------");
		System.out.println("strValue : " + OgnlCache.getValue("strValue", context.getBindings()));
		
		System.out.println("-------- List -----------");
		System.out.println("获取List指定key的值 : " + OgnlCache.getValue("listValue[0]", context.getBindings()));
		
		System.out.println("-------- Map -----------");
		System.out.println("获取Map指定key的值 : " + OgnlCache.getValue("mapValue['key1']", context.getBindings()));
		System.out.println("获取Map指定key的值 : " + OgnlCache.getValue("mapValue.key1", context.getBindings()));
		
		System.out.println("-------- Object -----------");
		System.out.println("调用对象get方法 : " + OgnlCache.getValue("objectValue['property1']", context.getBindings()));
		System.out.println("调用对象get方法 : " + OgnlCache.getValue("objectValue.property1", context.getBindings()));
		System.out.println("调用对象方法 : " + OgnlCache.getValue("objectValue.method1()", context.getBindings()));
	}

}
