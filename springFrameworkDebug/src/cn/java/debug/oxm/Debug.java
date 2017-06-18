package cn.java.debug.oxm;

import javax.xml.bind.JAXBContext;

import cn.java.demo.oxmtag.clazztobebound.FooOneClazz;

public class Debug {

	public static void main(String[] args) throws Exception {
		/*
		 	命名空间处理器
		 	org.springframework.oxm.config.OxmNamespaceHandler
		 	
		 	注册的bean
		 	org.springframework.oxm.jaxb.Jaxb2Marshaller
		 	{
		 		contextPath : ""
		 		classesToBeBound : [
		 			"cn.java.demo.oxmtag.clazztobebound.FooClazz"
		 		]
		 	
		 	}
		 	
		 */
		Class<?>[] classesToBeBound = {FooOneClazz.class};
		JAXBContext jaxbContext = JAXBContext.newInstance(classesToBeBound);
	}

}
