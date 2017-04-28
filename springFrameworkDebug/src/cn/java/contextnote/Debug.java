package cn.java.contextnote;

public class Debug {
	public static void main(String[] args) {
		/*
		 	http\://www.springframework.org/schema/context=org.springframework.context.config.ContextNamespaceHandler
			http\://www.springframework.org/schema/jee=org.springframework.ejb.config.JeeNamespaceHandler
			http\://www.springframework.org/schema/lang=org.springframework.scripting.config.LangNamespaceHandler
			http\://www.springframework.org/schema/task=org.springframework.scheduling.config.TaskNamespaceHandler
			http\://www.springframework.org/schema/cache=org.springframework.cache.config.CacheNamespaceHandler
		 */
		
		/*
		 	<context:component-scan base-package="cn.java.note.contexttag.bean" />
		 	
		 	org.springframework.context.config.ContextNamespaceHandler.parse(...)
		 	{
		 		org.springframework.context.annotation.ComponentScanBeanDefinitionParser.parse(...)
		 		{
		 		
		 		}
		 	}
		 	
		 	1、用 <context:include-filter>、<context:exclude-filter>过滤掉
		 	2、迭代符合条件的Component、获取Scope名、生成beanName名
		 	
		 */
	}
}
