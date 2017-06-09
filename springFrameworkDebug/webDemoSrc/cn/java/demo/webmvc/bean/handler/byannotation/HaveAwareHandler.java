package cn.java.demo.webmvc.bean.handler.byannotation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.AbstractRefreshableConfigApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import cn.java.demo.util.ApplicationContextUtil;

/**
 * 实现感知接口
 * @author zhouzhian
 *
 */
@RequestMapping(
	path={"/have-aware-handler"},
	method={RequestMethod.GET,RequestMethod.POST}
)
public class HaveAwareHandler implements ApplicationContextAware {
	
	/**
	 * /have-aware-handler/method0
	 */
	@RequestMapping(path={"/method0"},method={RequestMethod.GET,RequestMethod.POST})
	public ModelAndView method0(HttpServletRequest request,HttpServletResponse response) throws Exception{
		System.out.println("\n---> code in : " + this.getClass().getName());
		ApplicationContext applicationContext = getApplicationContext();
		if(applicationContext instanceof XmlWebApplicationContext){
			ApplicationContextUtil.printBeanDefinitionInRegistry((AbstractRefreshableConfigApplicationContext) applicationContext);
			if((applicationContext.getParent() != null) && (applicationContext.getParent() instanceof XmlWebApplicationContext)){
				System.out.println("\n applicationContext.getParent()--->");
				ApplicationContextUtil.printBeanDefinitionInRegistry((AbstractRefreshableConfigApplicationContext) applicationContext.getParent());
			}
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
