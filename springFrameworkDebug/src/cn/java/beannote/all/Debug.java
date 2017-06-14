package cn.java.beannote.all;

public class Debug {

	/**
	 * https://github.com/spring-projects
	 */
	public static void main(String[] args) {
//		org.springframework.web.context.ContextLoaderListener
//		new org.springframework.web.servlet.DispatcherServlet();
//		--------------
//		核心就四个标签：<bean /> <beans />  <alias /> <import />
//		解析标签的核心入口：org.springframework.beans.factory.xml.DefaultBeanDefinitionDocumentReader.parseBeanDefinitions(...)
//		关于bean的定义信息存放在：org.springframework.beans.factory.support.DefaultListableBeanFactory
//		有如下bean类型：工厂类型的bean、
		
		/*
		 	org.springframework.beans.factory.xml.DefaultBeanDefinitionDocumentReader.registerBeanDefinitions(...)
			 	org.springframework.beans.factory.xml.DefaultBeanDefinitionDocumentReader.doRegisterBeanDefinitions(...)
				 	org.springframework.beans.factory.xml.DefaultBeanDefinitionDocumentReader.parseBeanDefinitions(...)
				 		org.springframework.beans.factory.xml.BeanDefinitionParserDelegate.parseCustomElement(...) 自定义标签
						 	org.springframework.aop.config.AopNamespaceHandler.parse(..) 解析
						 		org.springframework.aop.config.ConfigBeanDefinitionParser.parse(Element element, ParserContext parserContext)
						org.springframework.beans.factory.xml.DefaultBeanDefinitionDocumentReader.parseDefaultElement(...) 默认标签
							org.springframework.beans.factory.xml.DefaultBeanDefinitionDocumentReader.processBeanDefinition(...) 装饰器
								org.springframework.beans.factory.xml.BeanDefinitionParserDelegate.decorateBeanDefinitionIfRequired(...)
		 */
		
	}
	
}
