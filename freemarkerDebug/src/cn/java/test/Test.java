package cn.java.test;

import java.io.File;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

public class Test {

	public static void main(String[] args) throws Exception {
		// 定义配置
		Configuration cfg = new Configuration(Configuration.VERSION_2_3_25);
		cfg.setDirectoryForTemplateLoading(new File("./templates"));
		cfg.setDefaultEncoding("UTF-8");
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
		cfg.setLogTemplateExceptions(false);
		
		// 设置值
		Map<String, Object> root = new HashMap<>();
		root.put("user", "Big Joe");
		
		// 获取模板
		Template temp = cfg.getTemplate("test.ftlh");
		Writer out = new OutputStreamWriter(System.out);
		temp.process(root, out);
	}

}
