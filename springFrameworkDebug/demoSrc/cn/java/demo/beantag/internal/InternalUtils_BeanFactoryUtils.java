package cn.java.demo.beantag.internal;

import org.springframework.beans.factory.BeanFactoryUtils;

public class InternalUtils_BeanFactoryUtils {
	
	public static void main(String[] args) throws Exception {
		String beanName = "beanName0";
		String beanNameToRegister = BeanFactoryUtils.transformedBeanName(beanName);
		System.out.println(beanNameToRegister);
	}
}
