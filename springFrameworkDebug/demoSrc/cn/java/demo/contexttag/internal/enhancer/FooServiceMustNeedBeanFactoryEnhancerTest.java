package cn.java.demo.contexttag.internal.enhancer;

public class FooServiceMustNeedBeanFactoryEnhancerTest {

	private static ClassLoader classLoader = FooServiceMustNeedBeanFactoryEnhancerTest.class.getClassLoader();
	
	/**
	 * 
	 * 原来FooServiceWithOutImplBeanFactoryAware有实现BeanFactoryAware接口
	 * 		使用生成后，就实现了BeanFactoryAware接口
	 */
	public static void testFooServiceWithImplBeanFactoryAware() throws Exception {
		System.out.println("\n--------testFooServiceWithImplBeanFactoryAware----------");
		FooServiceMustNeedBeanFactoryEnhancer enhancer = new FooServiceMustNeedBeanFactoryEnhancer();
		Class superclass = cn.java.demo.contexttag.internal.enhancer.FooServiceWithImplBeanFactoryAware.class;
		Class<?> subclass = enhancer.enhance(superclass, classLoader); // 生成类
		System.out.println("---> subclass : " + subclass.getName());
		
		Object object = subclass.newInstance();
		
		// 设置值
		{
			if(object instanceof BeanFactoryAware){
				BeanFactoryAware beanFactoryAware = (BeanFactoryAware) object;
				BeanFactory beanFactory = new DefaultListableBeanFactory("DefaultListableBeanFactory in testFooServiceWithImplBeanFactoryAware...");
				beanFactoryAware.setBeanFactory(beanFactory);
			}
		}
		
		// 获取值
		{
			if(object instanceof FooServiceWithImplBeanFactoryAware){
				FooServiceWithImplBeanFactoryAware beanFactoryAwareDeepest = (FooServiceWithImplBeanFactoryAware)object;
				System.out.println(beanFactoryAwareDeepest.getBean0());
				System.out.println(beanFactoryAwareDeepest.getBeanFactoryName());
			}
		}
		
	}
	
	/**
	 * 原来FooServiceWithOutImplBeanFactoryAware没有实现BeanFactoryAware接口
	 * 		使用生成后，也实现了BeanFactoryAware接口
	 */
	public static void testFooServiceWithOutImplBeanFactoryAware() throws Exception  {
		System.out.println("\n--------testFooServiceWithOutImplBeanFactoryAware----------");
		FooServiceMustNeedBeanFactoryEnhancer enhancer = new FooServiceMustNeedBeanFactoryEnhancer();
		Class superclass = cn.java.demo.contexttag.internal.enhancer.FooServiceWithOutImplBeanFactoryAware.class;
		Class<?> subclass = enhancer.enhance(superclass, classLoader); // 生成类
		System.out.println("---> subclass : " + subclass.getName());
		
		Object object = subclass.newInstance();
		// 设置值
		{
			if(object instanceof BeanFactoryAware){
				BeanFactoryAware beanFactoryAware = (BeanFactoryAware) object;
				BeanFactory beanFactory = new DefaultListableBeanFactory("DefaultListableBeanFactory in testFooServiceWithOutImplBeanFactoryAware...");
				beanFactoryAware.setBeanFactory(beanFactory);
			}
		}
		

		// 获取值
		{
			if(object instanceof FooServiceWithOutImplBeanFactoryAware){
				FooServiceWithOutImplBeanFactoryAware beanFactoryAwareDeepest = (FooServiceWithOutImplBeanFactoryAware)object;
				System.out.println(beanFactoryAwareDeepest.getBean0());
			}
		}
	}
	
}
