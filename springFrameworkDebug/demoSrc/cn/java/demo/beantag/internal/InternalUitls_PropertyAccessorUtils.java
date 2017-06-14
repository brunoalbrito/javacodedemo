package cn.java.demo.beantag.internal;

import org.springframework.beans.PropertyAccessorUtils;
import org.springframework.context.support.AbstractRefreshableConfigApplicationContext;

public class InternalUitls_PropertyAccessorUtils {

	public static void main(String[] args)  {
		testPropertyAccessorUtils(null);
	}
	
	public static void testPropertyAccessorUtils(AbstractRefreshableConfigApplicationContext context)  {
		System.out.println(PropertyAccessorUtils.canonicalPropertyName("UserName"));
	}

}
