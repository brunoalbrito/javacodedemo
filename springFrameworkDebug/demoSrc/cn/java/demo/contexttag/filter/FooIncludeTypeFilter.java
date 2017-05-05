package cn.java.demo.contexttag.filter;

import java.beans.Introspector;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopeMetadata;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.io.Resource;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

/**
 * 
 * @author zhouzhian
 *
 */
public class FooIncludeTypeFilter extends AbstractTypeHierarchyTraversingFilterMock {

	protected FooIncludeTypeFilter() {
		this(false,false);
	}
	protected FooIncludeTypeFilter(boolean considerInherited, boolean considerInterfaces) {
		super(considerInherited, considerInterfaces);
	}

	public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory)
			throws IOException {
		
		{
		// if(metadataReader instanceof org.springframework.core.type.classreading.SimpleMetadataReader){ // 类的元信息读取器
			ClassMetadata metadata = metadataReader.getClassMetadata();
			metadata.getClassName();
			if (metadata.hasSuperClass()) {
				metadata.getSuperClassName();
			}
			for (String ifc : metadata.getInterfaceNames()) {
				
			}
		// }
		}
		
		if(metadataReaderFactory instanceof CachingMetadataReaderFactory){
			
		}
		
		{
			AnnotationMetadata metadata = metadataReader.getAnnotationMetadata(); // 获取注解信息
			
			// org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider.isCandidateComponent(
			{
				Object beanClass = metadata.getClassName();
				if((metadata.isIndependent() && (metadata.isConcrete() ||
						(metadata.isAbstract() && metadata.hasAnnotatedMethods(Lookup.class.getName()))))){
					System.out.println("isCandidateComponent...");
				}
			}
			
			Resource resource = null; // cn/java/demo/contexttag/FooComponent
			ScannedGenericBeanDefinition definition = new ScannedGenericBeanDefinition(metadataReader);
			definition.setResource(resource);
			definition.setSource(resource);
			
			// org.springframework.context.annotation.AnnotationScopeMetadataResolver.resolveScopeMetadata(...)
			{
				
				ScopeMetadata scopeMetadata = new ScopeMetadata();
				if (definition instanceof AnnotatedBeanDefinition) {
					AnnotatedBeanDefinition annDef = (AnnotatedBeanDefinition) definition;
					AnnotationAttributes attributes = AnnotationAttributes.fromMap(metadata.getAnnotationAttributes(Scope.class.getName(), false));
					if (attributes != null) {
						scopeMetadata.setScopeName(attributes.getString("value"));
						ScopedProxyMode proxyMode = attributes.getEnum("proxyMode");
						if (proxyMode == null || proxyMode == ScopedProxyMode.DEFAULT) {
							proxyMode = ScopedProxyMode.NO;
						}
						scopeMetadata.setScopedProxyMode(proxyMode);
					}
				}
			}
			
			// org.springframework.context.annotation.AnnotationBeanNameGenerator.generateBeanName(...)
			{
				if (definition instanceof AnnotatedBeanDefinition) {
					String beanName = determineBeanNameFromAnnotation((AnnotatedBeanDefinition) definition);
					if (StringUtils.hasText(beanName)) {
						// Explicit bean name found.
						System.out.println(beanName);;
					}
				}
				else{
					System.out.println(buildDefaultBeanName(definition, null));
				}
			}
		}
		
		if(true){
			return true;
		}
		
		return super.match(metadataReader, metadataReaderFactory);
	}
	
	protected String buildDefaultBeanName(BeanDefinition definition, BeanDefinitionRegistry registry) {
		return buildDefaultBeanName(definition);
	}
	protected String buildDefaultBeanName(BeanDefinition definition) {
		String shortClassName = ClassUtils.getShortName(definition.getBeanClassName());
		return Introspector.decapitalize(shortClassName);
	}
	
	protected String determineBeanNameFromAnnotation(AnnotatedBeanDefinition annotatedDef) {
		AnnotationMetadata amd = annotatedDef.getMetadata();
		Set<String> types = amd.getAnnotationTypes();
		String beanName = null;
		for (String type : types) { // 迭代所有注解
			AnnotationAttributes attributes = AnnotationAttributes.fromMap(amd.getAnnotationAttributes(type, false)); // 注解的属性
			if (isStereotypeWithNameValue(type, amd.getMetaAnnotationTypes(type), attributes)) {
				Object value = attributes.get("value");
				if (value instanceof String) {
					String strVal = (String) value;
					if (StringUtils.hasLength(strVal)) {
						if (beanName != null && !strVal.equals(beanName)) {
							throw new IllegalStateException("Stereotype annotations suggest inconsistent " +
									"component names: '" + beanName + "' versus '" + strVal + "'");
						}
						beanName = strVal;
					}
				}
			}
		}
		return beanName;
	}
	
	private static final String COMPONENT_ANNOTATION_CLASSNAME = "org.springframework.stereotype.Component";
	protected boolean isStereotypeWithNameValue(String annotationType,
			Set<String> metaAnnotationTypes, Map<String, Object> attributes) {

		boolean isStereotype = annotationType.equals(COMPONENT_ANNOTATION_CLASSNAME) || // 该注解是@Component注解 
				(metaAnnotationTypes != null && metaAnnotationTypes.contains(COMPONENT_ANNOTATION_CLASSNAME)) || // 该注解包含@Component注解 
				annotationType.equals("javax.annotation.ManagedBean") ||
				annotationType.equals("javax.inject.Named");

		return (isStereotype && attributes != null && attributes.containsKey("value"));
	}
}
