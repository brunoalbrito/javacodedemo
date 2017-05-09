package cn.java.demo.contexttag.scanner;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationScopeMetadataResolver;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;
import org.springframework.context.annotation.ScopeMetadata;
import org.springframework.core.type.AnnotationMetadata;

public class FooScopeMetadataResolver extends AnnotationScopeMetadataResolver /*implements ScopeMetadataResolver*/ {
	@Override
	public ScopeMetadata resolveScopeMetadata(BeanDefinition definition) {
		
		if(definition instanceof ScannedGenericBeanDefinition){
			ScannedGenericBeanDefinition scannedGenericBeanDefinition = (ScannedGenericBeanDefinition)definition;
			AnnotationMetadata annotationMetadata = scannedGenericBeanDefinition.getMetadata(); // 类的元信息
			System.out.println("类名 : " + annotationMetadata.getClassName());
		}
		
		return super.resolveScopeMetadata(definition);
	}

}
