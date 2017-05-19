package cn.java.debug.webmvc;

import org.springframework.web.HttpRequestHandler;
import org.springframework.web.servlet.mvc.Controller;

public class Interceptor的识别_Handler的识别_beanNameMapper {

	public static void main(String[] args) {
		/*
		 	org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping
		 	
		 	--------------BeanNameUrlHandlerMapping如何扫描Interceptor、Handler-------------------
		 	org.springframework.context.support.ApplicationObjectSupport.setApplicationContext(ApplicationContext context)
		 	{
		 		this.applicationContext = context;
		 		org.springframework.web.context.support.WebApplicationObjectSupport.initApplicationContext(ApplicationContext context)
		 		{
		 			org.springframework.context.support.ApplicationObjectSupport.initApplicationContext(ApplicationContext context)
		 			{
		 				org.springframework.web.servlet.handler.AbstractDetectingUrlHandlerMapping.initApplicationContext()
		 				{
		 					org.springframework.web.servlet.handler.AbstractHandlerMapping.initApplicationContext()
		 					{
		 						AbstractHandlerMapping.extendInterceptors(this.interceptors); // 空方法
								AbstractHandlerMapping.detectMappedInterceptors(this.adaptedInterceptors); // 扫描applicationContext.xml文件，找出MappedInterceptor类型的bean
								{
									mappedInterceptors.addAll(
										BeanFactoryUtils.beansOfTypeIncludingAncestors(
												getApplicationContext(), MappedInterceptor.class, true, false).values());
								}
								AbstractHandlerMapping.initInterceptors(); // 适配 Interceptors
								{
									if (!this.interceptors.isEmpty()) {
										for (int i = 0; i < this.interceptors.size(); i++) {
											Object interceptor = this.interceptors.get(i);
											if (interceptor == null) {
												throw new IllegalArgumentException("Entry number " + i + " in interceptors array is null");
											}
											this.adaptedInterceptors.add(adaptInterceptor(interceptor)); // 注册Interceptor ----- 1
										}
									}
								}
		 					}
		 					
		 					org.springframework.web.servlet.handler.AbstractDetectingUrlHandlerMapping.detectHandlers() // 扫描applicationContext.xml文件的所有bean，根据beanName识别出Handler
		 					{
			 					String[] beanNames = (this.detectHandlersInAncestorContexts ?
										BeanFactoryUtils.beanNamesForTypeIncludingAncestors(getApplicationContext(), Object.class) :
										getApplicationContext().getBeanNamesForType(Object.class));
								// Take any bean name that we can determine URLs for.
								for (String beanName : beanNames) {
									String[] urls = determineUrlsForHandler(beanName);
									{
										org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping.determineUrlsForHandler(String beanName)
										{
											List<String> urls = new ArrayList<String>();
											if (beanName.startsWith("/")) { // 以“/”开头的beanName被认为是Handler
												urls.add(beanName);
											}
											String[] aliases = getApplicationContext().getAliases(beanName);
											for (String alias : aliases) {
												if (alias.startsWith("/")) {
													urls.add(alias);
												}
											}
											return StringUtils.toStringArray(urls);
										}
									}
									if (!ObjectUtils.isEmpty(urls)) {
										// URL paths found: Let's consider it a handler.
										registerHandler(urls, beanName); // !!!
										{
											org.springframework.web.servlet.handler.AbstractUrlHandlerMapping.registerHandler(String[] urlPaths, String beanName)
											{
												for (String urlPath : urlPaths) {
													AbstractUrlHandlerMapping.registerHandler(urlPath, beanName);
													{
														if (urlPath.equals("/")) {
															setRootHandler(resolvedHandler);
														}
														else if (urlPath.equals("/*")) {
															setDefaultHandler(resolvedHandler);
														}
														else {
															this.handlerMap.put(urlPath, resolvedHandler); // 注册Handler ----- 2
														}
													}
												}
											}
										}
									}
									else {
									}
								}
		 					}
		 				}
		 			}
		 			this.servletContext = ((WebApplicationContext) context).getServletContext();
		 			org.springframework.web.context.support.WebApplicationObjectSupport.initServletContext(ServletContext servletContext);
		 		}
		 	}
		 	
		 	--------------BeanNameUrlHandlerMapping如何Handler-------------------
		 	org.springframework.web.servlet.handler.AbstractHandlerMapping.getHandler(HttpServletRequest request)
		 	{
		 		org.springframework.web.servlet.handler.AbstractUrlHandlerMapping.getHandlerInternal(HttpServletRequest request)
		 		{
			 		// org.springframework.web.util.UrlPathHelper.getLookupPathForRequest(request);
					String lookupPath = getUrlPathHelper().getLookupPathForRequest(request); // 请求地址
					Object handler = lookupHandler(lookupPath, request); // !!! 返回Handler执行链
					{
						// Direct match?  直接匹配
						Object handler = this.handlerMap.get(urlPath);
						if (handler != null) {
							// Bean name or resolved handler?
							if (handler instanceof String) {
								String handlerName = (String) handler;
								handler = getApplicationContext().getBean(handlerName);
							}
							validateHandler(handler, request);
							return buildPathExposingHandler(handler, urlPath, urlPath, null);
						}
						// Pattern match? 正则表达式匹配
						List<String> matchingPatterns = new ArrayList<String>();
						for (String registeredPattern : this.handlerMap.keySet()) {
							if (getPathMatcher().match(registeredPattern, urlPath)) {
								matchingPatterns.add(registeredPattern);
							}
							else if (useTrailingSlashMatch()) {
								if (!registeredPattern.endsWith("/") && getPathMatcher().match(registeredPattern + "/", urlPath)) {
									matchingPatterns.add(registeredPattern +"/");
								}
							}
						}
						String bestPatternMatch = null;
						Comparator<String> patternComparator = getPathMatcher().getPatternComparator(urlPath);
						if (!matchingPatterns.isEmpty()) {
							Collections.sort(matchingPatterns, patternComparator);
							if (logger.isDebugEnabled()) {
								logger.debug("Matching patterns for request [" + urlPath + "] are " + matchingPatterns);
							}
							bestPatternMatch = matchingPatterns.get(0); // 取第一个作为命中
						}
						if (bestPatternMatch != null) { 
							handler = this.handlerMap.get(bestPatternMatch);
							if (handler == null) {
								Assert.isTrue(bestPatternMatch.endsWith("/"));
								handler = this.handlerMap.get(bestPatternMatch.substring(0, bestPatternMatch.length() - 1));
							}
							// Bean name or resolved handler?
							if (handler instanceof String) { // 配置的是beanName
								String handlerName = (String) handler;
								handler = getApplicationContext().getBean(handlerName); // 实例化
							}
							validateHandler(handler, request);
							String pathWithinMapping = getPathMatcher().extractPathWithinPattern(bestPatternMatch, urlPath);
				
							// There might be multiple 'best patterns', let's make sure we have the correct URI template variables
							// for all of them
							Map<String, String> uriTemplateVariables = new LinkedHashMap<String, String>(); // URL中的模板变量
							for (String matchingPattern : matchingPatterns) {
								if (patternComparator.compare(bestPatternMatch, matchingPattern) == 0) {
									Map<String, String> vars = getPathMatcher().extractUriTemplateVariables(matchingPattern, urlPath);
									Map<String, String> decodedVars = getUrlPathHelper().decodePathVariables(request, vars);
									uriTemplateVariables.putAll(decodedVars);
								}
							}
							return buildPathExposingHandler(handler, bestPatternMatch, pathWithinMapping, uriTemplateVariables);
							{
								HandlerExecutionChain chain = new HandlerExecutionChain(rawHandler); // Handler执行链
								chain.addInterceptor(new PathExposingHandlerInterceptor(bestMatchingPattern, pathWithinMapping));
								if (!CollectionUtils.isEmpty(uriTemplateVariables)) {
									chain.addInterceptor(new UriTemplateVariablesHandlerInterceptor(uriTemplateVariables));
								}
								return chain;
							}
						}
						// No handler found...
						return null;
					}
					if (handler == null) {
						// We need to care for the default handler directly, since we need to
						// expose the PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE for it as well.
						Object rawHandler = null;
						if ("/".equals(lookupPath)) {
							rawHandler = getRootHandler(); // 根处理器
						}
						if (rawHandler == null) {
							rawHandler = getDefaultHandler(); // 默认处理器
						}
						if (rawHandler != null) {
							// Bean name or resolved handler?
							if (rawHandler instanceof String) {
								String handlerName = (String) rawHandler;
								rawHandler = getApplicationContext().getBean(handlerName);
							}
							validateHandler(rawHandler, request); // 校验请求的method、参数、header是否符合@RequestMapping注解配置的
							handler = buildPathExposingHandler(rawHandler, lookupPath, lookupPath, null); // !!! 返回Handler执行链
							{
								HandlerExecutionChain chain = new HandlerExecutionChain(rawHandler); // Handler执行链
								chain.addInterceptor(new PathExposingHandlerInterceptor(bestMatchingPattern, pathWithinMapping));
								if (!CollectionUtils.isEmpty(uriTemplateVariables)) {
									chain.addInterceptor(new UriTemplateVariablesHandlerInterceptor(uriTemplateVariables));
								}
								return chain;
							}
						}
					}
					return handler;
		 		}
		 	}
		 	
			--------------------执行Handler的过程--------------------------
			org.springframework.web.servlet.DispatcherServlet.doDispatch(HttpServletRequest request, HttpServletResponse response)
			{
				// 如果不是文件上传，processedRequest就是request本身
				// Determine handler for the current request.
				mappedHandler = getHandler(processedRequest); // 决策一个适合processedRequest的Handler，执行链管理器HandlerExecutionChain
				if (mappedHandler == null || mappedHandler.getHandler() == null) {
					noHandlerFound(processedRequest, response);
					return;
				}
				
				// ha === org.springframework.web.servlet.mvc.HttpRequestHandlerAdapter
				// ha === org.springframework.web.servlet.mvc.SimpleControllerHandlerAdapter
				HandlerAdapter ha = getHandlerAdapter(mappedHandler.getHandler()); // 决策一个适合Handler的“Handler适配器”， 四种类型的适配器
				
				// 执行拦截器的前置方法
				org.springframework.web.servlet.HandlerExecutionChain.applyPreHandle(processedRequest, response)
				{
					HandlerInterceptor[] interceptors = getInterceptors();
					if (!ObjectUtils.isEmpty(interceptors)) {
						for (int i = 0; i < interceptors.length; i++) {
							HandlerInterceptor interceptor = interceptors[i];
							if (!interceptor.preHandle(request, response, this.handler)) {
								triggerAfterCompletion(request, response, null); // 如果没有过，直接触发结束的
								{
									HandlerInterceptor[] interceptors = getInterceptors();
									if (!ObjectUtils.isEmpty(interceptors)) {
										for (int i = this.interceptorIndex; i >= 0; i--) {
											HandlerInterceptor interceptor = interceptors[i];
											try {
												interceptor.afterCompletion(request, response, this.handler, ex);
											}
											catch (Throwable ex2) {
												logger.error("HandlerInterceptor.afterCompletion threw exception", ex2);
											}
										}
									}
								}
								return false;
							}
							this.interceptorIndex = i;
						}
					}
					return true;
				}
				 	
				// 执行业务方法
				{
					if(当是“实现HttpRequestHandler接口的Handler”)
					{
						mv = org.springframework.web.servlet.mvc.HttpRequestHandlerAdapter.handle(processedRequest, response, mappedHandler.getHandler()); // 执行handler的方法，返回值	
						{
							((HttpRequestHandler) handler).handleRequest(request, response); 
							return null; // 没有返回值
						}
					}
					else if(当是“实现Controller接口的Handler”)
					{
						mv = org.springframework.web.servlet.mvc.SimpleControllerHandlerAdapter.handle(processedRequest, response, mappedHandler.getHandler()); // 执行handler的方法，返回值	
						{
							return ((Controller) handler).handleRequest(request, response); // 有返回值
						}
					}
				}
				
				// 执行拦截器的后置方法
				org.springframework.web.servlet.HandlerExecutionChain.applyPostHandle(processedRequest, response, mv);
				{
					HandlerInterceptor[] interceptors = getInterceptors();
					if (!ObjectUtils.isEmpty(interceptors)) {
						for (int i = interceptors.length - 1; i >= 0; i--) {
							HandlerInterceptor interceptor = interceptors[i];
							interceptor.postHandle(request, response, this.handler, mv);
						}
					}
				}
				
				// 处理视图
				org.springframework.web.servlet.DispatcherServlet.processDispatchResult( request,  response,  mappedHandler,  mv,  exception)
				{
					boolean errorView = false;
	
					if (exception != null) {
						if (exception instanceof ModelAndViewDefiningException) {
							logger.debug("ModelAndViewDefiningException encountered", exception);
							mv = ((ModelAndViewDefiningException) exception).getModelAndView();
						}
						else {
							Object handler = (mappedHandler != null ? mappedHandler.getHandler() : null);
							mv = processHandlerException(request, response, handler, exception);
							errorView = (mv != null);
						}
					}
					render(mv, request, response); // 进行渲染 !!!
					{
						View view;
						if (mv.isReference()) {
							// We need to resolve the view name.
							view = resolveViewName(mv.getViewName(), mv.getModelInternal(), locale, request); // 解析视图
						}
						else {
							// No need to lookup: the ModelAndView object contains the actual View object.
							view = mv.getView();
						}
				
						// Delegate to the View object for rendering.
						try {
							if (mv.getStatus() != null) {
								response.setStatus(mv.getStatus().value()); // 响应状态
							}
							view.render(mv.getModelInternal(), request, response); // 渲染
						}
					}
					
					if (mappedHandler != null) {
						mappedHandler.triggerAfterCompletion(request, response, null); // 触发结束方法
						{
							HandlerInterceptor[] interceptors = getInterceptors();
							if (!ObjectUtils.isEmpty(interceptors)) {
								for (int i = this.interceptorIndex; i >= 0; i--) {
									HandlerInterceptor interceptor = interceptors[i];
									try {
										interceptor.afterCompletion(request, response, this.handler, ex);
									}
									catch (Throwable ex2) {
										logger.error("HandlerInterceptor.afterCompletion threw exception", ex2);
									}
								}
							}
						}
					}
				}
			}
			 	
	

		 */
	}

}
