package cn.java.test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import cn.java.test.bean.User;
import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapperBuilder;
import freemarker.template.SimpleHash;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

public class Test {
	
	/**
	 * http://blog.csdn.net/fhx007/article/details/7902040/
	 */
	public static void main(String[] args) throws Exception {
		test4Show();	
		test4Fetch();	
	}
	
	private final static String TEST_TPL_NAME = "test.ftl";


	public static void test4Fetch() throws Exception {
		System.out.println("\n------------------------stringWriter------------------------------------");
		WriterAndTemplateHolder writerAndTemplateHolder = getWriterAndTemplateHolder();
		Map<String, Object> root = writerAndTemplateHolder.getRoot();
		Template template = writerAndTemplateHolder.getTemplate();
		
		StringWriter stringWriter = new StringWriter();
		template.process(root, stringWriter);
		System.out.println(stringWriter);
	}
	
	public static void test4Show() throws Exception {
		System.out.println("\n------------------------test4Show------------------------------------");
		WriterAndTemplateHolder writerAndTemplateHolder = getWriterAndTemplateHolder();
		Map<String, Object> root = writerAndTemplateHolder.getRoot();
		Template template = writerAndTemplateHolder.getTemplate();
		Writer out = new OutputStreamWriter(System.out);
		template.process(root, out);
	}
	
	private static WriterAndTemplateHolder getWriterAndTemplateHolder() throws Exception {
		// 定义配置
		Configuration cfg = new Configuration(Configuration.VERSION_2_3_25);
		cfg.setDirectoryForTemplateLoading(new File("./templates"));
		cfg.setDefaultEncoding("UTF-8");
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
		cfg.setLogTemplateExceptions(false);
		
		// 设置值
		Map<String, Object> root = new HashMap<String, Object>();
		{
			root.put("username", "username1");
			
			List<String> list  = new ArrayList();
			list.add("item1Value");
			list.add("item2Value");
			list.add("item3Value");
			root.put("list",list);
			
			Map<String, String> map = new HashMap<String, String>();
			map.put("key1", "key1Value");
			map.put("key2", "key2Value");
			root.put("map",map);
			
			Object object = new Object(){
				private String property1 = "property1";
				
				public String getProperty1() {
					return property1;
				}
				
				public void setProperty1(String property1) {
					this.property1 = property1;
				}
				
			};

			User user = new User(1, "username1");
			root.put("user",user);
			root.put("object",object);
			root.put("intvar",1);
		}
		
		// 获取模板
		Template template = cfg.getTemplate(TEST_TPL_NAME);
		template = cfg.getTemplate(TEST_TPL_NAME, Locale.CHINESE, "UTF-8");
		return new WriterAndTemplateHolder(root,template);
	}
	
	public static void test4Console1() throws Exception {
		Configuration config = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
		
		// 设置
		{
			Properties props = new Properties();
			if (!props.isEmpty()) {
				config.setSettings(props);
			}
		}
		
		// 共享变量
		{
			Map<String, Object> freemarkerVariables = null;
			if (!(freemarkerVariables == null || freemarkerVariables.isEmpty())) {
				config.setAllSharedVariables(new SimpleHash(freemarkerVariables, config.getObjectWrapper()));
			}
		}
		
		// 编码类型
		String defaultEncoding = "UTF-8";
		{
			config.setDefaultEncoding(defaultEncoding);
		}
		
		// 模板加载器
		{
			List<TemplateLoader> templateLoaders = new ArrayList<TemplateLoader>();
			templateLoaders.add(new ClassTemplateLoader(Test.class, ""));
			int loaderCount = templateLoaders.size();
			TemplateLoader[] loaders = templateLoaders.toArray(new TemplateLoader[loaderCount]);
			TemplateLoader templateLoader = new MultiTemplateLoader(loaders);
			if (templateLoader != null) {
				config.setTemplateLoader(templateLoader);
			}
		}
		
		// 模板变量
		SimpleHash simpleHash = new SimpleHash(new DefaultObjectWrapperBuilder(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS).build());
		{
			simpleHash.put("username", "username1");
			
			List<String> list  = new ArrayList();
			list.add("item1Value");
			list.add("item2Value");
			list.add("item3Value");
			simpleHash.put("list",list);
			
			Map<String, String> map = new HashMap<String, String>();
			map.put("key1", "key1Value");
			map.put("key2", "key2Value");
			simpleHash.put("map",map);
			
			Object bean = new Object(){
				private String property1 = "property1";
				
				public String getProperty1() {
					return property1;
				}
				
				public void setProperty1(String property1) {
					this.property1 = property1;
				}
				
			};
			
			User user = new User(1, "username1");
			simpleHash.put("user",user);
			simpleHash.put("bean",bean);
			simpleHash.put("intvar",1);
		}
		
		// 模板对象
		Template template = null;
		{
			Locale locale = Locale.CHINESE;
			String tplName = TEST_TPL_NAME;
			template = config.getTemplate(tplName, locale, defaultEncoding);
		}
		// 渲染模板
		{
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));
			template.process(simpleHash,writer); // !!!
		}
		
	}
	
	public static void test4JavaWeb() throws Exception {
//		Configuration config = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
//		{
//			Properties props = new Properties();
//			if (!props.isEmpty()) {
//				config.setSettings(props);
//			}
//		}
//		
//		{
//			Map<String, Object> freemarkerVariables = null;
//			if (!(freemarkerVariables == null || freemarkerVariables.isEmpty())) {
//				config.setAllSharedVariables(new SimpleHash(freemarkerVariables, config.getObjectWrapper()));
//			}
//		}
//		String defaultEncoding = "UTF-8";
//		{
//			config.setDefaultEncoding(defaultEncoding);
//		}
//		
//		{
//			List<TemplateLoader> templateLoaders = new ArrayList<TemplateLoader>();
//			templateLoaders.add(new ClassTemplateLoader(Test.class, ""));
//			int loaderCount = templateLoaders.size();
//			TemplateLoader[] loaders = templateLoaders.toArray(new TemplateLoader[loaderCount]);
//			TemplateLoader templateLoader = new MultiTemplateLoader(loaders);
//			if (templateLoader != null) {
//				config.setTemplateLoader(templateLoader);
//			}
//		}
//		
//		SimpleHash simpleHash = null;
//		{
//			AllHttpScopesHashModel fmModel = new AllHttpScopesHashModel(getObjectWrapper(), getServletContext(), request);
//			fmModel.put(FreemarkerServlet.KEY_JSP_TAGLIBS, this.taglibFactory);
//			fmModel.put(FreemarkerServlet.KEY_APPLICATION, this.servletContextHashModel);
//			fmModel.put(FreemarkerServlet.KEY_SESSION, buildSessionModel(request, response));
//			fmModel.put(FreemarkerServlet.KEY_REQUEST, new HttpRequestHashModel(request, response, getObjectWrapper()));
//			fmModel.put(FreemarkerServlet.KEY_REQUEST_PARAMETERS, new HttpRequestParametersHashModel(request));
//			fmModel.putAll(model);
//			simpleHash = fmModel;
//		}
//		Locale locale = null;
//		{
//			locale = RequestContextUtils.getLocale(request);
//		}
//		String tplName = TEST_TPL_NAME;
//		Template template = null;
//		{
//			template = config.getTemplate(tplName, locale, defaultEncoding);
////			template = config.getTemplate(tplName, locale);
//		}
//		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));
//		template.process(simpleHash, response.getWriter()); // !!!
	}
	
	private static class WriterAndTemplateHolder {
		private Map<String, Object> root;
		private Template template;
		public Map<String, Object> getRoot() {
			return root;
		}
		public void setRoot(Map<String, Object> root) {
			this.root = root;
		}
		public Template getTemplate() {
			return template;
		}
		public void setTemplate(Template template) {
			this.template = template;
		}
		
		public WriterAndTemplateHolder(Map<String, Object> root, Template template) {
			super();
			this.root = root;
			this.template = template;
		}

		
	}
}
