package cn.java.note.beans.reflection.introspector;

import java.beans.BeanInfo;
import java.beans.IndexedPropertyDescriptor;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class IntrospectorTest {

	/**
	 * org.springframework.beans.ExtendedBeanInfo.ExtendedBeanInfo(BeanInfo delegate)
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) {
		try {
			Class<?> beanClass = Service1.class;
			new IntrospectorTest().testBeanInfo(beanClass);
			
			
			System.out.println("----------------------------------------");
			// Explicitly check implemented interfaces for setter/getter methods
			// as well,
			// in particular for Java 8 default methods...
			Class<?> clazz = beanClass;
			while (clazz != null) {
				Class<?>[] ifcs = clazz.getInterfaces();
				for (Class<?> ifc : ifcs) {
					BeanInfo ifcInfo = Introspector.getBeanInfo(ifc, Introspector.IGNORE_ALL_BEANINFO);
					PropertyDescriptor[] ifcPds = ifcInfo.getPropertyDescriptors();
					for (PropertyDescriptor pd : ifcPds) {
						System.out.println("getName = " + pd.getName() +" , getReadMethod = " +pd.getReadMethod() +" , getWriteMethod = " +
								pd.getWriteMethod() +" , getPropertyEditorClass = " + pd.getPropertyEditorClass());
					}
				}
				clazz = clazz.getSuperclass();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private final Set<PropertyDescriptor> propertyDescriptors = new TreeSet<PropertyDescriptor>(
			new PropertyDescriptorComparator());

	public void testBeanInfo(Class<?> beanClass) throws Exception {
		BeanInfo beanInfo = Introspector.getBeanInfo(beanClass);
		System.out.println("----------getPropertyDescriptors--------------");
		for (PropertyDescriptor pd : beanInfo.getPropertyDescriptors()) {
			if (pd instanceof IndexedPropertyDescriptor) { // 带“索引参数”的“属性设置器”
				// ，如：public void
				// setUsername(int
				// index,String
				// username)
				IndexedPropertyDescriptor indexedPd = (IndexedPropertyDescriptor) pd;

				this.propertyDescriptors.add(new SimpleIndexedPropertyDescriptor((IndexedPropertyDescriptor) pd));
				System.out.println("getName = " + pd.getName() + " , getIndexedPropertyType = "
						+ indexedPd.getIndexedPropertyType() + " , getPropertyType = " + indexedPd.getPropertyType());
			} else {
				this.propertyDescriptors.add(new SimplePropertyDescriptor(pd));
				System.out.println("getName = " + pd.getName() + " , getPropertyType = " + pd.getPropertyType());
			}
		}
		System.out.println("----------getMethodDescriptors--------------");
		MethodDescriptor[] methodDescriptors = beanInfo.getMethodDescriptors();
		if (methodDescriptors != null) {
			for (Method method : findCandidateWriteMethods(methodDescriptors)) { // 带返回值的setter方法
				handleCandidateWriteMethod(method);
			}
		}
	}

	private void handleCandidateWriteMethod(Method method) throws IntrospectionException {
		int nParams = method.getParameterTypes().length; // 参数数量
		String propertyName = propertyNameFor(method);
		Class<?> propertyType = method.getParameterTypes()[nParams - 1]; // "最后一个参数"为"传值参数"
		PropertyDescriptor existingPd = findExistingPropertyDescriptor(propertyName, propertyType);
		System.out.println("propertyName = " + propertyName + " , propertyType = " + propertyType);

		if (nParams == 1) { // 只有一个参数，如：public void setUsername(String username)
			if (existingPd == null) {
				this.propertyDescriptors.add(new SimplePropertyDescriptor(propertyName, null, method));
			} else {
				existingPd.setWriteMethod(method); // 已经存在，那么会覆盖
			}
		} else if (nParams == 2) { // 只有2个参数，第一个参数为“索引编号”，第二个为"传值参数" ，如：public
			// void setUsername(int index,String
			// username)
			if (existingPd == null) {
				this.propertyDescriptors
				.add(new SimpleIndexedPropertyDescriptor(propertyName, null, null, null, method));
			} else if (existingPd instanceof IndexedPropertyDescriptor) { // 已经存在，那么会覆盖
				((IndexedPropertyDescriptor) existingPd).setIndexedWriteMethod(method);
			} else {
				this.propertyDescriptors.remove(existingPd);
				this.propertyDescriptors.add(new SimpleIndexedPropertyDescriptor(propertyName,
						existingPd.getReadMethod(), existingPd.getWriteMethod(), null, method));
			}
		} else {
			throw new IllegalArgumentException("Write method must have exactly 1 or 2 parameters: " + method);
		}
	}

	private PropertyDescriptor findExistingPropertyDescriptor(String propertyName, Class<?> propertyType) {
		for (PropertyDescriptor pd : this.propertyDescriptors) {
			final Class<?> candidateType;
			final String candidateName = pd.getName();
			if (pd instanceof IndexedPropertyDescriptor) {
				IndexedPropertyDescriptor ipd = (IndexedPropertyDescriptor) pd;
				candidateType = ipd.getIndexedPropertyType();
				if (candidateName.equals(propertyName) && (candidateType.equals(propertyType)
						|| candidateType.equals(propertyType.getComponentType()))) {
					return pd;
				}
			} else {
				candidateType = pd.getPropertyType();
				if (candidateName.equals(propertyName) && (candidateType.equals(propertyType)
						|| propertyType.equals(candidateType.getComponentType()))) {
					return pd;
				}
			}
		}
		return null;
	}

	private String propertyNameFor(Method method) {
		return Introspector.decapitalize(method.getName().substring(3, method.getName().length()));
	}

	private List<Method> findCandidateWriteMethods(MethodDescriptor[] methodDescriptors) {
		List<Method> matches = new ArrayList<Method>();
		for (MethodDescriptor methodDescriptor : methodDescriptors) {
			Method method = methodDescriptor.getMethod();
			StringBuilder sb = new StringBuilder();
			sb.append("method before = ");
			sb.append(method.getName());
			if (isCandidateWriteMethod(method)) {
				matches.add(method);
				sb.append(" ,{带返回值的setter方法} method after = ");
				sb.append(method.getName());
			}
			System.out.println(sb);
		}
		// Sort non-void returning write methods to guard against the ill
		// effects of
		// non-deterministic sorting of methods returned from
		// Class#getDeclaredMethods
		// under JDK 7. See http://bugs.sun.com/view_bug.do?bug_id=7023180
		Collections.sort(matches, new Comparator<Method>() {
			@Override
			public int compare(Method m1, Method m2) {
				return m2.toString().compareTo(m1.toString());
			}
		});
		System.out.println("----------findCandidateWriteMethods--------------");
		return matches;
	}

	public static boolean isCandidateWriteMethod(Method method) {
		String methodName = method.getName();
		Class<?>[] parameterTypes = method.getParameterTypes();
		int nParams = parameterTypes.length;
		return (methodName.length() > 3 && methodName.startsWith("set") && Modifier.isPublic(method.getModifiers())
				&& (!void.class.isAssignableFrom(method.getReturnType()) || Modifier.isStatic(method.getModifiers()))
				&& (nParams == 1 || (nParams == 2 && int.class == parameterTypes[0])));
	}

	private static class Service1 {
		private int userid;
		private String username;

		public int getUserid() {
			return userid;
		}

		public void setUserid(int userid) {
			this.userid = userid;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String setUsernameWithReturn(String username) {
			this.username = username;
			return this.username;
		}

	}

	static class SimplePropertyDescriptor extends PropertyDescriptor {

		public SimplePropertyDescriptor(PropertyDescriptor original) throws IntrospectionException {
			this(original.getName(), original.getReadMethod(), original.getWriteMethod());
		}

		public SimplePropertyDescriptor(String propertyName, Method readMethod, Method writeMethod)
				throws IntrospectionException {
			super(propertyName, null, null);
		}
	}

	static class SimpleIndexedPropertyDescriptor extends IndexedPropertyDescriptor {

		public SimpleIndexedPropertyDescriptor(IndexedPropertyDescriptor original) {
			this(original.getName(), original.getReadMethod(), original.getWriteMethod(),
					original.getIndexedReadMethod(), original.getIndexedWriteMethod());
		}

		public SimpleIndexedPropertyDescriptor(String propertyName, Method readMethod, Method writeMethod,
				Method indexedReadMethod, Method indexedWriteMethod) throws IntrospectionException {
			super(propertyName, null, null, null, null);
		}

	}

	/**
	 * Sorts PropertyDescriptor instances alpha-numerically to emulate the
	 * behavior of {@link java.beans.BeanInfo#getPropertyDescriptors()}.
	 * 
	 * @see ExtendedBeanInfo#propertyDescriptors
	 */
	static class PropertyDescriptorComparator implements Comparator<PropertyDescriptor> {

		@Override
		public int compare(PropertyDescriptor desc1, PropertyDescriptor desc2) {
			String left = desc1.getName();
			String right = desc2.getName();
			for (int i = 0; i < left.length(); i++) {
				if (right.length() == i) {
					return 1;
				}
				int result = left.getBytes()[i] - right.getBytes()[i];
				if (result != 0) {
					return result;
				}
			}
			return left.length() - right.length();
		}
	}
}
