package cn.java.internal.packagetest;

import java.lang.annotation.Annotation;

import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.common.reflection.MetadataProvider;
import org.hibernate.annotations.common.reflection.MetadataProviderInjector;
import org.hibernate.annotations.common.reflection.ReflectionManager;
import org.hibernate.annotations.common.reflection.XAnnotatedElement;
import org.hibernate.annotations.common.reflection.XPackage;
import org.hibernate.annotations.common.reflection.java.JavaReflectionManager;
import org.hibernate.cfg.annotations.reflection.JPAMetadataProvider;

public class Test {
	
	public static void main(String[] args) throws Exception {
		String packageName = Test.class.getPackage().getName(); // 包名
		
		{ // 方式一
			
			MetadataProvider metadataProvider = new JPAMetadataProvider();
			ReflectionManager reflectionManager = new JavaReflectionManager();
			( ( MetadataProviderInjector ) reflectionManager ).setMetadataProvider( metadataProvider );
			XPackage pckg = reflectionManager.packageForName( packageName ); // pckg == org.hibernate.annotations.common.reflection.java.JavaXPackage
			
			{
				System.out.println(pckg.getName()); // 包名
			}
			
			{ // 包含 @SequenceGenerator 注解
				if ( pckg.isAnnotationPresent( SequenceGenerator.class ) ) {
					SequenceGenerator ann = pckg.getAnnotation( SequenceGenerator.class );
				}
			}
			
			
			{
				System.out.println("---------反射出所有注解----------");
				Annotation[] annotations = pckg.getAnnotations();
				for ( Annotation annotation : annotations ) {
					System.out.println(annotation.annotationType());
				}
			}
			
			{
				XAnnotatedElement annotatedElement = pckg;
			}
			
			
		}
		
		/*
		{  // 方式二
			MetadataProvider metadataProvider = new JPAMetadataProvider();
			ReflectionManager reflectionManager = new JavaReflectionManager();
			( ( MetadataProviderInjector ) reflectionManager ).setMetadataProvider( metadataProvider );
			ClassLoaderDelegate classLoaderDelegate = StandardClassLoaderDelegateImpl.INSTANCE;
			Package pkg = classLoaderDelegate.classForName( packageName + ".package-info" ).getPackage();
			JavaXPackage xPackage = new JavaXPackage( pkg, reflectionManager ); // org.hibernate.annotations.common.reflection.java.JavaXPackage
		}
		*/
	}



}
