package cn.java.note.instancemanager;

import java.util.HashMap;
import java.util.Map;

import javax.naming.Context;

/**
 * 实例管理器
 * 
 * 	
 * 
 * @author Administrator
 *
 */
//具备如下内容：
//	访问权限的控制（用于控制是否可以实例化）
public class Test {

	public static void main(String[] args) throws Exception {

	}
	
	public static void test2() throws Exception {
		Context context = null;
		cn.java.note.instancemanager.Context catalinaContext = new StandardContext();
		catalinaContext.setPostConstructMethods(new HashMap());
		catalinaContext.setPreDestroyMethods(new HashMap());
		
		InstanceManager instanceManager = new DefaultInstanceManager(context, new HashMap(), catalinaContext, Test.class.getClassLoader());
		instanceManager.newInstance("cn.java.HelloServlet");
	}
	
	public static void test1() throws Exception {
		
		Map<String, Map<String, String>> injectionMap = new HashMap();
		injectionMap.put("cn.java.note.instancemanager.HelloServlet", new HashMap(){ // 当实例化的类是cn.java.note.instancemanager.HelloServlet
			{
				put("fieldName1", "jndi/mybatis1"); // 字段fieldName1，注入 "jndi/mybatis1" 资源
				put("fieldName2", "jndi/mybatis2"); // 字段fieldName1，注入 "jndi/mybatis2" 资源
			}
		});

		Context context = null;
		cn.java.note.instancemanager.Context catalinaContext = new StandardContext();
		catalinaContext.setPostConstructMethods(new HashMap() {
			{
				// 当实例化cn.java.note.instancemanager.HelloServlet并调用构造函数后，调用postConstruct1方法
				put("cn.java.note.instancemanager.HelloServlet", "postConstruct1");
			}
		});
		catalinaContext.setPreDestroyMethods(new HashMap() {
			{
				// 当实例化cn.java.note.instancemanager.HelloServlet调用析构函数前，调用preDestroy1方法
				put("cn.java.note.instancemanager.HelloServlet", "preDestroy1");
			}
		});
		InstanceManager instanceManager = new DefaultInstanceManager(context, injectionMap, catalinaContext, Test.class.getClassLoader());
		instanceManager.newInstance("cn.java.HelloServlet");
	}

}
