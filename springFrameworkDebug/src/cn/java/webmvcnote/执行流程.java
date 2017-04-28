package cn.java.webmvcnote;

import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpMethod;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.async.WebAsyncManager;
import org.springframework.web.context.request.async.WebAsyncUtils;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.FrameworkServlet;
import org.springframework.web.servlet.FrameworkServlet.RequestBindingInterceptor;

public class 执行流程 {
	/**
	 	------------------初始化--------------------------
	 	org.springframework.web.servlet.HttpServletBean.init()
	 	{
	 		bw.setPropertyValues(pvs, true); // 把web.xml中的配置设置到 org.springframework.web.servlet.DispatcherServlet
	 		
	 		org.springframework.web.servlet.FrameworkServlet.initServletBean(); // !!!! 获取在ContextLoaderListener初始化的 XmlWebApplicationContext
	 		{
		 		this.webApplicationContext = org.springframework.web.servlet.FrameworkServlet.initWebApplicationContext(); // !!!! 创建自己的的 XmlWebApplicationContext
		 		{
			 		// !!! rootContext = servletContext.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
					WebApplicationContext rootContext =
							WebApplicationContextUtils.getWebApplicationContext(getServletContext());
					WebApplicationContext wac = null;
					if (wac == null) {
						// No context instance is defined for this servlet -> create a local one
						wac = org.springframework.web.servlet.FrameworkServlet.createWebApplicationContext(rootContext); // !!!
						{
							Class<?> contextClass = getContextClass(); // contextClass === org.springframework.web.context.support.XmlWebApplicationContext
							ConfigurableWebApplicationContext wac =	(ConfigurableWebApplicationContext) BeanUtils.instantiateClass(contextClass); // 实例化

							wac.setEnvironment(getEnvironment());
							wac.setParent(parent); // 设置对父类的依赖
							wac.setConfigLocation(getContextConfigLocation());
					
							org.springframework.web.servlet.FrameworkServlet.configureAndRefreshWebApplicationContext(wac); // !!!!
							{
								wac.setId(ConfigurableWebApplicationContext.APPLICATION_CONTEXT_ID_PREFIX +
										ObjectUtils.getDisplayString(getServletContext().getContextPath()) + '/' + getServletName());
								wac.setServletContext(getServletContext());
								wac.setServletConfig(getServletConfig());
								wac.setNamespace(getNamespace());
								wac.addApplicationListener(new SourceFilteringListener(wac, new ContextRefreshListener()));
								ConfigurableEnvironment env = wac.getEnvironment();
								if (env instanceof ConfigurableWebEnvironment) {
									((ConfigurableWebEnvironment) env).initPropertySources(getServletContext(), getServletConfig());
								}
						
								postProcessWebApplicationContext(wac); // 空方法
								applyInitializers(wac); // 应用初始化器  !!!!
								wac.refresh();
							}
							return wac;
						}
					}
					
					org.springframework.web.servlet.DispatcherServlet.onRefresh(wac); // !!!!
					{
						initStrategies(context);
						{
							initMultipartResolver(context);
							initLocaleResolver(context);
							initThemeResolver(context);
							initHandlerMappings(context);
							initHandlerAdapters(context);
							initHandlerExceptionResolvers(context);
							initRequestToViewNameTranslator(context);
							initViewResolvers(context);
							initFlashMapManager(context);
						}
					}
		 		}
				initFrameworkServlet(); // 空方法
	 		}
	 	}
	 	
	 	------------------执行--------------------------
	 	org.springframework.web.servlet.FrameworkServlet.service(...)
	 	{
		 	HttpMethod httpMethod = HttpMethod.resolve(request.getMethod());
			if (HttpMethod.PATCH == httpMethod || httpMethod == null) {
				org.springframework.web.servlet.FrameworkServlet.processRequest(request, response);
				{
					LocaleContext previousLocaleContext = LocaleContextHolder.getLocaleContext();
					LocaleContext localeContext = buildLocaleContext(request); // 本地
			
					RequestAttributes previousAttributes = RequestContextHolder.getRequestAttributes();
					ServletRequestAttributes requestAttributes = buildRequestAttributes(request, response, previousAttributes); // 本地化
			
					WebAsyncManager asyncManager = WebAsyncUtils.getAsyncManager(request); // 异步
					asyncManager.registerCallableInterceptor(FrameworkServlet.class.getName(), new RequestBindingInterceptor());
			
					initContextHolders(request, localeContext, requestAttributes); // 多少线程“参数本地化”
					
					org.springframework.web.servlet.DispatcherServlet.doService(request, response); // !!!!
					{
						request.setAttribute(WEB_APPLICATION_CONTEXT_ATTRIBUTE, getWebApplicationContext()); // !!!!  getWebApplicationContext() === org.springframework.web.context.support.XmlWebApplicationContext
						request.setAttribute(LOCALE_RESOLVER_ATTRIBUTE, this.localeResolver); // org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver
						request.setAttribute(THEME_RESOLVER_ATTRIBUTE, this.themeResolver); // org.springframework.web.servlet.theme.FixedThemeResolver
						request.setAttribute(THEME_SOURCE_ATTRIBUTE, getThemeSource()); // !!!!  getThemeSource() === org.springframework.web.context.support.XmlWebApplicationContext
				
						FlashMap inputFlashMap = this.flashMapManager.retrieveAndUpdate(request, response); // org.springframework.web.servlet.support.SessionFlashMapManager
						if (inputFlashMap != null) {
							request.setAttribute(INPUT_FLASH_MAP_ATTRIBUTE, Collections.unmodifiableMap(inputFlashMap));
						}
						request.setAttribute(OUTPUT_FLASH_MAP_ATTRIBUTE, new FlashMap());
						request.setAttribute(FLASH_MAP_MANAGER_ATTRIBUTE, this.flashMapManager);
						
						org.springframework.web.servlet.DispatcherServlet.doDispatch(request, response);
						{
							WebAsyncManager asyncManager = WebAsyncUtils.getAsyncManager(request); //!!!
						}
					}
				}
			}
			else {
				super.service(request, response);
			}
			
			
			
			
			
			
			
			protected final void doGet(HttpServletRequest request, HttpServletResponse response)
			{
				processRequest(request, response);
			}
			protected final void doOptions(HttpServletRequest request, HttpServletResponse response)
			{
				processRequest(request, response);
			}
			protected final void doPost(HttpServletRequest request, HttpServletResponse response)
			{
				processRequest(request, response);
			}
			protected final void doPut(HttpServletRequest request, HttpServletResponse response)
			{
				processRequest(request, response);
			}
			protected final void doDelete(HttpServletRequest request, HttpServletResponse response)
			{
				processRequest(request, response);
			}
	 	}
	 */
}
