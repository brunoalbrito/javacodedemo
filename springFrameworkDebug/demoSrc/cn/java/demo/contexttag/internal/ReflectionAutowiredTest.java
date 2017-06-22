package cn.java.demo.contexttag.internal;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

public class ReflectionAutowiredTest {

	public static void testReflectionAutowired() {
		new ReflectionAutowired().test();
	}

	public static class ReflectionAutowired {
		private final Set<Class<? extends Annotation>> autowiredAnnotationTypes =
				new LinkedHashSet<Class<? extends Annotation>>();
		private String requiredParameterName = "required";
		private boolean requiredParameterValue = true;
		
		public ReflectionAutowired() {
			this.autowiredAnnotationTypes.add(Autowired.class);
			this.autowiredAnnotationTypes.add(Value.class);
			try {
				this.autowiredAnnotationTypes.add((Class<? extends Annotation>)
						ClassUtils.forName("javax.inject.Inject", AutowiredAnnotationBeanPostProcessor.class.getClassLoader()));
			}
			catch (ClassNotFoundException ex) {
				// JSR-330 API not available - simply skip.
			}
		}

		/**
		 * 查找是否存在要自动装配的注解  @Autowired 、 @Value
		 * @param ao
		 * @return
		 */
		private AnnotationAttributes findAutowiredAnnotation(AccessibleObject ao) {
			if (ao.getAnnotations().length > 0) {
				for (Class<? extends Annotation> type : this.autowiredAnnotationTypes) {
					AnnotationAttributes attributes = AnnotatedElementUtils.getMergedAnnotationAttributes(ao, type);
					if (attributes != null) {
						return attributes;
					}
				}
			}
			return null;
		}

		/**
		 * 检查属性
		 * @param ann
		 * @return
		 */
		protected boolean determineRequiredStatus(AnnotationAttributes ann) {
			return (!ann.containsKey(this.requiredParameterName) ||
					this.requiredParameterValue == ann.getBoolean(this.requiredParameterName));
		}


		public void test() {
			Class<?> clazz = FooService.class;
			Class<?> targetClass = clazz;
			ReflectionUtils.doWithLocalFields(targetClass, new ReflectionUtils.FieldCallback() {
				@Override
				public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
					AnnotationAttributes ann = findAutowiredAnnotation(field); // 查找自动装配注解
					if (ann != null) {
						if (Modifier.isStatic(field.getModifiers())) {
							return;
						}
						boolean required = determineRequiredStatus(ann);
						System.out.println("field = "+field.getName()+" , required = "+ required);
					}
				}
			});

			ReflectionUtils.doWithLocalMethods(targetClass, new ReflectionUtils.MethodCallback() {
				@Override
				public void doWith(Method method) throws IllegalArgumentException, IllegalAccessException {
					Method bridgedMethod = BridgeMethodResolver.findBridgedMethod(method); 
					if (!BridgeMethodResolver.isVisibilityBridgeMethodPair(method, bridgedMethod)) {
						return;
					}
					AnnotationAttributes ann = findAutowiredAnnotation(bridgedMethod);  // 查找自动装配注解
					if (ann != null && method.equals(ClassUtils.getMostSpecificMethod(method, clazz))) {
						if (Modifier.isStatic(method.getModifiers())) {
							return;
						}
						if (method.getParameterTypes().length == 0) {
						}
						boolean required = determineRequiredStatus(ann);
						PropertyDescriptor pd = BeanUtils.findPropertyForMethod(bridgedMethod, clazz); // 获取方法对应的属性
						System.out.println("method = "+method.getName()+" , required = "+ required + " , pd = "+pd);
					}
				}
			});
		}
	}
	public class FooService {
		// --------定义字段 bof------------
		@Autowired(required=true)
		private Integer field0;
		// --------定义字段 eof------------
		
		
		// --------定义属性 bof------------
		@Autowired(required=true)
		public void setProperty1(String property1) {
		}
		
		@Autowired(required=true)
		public String getProperty0() {
			return "";
		}
		// --------定义属性 eof------------

		
		// --------非标准方法 bof------------
		@Autowired(required=true)
		public void method0() {
		}
		// --------非标准方法 3of------------

	}
}
