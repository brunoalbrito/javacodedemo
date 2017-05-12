package cn.java.demo.contexttag.internal;

import java.util.Hashtable;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.jndi.support.SimpleJndiBeanFactory;
import org.springframework.util.CollectionUtils;

public class SimpleJndiBeanFactoryTest {

	public static void test(String[] args) throws Exception {
		String lookupName ="java:comp/env/resource0";
		Class<?> requiredType = Object.class;
		
		// 获取JNDI资源 - 代码未展开
		{
			BeanFactory jndiFactory = new SimpleJndiBeanFactory();
			Object object = jndiFactory.getBean(lookupName,requiredType); //!!!
		}
		
		// 获取JNDI资源 - 代码展开
		{
			
			Hashtable<?, ?> icEnv = null;
			Properties env = null;
//			env = new StandardServletEnvironment(); // org.springframework.web.context.support.StandardServletEnvironment
			if (env != null) {
				icEnv = new Hashtable<Object, Object>(env.size());
				CollectionUtils.mergePropertiesIntoMap(env, icEnv);
			}
			icEnv = new Hashtable<Object, Object>();
			Context ctx = new InitialContext(icEnv);
			Object jndiObject = ctx.lookup(lookupName);
			if (requiredType != null && !requiredType.isInstance(jndiObject)) {
				throw new RuntimeException("类型无效");
			}
			Object object = jndiObject;
		}
	}

}
