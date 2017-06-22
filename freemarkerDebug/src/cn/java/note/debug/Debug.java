package cn.java.note.debug;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

public class Debug {

	public static void main(String[] args) throws Exception {
		/*
		 	在多语言中，定位模板的方式是：
		 		freemarker.cache.TemplateCache.TemplateCacheTemplateLookupContext.lookupWithLocalizedThenAcquisitionStrategy(String templateName, Locale templateLocale)
		 		查找方式（根据“_”一个个去除）：1、 "ctrl/test_zh_CN.ftl" ；2、"ctrl/test_zh.ftl" ；3、"ctrl/test.ftl"
		 */
		
		// 定义配置
		Configuration cfg = new Configuration(Configuration.VERSION_2_3_25);
		cfg.setDirectoryForTemplateLoading(new File("./templates"));
		cfg.setDefaultEncoding("UTF-8");
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
		cfg.setLogTemplateExceptions(false);
		
		// 设置值
		Map<String, Object> root = new HashMap<String, Object>();
		root.put("username", "username1");	
		
		// 获取模板
		Template template = cfg.getTemplate("test.ftl", Locale.CHINESE, "UTF-8");
		StringWriter stringWriter = new StringWriter();
		template.process(root, stringWriter);
		System.out.println(stringWriter);
	}

}
