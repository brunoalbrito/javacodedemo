package cn.java.demo.web;

import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@SuppressWarnings(value={  "serial" })
public class CommonServlet extends HttpServlet   {
	
	/**
	 * 渲染模板
	 * @param viewName
	 * @param model
	 * @param request
	 * @param response
	 * @param locale
	 */
	protected void render(String viewName,Map<String, Object> model, HttpServletRequest request, HttpServletResponse response, Locale locale){
		View view = resolveViewName(viewName,model, locale, request);
		try {
			view.render(model, request, response); // 渲染
		}
		catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	
	private  View resolveViewName(String viewName, Map<String, Object> model, Locale locale,
			HttpServletRequest request){
		ViewResolver viewResolver = new InternalResourceViewResolver(); // 这个类在spring-webmvc-4.3.6.RELEASE.jar包中
		View view = null;
		try {
			// org.springframework.web.servlet.view.InternalResourceView
			// org.springframework.web.servlet.view.JstlView
			view = viewResolver.resolveViewName(viewName, locale);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return view;
	}
	
}
