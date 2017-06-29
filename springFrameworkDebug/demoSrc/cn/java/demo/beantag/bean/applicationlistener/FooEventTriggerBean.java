package cn.java.demo.beantag.bean.applicationlistener;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;

public class FooEventTriggerBean implements ApplicationContextAware {

	private Integer beanId;
	private String beanName;
	
	private ApplicationContext applicationContext;
	public FooEventTriggerBean(Integer beanId, String beanName) {
		super();
		this.beanId = beanId;
		this.beanName = beanName;
	}

	public Integer getBeanId() {
		return beanId;
	}


	public void setBeanId(Integer beanId) {
		this.beanId = beanId;
	}


	public String getBeanName() {
		return beanName;
	}


	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	/*
	 	以下在  org.springframework.context.support.ApplicationContextAwareProcessor.postProcessBeforeInitialization(...) 注入
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
	
	/**
	 * 触发事件
	 */
	public void testTriggerEvent() {
		ApplicationContext applicationContext = this.applicationContext;
		
		if(applicationContext instanceof XmlWebApplicationContext){
			XmlWebApplicationContext xmlWebApplicationContext = (XmlWebApplicationContext)applicationContext;
			
		}
		else if(applicationContext instanceof ClassPathXmlApplicationContext){

		}
		
		{ // 触发事件
			
			if(applicationContext instanceof AbstractApplicationContext){
				AbstractApplicationContext abstractApplicationContext = (AbstractApplicationContext)applicationContext;
				System.out.println("--- 触发FooApplicationEvent事件 ---");
				{
					ApplicationEvent applicationEvent = new FooApplicationEvent(this);
					abstractApplicationContext.publishEvent(applicationEvent);
				}
				
				System.out.println("--- 触发Object事件 ---");
				{
					Object object = new Object();
					Map hashMap = new HashMap();
					hashMap.put("key0", "key0_value");
					hashMap.put("key1", "key1_value");
					abstractApplicationContext.publishEvent(hashMap);
				}
			}
		}
		
		System.out.println("-------- ... 事件触发结束 ... --------");
	}

	@Override
	public String toString() {
		return "FooEventTriggerBean [beanId=" + beanId + ", beanName=" + beanName + "]";
	}
	
	

}
