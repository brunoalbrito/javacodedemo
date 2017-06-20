package cn.java.demo.contexttag.component.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Role;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import cn.java.demo.contexttag.component.api.FooComponent;

@Component(value="implOneFooComponent") // beanName == "implOneFooComponent"
@Scope(scopeName=ConfigurableBeanFactory.SCOPE_PROTOTYPE,proxyMode=ScopedProxyMode.DEFAULT) // 当 proxyMode=ScopedProxyMode.TARGET_CLASS , BeanDefinition 被代理
@Lazy(value=false)
@Primary()
@DependsOn(value={})
@Role(value=BeanDefinition.ROLE_APPLICATION)
@Description(value="this is Description..")
public class ImplOneFooComponent implements FooComponent {
	private String currentTimeMillis = "";
	public String method1(){
		if("".equals(currentTimeMillis)){
			currentTimeMillis = System.currentTimeMillis() + "";
		}
		else{
			currentTimeMillis = currentTimeMillis + " - " + System.currentTimeMillis();
		}
		return this.getClass().getSimpleName() + ":method1() , currentTimeMillis = " + currentTimeMillis;
	}
}
