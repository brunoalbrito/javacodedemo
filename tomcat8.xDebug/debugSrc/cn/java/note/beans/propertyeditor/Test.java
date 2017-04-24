package cn.java.note.beans.propertyeditor;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.net.URL;
import java.util.Hashtable;

public class Test {

	
	public static void main(String[] args) throws Exception {
//		testPropertyDescriptors();
		testPropertyEditor();
	}

	/**
	 * 属性描述
	 * @throws Exception
	 */
	public static void testPropertyDescriptors() throws Exception {
		Class  beanClass = cn.java.note.beans.propertyeditor.User.class; // 类
		BeanInfo beanInfo = Introspector.getBeanInfo(beanClass);
		PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors(); // 反射处理类的属性
		for (int i = 0; i < pds.length; i++) {
			
			System.out.println(pds[i].getName());
			System.out.println(pds[i].getPropertyEditorClass());
			
			if (pds[i].getWriteMethod() != null) { // 可以写
			}
			if (pds[i].getPropertyEditorClass() != null){ // 或者有属性编辑类
			}
		}
	}
	
	/**
	 * 自定义属性描述器
	 * @throws Exception
	 */
	public static void testPropertyEditor() throws Exception {
		
		Hashtable methodMaps = new Hashtable<>();
		Hashtable propertyEditorMaps = new Hashtable<>();

		Class  beanClass = cn.java.note.beans.propertyeditor.User.class; // 类
		
		// 设置BeanInfo的搜索路径，类以 BeanInfo结尾
		URL url = beanClass.getClassLoader().getResource("");
		String path = url.toString();
		Introspector.setBeanInfoSearchPath(new String[]{
				path
		});
		
		BeanInfo beanInfo = Introspector.getBeanInfo(beanClass);
		PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors(); // 反射处理类的属性
		for (int i = 0; i < pds.length; i++) {
			if (pds[i].getWriteMethod() != null) { // 可以写
				methodMaps.put(pds[i].getName(), pds[i].getWriteMethod());
			}
			System.out.println(pds[i].getName());
			System.out.println(pds[i].getPropertyEditorClass());
			if (pds[i].getPropertyEditorClass() != null){ // 或者有属性编辑类
				propertyEditorMaps.put(pds[i].getName(), pds[i].getPropertyEditorClass());
			}
		}
		
		User mUser = new User();
		mUser.setSex(0);
		System.out.println(mUser.getSex());
	}

}
