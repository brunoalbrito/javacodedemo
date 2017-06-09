package cn.java.demo.webmvc.bean.handler.byannotation;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.PropertySourcesPropertyResolver;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.HandlerMapping;

/**
 * @author zhouzhian
 *
 */
@RequestMapping(path = { "/throw-exception-handler" }, method = {RequestMethod.GET})
public class ThrowExceptionHandler {
	
	/**
	 * /throw-exception-handler
	 */
	@RequestMapping(path = { "/index" }, method = {RequestMethod.GET})
	public String index(HttpServletRequest request,HttpServletResponse response) throws BindException{
		debugPrint(request);
		if(true){
			throw new RuntimeException("测试异常拦截");
		}
		try {
			ResourcePropertySource resourcePropertySource0 = new ResourcePropertySource("classpath:/configures/global.properties");
			ResourcePropertySource resourcePropertySource1 = new ResourcePropertySource("classpath:/configures/configure0.properties");
			PropertySource[] propertySources= {resourcePropertySource0,resourcePropertySource1};
			ArrayList<PropertySource> propertySourceList = new ArrayList<PropertySource>();
			MutablePropertySources mutablePropertySources = new MutablePropertySources(propertySourceList);
			PropertySourcesPropertyResolver propertySourcesPropertyResolver  = new PropertySourcesPropertyResolver(mutablePropertySources);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "";
	}
	
	private void debugPrint(HttpServletRequest request){
		System.out.println("--->debugPrint");
		String bestMatchingPattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
		String pathWithinMapping = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
		System.out.println("bestMatchingPattern : " + bestMatchingPattern); // 内部路由路径  “/valid-handler/*”
		System.out.println("pathWithinMapping : " + pathWithinMapping); // 用户访问路径  “/valid-handler/login”
	}

}
