package cn.java.demo.webmvc.bean.handler.byxml;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

/**
 * 支持的请求地址  
 * @author zhouzhian
 */
public class ControllerImpl0 implements Controller{
	
	/**
	 * 处理如下的地址的请求：
	 * 	http://www.domain.com/foo2-with-annotation
	 * 	http://www.domain.com/foo2-with-annotation/
	 * 	http://www.domain.com/foo2-with-annotation.xxx
	 */
	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return null;
	}
}
