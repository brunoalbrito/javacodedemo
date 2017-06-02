package cn.java.note.debug;

public class Debug {

	public static void main(String[] args) {
		/*
		 	在多语言中，定位模板的方式是：
		 		freemarker.cache.TemplateCache.TemplateCacheTemplateLookupContext.lookupWithLocalizedThenAcquisitionStrategy(String templateName, Locale templateLocale)
		 		查找方式（根据“_”一个个去除）：1、 "ctrl/test_zh_CN.ftl" ；2、"ctrl/test_zh.ftl" ；3、"ctrl/test.ftl"
		 */
	}

}
