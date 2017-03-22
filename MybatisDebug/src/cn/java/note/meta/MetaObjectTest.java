package cn.java.note.meta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.property.PropertyTokenizer;
import org.apache.ibatis.session.Configuration;

public class MetaObjectTest {

	public static void main(String[] args) {
		test1();
		test2();
		test3();
	}

	public static void test1() {
		Object parameterObject = new Object() {
			private String property1 = "property1";

			public String getProperty1() {
				return property1;
			}
		};
		Configuration configuration = new Configuration();
		MetaObject metaObject = configuration.newMetaObject(parameterObject);
		System.out.println(metaObject.getValue("property1"));
	}

	public static void test2() {
		Map<String, Object> parameters = new HashMap();
		parameters.put("obj1", new Object() {
			private String property1 = "obj1.property1";

			public String getProperty1() {
				return property1;
			}
		});
		Configuration configuration = new Configuration();
		MetaObject metaObject = configuration.newMetaObject(parameters);
		System.out.println(metaObject.getValue("obj1.property1"));
	}

	public static void test3() {
		List list = new ArrayList();
		list.add("item1");
		list.add("item2");
		Configuration configuration = new Configuration();
		MetaObject metaObject = configuration.newMetaObject(list);
		PropertyTokenizer prop = new PropertyTokenizer("list[0]");
	}

}
