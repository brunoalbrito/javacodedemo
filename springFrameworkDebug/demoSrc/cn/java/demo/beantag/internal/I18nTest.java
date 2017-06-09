package cn.java.demo.beantag.internal;

import java.util.Locale;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.support.AbstractRefreshableConfigApplicationContext;

import cn.java.demo.util.ApplicationContextUtil;

public class I18nTest {
	public static void testI18n(AbstractRefreshableConfigApplicationContext context) {
		Object[] args = { new Long(1273), "DiskOne" };
		System.out.println(context.getMessage("module0.ctrl0.action0", args, "The disk \"{1}\" contains {0} file(s).", Locale.ENGLISH));
		System.out.println(context.getMessage("module0.ctrl0.action0", args, "硬盘\"{1}\"有{0}个文件。", Locale.CHINESE));
	}
}
