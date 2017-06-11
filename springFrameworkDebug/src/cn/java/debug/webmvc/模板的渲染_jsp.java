package cn.java.debug.webmvc;

import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;

import org.springframework.util.StringUtils;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.support.JstlUtils;
import org.springframework.web.servlet.support.RequestContext;

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
				{
					org.springframework.web.servlet.view.AbstractView.render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response)
					{
						Map<String, Object> mergedModel = AbstractView.createMergedOutputModel(model, request, response);
						{
							Map<String, Object> pathVars = (this.exposePathVariables ?
							(Map<String, Object>) request.getAttribute(View.PATH_VARIABLES) : null);
			
							// Consolidate static and dynamic model attributes.
							int size = this.staticAttributes.size();
							size += (model != null ? model.size() : 0);
							size += (pathVars != null ? pathVars.size() : 0);
					
							Map<String, Object> mergedModel = new LinkedHashMap<String, Object>(size);
							mergedModel.putAll(this.staticAttributes);
							if (pathVars != null) {
								mergedModel.putAll(pathVars);
							}
							if (model != null) {
								mergedModel.putAll(model);
							}
					
							// Expose RequestContext?
							if (this.requestContextAttribute != null) {
								mergedModel.put(this.requestContextAttribute, AbstractView.createRequestContext(request, response, mergedModel)); // !!!! 设置请求上下文
							}
					
							return mergedModel;
						}
						AbstractView.prepareResponse(request, response);
						{
							if (generatesDownloadContent()) {
								response.setHeader("Pragma", "private");
								response.setHeader("Cache-Control", "private, must-revalidate");
							}
						}
						InternalResourceView.renderMergedOutputModel(mergedModel, getRequestToExpose(request), response);
						{
							// Expose the model object as request attributes.
							exposeModelAsRequestAttributes(model, request); // 把modle的数据导出到request.setAttributes(...)
							{
								for (Map.Entry<String, Object> entry : model.entrySet()) {
									String modelName = entry.getKey();
									Object modelValue = entry.getValue();
									if (modelValue != null) {
										request.setAttribute(modelName, modelValue); // !!!! 所有的属性值，放入request
										if (logger.isDebugEnabled()) {
											logger.debug("Added model object '" + modelName + "' of type [" + modelValue.getClass().getName() +
													"] to request in view with name '" + getBeanName() + "'");
										}
									}
									else {
										request.removeAttribute(modelName);
										if (logger.isDebugEnabled()) {
											logger.debug("Removed model object '" + modelName +
													"' from request in view with name '" + getBeanName() + "'");
										}
									}
								}
							}
					
							// Expose helpers as request attributes, if any.
							// 当使用的是jsp的渲染引擎，执行如下代码
							{
								InternalResourceView.exposeHelpers(request); // 空方法
							}
							// 当使用的是jstl的渲染引擎，执行如下代码
							{
								JstlView.exposeHelpers(request);
								{
									if (this.messageSource != null) {
										JstlUtils.exposeLocalizationContext(request, this.messageSource);
									}
									else {
										JstlUtils.exposeLocalizationContext(new RequestContext(request, getServletContext()));
									}
								}
							}
					
							// Determine the path for the request dispatcher.
							String dispatcherPath = InternalResourceView.prepareForRendering(request, response); // 防止“死循环”，获取文件路径
							{
								String path = getUrl();
								if (this.preventDispatchLoop) { // 防止“死循环”
									String uri = request.getRequestURI();
									if (path.startsWith("/") ? uri.equals(path) : uri.equals(StringUtils.applyRelativePath(uri, path))) {
										throw new ServletException("Circular view path [" + path + "]: would dispatch back " +
												"to the current handler URL [" + uri + "] again. Check your ViewResolver setup! " +
												"(Hint: This may be the result of an unspecified view, due to default view name generation.)");
									}
								}
								return path;
							}
							
							// Obtain a RequestDispatcher for the target resource (typically a JSP).
							RequestDispatcher rd = InternalResourceView.getRequestDispatcher(request, dispatcherPath);
							{
								return request.getRequestDispatcher(path);
							}
							
							if (rd == null) {
								throw new ServletException("Could not get RequestDispatcher for [" + getUrl() +
										"]: Check that the corresponding file exists within your web application archive!");
							}
					
							// If already included or response already committed, perform include, else forward.
							if (useInclude(request, response)) {
								response.setContentType(getContentType());
								if (logger.isDebugEnabled()) {
									logger.debug("Including resource [" + getUrl() + "] in InternalResourceView '" + getBeanName() + "'");
								}
								rd.include(request, response); // !!!! 使用include的方式渲染
							}
					
							else {
								// Note: The forwarded resource is supposed to determine the content type itself.
								if (logger.isDebugEnabled()) {
									logger.debug("Forwarding to resource [" + getUrl() + "] in InternalResourceView '" + getBeanName() + "'");
								}
								rd.forward(request, response); // !!!! 使用forward的方式渲染
							}
						}
					}
				}
			}
		}
	 */
}
