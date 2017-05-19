package cn.java.debug.webmvc;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;

import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.method.ControllerAdviceBean;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolverComposite;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.HandlerMethodReturnValueHandlerComposite;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndViewDefiningException;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.handler.MappedInterceptor;
import org.springframework.web.servlet.handler.AbstractHandlerMethodMapping.Match;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

public class Interceptor的识别_Handler的识别_AnnotationMapper {

	public static void main(String[] args) {
		/*
		 	org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping
		 	
		 	--------------RequestMappingHandlerMapping如何扫描Interceptors-------------------
		 	org.springframework.context.support.ApplicationObjectSupport.setApplicationContext(ApplicationContext context)
		 	{
		 		this.applicationContext = context; // === org.springframework.web.context.support.XmlWebApplicationContext
		 		initApplicationContext(context);
		 		{
		 			org.springframework.web.context.support.WebApplicationObjectSupport.initApplicationContext(ApplicationContext context)
		 			{
		 				org.springframework.context.support.ApplicationObjectSupport.initApplicationContext(ApplicationContext context)
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
											this.adaptedInterceptors.add(adaptInterceptor(interceptor));  // 注册Interceptor ----- 1
										}
									}
								}
				 			}
		 				}
		 				
		 				this.servletContext = ((WebApplicationContext) context).getServletContext();
						if (this.servletContext != null) {
							initServletContext(this.servletContext); // 空方法
						}
		 			}
		 		}
		 	}
		 	
		 	--------------RequestMappingHandlerMapping如何扫描Handler-------------------
		 	
	 		org.springframework.web.servlet.handler.AbstractHandlerMethodMapping.afterPropertiesSet()
	 		{
	 			org.springframework.web.servlet.handler.AbstractHandlerMethodMapping.initHandlerMethods()
	 			{
	 				String[] beanNames = (this.detectHandlerMethodsInAncestorContexts ?
							BeanFactoryUtils.beanNamesForTypeIncludingAncestors(getApplicationContext(), Object.class) :
							getApplicationContext().getBeanNamesForType(Object.class)); // 扫描所有bean
			
					for (String beanName : beanNames) {
						if (!beanName.startsWith(SCOPED_TARGET_NAME_PREFIX)) { // 不处理被代理的bean
							Class<?> beanType = null;
							try {
								beanType = getApplicationContext().getType(beanName);
							}
							catch (Throwable ex) {
							}
							if (beanType != null && isHandler(beanType){
									return (AnnotatedElementUtils.hasAnnotation(beanType, Controller.class) || AnnotatedElementUtils.hasAnnotation(beanType, RequestMapping.class));
							}) {
								AbstractHandlerMethodMapping.detectHandlerMethods(beanName); // !!!!
								{
									Class<?> handlerType = (handler instanceof String ?	getApplicationContext().getType((String) handler) : handler.getClass()); // 获取bean的类型
									final Class<?> userType = ClassUtils.getUserClass(handlerType);
							
									Map<Method, T> methods = MethodIntrospector.selectMethods(userType,
											new MethodIntrospector.MetadataLookup<T>() {
												@Override
												public T inspect(Method method) {
													try {
														return RequestMappingHandlerMapping.getMappingForMethod(method, userType); // 识别@RequestMapping注解，获取配置
													}
													catch (Throwable ex) {
														throw new IllegalStateException("Invalid mapping on handler class [" +
																userType.getName() + "]: " + method, ex);
													}
												}
									});
									for (Map.Entry<Method, T> entry : methods.entrySet()) {
										Method invocableMethod = AopUtils.selectInvocableMethod(entry.getKey(), userType);
										T mapping = entry.getValue(); 
										// handler === "beanName0"
										// mapping === org.springframework.web.servlet.mvc.method.RequestMappingInfo
										registerHandlerMethod(handler, invocableMethod, mapping); // !!!!
										{
											org.springframework.web.servlet.handler.AbstractHandlerMethodMapping.MappingRegistry.register(T mapping, Object handler, Method method)
											{
												// handler === "beanName0"
												// mapping === org.springframework.web.servlet.mvc.method.RequestMappingInfo  -- @RequestMapping注解信息
												HandlerMethod handlerMethod = AbstractHandlerMethodMapping.createHandlerMethod(handler, method); // 方法信息
												this.mappingLookup.put(mapping, handlerMethod);  // 注册HandlerMethod ----- 2

												List<String> directUrls = getDirectUrls(mapping); // 获取 @RequestMapping注解上配置的path，要求path不是带*或者？
												for (String url : directUrls) {
													this.urlLookup.add(url, mapping);
												}
								
												String name = null;
												if (getNamingStrategy() != null) { 
													// org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMethodMappingNamingStrategy，生成规则是："类名中的每个大写字符"+"#"+"方法名"
													name = getNamingStrategy().getName(handlerMethod, mapping);
													addMappingName(name, handlerMethod);
												}
								
												CorsConfiguration corsConfig = initCorsConfiguration(handler, method, mapping); // 跨域配置
												if (corsConfig != null) {
													this.corsLookup.put(handlerMethod, corsConfig);
												}
								
												this.registry.put(mapping, new MappingRegistration<T>(mapping, handlerMethod, directUrls, name)); // 
											}
										}
									}
								}
							}
						}
					}
	 			}
 			}
		 	
		 	--------------------RequestMappingHandlerMapping如何匹配Handler--------------------------
		 	org.springframework.web.servlet.handler.AbstractHandlerMapping.getHandler(HttpServletRequest request)
		 	{
		 		org.springframework.web.servlet.handler.AbstractHandlerMethodMapping.getHandlerInternal(HttpServletRequest request)
		 		{
		 			String lookupPath = getUrlPathHelper().getLookupPathForRequest(request); // 请求资源的地址
		 			HandlerMethod handlerMethod = AbstractHandlerMethodMapping.lookupHandlerMethod(lookupPath, request);
		 			{
			 			List<Match> matches = new ArrayList<Match>();
			 			// this.mappingRegistry === org.springframework.web.servlet.handler.AbstractHandlerMethodMapping.MappingRegistry
						List<T> directPathMatches = this.mappingRegistry.getMappingsByUrl(lookupPath); // 完整匹配
						{
							org.springframework.web.servlet.handler.AbstractHandlerMethodMapping.MappingRegistry.getMappingsByUrl(String urlPath)
							{
								return this.urlLookup.get(urlPath);
							}
						}
						if (directPathMatches != null) {
							addMatchingMappings(directPathMatches, matches, request);
						}
						if (matches.isEmpty()) {
							// No choice but to go through all mappings...
							// this.mappingRegistry.getMappings() === mappingLookup
							addMatchingMappings(this.mappingRegistry.getMappings().keySet(), matches, request); // !!! 迭代进行匹配
							{
								for (T mapping : mappings) {
									T match = getMatchingMapping(mapping, request);
									{
										org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping.getMatchingMapping(RequestMappingInfo info, HttpServletRequest request)
										{
											// info === org.springframework.web.servlet.mvc.method.RequestMappingInfo
											return info.getMatchingCondition(request);
										}
									}
									if (match != null) {
										matches.add(new Match(match, this.mappingRegistry.getMappings().get(mapping)));
									}
								}
							}
						}
						Match bestMatch = matches.get(0);
						if (matches.size() > 1) { // 多个规则命中
							if (CorsUtils.isPreFlightRequest(request)) {
								return PREFLIGHT_AMBIGUOUS_MATCH;
							}
							Match secondBestMatch = matches.get(1);
							if (comparator.compare(bestMatch, secondBestMatch) == 0) {
								Method m1 = bestMatch.handlerMethod.getMethod();
								Method m2 = secondBestMatch.handlerMethod.getMethod();
								throw new IllegalStateException("Ambiguous handler methods mapped for HTTP path '" +
										request.getRequestURL() + "': {" + m1 + ", " + m2 + "}");
							}
						}
						org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping.handleMatch(bestMatch.mapping, lookupPath, request);
						{
							request.setAttribute(BEST_MATCHING_PATTERN_ATTRIBUTE, bestPattern);
							request.setAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE, decodedUriVariables); // 地址上的变量
						}
						return bestMatch.handlerMethod;
		 			}
		 			return (handlerMethod != null ? handlerMethod.createWithResolvedBean() : null);
		 		}
		 		
		 		if (handler == null) {
					handler = getDefaultHandler(); // 使用默认的
				}
				if (handler == null) {
					return null;
				}
				// Bean name or resolved handler?
				if (handler instanceof String) {
					String handlerName = (String) handler;
					handler = getApplicationContext().getBean(handlerName);
				}
		
				HandlerExecutionChain executionChain = getHandlerExecutionChain(handler, request); // !!!!
				if (CorsUtils.isCorsRequest(request)) { // 是跨域请求
					CorsConfiguration globalConfig = this.corsConfigSource.getCorsConfiguration(request);
					CorsConfiguration handlerConfig = getCorsConfiguration(handler, request);
					CorsConfiguration config = (globalConfig != null ? globalConfig.combine(handlerConfig) : handlerConfig);
					executionChain = getCorsHandlerExecutionChain(request, executionChain, config);
				}
				return executionChain;
		 	}
		 	
			 
			--------------------执行Handler的过程--------------------------
			org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.afterPropertiesSet()
			{
				// Do this first, it may add ResponseBody advice beans
				initControllerAdviceCache();
				{
					List<ControllerAdviceBean> beans = ControllerAdviceBean.findAnnotatedBeans(getApplicationContext()); // 带有 @ControllerAdvice注解的类
					AnnotationAwareOrderComparator.sort(beans);
			
					List<Object> requestResponseBodyAdviceBeans = new ArrayList<Object>();
			
					for (ControllerAdviceBean bean : beans) {
						Set<Method> attrMethods = MethodIntrospector.selectMethods(bean.getBeanType(), MODEL_ATTRIBUTE_METHODS); // @ModelAttribute注解的方法
						if (!attrMethods.isEmpty()) {
							this.modelAttributeAdviceCache.put(bean, attrMethods);
						}
						Set<Method> binderMethods = MethodIntrospector.selectMethods(bean.getBeanType(), INIT_BINDER_METHODS);// @InitBinder注解的方法
						if (!binderMethods.isEmpty()) {
							this.initBinderAdviceCache.put(bean, binderMethods);
						}
						if (RequestBodyAdvice.class.isAssignableFrom(bean.getBeanType())) {
							requestResponseBodyAdviceBeans.add(bean);
						}
						if (ResponseBodyAdvice.class.isAssignableFrom(bean.getBeanType())) {
							requestResponseBodyAdviceBeans.add(bean);
						}
					}
			
					if (!requestResponseBodyAdviceBeans.isEmpty()) {
						this.requestResponseBodyAdvice.addAll(0, requestResponseBodyAdviceBeans);
					}
				}
		
				if (this.argumentResolvers == null) {
					List<HandlerMethodArgumentResolver> resolvers = getDefaultArgumentResolvers(); // “参数”解析器!!!
					this.argumentResolvers = new HandlerMethodArgumentResolverComposite().addResolvers(resolvers);
				}
				if (this.initBinderArgumentResolvers == null) {
					List<HandlerMethodArgumentResolver> resolvers = getDefaultInitBinderArgumentResolvers(); // “初始化参数绑定”解析器!!!
					this.initBinderArgumentResolvers = new HandlerMethodArgumentResolverComposite().addResolvers(resolvers);
				}
				if (this.returnValueHandlers == null) {
					List<HandlerMethodReturnValueHandler> handlers = getDefaultReturnValueHandlers(); // “返回值”处理器!!!
					this.returnValueHandlers = new HandlerMethodReturnValueHandlerComposite().addHandlers(handlers);
				}
			}
			
			org.springframework.web.servlet.DispatcherServlet.doDispatch(HttpServletRequest request, HttpServletResponse response)
			{
				// 如果不是文件上传，processedRequest就是request本身
				// Determine handler for the current request.
				mappedHandler = getHandler(processedRequest); // 决策一个适合processedRequest的Handler，执行链管理器HandlerExecutionChain
				if (mappedHandler == null || mappedHandler.getHandler() == null) {
					noHandlerFound(processedRequest, response);
					return;
				}
				
				// ha === org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter
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
				mv = org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.handle(processedRequest, response, mappedHandler.getHandler()); // 执行handler的方法，返回值	
				{
				
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
			
			-----------------“参数”的识别--------------------------------
			org.springframework.web.method.support.InvocableHandlerMethod.getMethodArgumentValues(...)
			{
			
			}
			
			----------------“返回值”的处理---------------------
		 	org.springframework.web.method.support.HandlerMethodReturnValueHandlerComposite.handleReturnValue(...)
		 	{
		 	
		 	}
	

		 */
	}

}
