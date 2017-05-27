package cn.java.demo.webmvc.bean.handler.byannotation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

/**
 * 处理上传
 * @author zhouzhian
 *
 */
@RequestMapping(
	path={"/have-aware-handler"},
	method={RequestMethod.GET,RequestMethod.POST}
)
public class HaveAwareHandler implements ApplicationContextAware {
	
	@RequestMapping(path={"/method0"},method={RequestMethod.POST})
	public ModelAndView method0(HttpServletRequest request,HttpServletResponse response) throws Exception{ 
		ApplicationContext applicationContext = getApplicationContext();
		if(applicationContext instanceof XmlWebApplicationContext){
			
		}
		return null;
	}
	
	private ApplicationContext getApplicationContext(){
		if(applicationContextThreadLocal.get()==null){
			throw new RuntimeException("applicationContextThreadLocal is null.");
		}
		return applicationContextThreadLocal.get();
	}
	
	ThreadLocal<ApplicationContext> applicationContextThreadLocal = new ThreadLocal<ApplicationContext>();
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		applicationContextThreadLocal.set(applicationContext);
	}

}
