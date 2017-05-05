package cn.java.demo.contexttag.internal.asm;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.asm.ClassReader;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Role;
import org.springframework.context.annotation.Scope;
import org.springframework.core.NestedIOException;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.annotation.Order;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.StandardAnnotationMetadata;
import org.springframework.core.type.classreading.AnnotationMetadataReadingVisitor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

public class ClassMetadataTest {

	public static void testClassFileVisitor()  {
		try {
			new ClassFileVisitorTest().test();
		} catch (Exception e) {
		}
	}
	
	public static void testStandardAnnotationMetadata() {
		new StandardAnnotationMetadataTest().test();
	}
	
	/**
	 * StandardAnnotationMetadata
	 * @author zhouzhian
	 *
	 */
	private static class StandardAnnotationMetadataTest {
		
		public void test() {
			AnnotationMetadata metadata = new StandardAnnotationMetadata(HelloService.class, true); //!!!
			if (isFullConfigurationCandidate(metadata)) {
				
			}
			else if (isLiteConfigurationCandidate(metadata)) {
				
			}
			Map<String, Object> orderAttributes = metadata.getAnnotationAttributes(Order.class.getName());
		}
		
		private static final Set<String> candidateIndicators = new HashSet<String>(4);
		static {
			candidateIndicators.add(Component.class.getName());
			candidateIndicators.add(ComponentScan.class.getName());
			candidateIndicators.add(Import.class.getName());
			candidateIndicators.add(ImportResource.class.getName());
		}
		
		public static boolean isFullConfigurationCandidate(AnnotationMetadata metadata) {
			return metadata.isAnnotated(Configuration.class.getName());
		}
		
		public static boolean isLiteConfigurationCandidate(AnnotationMetadata metadata) {
			// Do not consider an interface or an annotation...
			if (metadata.isInterface()) {
				return false;
			}

			// Any of the typical annotations found?
			for (String indicator : candidateIndicators) {
				if (metadata.isAnnotated(indicator)) {
					return true;
				}
			}

			// Finally, let's look for @Bean methods...
			try {
				return metadata.hasAnnotatedMethods(Bean.class.getName());
			}
			catch (Throwable ex) {
				return false;
			}
		}
	}
	
	
	/**
	 * 解析*.class文件
	 * @author zhouzhian
	 *
	 */
	private static class ClassFileVisitorTest {
		private String getPackagePath() {
			return this.getClass().getPackage().getName().replace(".", File.separator);
		}

		public void test() throws IOException {
			// 读取类文件
			String resource = File.separator + getPackagePath() + File.separator + HelloService.class.getSimpleName()
					+ ".class";
			ClassLoader classLoader = this.getClass().getClassLoader();
			InputStream inputStream = classLoader.getResourceAsStream(resource);
			InputStream is = new BufferedInputStream(inputStream);
			ClassReader classReader;
			try {
				classReader = new ClassReader(is);
			} catch (IllegalArgumentException ex) {
				throw new NestedIOException(
						"ASM ClassReader failed to parse class file - "
								+ "probably due to a new Java class file version that isn't supported yet: " + resource,
						ex);
			} finally {
				is.close();
			}

			// 创建类读取器
			AnnotationMetadataReadingVisitor visitor = new AnnotationMetadataReadingVisitor(classLoader);
			classReader.accept(visitor, ClassReader.SKIP_DEBUG);

			System.out.println("------------获取注解信息-------------");
			{
				AnnotationMetadata annotationMetadata = visitor;
				Object beanClass = annotationMetadata.getClassName();
				System.out.println("beanClass : " + beanClass);
				if ((annotationMetadata.isIndependent()
						&& (annotationMetadata.isConcrete() || (annotationMetadata.isAbstract()
								&& annotationMetadata.hasAnnotatedMethods(Lookup.class.getName()))))) {
					System.out.println("isCandidateComponent...");
				}
				
				//　获取Scope信息 , org.springframework.context.annotation.ScopeMetadataResolver.resolveScopeMetadata(...)
				{ 
					AnnotationAttributes attributes = AnnotationAttributes
							.fromMap(annotationMetadata.getAnnotationAttributes(Scope.class.getName(), false));
					if (attributes != null) {
						System.out.println("Scope ---> value = " + attributes.getString("value")+" , proxyMode = " + attributes.getEnum("proxyMode"));
					}
				}
				
				// 获取 beanName , org.springframework.beans.factory.support.BeanNameGenerator.generateBeanName(...)
				{
					Set<String> types = annotationMetadata.getAnnotationTypes();
					String beanName = null;
					for (String type : types) {
						AnnotationAttributes attributes = AnnotationAttributes.fromMap(annotationMetadata.getAnnotationAttributes(type, false));
						if (isStereotypeWithNameValue(type, annotationMetadata.getMetaAnnotationTypes(type), attributes)) {
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
					System.out.println("beanName = "+beanName);
				}
				
				// org.springframework.context.annotation.AnnotationConfigUtils.processCommonDefinitionAnnotations(..)
				{
					if(annotationMetadata.isAnnotated(Lazy.class.getName())){
						
					}
					if (annotationMetadata.isAnnotated(Primary.class.getName())) {
						
					}
					if (annotationMetadata.isAnnotated(DependsOn.class.getName())) {
						
					}
					if (annotationMetadata.isAnnotated(Role.class.getName())) {
						
					}
					if (annotationMetadata.isAnnotated(Description.class.getName())) {
						
					}
				}
			}

			System.out.println("------------获取类信息-------------");
			{
				ClassMetadata classMetadata = visitor;
				System.out.println("getClassName() : " + classMetadata.getClassName());
				if (classMetadata.hasSuperClass()) {
					classMetadata.getSuperClassName();
				}
				for (String ifc : classMetadata.getInterfaceNames()) {

				}
				classMetadata.isConcrete();
			}

		}
		
		private static final String COMPONENT_ANNOTATION_CLASSNAME = "org.springframework.stereotype.Component";
		protected boolean isStereotypeWithNameValue(String annotationType,
				Set<String> metaAnnotationTypes, Map<String, Object> attributes) {
			boolean isStereotype = annotationType.equals(COMPONENT_ANNOTATION_CLASSNAME) ||
					(metaAnnotationTypes != null && metaAnnotationTypes.contains(COMPONENT_ANNOTATION_CLASSNAME)) ||
					annotationType.equals("javax.annotation.ManagedBean") ||
					annotationType.equals("javax.inject.Named");

			return (isStereotype && attributes != null && attributes.containsKey("value"));
		}
	}
	

}
