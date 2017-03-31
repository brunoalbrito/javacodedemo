package cn.java.proxy.cglib;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Vector;

public class Test {

	public static void main(String[] args) {
		testBeanMethodInterceptor();
		testTrace();
	}

	public static void testBeanMethodInterceptor() {
		System.out.println("-----------testBeanMethodInterceptor----------------");
		Bean bean = (Bean) BeanMethodInterceptor.newInstance(Bean.class);
		bean.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				System.out.println(evt);
			}
		});
		bean.setSampleProperty("test");
	}

	public static void testTrace() {
		System.out.println("-----------testTrace----------------");
		List list = (List) Trace.newInstance(Vector.class);
		Object value = "TEST";
		list.add(value);
		list.contains(value);
		try {
			list.set(2, "ArrayIndexOutOfBounds");
		} catch (ArrayIndexOutOfBoundsException ignore) {

		}
		list.add(value + "1");
		list.add(value + "2");
		list.toString();
		list.equals(list);
		list.set(0, null);
		list.toString();
		list.add(list);
		list.get(1);
		list.toArray();
		list.remove(list);
		list.remove("");
		list.containsAll(list);
		list.lastIndexOf(value);
	}

}
