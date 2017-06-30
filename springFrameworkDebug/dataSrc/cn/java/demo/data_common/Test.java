package cn.java.demo.data_common;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.AbstractRefreshableConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.repository.init.ResourceReaderRepositoryPopulator;
import org.springframework.data.repository.support.Repositories;

import cn.java.demo.util.ApplicationContextUtil;

public class Test {
	
	/**
	 */
	@SuppressWarnings("unused")
	public static void main(String[] args) throws Exception {
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"classpath:cn/java/demo/data_common/applicationContext.xml");
		
		ConfigurableListableBeanFactory beanFactory = ApplicationContextUtil.getBeanFactoryAndTryCastTypeToConfigurableListableBeanFactory((AbstractRefreshableConfigApplicationContext) context);
		
		{
			if(context instanceof AbstractApplicationContext){
				AbstractApplicationContext abstractApplicationContext = (AbstractApplicationContext)context;
				abstractApplicationContext.refresh();
			}
		}
		
		if(false){
			
			// 保存
			{
				ResourceReaderRepositoryPopulator initializer = (ResourceReaderRepositoryPopulator) context.getBean("jackson2Populator0");
				Repositories repositories = new Repositories(beanFactory); 
				initializer.populate(repositories); // 使用reader读取指定"文件中的对象"信息，保存到指定位置
			}
			
			// 保存
			{
				ResourceReaderRepositoryPopulator initializer = (ResourceReaderRepositoryPopulator) context.getBean("unmarshallerPopulator0");
				Repositories repositories = new Repositories(beanFactory); 
				initializer.populate(repositories); // 使用reader读取指定"文件中的对象"信息，保存到指定位置
			}
		}
		
		
		
		
	}
}
