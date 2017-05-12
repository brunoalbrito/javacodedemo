package cn.java.demo.contexttag.component.impl;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Role;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.support.PropertySourceFactory;
import org.springframework.stereotype.Component;

import cn.java.demo.contexttag.component.api.FooComponent;

//--第一扫描时识别的注解列表--
@Component(value="implThreeFooComponent") 
@Scope(scopeName=ConfigurableBeanFactory.SCOPE_SINGLETON,proxyMode=ScopedProxyMode.DEFAULT) 
@Lazy(value=false)
@Primary()
@DependsOn(value={})
@Role(value=BeanDefinition.ROLE_APPLICATION)
@Description(value="this is Description..")
//--第二次registry级别的hook扫描的注解列表--
@Order(value=10)
public class ImplThreeFooComponentx implements FooComponent {
	
	public String method1(){
		return this.getClass().getSimpleName() + ":method1()";
	}
	
	/**
	 * 内部类 0
	 * @author zhouzhian
	 *
	 */
	@Configuration(value="cn.java.demo.contexttag.component.impl.annotation.ConfigurationCondition4ParseConfiguration")
	@PropertySource(name="propertySource0",value={"classpath:cn/java/demo/contexttag/property0.properties"},ignoreResourceNotFound=true,encoding="UTF-8",factory=PropertySourceFactory.class)
	@Import(value={cn.java.demo.contexttag.component.impl.annotation.ImportSelectorImpl.class,cn.java.demo.contexttag.component.impl.annotation.ImportBeanDefinitionRegistrarImpl.class})
	@ImportResource()
	public static class PropertySourceInternal {
		public String method1() {
			return this.getClass().getSimpleName() + ":method1() " ;
		}
	}
	
	/**
	 * 内部类 1
	 * @author zhouzhian
	 *
	 */
	//--第二次registry级别的hook扫描的注解--
	@PropertySource(name="propertySource0",value={"classpath:cn/java/demo/contexttag/property0.properties"},ignoreResourceNotFound=true,encoding="UTF-8",factory=PropertySourceFactory.class) 
	@Configuration(value="cn.java.demo.contexttag.component.impl.annotation.ConfigurationCondition4RegisterBean") // 限制ComponentScan
	@ComponentScan(basePackages={"cn.java.demo.contexttag.bean,cn.java.demo.contexttag.component.implx"},
				basePackageClasses={},
				nameGenerator=org.springframework.context.annotation.AnnotationBeanNameGenerator.class,
				scopeResolver=org.springframework.context.annotation.AnnotationScopeMetadataResolver.class,
				scopedProxy=ScopedProxyMode.DEFAULT,
				resourcePattern="**/*.class",
				useDefaultFilters=true,
				includeFilters={
						@ComponentScan.Filter(type=FilterType.ANNOTATION,classes={org.springframework.stereotype.Component.class}),
						@ComponentScan.Filter(type=FilterType.ASSIGNABLE_TYPE,classes={cn.java.demo.contexttag.component.api.FooComponent.class}),
						@ComponentScan.Filter(type=FilterType.REGEX,pattern={"[A-Z][a-z0-9A-Z]*Component"}),
						@ComponentScan.Filter(type=FilterType.CUSTOM,classes={cn.java.demo.contexttag.filter.FooIncludeTypeFilter.class})
				},
				excludeFilters={
						@ComponentScan.Filter(type=FilterType.CUSTOM,classes={cn.java.demo.contexttag.filter.FooExcludeTypeFilter.class})
				},
				lazyInit=false)
	@Import(value={cn.java.demo.contexttag.component.impl.annotation.ImportSelectorImpl.class})
	@ImportResource(locations={},reader=BeanDefinitionReader.class)
	public static class ConfigurationInternal  {
		
		public String method1() {
			return this.getClass().getSimpleName() + ":method1() " ;
		}
		
		@Bean()
		public String method2(){
			return "";
		}
	}
	
	/**
	 * 内部类2
	 * @author zhouzhian
	 */
	@Component(value="implThreeFooComponentInternal1") 
	public static class ComponentInternal  {
		public String method1() {
			return this.getClass().getSimpleName() + ":method1() " ;
		}
	}
	
}
