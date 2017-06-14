package cn.java.demo.beantag.internal;

import org.springframework.context.support.AbstractRefreshableConfigApplicationContext;
import org.springframework.util.ObjectUtils;

public class InternalUtils_ObjectUtilsTest {
	
	public static void testObjectUtils(AbstractRefreshableConfigApplicationContext context) {
		String[] validationHints = null;
		ObjectUtils.isEmpty(validationHints);
		ObjectUtils.isArray(validationHints);
	}

}
