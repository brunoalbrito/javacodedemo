package cn.java.demo.contexttag.internal.enhancer;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.springframework.asm.Type;
import org.springframework.cglib.core.ClassGenerator;
import org.springframework.cglib.core.Constants;
import org.springframework.cglib.core.DefaultGeneratorStrategy;
import org.springframework.cglib.core.SpringNamingPolicy;
import org.springframework.cglib.proxy.Callback;
import org.springframework.cglib.proxy.CallbackFilter;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.cglib.proxy.NoOp;
import org.springframework.cglib.transform.ClassEmitterTransformer;
import org.springframework.cglib.transform.TransformingClassGenerator;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

/**
 * 替父类实现BeanFactoryAware接口，并在新生成的类中添加一个字段 $$beanFactory
 * 
 * @author zhouzhian
 *
 */
public class FooServiceMustNeedBeanFactoryEnhancer {
	
	private static final Callback[] CALLBACKS = new Callback[] {
			new SetBeanFactoryMethodInterceptor(), // 拦截器1
			new GetBeanMethodInterceptor(), // 拦截器2
			NoOp.INSTANCE  // 无需被拦截
	};
	private static final String BEAN_FACTORY_FIELD = "$$beanFactory";
	private static final ConditionalCallbackFilter CALLBACK_FILTER = new ConditionalCallbackFilter(CALLBACKS);
	
	/**
	 * 创建类
	 * @param configClass
	 * @param classLoader
	 * @return
	 */
	public Class<?> enhance(Class<?> configClass, ClassLoader classLoader) {
		if (EnhancedBeanFactoryAware.class.isAssignableFrom(configClass)) { // 已经是生成类，无需再生成
			return configClass;
		}
		Class<?> enhancedClass = createClass(newEnhancer(configClass, classLoader));
		return enhancedClass;
	}
	
	/**
	 * 创建类
	 * @param enhancer
	 * @return
	 */
	private Class<?> createClass(Enhancer enhancer) {
		Class<?> subclass = enhancer.createClass(); // 生成类
		// Registering callbacks statically (as opposed to thread-local)
		// is critical for usage in an OSGi environment (SPR-5932)...
		Enhancer.registerStaticCallbacks(subclass, CALLBACKS);
		return subclass;
	}
	
	/**
	 * Creates a new CGLIB {@link Enhancer} instance.
	 */
	private Enhancer newEnhancer(Class<?> superclass, ClassLoader classLoader) { // 生成类
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(superclass);
		enhancer.setInterfaces(new Class<?>[] {EnhancedBeanFactoryAware.class}); // 扩展接口
		enhancer.setUseFactory(false);
		enhancer.setNamingPolicy(SpringNamingPolicy.INSTANCE);
		enhancer.setStrategy(new BeanFactoryAwareGeneratorStrategy(classLoader)); // 会生成一个字段
		enhancer.setCallbackFilter(CALLBACK_FILTER);
		enhancer.setCallbackTypes(CALLBACK_FILTER.getCallbackTypes());
		return enhancer;
	}
	

	
	/**
	 * setter方法
	 * @author zhouzhian
	 */
	private static class SetBeanFactoryMethodInterceptor implements MethodInterceptor, ConditionalCallback {
		
		/**
		 * 是否匹配要拦截的方法
		 */
		@Override
		public boolean isMatch(Method candidateMethod) {
			return (candidateMethod.getName().equals("setBeanFactory") &&
					candidateMethod.getParameterTypes().length == 1 &&
					BeanFactory.class == candidateMethod.getParameterTypes()[0] &&
					BeanFactoryAware.class.isAssignableFrom(candidateMethod.getDeclaringClass())); 
		}

		/**
		 * 拦截处理
		 */
		@Override
		public Object intercept(Object obj, Method method, Object[] args,
				MethodProxy proxy) throws Throwable {
			Field field = obj.getClass().getDeclaredField(BEAN_FACTORY_FIELD);
			Assert.state(field != null, "Unable to find generated Foo field");
			field.set(obj, args[0]); // 设置字段值
			if (BeanFactoryAware.class.isAssignableFrom(obj.getClass().getSuperclass())) { // 如果"原来的类"有实现BeanFactoryAware接口，那么继续调用父类的方法
				System.out.println("--------调用父类实现的方法-----------");
				Object result = proxy.invokeSuper(obj, args);
				return result;
			}
			return null;
		}
	}
	
	
	/**
	 * getter方法
	 * @author zhouzhian
	 *
	 */
	private static class GetBeanMethodInterceptor implements MethodInterceptor, ConditionalCallback {

		/**
		 * 是否匹配要拦截的方法
		 */
		@Override
		public boolean isMatch(Method candidateMethod) {
			if("getBean0".equals(candidateMethod.getName()) || "getBean1".equals(candidateMethod.getName())){
				return true;
			}
			return false;
		}
		
		/**
		 * 拦截处理
		 */
		@Override
		public Object intercept(Object object, Method method, Object[] args,
				MethodProxy proxy) throws Throwable {
			BeanFactory beanFactory = getBeanFactory(object);
			
			String methodName = method.getName();
			if("getBean0".equals(methodName)){
				System.out.println("print in “ " + beanFactory.getBeanFactoryName()+" ”");
				return "getBean0 in GetBeanMethodInterceptor...";
//				return proxy.invokeSuper(object, args); // 调用父类的
			}
			else{
				Object result =  "getBean1 in GetBeanMethodInterceptor...";// proxy.invokeSuper(object, args); // 调用父类的
				return result;
			}
		}
		
		private BeanFactory getBeanFactory(Object object) {
			Field field = ReflectionUtils.findField(object.getClass(), BEAN_FACTORY_FIELD);
			Assert.state(field != null, "Unable to find generated bean factory field");
			Object beanFactory = ReflectionUtils.getField(field, object); // 从本类的字段中获取
			Assert.state(beanFactory != null, "BeanFactory has not been injected into class");
			return (BeanFactory) beanFactory;
		}

	}
	
	
	
	public interface EnhancedBeanFactoryAware extends BeanFactoryAware {
	}
	
	/**
	 * Custom extension of CGLIB's DefaultGeneratorStrategy, introducing a {@link BeanFactory} field.
	 * Also exposes the application ClassLoader as thread context ClassLoader for the time of
	 * class generation (in order for ASM to pick it up when doing common superclass resolution).
	 */
	private static class BeanFactoryAwareGeneratorStrategy extends DefaultGeneratorStrategy {

		private final ClassLoader classLoader;

		public BeanFactoryAwareGeneratorStrategy(ClassLoader classLoader) {
			this.classLoader = classLoader;
		}

		@Override
		protected ClassGenerator transform(ClassGenerator cg) throws Exception {
			ClassEmitterTransformer transformer = new ClassEmitterTransformer() {
				@Override
				public void end_class() {
					declare_field(Constants.ACC_PUBLIC, BEAN_FACTORY_FIELD, Type.getType(BeanFactory.class), null); // 声明一个字段
					super.end_class();
				}
			};
			return new TransformingClassGenerator(cg, transformer);
		}

		@Override
		public byte[] generate(ClassGenerator cg) throws Exception {
			if (this.classLoader == null) {
				return super.generate(cg);
			}

			Thread currentThread = Thread.currentThread();
			ClassLoader threadContextClassLoader;
			try {
				threadContextClassLoader = currentThread.getContextClassLoader();
			}
			catch (Throwable ex) {
				// Cannot access thread context ClassLoader - falling back...
				return super.generate(cg);
			}

			boolean overrideClassLoader = !this.classLoader.equals(threadContextClassLoader);
			if (overrideClassLoader) {
				currentThread.setContextClassLoader(this.classLoader);
			}
			try {
				return super.generate(cg);
			}
			finally {
				if (overrideClassLoader) {
					// Reset original thread context ClassLoader.
					currentThread.setContextClassLoader(threadContextClassLoader);
				}
			}
		}
	}
	
	/**
	 * Conditional {@link Callback}.
	 * @see ConditionalCallbackFilter
	 */
	private interface ConditionalCallback extends Callback {

		boolean isMatch(Method candidateMethod);
	}

	/**
	 */
	private static class ConditionalCallbackFilter implements CallbackFilter {

		private final Callback[] callbacks;

		private final Class<?>[] callbackTypes;

		public ConditionalCallbackFilter(Callback[] callbacks) {
			this.callbacks = callbacks;
			this.callbackTypes = new Class<?>[callbacks.length];
			for (int i = 0; i < callbacks.length; i++) {
				this.callbackTypes[i] = callbacks[i].getClass();
			}
		}

		@Override
		public int accept(Method method) {
			for (int i = 0; i < this.callbacks.length; i++) {
				if (!(this.callbacks[i] instanceof ConditionalCallback) ||
						((ConditionalCallback) this.callbacks[i]).isMatch(method)) {
					return i;
				}
			}
			throw new IllegalStateException("No callback available for method " + method.getName());
		}

		public Class<?>[] getCallbackTypes() {
			return this.callbackTypes;
		}
	}
}
