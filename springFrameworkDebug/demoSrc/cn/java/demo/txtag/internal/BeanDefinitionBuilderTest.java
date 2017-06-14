package cn.java.demo.txtag.internal;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.GenericBeanDefinition;

public class BeanDefinitionBuilderTest {

	public static void main(String[] args) {
		BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition();
		GenericBeanDefinition beanDefinition = (GenericBeanDefinition) builder.getRawBeanDefinition();
	}

}
