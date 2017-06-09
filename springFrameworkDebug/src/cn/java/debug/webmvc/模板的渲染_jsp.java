package cn.java.debug.webmvc;

import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

public class 模板的渲染_jsp {
	/*
 		
 		//-------------------部分初始化------------------------
		org.springframework.context.support.ApplicationObjectSupport.setApplicationContext(ApplicationContext context)
		{
			this.applicationContext = context; // === org.springframework.web.context.support.XmlWebApplicationContext
			this.messageSourceAccessor = new MessageSourceAccessor(context);
			ApplicationObjectSupport.initApplicationContext(context);
			{
				org.springframework.web.context.support.WebApplicationObjectSupport.initApplicationContext(ApplicationContext context)
				{
					super.initApplicationContext(context);
					{
						org.springframework.context.support.ApplicationObjectSupport.initApplicationContext(ApplicationContext context)
						{
							ApplicationObjectSupport.initApplicationContext() // 空方法
						}
					}
					if ((this.servletContext == null) && (context instanceof WebApplicationContext)) {
						this.servletContext = ((WebApplicationContext) context).getServletContext();
						if (this.servletContext != null)
							initServletContext(this.servletContext);
					}
				}
				
			}
		}
 		
 		----------------“根据模板名称实例化模板”---------------------
		org.springframework.web.servlet.view.InternalResourceViewResolver.InternalResourceViewResolver()
		{
			Class<?> viewClass = requiredViewClass(); // org.springframework.web.servlet.view.InternalResourceView
			if (InternalResourceView.class == viewClass && jstlPresent) {
				viewClass = JstlView.class; // !!!!
			}
			setViewClass(viewClass);
		}
		
		
		org.springframework.web.servlet.DispatcherServlet.render(ModelAndView mv, HttpServletRequest request, HttpServletResponse response)
		{
			org.springframework.web.servlet.DispatcherServlet.resolveViewName(String viewName, Map<String, Object> model, Locale locale, HttpServletRequest request)
			{
				for (ViewResolver viewResolver : this.viewResolvers) { // 迭代匹配渲染器
					// 如：viewResolver == org.springframework.web.servlet.view.InternalResourceViewResolver
					View view = viewResolver.resolveViewName(viewName, locale);
					{
						View view = org.springframework.web.servlet.view.AbstractCachingViewResolver.resolveViewName(String viewName, Locale locale)
						{
							org.springframework.web.servlet.view.AbstractCachingViewResolver.createView(String viewName, Locale locale)
							{
								org.springframework.web.servlet.view.UrlBasedViewResolver.loadView(String viewName, Locale locale)
								{
									org.springframework.web.servlet.view.InternalResourceViewResolver.buildView(String viewName)
									{
										InternalResourceView view = (InternalResourceView) super.buildView(viewName);
										{
											org.springframework.web.servlet.view.UrlBasedViewResolver.buildView(String viewName) 
											{
												// getViewClass() === org.springframework.web.servlet.view.JstlView
												// getViewClass() === org.springframework.web.servlet.view.InternalResourceView
												AbstractUrlBasedView view = (AbstractUrlBasedView) BeanUtils.instantiateClass(getViewClass());
												view.setUrl(getPrefix() + viewName + getSuffix()); // 设置地址
										
												String contentType = getContentType();
												if (contentType != null) {
													view.setContentType(contentType);
												}
										
												view.setRequestContextAttribute(getRequestContextAttribute());
												view.setAttributesMap(getAttributesMap());
										
												Boolean exposePathVariables = getExposePathVariables();
												if (exposePathVariables != null) {
													view.setExposePathVariables(exposePathVariables);
												}
												Boolean exposeContextBeansAsAttributes = getExposeContextBeansAsAttributes();
												if (exposeContextBeansAsAttributes != null) {
													view.setExposeContextBeansAsAttributes(exposeContextBeansAsAttributes);
												}
												String[] exposedContextBeanNames = getExposedContextBeanNames();
												if (exposedContextBeanNames != null) {
													view.setExposedContextBeanNames(exposedContextBeanNames);
												}
										
												return view;
											}
										}
										if (this.alwaysInclude != null) {
											view.setAlwaysInclude(this.alwaysInclude);
										}
										view.setPreventDispatchLoop(true);
										return view;
									}
								}
							}
						}
					}
					if (view != null) { // 匹配到了渲染器
						return view;
					}
				}
			}
			
			try {
				if (mv.getStatus() != null) {
					response.setStatus(mv.getStatus().value()); // 响应状态
				}
				// view === org.springframework.web.servlet.view.JstlView
				// view === org.springframework.web.servlet.view.InternalResourceView
				view.render(mv.getModelInternal(), request, response); // 渲染
			}
		}
	 */
}
