package cn.java.demo.webmvc.bean.handler.byannotation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.PropertySourcesPropertyResolver;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

/**
 * 实现感知接口
 * @author zhouzhian
 *
 */
@RequestMapping(
	path={"/property-handler"},
	method={RequestMethod.GET,RequestMethod.POST}
)
public class PropertyHandler implements ApplicationContextAware {
	
	/**
	 * /property-handler/method0
	 */
	@RequestMapping(path={"/method0"},method={RequestMethod.GET,RequestMethod.POST})
	public ModelAndView method0(HttpServletRequest request,HttpServletResponse response) throws Exception{
		System.out.println("\n---> code in : " + this.getClass().getName());
		// 获取属性
		{
			ApplicationContext applicationContext = getApplicationContext();
			PropertySourcesPropertyResolver propertySourcesPropertyResolver = applicationContext.getBean("propertySourcesPropertyResolver",PropertySourcesPropertyResolver.class);
			System.out.println(propertySourcesPropertyResolver.getProperty("SPRING_PROJECT_DEBUG", Boolean.class));
			System.out.println(propertySourcesPropertyResolver.getProperty("property0"));
		}
		return null;
	}
	
	private ApplicationContext getApplicationContext(){
		if(this.applicationContext==null){
			throw new RuntimeException("applicationContext is null.");
		}
		return this.applicationContext;
	}
	
	private ApplicationContext applicationContext = null;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		System.out.println("hello,"+applicationContext);
		this.applicationContext = applicationContext;
	}

}
