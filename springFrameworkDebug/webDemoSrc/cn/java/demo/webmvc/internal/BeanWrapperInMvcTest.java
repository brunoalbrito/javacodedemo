package cn.java.demo.webmvc.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.ConfigurablePropertyAccessor;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.bind.ServletRequestParameterPropertyValues;
import org.springframework.web.context.request.ServletWebRequest;

public class BeanWrapperInMvcTest {

	/**
	 * 把MutablePropertyValues值注入到FooBean对象
	 */
	public static void testPropertyAccessorFactory2() {
		Object target = new FooBean();
		String objectName = "objectName0";
		// 要注入的属性值
		//	MutablePropertyValues mpvs = new ServletRequestParameterPropertyValues(request);
		MutablePropertyValues mpvs = new MutablePropertyValues();
		{
			mpvs.addPropertyValue("str", "strValue");
			
			List list = new ArrayList();
			list.add("list_item0");
			list.add("list_item1");
			mpvs.addPropertyValue("list", list);
			
			Map map = new HashMap<>();
			map.put("mapKey0", "mapKey0Value");
			map.put("mapKey1", "mapKey1Value");
			mpvs.addPropertyValue("map", map);
		}
		
		{
			final boolean autoGrowNestedPaths = true;
			final int autoGrowCollectionLimit = 256;
			
			// 反射注入属性值--方式1
			{
				String field = "str";
				BeanPropertyBindingResult beanPropertyBindingResult = new BeanPropertyBindingResult(target,objectName, autoGrowNestedPaths, autoGrowCollectionLimit);
				ConfigurablePropertyAccessor configurablePropertyAccessor = beanPropertyBindingResult.getPropertyAccessor(); 
				boolean ignoreUnknownFields = true;
				boolean ignoreInvalidFields = false;
				// 设置属性值
				configurablePropertyAccessor.setPropertyValues(mpvs, ignoreUnknownFields, ignoreInvalidFields);	
				// 获取字段值
				System.out.println(configurablePropertyAccessor.getPropertyValue(field));
				if(configurablePropertyAccessor.isWritableProperty(field)){
					
				}
			}
			
			// 反射注入属性值--方式2（方式1的代码展开）
			boolean useBeanWrapper2 = false;
			if(useBeanWrapper2){
				BeanWrapper beanWrapper =PropertyAccessorFactory.forBeanPropertyAccess(target);
				beanWrapper.setExtractOldValueForEditor(true);
				beanWrapper.setAutoGrowNestedPaths(autoGrowNestedPaths);
				beanWrapper.setAutoGrowCollectionLimit(autoGrowCollectionLimit);
				boolean ignoreUnknownFields = true;
				boolean ignoreInvalidFields = false;
				// 设置属性值
				beanWrapper.setPropertyValues(mpvs, ignoreUnknownFields, ignoreInvalidFields);	
				// 获取字段值
				System.out.println(beanWrapper.getPropertyValue("str"));
			}
		}
		
		// 输出对象
		System.out.println(target);
	}

	public static void testPropertyAccessorFactory1() {
		if (true)
			return;
		ServletWebRequestX servletWebRequestX = new ServletWebRequestX(null);
		BeanWrapper beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(servletWebRequestX);
		beanWrapper.setExtractOldValueForEditor(true);
		beanWrapper.setAutoGrowNestedPaths(true);
		beanWrapper.setAutoGrowCollectionLimit(256);

		{
			ServletRequest request = null;
			MutablePropertyValues mpvs = new ServletRequestParameterPropertyValues(request);

			boolean ignoreUnknownFields = true;
			boolean ignoreInvalidFields = false;

			// 把mpvs的属性值设置到beanWrapper
			beanWrapper.setPropertyValues(mpvs, ignoreUnknownFields, ignoreInvalidFields);
		}
	}

	private static class ServletWebRequestX extends ServletWebRequest {

		public ServletWebRequestX(HttpServletRequest request) {
			super(request);
		}
	}
	
	private static class FooBean{
		private String str;
		private List list;
		private Map map;
		public String getStr() {
			return str;
		}
		public void setStr(String str) {
			this.str = str;
		}
		public List getList() {
			return list;
		}
		public void setList(List list) {
			this.list = list;
		}
		public Map getMap() {
			return map;
		}
		public void setMap(Map map) {
			this.map = map;
		}
		@Override
		public String toString() {
			return "FooBean [str=" + str + ", list=" + list + ", map=" + map + "]";
		}
		
	}
	
}
