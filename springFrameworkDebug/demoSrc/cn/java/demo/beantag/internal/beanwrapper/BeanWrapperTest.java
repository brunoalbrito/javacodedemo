package cn.java.demo.beantag.internal.beanwrapper;

import java.beans.PropertyDescriptor;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.MethodParameter;
import org.springframework.core.PriorityOrdered;
import org.springframework.util.StringUtils;

import cn.java.demo.beantag.bean.autowirebytype.FooBeanFactory;

public class BeanWrapperTest {

	public static void main(String[] args) {
		Object beanInstance = new Bean1();
		// 包装实例
		BeanWrapper beanWrapper = new BeanWrapperImpl(beanInstance); 
		
		// 反省实例
		Set<String> result = new TreeSet<String>();
		PropertyDescriptor[] pds = beanWrapper.getPropertyDescriptors(); // 对象的属性
		for (PropertyDescriptor pd : pds) {
			if (pd.getWriteMethod() != null && !BeanUtils.isSimpleProperty(pd.getPropertyType())) { // 非普通类型的setter列表
				result.add(pd.getName());
			}
		}
		String[] results = StringUtils.toStringArray(result);
		for (int i = 0; i < results.length; i++) {
			System.out.println(results[i]);
		}
		
		// 某个属性的信息
		System.out.println("-----------------DependencyDescriptor-------------------");
		PropertyDescriptor pd = beanWrapper.getPropertyDescriptor("objectFactory");
		if (Object.class != pd.getPropertyType()) {
			MethodParameter methodParam = BeanUtils.getWriteMethodParameter(pd);
			boolean eager = !PriorityOrdered.class.isAssignableFrom(beanWrapper.getWrappedClass()); // 不继承自PriorityOrdered，就是饥渴模式
			DependencyDescriptor descriptor = new AutowireByTypeDependencyDescriptor(methodParam, eager);
			
			descriptor.initParameterNameDiscovery(new DefaultParameterNameDiscoverer());
			System.out.println("descriptor.getDependencyType() =  " + descriptor.getDependencyType());
			// descriptor.getDependencyType() =  interface org.springframework.beans.factory.ObjectFactory
			
			if (ObjectFactory.class == descriptor.getDependencyType() || // !!! 参数类型是 ObjectFactory
					ObjectProvider.class == descriptor.getDependencyType()) { // !!! 参数类型是 ObjectProvider
				DependencyDescriptor descriptorNest = new NestedDependencyDescriptor(descriptor);
				System.out.println("descriptorNest.getDependencyType() =  " + descriptorNest.getDependencyType());
				// descriptorNest.getDependencyType() =  class cn.java.demo.beantag.bean.autowirebytype.FooBeanFactory
//				descriptorNest.resolveCandidate("beanName1", descriptorNest.getDependencyType(), org.springframework.beans.factory.support.DefaultListableBeanFactory);
			}
		}
	}

	private static class Bean1{
		private int userid;
		private String username;
		private ObjectFactory objectFactory;
		public int getUserid() {
			return userid;
		}
		public void setUserid(int userid) {
			this.userid = userid;
		}
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		public ObjectFactory getObjectFactory() {
			return objectFactory;
		}
		public void setObjectFactory(ObjectFactory<FooBeanFactory> objectFactory) {
			this.objectFactory = objectFactory;
		}
	}
}
