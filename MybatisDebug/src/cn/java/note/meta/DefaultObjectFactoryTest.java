package cn.java.note.meta;

import org.apache.ibatis.reflection.factory.DefaultObjectFactory;

public class DefaultObjectFactoryTest {

	public static void main(String[] args) {
		DefaultObjectFactory defaultObjectFactory = new DefaultObjectFactory();
		Object object = defaultObjectFactory.create(Object.class,null,null); // object = new Object();

	}

}
