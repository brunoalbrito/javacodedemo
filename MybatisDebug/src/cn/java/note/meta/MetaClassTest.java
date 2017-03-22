package cn.java.note.meta;

import java.util.HashMap;

import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaClass;
import org.apache.ibatis.reflection.ReflectorFactory;

public class MetaClassTest {

	public static void main(String[] args) {
		Class<?> resultType = HashMap.class;
		ReflectorFactory reflectorFactory = new DefaultReflectorFactory();
		MetaClass metaType = MetaClass.forClass(resultType, reflectorFactory);
		if(metaType.hasDefaultConstructor()){
			for (String property : metaType.getGetterNames()) {
				System.out.println("get"+property.substring(0, 1).toUpperCase() + property.substring(1, property.length()));
			}
			for (String property : metaType.getSetterNames()) {
				System.out.println("set"+property.substring(0, 1).toUpperCase() + property.substring(1, property.length())+"("+metaType.getSetterType(property)+")");
			}
		}
	}

}
