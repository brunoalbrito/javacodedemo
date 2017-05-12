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
import org.springframework.core.type.MethodMetadata;
import org.springframework.core.type.StandardAnnotationMetadata;
import org.springframework.core.type.classreading.AnnotationMetadataReadingVisitor;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

public class ClassMetadataTest {

	public static void testGetClassMetadataInfoByParseBytecode()  {
		System.out.println("\n----------testGetClassMetadataInfoByParseBytecode--------------");
		try {
			new ClassMetadataInfoByParseBytecode0().test();
			new ClassMetadataInfoByParseBytecode1().test();
		} catch (Exception e) {
		}
	}

	public static void testGetClassMetadataInfoByReflectClass() {
		System.out.println("\n----------testGetClassMetadataInfoByReflectClass--------------");
		new ClassMetadataInfoByReflectClass().test();
	}
	

	private static String getPackagePath() {
		return ClassMetadataTest.class.getPackage().getName().replace(".", File.separator);
	}

	/**
	 * 打印类的原信息
	 * @param classMetadata
	 */
	private static void printClassMetadata(ClassMetadata classMetadata){
		System.out.println("getClassName() : " + classMetadata.getClassName());
		if (classMetadata.hasSuperClass()) { // 类的超类
			classMetadata.getSuperClassName();
		}
		for (String ifc : classMetadata.getInterfaceNames()) { // 类的接口

		}
		classMetadata.isConcrete();
		
		String[] memberClassNames = classMetadata.getMemberClassNames(); // 内部类（成员类）
		for (String memberClassName : memberClassNames) {
			System.out.println("memberClassName : "+memberClassName);
		}
	}
	
	/**
	 * 打印类的注解的原信息
	 * @param annotationMetadata
	 */
	private static void printAnnotationMetadata(AnnotationMetadata annotationMetadata){
		Object beanClass = annotationMetadata.getClassName();
		System.out.println("beanClass : " + beanClass);
		if ((annotationMetadata.isIndependent()
				&& (annotationMetadata.isConcrete() || (annotationMetadata.isAbstract()
						&& annotationMetadata.hasAnnotatedMethods(Lookup.class.getName()))))) {
			System.out.println("isCandidateComponent...");
		}

		// 注解的属性
		//　获取Scope信息 , org.springframework.context.annotation.ScopeMetadataResolver.resolveScopeMetadata(...)
		{ 
			AnnotationAttributes attributes = AnnotationAttributes
					.fromMap(annotationMetadata.getAnnotationAttributes(Scope.class.getName(), false));
			if (attributes != null) {
				System.out.println("Scope ---> value = " + attributes.getString("value")+" , proxyMode = " + attributes.getEnum("proxyMode"));
			}
		}

		// 注解的属性
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

		// 类级别的注解
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
		
		// 方法级别的注解 - 有方法声明注解 @Bean
		{
			annotationMetadata.hasAnnotatedMethods(Bean.class.getName());
			Set<MethodMetadata> beanMethods = annotationMetadata.getAnnotatedMethods(Bean.class.getName());
			if (beanMethods.size() > 1){
				for (MethodMetadata beanMethod : beanMethods) {
					System.out.println("beanMethod.getMethodName() "+beanMethod.getMethodName());
				}
			}
		}
	}


	/**
	 * 反射类的注解信息 - 要走加载类、解析类、反射类的过程
	 * @author zhouzhian
	 */
	private static class ClassMetadataInfoByReflectClass {

		public void test() {
			AnnotationMetadata annotationMetadata = new StandardAnnotationMetadata(HelloService.class, true); //!!!
			if (isFullConfigurationCandidate(annotationMetadata)) {

			}
			else if (isLiteConfigurationCandidate(annotationMetadata)) {

			}
			Map<String, Object> orderAttributes = annotationMetadata.getAnnotationAttributes(Order.class.getName());
			printAnnotationMetadata(annotationMetadata);
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
	 * 读取 *.class文件 - 直接解析bytecode，减少了加载类、解析类、反射类的过程
	 * @author zhouzhian
	 */
	private static class ClassMetadataInfoByParseBytecode0 {
		public void test() throws IOException {
			String className = HelloService.class.getName();
			MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory();
			MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(className);
			
			System.out.println("------------获取注解信息-------------");
			{

				AnnotationMetadata annotationMetadata = metadataReader.getAnnotationMetadata();
				printAnnotationMetadata(annotationMetadata);
			}
			
			System.out.println("------------获取类信息-------------");
			{
				ClassMetadata classMetadata = metadataReader.getClassMetadata();
				printClassMetadata(classMetadata);
			}
		}
	}

	/**
	 * 读取 *.class文件 - 直接解析bytecode，减少了加载类、解析类、反射类的过程
	 */
	private static class ClassMetadataInfoByParseBytecode1 {

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
				printAnnotationMetadata(annotationMetadata);

			}

			System.out.println("------------获取类信息-------------");
			{
				ClassMetadata classMetadata = visitor;
				printClassMetadata(classMetadata);
			}
		}
	}
	
	private static final String COMPONENT_ANNOTATION_CLASSNAME = "org.springframework.stereotype.Component";
	protected static boolean isStereotypeWithNameValue(String annotationType,
			Set<String> metaAnnotationTypes, Map<String, Object> attributes) {
		boolean isStereotype = annotationType.equals(COMPONENT_ANNOTATION_CLASSNAME) ||
				(metaAnnotationTypes != null && metaAnnotationTypes.contains(COMPONENT_ANNOTATION_CLASSNAME)) ||
				annotationType.equals("javax.annotation.ManagedBean") ||
				annotationType.equals("javax.inject.Named");
		
		return (isStereotype && attributes != null && attributes.containsKey("value"));
	}


}
