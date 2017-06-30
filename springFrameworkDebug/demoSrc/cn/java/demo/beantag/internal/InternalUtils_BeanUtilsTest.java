package cn.java.demo.beantag.internal;

import org.springframework.beans.BeanUtils;
import org.springframework.context.support.AbstractRefreshableConfigApplicationContext;
import org.springframework.util.ClassUtils;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

public class InternalUtils_BeanUtilsTest {

	public static void testIsPresent(AbstractRefreshableConfigApplicationContext context) {
		boolean jstlPresent = ClassUtils.isPresent(
				"javax.servlet.jsp.jstl.core.Config", InternalResourceViewResolver.class.getClassLoader());
		System.out.println("是否支持javax.servlet.jsp.jstl.core.Config："+jstlPresent);
	}
	
	public static void testInstantiateClass(AbstractRefreshableConfigApplicationContext context) {
		FooService fooService = BeanUtils.instantiateClass(cn.java.demo.beantag.internal.InternalUtils_BeanUtilsTest.FooService.class);
		System.out.println(fooService);
		
		Class<?> clazz = String.class;
		if(BeanUtils.isSimpleProperty(clazz)){
			System.out.println(clazz+" isSimpleProperty ");
		}

	}

	private static class FooService {
		private Integer beanId;
		private String beanName;


		public FooService() {
			super();
		}

		public FooService(Integer beanId, String beanName) {
			super();
			this.beanId = beanId;
			this.beanName = beanName;
		}

		@Override
		public String toString() {
			return "FooService [beanId=" + beanId + ", beanName=" + beanName + "]";
		}

	}
}
