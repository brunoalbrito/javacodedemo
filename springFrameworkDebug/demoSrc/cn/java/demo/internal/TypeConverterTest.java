package cn.java.demo.internal;

import org.springframework.core.convert.TypeDescriptor;
import org.springframework.expression.TypeConverter;
import org.springframework.expression.TypedValue;
import org.springframework.expression.spel.support.StandardTypeConverter;

public class TypeConverterTest {

	/**
	 * 类型转换器
	 * @param args
	 */
	public static void testStandardTypeConverterTest() {
		TypeConverter typeConverter = new StandardTypeConverter();
		TypedValue typedValue = new TypedValue("this is string");
		Class targetType = String.class;
		String value = (String)typeConverter.convertValue(typedValue.getValue(), typedValue.getTypeDescriptor(),
				TypeDescriptor.valueOf(targetType));
		System.out.println(value);
	}

}
