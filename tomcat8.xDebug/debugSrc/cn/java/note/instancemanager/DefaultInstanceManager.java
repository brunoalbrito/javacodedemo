package cn.java.note.instancemanager;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.WeakHashMap;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.xml.ws.WebServiceRef;

public class DefaultInstanceManager implements InstanceManager {

	// Used when there are no annotations in a class
	private static final AnnotationCacheEntry[] ANNOTATIONS_EMPTY = new AnnotationCacheEntry[0];
	protected final boolean ignoreAnnotations;
	private final Map<String, String> postConstructMethods;
	private final Map<String, String> preDestroyMethods;
	protected final ClassLoader classLoader;
	private final Map<Class<?>, AnnotationCacheEntry[]> annotationCache = new WeakHashMap<>();
	protected final ClassLoader containerClassLoader;
	private final Map<String, Map<String, String>> injectionMap;
	protected final boolean privileged; // 是否所有类都具备特权（即：所有类都能实例化）
	private final Context context;
	private final Set<String> restrictedClasses; // 被限制实例化的类（即：只有没被限制的类，才能实例化）
	protected static final StringManager sm = StringManager.getManager(Globals.Package);

	/**
	 * 
	 * @param context
	 * @param classLoader 加载器
	 * @param containerClassLoader 容器加载器
	 * @param injectionMap 需要注入的字段名和相应的资源
	 * @param postConstructMethods 创建后调用的方法
	 * @param preDestroyMethods 销毁前调用的方法
	 * @param ignoreAnnotations 是否忽略注解
	 * @param privileged 是否所有类都具备特权
	 */
	public DefaultInstanceManager(Context context, 
			Map<String,Map<String, String>> injectionMap, 
			cn.java.note.instancemanager.Context catalinaContext,
			ClassLoader containerClassLoader
		) {
		classLoader = catalinaContext.getLoader().getClassLoader();
		privileged = catalinaContext.getPrivileged();
		this.containerClassLoader = containerClassLoader;
		// 是否忽略注解
		ignoreAnnotations = catalinaContext.getIgnoreAnnotations();

		Set<String> classNames = new HashSet<>();
		loadProperties(classNames, "org/apache/catalina/core/RestrictedServlets.properties", "defaultInstanceManager.restrictedServletsResource");
		loadProperties(classNames, "org/apache/catalina/core/RestrictedListeners.properties", "defaultInstanceManager.restrictedListenersResource");
		loadProperties(classNames, "org/apache/catalina/core/RestrictedFilters.properties", "defaultInstanceManager.restrictedFiltersResource");
		restrictedClasses = Collections.unmodifiableSet(classNames);
		// !!! context === org.apache.naming.NamingContext
		this.context = context;
		this.injectionMap = injectionMap;
		this.postConstructMethods = catalinaContext.findPostConstructMethods();
		this.preDestroyMethods = catalinaContext.findPreDestroyMethods();
	}

	@Override
	public Object newInstance(Class<?> clazz) throws IllegalAccessException, InvocationTargetException, NamingException, InstantiationException {
		return newInstance(clazz.newInstance(), clazz);
	}

	@Override
	public Object newInstance(String className) throws IllegalAccessException, InvocationTargetException, NamingException, InstantiationException, ClassNotFoundException {
		// 加载类，检查是否保护
		Class<?> clazz = loadClassMaybePrivileged(className, classLoader);
		return newInstance(clazz.newInstance(), clazz);
	}

	@Override
	public Object newInstance(String className, ClassLoader classLoader) throws IllegalAccessException, InvocationTargetException, NamingException, InstantiationException, ClassNotFoundException {
		Class<?> clazz = classLoader.loadClass(className);
		return newInstance(clazz.newInstance(), clazz);
	}

	@Override
	public void newInstance(Object o) throws IllegalAccessException, InvocationTargetException, NamingException {
		newInstance(o, o.getClass());
	}

	private Object newInstance(Object instance, Class<?> clazz) throws IllegalAccessException, InvocationTargetException, NamingException {
		// 不忽略注解
		if (!ignoreAnnotations) {
			// 从Servlet类的继承树中收集Servlet的注解
			Map<String, String> injections = assembleInjectionsFromClassHierarchy(clazz); // 查看配置的注解列表
			// 分析出类上的注解信息: 包括字段和方法，放入 annotationCache 对象
			//  @Resource、@WebServiceRef、@PersistenceContext、@PersistenceUnit、@PostConstruct、@PreDestroy
			populateAnnotationsCache(clazz, injections);
			// 注入名称上下文的资源 - 根据注解说明，给调用Servlet的方法注入名称上下文的资源
			processAnnotations(instance, injections);
			// 调用声明 @PostConstruct 注解的方法
			postConstruct(instance, clazz);
		}
		return instance;
	}

	@Override
	public void destroyInstance(Object instance) throws IllegalAccessException, InvocationTargetException {
		if (!ignoreAnnotations) {
			// 调用声明 @PreDestroy 注解的方法
			preDestroy(instance, instance.getClass());
		}
	}

	/**
	 * 加载属性文件
	 * 
	 * @param classNames
	 * @param resourceName
	 * @param messageKey
	 */
	private static void loadProperties(Set<String> classNames, String resourceName, String messageKey) {
		Properties properties = new Properties();
		ClassLoader cl = DefaultInstanceManager.class.getClassLoader();
		try (InputStream is = cl.getResourceAsStream(resourceName)) {
			if (is == null) {
				System.out.println("error : " + sm.getString(messageKey, resourceName));
			} else {
				properties.load(is);
			}
		} catch (IOException ioe) {
			System.out.println("error : " + sm.getString(messageKey, resourceName) + ioe);
		}
		if (properties.isEmpty()) {
			return;
		}
		for (Map.Entry<Object, Object> e : properties.entrySet()) {
			if ("restricted".equals(e.getValue())) {
				classNames.add(e.getKey().toString());
			} else {
				System.out.println("warn : " + sm.getString("defaultInstanceManager.restrictedWrongValue", resourceName, e.getKey(), e.getValue()));
			}
		}
	}

	/**
	 * 解析注解
	 * 
	 * @param clazz
	 * @param injections
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NamingException
	 */
	protected void populateAnnotationsCache(Class<?> clazz, Map<String, String> injections) throws IllegalAccessException, InvocationTargetException, NamingException {

		List<AnnotationCacheEntry> annotations = null;

		while (clazz != null) {
			AnnotationCacheEntry[] annotationsArray = null;
			synchronized (annotationCache) {
				annotationsArray = annotationCache.get(clazz);
			}
			if (annotationsArray == null) {
				if (annotations == null) {
					annotations = new ArrayList<>();
				} else {
					annotations.clear();
				}

				// !! context === org.apache.naming.NamingContext
				if (context != null) {
					// Initialize fields annotations for resource injection if
					// JNDI is enabled
					// 反射字段上的描述
					Field[] fields = Introspection.getDeclaredFields(clazz);
					for (Field field : fields) {
						Resource resourceAnnotation;
						EJB ejbAnnotation;
						WebServiceRef webServiceRefAnnotation;
						PersistenceContext persistenceContextAnnotation;
						PersistenceUnit persistenceUnitAnnotation;
						if (injections != null && injections.containsKey(field.getName())) { // 特殊注入
							annotations.add(new AnnotationCacheEntry(field.getName(), null, injections.get(field.getName()), AnnotationCacheEntryType.FIELD));
						} else if ((resourceAnnotation = field.getAnnotation(Resource.class)) != null) { // 字段有声明资源的注解 @Resource
							annotations.add(new AnnotationCacheEntry(field.getName(), null, resourceAnnotation.name(), AnnotationCacheEntryType.FIELD));
						} else if ((ejbAnnotation = field.getAnnotation(EJB.class)) != null) { // ejb的注解  @EJB
							annotations.add(new AnnotationCacheEntry(field.getName(), null, ejbAnnotation.name(), AnnotationCacheEntryType.FIELD));
						} else if ((webServiceRefAnnotation = field.getAnnotation(WebServiceRef.class)) != null) { //注解 @WebServiceRef
							annotations.add(new AnnotationCacheEntry(field.getName(), null, webServiceRefAnnotation.name(), AnnotationCacheEntryType.FIELD));
						} else if ((persistenceContextAnnotation = field.getAnnotation(PersistenceContext.class)) != null) { //注解 @PersistenceContext
							annotations.add(new AnnotationCacheEntry(field.getName(), null, persistenceContextAnnotation.name(), AnnotationCacheEntryType.FIELD));
						} else if ((persistenceUnitAnnotation = field.getAnnotation(PersistenceUnit.class)) != null) { //注解 @PersistenceUnit
							annotations.add(new AnnotationCacheEntry(field.getName(), null, persistenceUnitAnnotation.name(), AnnotationCacheEntryType.FIELD));
						}
					}
				}

				// Initialize methods annotations 方法上的注解
				Method[] methods = Introspection.getDeclaredMethods(clazz);
				Method postConstruct = null;
				String postConstructFromXml = postConstructMethods.get(clazz.getName());
				Method preDestroy = null;
				String preDestroyFromXml = preDestroyMethods.get(clazz.getName());
				for (Method method : methods) {
					if (context != null) {
						// Resource injection only if JNDI is enabled
						if (injections != null && Introspection.isValidSetter(method)) {
							String fieldName = Introspection.getPropertyName(method);
							if (injections.containsKey(fieldName)) {
								annotations.add(new AnnotationCacheEntry(method.getName(), method.getParameterTypes(), injections.get(fieldName), AnnotationCacheEntryType.SETTER));
								continue;
							}
						}
						Resource resourceAnnotation;
						EJB ejbAnnotation;
						WebServiceRef webServiceRefAnnotation;
						PersistenceContext persistenceContextAnnotation;
						PersistenceUnit persistenceUnitAnnotation;
						if ((resourceAnnotation = method.getAnnotation(Resource.class)) != null) {
							annotations.add(new AnnotationCacheEntry(method.getName(), method.getParameterTypes(), resourceAnnotation.name(), AnnotationCacheEntryType.SETTER));
						} else if ((ejbAnnotation = method.getAnnotation(EJB.class)) != null) {
							annotations.add(new AnnotationCacheEntry(method.getName(), method.getParameterTypes(), ejbAnnotation.name(), AnnotationCacheEntryType.SETTER));
						} else if ((webServiceRefAnnotation = method.getAnnotation(WebServiceRef.class)) != null) {
							annotations.add(new AnnotationCacheEntry(method.getName(), method.getParameterTypes(), webServiceRefAnnotation.name(), AnnotationCacheEntryType.SETTER));
						} else if ((persistenceContextAnnotation = method.getAnnotation(PersistenceContext.class)) != null) {
							annotations.add(new AnnotationCacheEntry(method.getName(), method.getParameterTypes(), persistenceContextAnnotation.name(), AnnotationCacheEntryType.SETTER));
						} else if ((persistenceUnitAnnotation = method.getAnnotation(PersistenceUnit.class)) != null) {
							annotations.add(new AnnotationCacheEntry(method.getName(), method.getParameterTypes(), persistenceUnitAnnotation.name(), AnnotationCacheEntryType.SETTER));
						}
					}

					// 有声明 @PostConstruct注解
					postConstruct = findPostConstruct(postConstruct, postConstructFromXml, method);
					// 有声明 @PreDestroy注解
					preDestroy = findPreDestroy(preDestroy, preDestroyFromXml, method);
				}

				if (postConstruct != null) {
					annotations.add(new AnnotationCacheEntry(postConstruct.getName(), postConstruct.getParameterTypes(), null, AnnotationCacheEntryType.POST_CONSTRUCT));
				} else if (postConstructFromXml != null) {
					throw new IllegalArgumentException("Post construct method " + postConstructFromXml + " for class " + clazz.getName() + " is declared in deployment descriptor but cannot be found.");
				}
				if (preDestroy != null) {
					annotations.add(new AnnotationCacheEntry(preDestroy.getName(), preDestroy.getParameterTypes(), null, AnnotationCacheEntryType.PRE_DESTROY));
				} else if (preDestroyFromXml != null) {
					throw new IllegalArgumentException("Pre destroy method " + preDestroyFromXml + " for class " + clazz.getName() + " is declared in deployment descriptor but cannot be found.");
				}
				if (annotations.isEmpty()) {
					// Use common object to save memory
					annotationsArray = ANNOTATIONS_EMPTY;
				} else {
					annotationsArray = annotations.toArray(new AnnotationCacheEntry[annotations.size()]);
				}
				synchronized (annotationCache) {
					annotationCache.put(clazz, annotationsArray);
				}
			}
			clazz = clazz.getSuperclass(); // 查看父类的
		}
	}

	/**
	 * 注入资源
	 * 
	 * @param instance
	 * @param injections
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NamingException
	 */
	protected void processAnnotations(Object instance, Map<String, String> injections) throws IllegalAccessException, InvocationTargetException, NamingException {

		if (context == null) {
			// No resource injection
			return;
		}

		Class<?> clazz = instance.getClass();

		while (clazz != null) {
			AnnotationCacheEntry[] annotations;
			synchronized (annotationCache) {
				annotations = annotationCache.get(clazz);
			}
			for (AnnotationCacheEntry entry : annotations) {
				if (entry.getType() == AnnotationCacheEntryType.SETTER) { // 通过setter的反射出来的注解
					// context === org.apache.naming.NamingContext
					// 调用Servlet的方法，进行设置资源
					lookupMethodResource(context, instance, getMethod(clazz, entry), entry.getName(), clazz);
				} else if (entry.getType() == AnnotationCacheEntryType.FIELD) { // 通过字段反射出来的注解
					lookupFieldResource(context, instance, getField(clazz, entry), entry.getName(), clazz);
				}
			}
			clazz = clazz.getSuperclass();
		}
	}

	/**
	 * 查找资源，并注入
	 * 
	 * @param context
	 * @param instance
	 * @param field
	 * @param name
	 * @param clazz
	 * @throws NamingException
	 * @throws IllegalAccessException
	 */
	protected static void lookupFieldResource(Context context, Object instance, Field field, String name, Class<?> clazz) throws NamingException, IllegalAccessException {

		Object lookedupResource;
		boolean accessibility;

		String normalizedName = normalize(name); // jndi的名称

		if ((normalizedName != null) && (normalizedName.length() > 0)) {
			lookedupResource = context.lookup(normalizedName);
		} else {
			lookedupResource = context.lookup(clazz.getName() + "/" + field.getName());
		}

		synchronized (field) {
			accessibility = field.isAccessible();
			field.setAccessible(true);
			field.set(instance, lookedupResource);
			field.setAccessible(accessibility);
		}
	}

	/**
	 * 查找资源，并注入
	 * 
	 * @param context
	 * @param instance
	 * @param method
	 * @param name
	 * @param clazz
	 * @throws NamingException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	protected static void lookupMethodResource(Context context, Object instance, Method method, String name, Class<?> clazz) throws NamingException, IllegalAccessException, InvocationTargetException {

		if (!Introspection.isValidSetter(method)) {
			throw new IllegalArgumentException(sm.getString("defaultInstanceManager.invalidInjection"));
		}

		Object lookedupResource;
		boolean accessibility;

		String normalizedName = normalize(name); // jndi的名称

		// context === org.apache.naming.NamingContext
		if ((normalizedName != null) && (normalizedName.length() > 0)) {
			lookedupResource = context.lookup(normalizedName); // 在名称上下文中查找资源
		} else {
			lookedupResource = context.lookup(clazz.getName() + "/" + Introspection.getPropertyName(method));
		}

		synchronized (method) {
			accessibility = method.isAccessible();
			method.setAccessible(true);
			method.invoke(instance, lookedupResource); // 调用Servlet的方法，进行设置资源
			method.setAccessible(accessibility);
		}
	}

	/**
	 * 标准化
	 * 
	 * @param jndiName
	 * @return
	 */
	private static String normalize(String jndiName) {
		if (jndiName != null && jndiName.startsWith("java:comp/env/")) {
			return jndiName.substring(14);
		}
		return jndiName;
	}

	/**
	 * 获取自定方法
	 * 
	 * @param clazz
	 * @param entry
	 * @return
	 */
	private static Method getMethod(final Class<?> clazz, final AnnotationCacheEntry entry) {
		Method result = null;
		if (Globals.IS_SECURITY_ENABLED) {
			result = AccessController.doPrivileged(new PrivilegedAction<Method>() {
				@Override
				public Method run() {
					Method result = null;
					try {
						result = clazz.getDeclaredMethod(entry.getAccessibleObjectName(), entry.getParamTypes());
					} catch (NoSuchMethodException e) {
						// Should never happen. On that basis don't log
						// it.
					}
					return result;
				}
			});
		} else {
			try {
				result = clazz.getDeclaredMethod(entry.getAccessibleObjectName(), entry.getParamTypes());
			} catch (NoSuchMethodException e) {
				// Should never happen. On that basis don't log it.
			}
		}
		return result;
	}

	/**
	 * 获取自定字段
	 * 
	 * @param clazz
	 * @param entry
	 * @return
	 */
	private static Field getField(final Class<?> clazz, final AnnotationCacheEntry entry) {
		Field result = null;
		if (Globals.IS_SECURITY_ENABLED) {
			result = AccessController.doPrivileged(new PrivilegedAction<Field>() {
				@Override
				public Field run() {
					Field result = null;
					try {
						result = clazz.getDeclaredField(entry.getAccessibleObjectName());
					} catch (NoSuchFieldException e) {
						// Should never happen. On that basis don't log
						// it.
					}
					return result;
				}
			});
		} else {
			try {
				result = clazz.getDeclaredField(entry.getAccessibleObjectName());
			} catch (NoSuchFieldException e) {
				// Should never happen. On that basis don't log it.
			}
		}
		return result;
	}

	/**
	 * 从Servlet类的继承树中收集Servlet的注解
	 * 
	 * @param clazz
	 * @return
	 */
	private Map<String, String> assembleInjectionsFromClassHierarchy(Class<?> clazz) {
		Map<String, String> injections = new HashMap<>();
		Map<String, String> currentInjections = null;
		while (clazz != null) {
			currentInjections = this.injectionMap.get(clazz.getName());
			if (currentInjections != null) {
				injections.putAll(currentInjections);
			}
			clazz = clazz.getSuperclass();
		}
		return injections;
	}

	/**
	 * 加载类（可能是特权类）
	 * 
	 * @param className
	 * @param classLoader
	 * @return
	 * @throws ClassNotFoundException
	 */
	protected Class<?> loadClassMaybePrivileged(final String className, final ClassLoader classLoader) throws ClassNotFoundException {
		Class<?> clazz;
		if (SecurityUtil.isPackageProtectionEnabled()) {
			try {
				clazz = AccessController.doPrivileged(new PrivilegedExceptionAction<Class<?>>() {

					@Override
					public Class<?> run() throws Exception {
						return loadClass(className, classLoader);
					}
				});
			} catch (PrivilegedActionException e) {
				Throwable t = e.getCause();
				if (t instanceof ClassNotFoundException) {
					throw (ClassNotFoundException) t;
				}
				throw new RuntimeException(t);
			}
		} else {
			// 加载类到内存
			clazz = loadClass(className, classLoader);
		}
		// 检查访问权限
		checkAccess(clazz);
		return clazz;
	}

	/**
	 * 是否可访问
	 * 
	 * @param clazz
	 */
	private void checkAccess(Class<?> clazz) {
		if (privileged) {  // 所有实例都有特权，直接返回
			return;
		}
		if (ContainerServlet.class.isAssignableFrom(clazz)) { // 是 ContainerServlet 的子类
			throw new SecurityException(sm.getString("defaultInstanceManager.restrictedContainerServlet", clazz));
		}
		while (clazz != null) {
			if (restrictedClasses.contains(clazz.getName())) { // 是被限制的类
				throw new SecurityException(sm.getString("defaultInstanceManager.restrictedClass", clazz));
			}
			clazz = clazz.getSuperclass();
		}
	}

	/**
	 * 方法是否有 @PostConstruct 注解
	 * 
	 * @param currentPostConstruct
	 * @param postConstructFromXml
	 * @param method
	 * @return
	 */
	private static Method findPostConstruct(Method currentPostConstruct, String postConstructFromXml, Method method) {
		// 有声明 @PostConstruct注解
		return findLifecycleCallback(currentPostConstruct, postConstructFromXml, method, PostConstruct.class);
	}

	/**
	 * 方法是否有 @PreDestroy 注解
	 * 
	 * @param currentPreDestroy
	 * @param preDestroyFromXml
	 * @param method
	 * @return
	 */
	private static Method findPreDestroy(Method currentPreDestroy, String preDestroyFromXml, Method method) {
		// 有声明 @PreDestroy注解
		return findLifecycleCallback(currentPreDestroy, preDestroyFromXml, method, PreDestroy.class);
	}

	/**
	 * 检查该方法是否存在注解、或者该方法是指定名称
	 * 
	 * @param currentMethod
	 * @param methodNameFromXml
	 * @param method
	 * @param annotation
	 * @return
	 */
	private static Method findLifecycleCallback(Method currentMethod, String methodNameFromXml, Method method, Class<? extends Annotation> annotation) {
		Method result = currentMethod;
		if (methodNameFromXml != null) {
			if (method.getName().equals(methodNameFromXml)) {
				if (!Introspection.isValidLifecycleCallback(method)) {
					throw new IllegalArgumentException("Invalid " + annotation.getName() + " annotation");
				}
				result = method;
			}
		} else {
			if (method.isAnnotationPresent(annotation)) {
				if (currentMethod != null || !Introspection.isValidLifecycleCallback(method)) {
					throw new IllegalArgumentException("Invalid " + annotation.getName() + " annotation");
				}
				result = method;
			}
		}
		return result;
	}

	/**
	 * 加载类
	 * 
	 * @param className
	 * @param classLoader
	 * @return
	 * @throws ClassNotFoundException
	 */
	protected Class<?> loadClass(String className, ClassLoader classLoader) throws ClassNotFoundException {
		if (className.startsWith("org.apache.catalina")) {
			return containerClassLoader.loadClass(className);
		}
		try {
			// 加载类到内存
			Class<?> clazz = containerClassLoader.loadClass(className);
			if (ContainerServlet.class.isAssignableFrom(clazz)) {
				return clazz;
			}
		} catch (Throwable t) {
			ExceptionUtils.handleThrowable(t);
		}
		return classLoader.loadClass(className);
	}

	/**
	 * 调用声明 @PostConstruct 注解的方法
	 * 
	 * @param instance
	 * @param clazz
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	protected void postConstruct(Object instance, final Class<?> clazz) throws IllegalAccessException, InvocationTargetException {
		if (context == null) {
			// No resource injection
			return;
		}

		Class<?> superClass = clazz.getSuperclass();
		if (superClass != Object.class) {
			postConstruct(instance, superClass);
		}

		// At the end the postconstruct annotated
		// method is invoked
		AnnotationCacheEntry[] annotations;
		synchronized (annotationCache) {
			annotations = annotationCache.get(clazz);
		}
		for (AnnotationCacheEntry entry : annotations) {
			if (entry.getType() == AnnotationCacheEntryType.POST_CONSTRUCT) {
				Method postConstruct = getMethod(clazz, entry);
				synchronized (postConstruct) {
					boolean accessibility = postConstruct.isAccessible();
					postConstruct.setAccessible(true);
					postConstruct.invoke(instance);
					postConstruct.setAccessible(accessibility);
				}
			}
		}
	}

	/**
	 * 调用声明 @PreDestroy 注解的方法
	 * 
	 * @param instance
	 * @param clazz
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	protected void preDestroy(Object instance, final Class<?> clazz) throws IllegalAccessException, InvocationTargetException {
		Class<?> superClass = clazz.getSuperclass();
		if (superClass != Object.class) {
			preDestroy(instance, superClass);
		}

		// At the end the postconstruct annotated
		// method is invoked
		AnnotationCacheEntry[] annotations = null;
		synchronized (annotationCache) {
			annotations = annotationCache.get(clazz);
		}
		if (annotations == null) {
			// instance not created through the instance manager
			return;
		}
		for (AnnotationCacheEntry entry : annotations) {
			if (entry.getType() == AnnotationCacheEntryType.PRE_DESTROY) {
				Method preDestroy = getMethod(clazz, entry);
				synchronized (preDestroy) {
					boolean accessibility = preDestroy.isAccessible();
					preDestroy.setAccessible(true);
					preDestroy.invoke(instance);
					preDestroy.setAccessible(accessibility);
				}
			}
		}
	}

	/**
	 * 注解条目
	 * 
	 * @author Administrator
	 *
	 */
	private static final class AnnotationCacheEntry {
		private final String accessibleObjectName;
		private final Class<?>[] paramTypes;
		private final String name;
		private final AnnotationCacheEntryType type;

		public AnnotationCacheEntry(String accessibleObjectName, Class<?>[] paramTypes, String name, AnnotationCacheEntryType type) {
			this.accessibleObjectName = accessibleObjectName;
			this.paramTypes = paramTypes;
			this.name = name;
			this.type = type;
		}

		public String getAccessibleObjectName() {
			return accessibleObjectName;
		}

		public Class<?>[] getParamTypes() {
			return paramTypes;
		}

		public String getName() {
			return name;
		}

		public AnnotationCacheEntryType getType() {
			return type;
		}
	}

	private static enum AnnotationCacheEntryType {
		FIELD, SETTER, POST_CONSTRUCT, PRE_DESTROY
	}

}
