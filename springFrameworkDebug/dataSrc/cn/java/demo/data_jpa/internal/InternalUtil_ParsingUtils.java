package cn.java.demo.data_jpa.internal;

import org.springframework.data.util.ParsingUtils;

public class InternalUtil_ParsingUtils {

	public static void main(String[] args) {
		String name = "transactionManagerRef";
		String xmlAttributeName = ParsingUtils.reconcatenateCamelCase(name, "-");
		System.out.println(xmlAttributeName);
	}

}
