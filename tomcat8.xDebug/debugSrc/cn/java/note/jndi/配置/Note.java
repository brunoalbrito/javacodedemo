package cn.java.note.jndi.配置;

import javax.naming.Context;
import javax.naming.InitialContext;

public class Note {

	public static void main(String[] args) {
		/*
		 	
		 	
		 	tomcat独立实现了jndi接口
			 	1、tomcat实现了自己的上下文工厂 org.apache.naming.java.javaURLContextFactory.getObjectInstance(null, null, null, myProps);
			 	2、tomcat实现了自己的上下文 context === org.apache.naming.SelectorContext 
			 	3、tomcat实现了自己的 org.apache.naming.ContextBindings
			 	
			如果要能在tomcat中使用如下写法
				Context ctx = new InitialContext(icEnv);
				Object jndiObject = ctx.lookup("java:comp/env/resource0");
			的要求是，把JNDI上下文工厂指向 org.apache.naming.java.javaURLContextFactory
				java.naming.factory.url.pkgs=org.apache.naming
		 
		 */
	}

}
