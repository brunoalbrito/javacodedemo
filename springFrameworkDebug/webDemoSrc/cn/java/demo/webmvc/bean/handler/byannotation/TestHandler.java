package cn.java.demo.webmvc.bean.handler.byannotation;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.support.AbstractRefreshableApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.FlashMapManager;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ThemeResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
import org.springframework.web.servlet.support.SessionFlashMapManager;
import org.springframework.web.servlet.theme.FixedThemeResolver;
import org.springframework.web.util.WebUtils;

import cn.java.demo.webmvc.internal.BeanExpressionResolverTest;
import cn.java.demo.webmvc.internal.BeanWrapperInMvcTest;

@RequestMapping(path={"/test-handler"})
public class TestHandler {
	
	@RequestMapping(path={"/test"})
	public ModelAndView test(HttpServletRequest request,HttpServletResponse response,WebRequest webRequest) throws Exception{
		
		
		// request会预置入的一些对象
		{
			// 上下文管理器
			{
				XmlWebApplicationContext rootApplicationContext = (XmlWebApplicationContext) request.getServletContext().getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE); // 根上下文
				WebApplicationContext webApplicationContext = (WebApplicationContext) request.getAttribute(DispatcherServlet.WEB_APPLICATION_CONTEXT_ATTRIBUTE); // 子上下文
				if (rootApplicationContext instanceof AbstractRefreshableApplicationContext) {
					ConfigurableListableBeanFactory beanFactory = ((AbstractRefreshableApplicationContext) rootApplicationContext)
							.getBeanFactory();
				}
			}
			
			// 浏览器语言 zh_CN  en_US
			{
				LocaleResolver localeResolver = (LocaleResolver) request.getAttribute(DispatcherServlet.LOCALE_RESOLVER_ATTRIBUTE);
				if(localeResolver instanceof AcceptHeaderLocaleResolver){
					AcceptHeaderLocaleResolver acceptHeaderLocaleResolver = (AcceptHeaderLocaleResolver)localeResolver;
					Locale locale = acceptHeaderLocaleResolver.resolveLocale(request);
					System.out.println("locale = " + locale);
				}
			}
			
			// 主题
			{
				ThemeResolver themeResolver = (ThemeResolver) request.getAttribute(DispatcherServlet.THEME_RESOLVER_ATTRIBUTE);
				if(themeResolver instanceof FixedThemeResolver){
					FixedThemeResolver fixedThemeResolver = (FixedThemeResolver)themeResolver;
				}
			}
			
			// FLASH
			{
				FlashMapManager flashMapManager = (FlashMapManager) request.getAttribute(DispatcherServlet.FLASH_MAP_MANAGER_ATTRIBUTE);
				if(flashMapManager instanceof SessionFlashMapManager){
					SessionFlashMapManager sessionFlashMapManager = (SessionFlashMapManager)flashMapManager;
					FlashMap inputFlashMap = sessionFlashMapManager.retrieveAndUpdate(request, response);
					if (inputFlashMap != null) {
						System.out.println(Collections.unmodifiableMap(inputFlashMap));
					}
				}
			}
			
			// 国际化
			{
				WebApplicationContext webApplicationContext = (WebApplicationContext) request.getAttribute(DispatcherServlet.WEB_APPLICATION_CONTEXT_ATTRIBUTE); // 子上下文
				Object[] args = { new Long(1273), "DiskOne" };
				String message = webApplicationContext.getMessage("module0.ctrl0.action0", args, "硬盘\"{1}\"有{0}个文件。", Locale.CHINESE);
				System.out.println(message);
			}
		}
		
		
		// 从线程上下文中获取对象
		{
			RequestAttributes attributes = RequestContextHolder.currentRequestAttributes(); // attributes === org.springframework.web.context.request.ServletWebRequest
			attributes.getAttribute("key0", RequestAttributes.SCOPE_REQUEST);
		}
		
		// webRequest 对象
		{
			NativeWebRequest nativeWebRequest = (NativeWebRequest) webRequest;
			HttpServletRequest httpServletRequest = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
			HttpServletResponse httpServletResponse = nativeWebRequest.getNativeResponse(HttpServletResponse.class);
			
			{
				String name = "file0";
				MultipartHttpServletRequest multipartRequest =	WebUtils.getNativeRequest(httpServletRequest, MultipartHttpServletRequest.class);
				if (multipartRequest != null) {
					List<MultipartFile> files = multipartRequest.getFiles(name);
					if (!files.isEmpty()) {
						System.out.println((files.size() == 1 ? files.get(0) : files));
					}
				}
			}
		}
		
		// 其他
		{
			ServletContext servletContext = request.getServletContext();
			XmlWebApplicationContext rootApplicationContext = (XmlWebApplicationContext) servletContext.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE); // 根上下文
			WebApplicationContext webApplicationContext = (WebApplicationContext) request.getAttribute(DispatcherServlet.WEB_APPLICATION_CONTEXT_ATTRIBUTE); // 子上下文
			
			System.out.println("--------解析表达式--------");
			{
				BeanExpressionResolverTest.testBeanExpressionResolver(rootApplicationContext);
			}
			
			System.out.println("--------BeanWrapper--------");
			{
				BeanWrapperInMvcTest.testPropertyAccessorFactory1();
				BeanWrapperInMvcTest.testPropertyAccessorFactory2();
			}
		}
		return null;
	}
	
}
