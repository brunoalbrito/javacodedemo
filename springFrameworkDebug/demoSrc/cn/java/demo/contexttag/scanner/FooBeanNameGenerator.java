package cn.java.demo.contexttag.scanner;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;
import org.springframework.core.type.AnnotationMetadata;

public class FooBeanNameGenerator extends AnnotationBeanNameGenerator /*implements BeanNameGenerator*/ {
	
	@Override
	public String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry) {
		if(definition instanceof ScannedGenericBeanDefinition){
			ScannedGenericBeanDefinition scannedGenericBeanDefinition = (ScannedGenericBeanDefinition)definition;
			AnnotationMetadata annotationMetadata = scannedGenericBeanDefinition.getMetadata(); // 类的元信息
			System.out.println("类名 : " + annotationMetadata.getClassName());
		}
		
		if(registry instanceof DefaultListableBeanFactory){
//			registry.registerBeanDefinition(beanName, beanDefinition);
//			((DefaultListableBeanFactory) registry).getBean(name)
		}
		
		return super.generateBeanName(definition, registry);
	}

}
