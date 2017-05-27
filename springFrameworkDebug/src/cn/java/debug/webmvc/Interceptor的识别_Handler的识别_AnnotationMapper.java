package cn.java.debug.webmvc;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.SmartValidator;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.HttpSessionRequiredException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.bind.support.WebRequestDataBinder;
import org.springframework.web.bind.support.WebRequestDataBinder.Servlet3MultipartHelper;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;
import org.springframework.web.context.request.async.AsyncWebRequest;
import org.springframework.web.context.request.async.WebAsyncManager;
import org.springframework.web.context.request.async.WebAsyncUtils;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.method.ControllerAdviceBean;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.annotation.ErrorsMethodArgumentResolver;
import org.springframework.web.method.annotation.ExpressionValueMethodArgumentResolver;
import org.springframework.web.method.annotation.MapMethodProcessor;
import org.springframework.web.method.annotation.ModelAttributeMethodProcessor;
import org.springframework.web.method.annotation.ModelFactory;
import org.springframework.web.method.annotation.ModelMethodProcessor;
import org.springframework.web.method.annotation.RequestHeaderMapMethodArgumentResolver;
import org.springframework.web.method.annotation.RequestHeaderMethodArgumentResolver;
import org.springframework.web.method.annotation.RequestParamMapMethodArgumentResolver;
import org.springframework.web.method.annotation.RequestParamMethodArgumentResolver;
import org.springframework.web.method.annotation.SessionStatusMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolverComposite;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.HandlerMethodReturnValueHandlerComposite;
import org.springframework.web.method.support.InvocableHandlerMethod;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.handler.MappedInterceptor;
import org.springframework.web.servlet.handler.WebRequestHandlerInterceptorAdapter;
import org.springframework.web.servlet.handler.AbstractHandlerMethodMapping.Match;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.AsyncTaskMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.CallableMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.DeferredResultMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.HttpEntityMethodProcessor;
import org.springframework.web.servlet.mvc.method.annotation.HttpHeadersReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.MatrixVariableMapMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.MatrixVariableMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.ModelAndViewMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.ModelAndViewResolverMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.PathVariableMapMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.PathVariableMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.RedirectAttributesMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestAttributeMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestPartMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitterReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.ServletCookieValueMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.ServletModelAttributeMethodProcessor;
import org.springframework.web.servlet.mvc.method.annotation.ServletRequestMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.ServletResponseMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.SessionAttributeMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBodyReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.UriComponentsBuilderMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.ViewMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.ViewNameMethodReturnValueHandler;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.AbstractUrlBasedView;
import org.springframework.web.servlet.view.InternalResourceView;
import org.springframework.web.servlet.view.JstlView;

public class Interceptor的识别_Handler的识别_AnnotationMapper {

	public static void main(String[] args) {
		/*
		 	org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping
		 	
		 	--------------RequestMappingHandlerMapping如何感知Interceptors-------------------
		 	// 1.通过xml声明注入的拦截器
		 	org.springframework.web.servlet.handler.AbstractHandlerMapping.setInterceptors(Object... interceptors)
		 	{
		 		this.interceptors.addAll(Arrays.asList(interceptors));
		 	}
		 	
		 	// 2.自动扫描拦截器
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
											this.adaptedInterceptors.add(adaptInterceptor(interceptor){ // 适配拦截器 ，注册Interceptor ----- 1
												if (interceptor instanceof HandlerInterceptor) {
													return (HandlerInterceptor) interceptor;
												}
												else if (interceptor instanceof WebRequestInterceptor) {
													return new WebRequestHandlerInterceptorAdapter((WebRequestInterceptor) interceptor);
												}
												else {
													throw new IllegalArgumentException("Interceptor type not supported: " + interceptor.getClass().getName());
												}
											});  
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
							if (beanType != null && isHandler(beanType){ // 含有@Controller注解或者@RequestMapping注解
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
														{
															RequestMappingInfo info = RequestMappingHandlerMapping.createRequestMappingInfo(method); // 获取方法的@RequestMapping注解 
															{
																RequestMapping requestMapping = AnnotatedElementUtils.findMergedAnnotation(element, RequestMapping.class);
																RequestCondition<?> condition = (element instanceof Class ?
																		getCustomTypeCondition((Class<?>) element) : getCustomMethodCondition((Method) element));
																return (requestMapping != null ? RequestMappingHandlerMapping.createRequestMappingInfo(requestMapping, condition) : null);
																{
																	return RequestMappingInfo
																				.paths(resolveEmbeddedValuesInPatterns(requestMapping.path()))
																				.methods(requestMapping.method())
																				.params(requestMapping.params())
																				.headers(requestMapping.headers())
																				.consumes(requestMapping.consumes())
																				.produces(requestMapping.produces())
																				.mappingName(requestMapping.name())
																				.customCondition(customCondition)
																				.options(this.config)
																				.build();
																}
															}
															if (info != null) {
																RequestMappingInfo typeInfo = RequestMappingHandlerMapping.createRequestMappingInfo(handlerType);// 获取类的@RequestMapping注解 
																{
																	RequestMapping requestMapping = AnnotatedElementUtils.findMergedAnnotation(element, RequestMapping.class);
																	RequestCondition<?> condition = (element instanceof Class ?
																			getCustomTypeCondition((Class<?>) element) : getCustomMethodCondition((Method) element));
																	return (requestMapping != null ? RequestMappingHandlerMapping.createRequestMappingInfo(requestMapping, condition) : null);
																	{
																		return RequestMappingInfo
																					.paths(resolveEmbeddedValuesInPatterns(requestMapping.path()))
																					.methods(requestMapping.method())
																					.params(requestMapping.params())
																					.headers(requestMapping.headers())
																					.consumes(requestMapping.consumes())
																					.produces(requestMapping.produces())
																					.mappingName(requestMapping.name())
																					.customCondition(customCondition)
																					.options(this.config)
																					.build();
																	}
																}
																if (typeInfo != null) {
																	info = typeInfo.combine(info); // 合并方法上和类上的 @RequestMapping注解信息
																}
															}
															return info;
														}
													}
													catch (Throwable ex) {
													}
												}
									});
									for (Map.Entry<Method, T> entry : methods.entrySet()) {
										Method invocableMethod = AopUtils.selectInvocableMethod(entry.getKey(), userType); // 获取可调用方法，可能是本身
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
												{
													HandlerMethod handlerMethod;
													if (handler instanceof String) {
														String beanName = (String) handler;
														// getApplicationContext() === org.springframework.web.context.support.XmlWebApplicationContext
														// getApplicationContext().getAutowireCapableBeanFactory()  === org.springframework.beans.factory.support.DefaultListableBeanFactory
														handlerMethod = new HandlerMethod(beanName,getApplicationContext().getAutowireCapableBeanFactory(), method); // !!!
													}
													else {
														handlerMethod = new HandlerMethod(handler, method); // 使用HandlerMethod对象来描述调用者信息 ----- !!!
													}
													return handlerMethod;
												}
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
					org.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter.handle(HttpServletRequest request, HttpServletResponse response, Object handler) 
					{
						org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.handleInternal(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod)
						{
							ModelAndView mav;
							checkRequest(request); // 检查提交的方式和Session是否提供
							
							mav = invokeHandlerMethod(request, response, handlerMethod); // !!!! mav === org.springframework.web.servlet.ModelAndView
							{
								org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.invokeHandlerMethod(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod)
								{
									ServletWebRequest webRequest = new ServletWebRequest(request, response);
									try {
										WebDataBinderFactory binderFactory = getDataBinderFactory(handlerMethod); // !!! 归类带@InitBinder注解的方法
										ModelFactory modelFactory = getModelFactory(handlerMethod, binderFactory); // !!! 归类带@ModelAttribute注解的方法
							
										ServletInvocableHandlerMethod invocableMethod = createInvocableHandlerMethod(handlerMethod);
										invocableMethod.setHandlerMethodArgumentResolvers(this.argumentResolvers); // 参数解析器
										invocableMethod.setHandlerMethodReturnValueHandlers(this.returnValueHandlers); // 返回值处理器
										invocableMethod.setDataBinderFactory(binderFactory);  // 初始化处理器
										invocableMethod.setParameterNameDiscoverer(this.parameterNameDiscoverer); // 参数名识别器
							
										ModelAndViewContainer mavContainer = new ModelAndViewContainer();
										mavContainer.addAllAttributes(RequestContextUtils.getInputFlashMap(request));
										// modelFactory === org.springframework.web.method.annotation.ModelFactory
										modelFactory.initModel(webRequest, mavContainer, invocableMethod); // 迭代调用带@ModelAttribute注解的方法，返回值会以“返回值类型短名”作为键，放入mavContainer
										{
											// sessionAttributesHandler ==== org.springframework.web.method.annotation.SessionAttributesHandler 
											// handlerMethod === org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod
											
											Map<String, ?> sessionAttributes = this.sessionAttributesHandler.retrieveAttributes(request); // 从session中获取值
											container.mergeAttributes(sessionAttributes); // 合并值
											invokeModelAttributeMethods(request, container); // 迭代调用带@ModelAttribute注解的方法
											{
												org.springframework.web.method.annotation.ModelFactory.invokeModelAttributeMethods(NativeWebRequest request, ModelAndViewContainer container) 
												{
													while (!this.modelMethods.isEmpty()) {  // 迭代调用
														// getNextModelMethod(container) === org.springframework.web.method.annotation.ModelFactory.ModelMethod
														InvocableHandlerMethod modelMethod = getNextModelMethod(container).getHandlerMethod(); // 获取下一个带@ModelAttribute注解的方法
														// modelMethod === org.springframework.web.method.support.InvocableHandlerMethod
														ModelAttribute ann = modelMethod.getMethodAnnotation(ModelAttribute.class); // 获取方法上的@ModelAttribute注解
														if (container.containsAttribute(ann.name())) {
															if (!ann.binding()) {
																container.setBindingDisabled(ann.name());
															}
															continue;
														}
											
														Object returnValue = modelMethod.invokeForRequest(request, container); // !!!!调用带@ModelAttribute注解的方法
														{
															Object[] args = getMethodArgumentValues(request, mavContainer, providedArgs); // 参数值
															{
																MethodParameter[] parameters = getMethodParameters(); // 目标方法的参数
																Object[] args = new Object[parameters.length];
																for (int i = 0; i < parameters.length; i++) {
																	MethodParameter parameter = parameters[i];
																	// parameterNameDiscoverer === org.springframework.core.DefaultParameterNameDiscoverer
																	parameter.initParameterNameDiscovery(this.parameterNameDiscoverer); 
																	args[i] = resolveProvidedArgument(parameter, providedArgs);
																	if (args[i] != null) {
																		continue;
																	}
																	// argumentResolvers === org.springframework.web.method.support.HandlerMethodArgumentResolverComposite
																	if (this.argumentResolvers.supportsParameter(parameter)) { // 支持此种类型的参数
																		try {
																			args[i] = this.argumentResolvers.resolveArgument(parameter, mavContainer, request, this.dataBinderFactory); // 获取指定类型的参数的值 --- 
																			{
																				// ....
																			}
																			continue;
																		}
																	}
																}
																return args;
															}
															Object returnValue = doInvoke(args); // 传递参数，调用方法 ---- 调用“带@ModelAttribute注解的方法” ---- 1
															{
																ReflectionUtils.makeAccessible(getBridgedMethod());
																try {
																	return getBridgedMethod().invoke(getBean(), args); // 调用方法
																}
															}
															return returnValue;
														}
														
														if (!modelMethod.isVoid()){ // 返回值不是void
															String returnValueName = getNameForReturnValue(returnValue, modelMethod.getReturnType()); // 返回值的类型的“短名”作为键名
															if (!ann.binding()) {
																container.setBindingDisabled(returnValueName);
															}
															if (!container.containsAttribute(returnValueName)) {
																container.addAttribute(returnValueName, returnValue); // 把返回值放入container,返回值的类型的“短名”作为键名
															}
														}
													}
												}
											}
									
											for (String name : findSessionAttributeArguments(handlerMethod)) {
												if (!container.containsAttribute(name)) {
													Object value = this.sessionAttributesHandler.retrieveAttribute(request, name); // 从session中获取值
													if (value == null) {
														throw new HttpSessionRequiredException("Expected session attribute '" + name + "'", name);
													}
													container.addAttribute(name, value);
												}
											}
										}
										
										mavContainer.setIgnoreDefaultModelOnRedirect(this.ignoreDefaultModelOnRedirect);
							
										AsyncWebRequest asyncWebRequest = WebAsyncUtils.createAsyncWebRequest(request, response);
										asyncWebRequest.setTimeout(this.asyncRequestTimeout);
							
										WebAsyncManager asyncManager = WebAsyncUtils.getAsyncManager(request);
										asyncManager.setTaskExecutor(this.taskExecutor);
										asyncManager.setAsyncWebRequest(asyncWebRequest);
										asyncManager.registerCallableInterceptors(this.callableInterceptors);
										asyncManager.registerDeferredResultInterceptors(this.deferredResultInterceptors);
							
										if (asyncManager.hasConcurrentResult()) {
											Object result = asyncManager.getConcurrentResult();
											mavContainer = (ModelAndViewContainer) asyncManager.getConcurrentResultContext()[0];
											asyncManager.clearConcurrentResult();
											invocableMethod = invocableMethod.wrapConcurrentResult(result);
										}
							
										// invocableMethod === org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod
										invocableMethod.invokeAndHandle(webRequest, mavContainer); // !!!调用业务方法，让“返回值处理器”处理后，把值放入mavContainer
										{
											Object returnValue = invokeForRequest(webRequest, mavContainer, providedArgs); // 调用方法
											{
												org.springframework.web.method.support.InvocableHandlerMethod.invokeForRequest(NativeWebRequest request, ModelAndViewContainer mavContainer, Object... providedArgs)
												{
													Object[] args = getMethodArgumentValues(request, mavContainer, providedArgs); // 参数值
													{
														MethodParameter[] parameters = getMethodParameters(); // 目标方法的参数
														Object[] args = new Object[parameters.length];
														for (int i = 0; i < parameters.length; i++) {
															MethodParameter parameter = parameters[i];
															// parameterNameDiscoverer === org.springframework.core.DefaultParameterNameDiscoverer
															parameter.initParameterNameDiscovery(this.parameterNameDiscoverer); 
															args[i] = resolveProvidedArgument(parameter, providedArgs);
															if (args[i] != null) {
																continue;
															}
															// argumentResolvers === org.springframework.web.method.support.HandlerMethodArgumentResolverComposite
															if (this.argumentResolvers.supportsParameter(parameter)) { // 支持此种类型的参数
																try {
																	// 获取指定类型的参数的值 ---- 可能调用“带@InitBinder注解的方法”
																	args[i] = this.argumentResolvers.resolveArgument(parameter, mavContainer, request, this.dataBinderFactory); 
																	{
																		// 情况1，不调用“带@InitBinder注解的方法”， 如： resolver === org.springframework.web.servlet.mvc.method.annotation.ServletRequestMethodArgumentResolver
																		{
																			HandlerMethodArgumentResolver resolver = HandlerMethodArgumentResolverComposite.getArgumentResolver(parameter); // 获取参数解析器
																			return resolver.resolveArgument(parameter, mavContainer, webRequest, binderFactory); // 获取参数值
																		}
																		// 情况2，调用“带@InitBinder注解的方法”， 如： resolver === org.springframework.web.servlet.mvc.method.annotation.ServletModelAttributeMethodProcessor
																		{
																			org.springframework.web.method.annotation.ModelAttributeMethodProcessor.resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory)
																			{
																				//		webRequest === org.springframework.web.context.request.ServletWebRequest
																				String name = ModelFactory.getNameForParameter(parameter); // 变量名
																				Object attribute = (mavContainer.containsAttribute(name) ? mavContainer.getModel().get(name) :
																						createAttribute(name, parameter, binderFactory, webRequest)); // 创建参数类型的对象 cn.java.demo.webmvc.form.UserLoginForm
																				{
																					return BeanUtils.instantiateClass(methodParam.getParameterType());
																				}
																		
																				if (!mavContainer.isBindingDisabled(name)) {
																					ModelAttribute ann = parameter.getParameterAnnotation(ModelAttribute.class);
																					if (ann != null && !ann.binding()) {
																						mavContainer.setBindingDisabled(name);
																					}
																				}
																				// binderFactory == org.springframework.web.servlet.mvc.method.annotation.ServletRequestDataBinderFactory
																				WebDataBinder binder = binderFactory.createBinder(webRequest, attribute, name); // 
																				{
																					org.springframework.web.bind.support.DefaultDataBinderFactory.createBinder(NativeWebRequest webRequest, Object target, String objectName)
																					{
																						// target === cn.java.demo.webmvc.form.UserLoginForm
																						WebDataBinder dataBinder = DefaultDataBinderFactory.createBinderInstance(target, objectName, webRequest);
																						{
																							return new WebRequestDataBinder(target, objectName);
																						}
																						if (this.initializer != null) {
																							this.initializer.initBinder(dataBinder, webRequest);
																						}
																						initBinder(dataBinder, webRequest);//!!!!
																						{
																							org.springframework.web.method.annotation.InitBinderDataBinderFactory.initBinder(WebDataBinder binder, NativeWebRequest request)
																							{
																								for (InvocableHandlerMethod binderMethod : this.binderMethods) { // ---- 迭代调用“带@InitBinder注解的方法” ---- 2
																									if (isBinderMethodApplicable(binderMethod, binder){
																										InitBinder annot = initBinderMethod.getMethodAnnotation(InitBinder.class); // 有@InitBinder注解
																										Collection<String> names = Arrays.asList(annot.value());
																										return (names.size() == 0 || names.contains(binder.getObjectName()));
																									}) { // !!!!
																										Object returnValue = binderMethod.invokeForRequest(request, null, binder); // !!!
																										if (returnValue != null) { //不能有返回值
																											throw new IllegalStateException("@InitBinder methods should return void: " + binderMethod);
																										}
																									}
																								}
																							}
																						}
																						return dataBinder;
																					}
																				}
																				
																				if (binder.getTarget() != null) {
																					if (!mavContainer.isBindingDisabled(name)) {
																						ModelAttributeMethodProcessor.bindRequestParameters(binder, webRequest); // 把webRequest.getNativeRequest(ServletRequest.class)的值设置到target
																						{
																							((WebRequestDataBinder) binder).bind(request);
																							{
																								org.springframework.web.bind.support.WebRequestDataBinder.bind(WebRequest request)
																								{
																									MutablePropertyValues mpvs = new MutablePropertyValues(request.getParameterMap());
																									if (isMultipartRequest(request) && request instanceof NativeWebRequest) {
																										MultipartRequest multipartRequest = ((NativeWebRequest) request).getNativeRequest(MultipartRequest.class);
																										if (multipartRequest != null) {
																											bindMultipart(multipartRequest.getMultiFileMap(), mpvs);
																										}
																										else if (servlet3Parts) {
																											HttpServletRequest serlvetRequest = ((NativeWebRequest) request).getNativeRequest(HttpServletRequest.class);
																											new Servlet3MultipartHelper(isBindEmptyMultipartFiles()).bindParts(serlvetRequest, mpvs);
																										}
																									}
																									WebRequestDataBinder.doBind(mpvs);
																									{
																										org.springframework.web.bind.WebDataBinder.doBind(MutablePropertyValues mpvs)
																										{
																											WebDataBinder.checkFieldDefaults(mpvs);
																											WebDataBinder.checkFieldMarkers(mpvs);
																											super.doBind(mpvs);
																											{
																												org.springframework.validation.DataBinder.doBind(MutablePropertyValues mpvs)
																												{
																													DataBinder.checkAllowedFields(mpvs);
																													DataBinder.checkRequiredFields(mpvs);
																													DataBinder.applyPropertyValues(mpvs); // 把mpvs的属性值设置到beanWrapper
																													{
																														// Bind request parameters onto target object.
																														getPropertyAccessor().setPropertyValues(mpvs, isIgnoreUnknownFields(), isIgnoreInvalidFields()); // 把mpvs的属性值设置到beanWrapper
																													}
																												}
																											}
																										}
																									}
																								}
																							}
																						}
																					}
																					validateIfApplicable(binder, parameter); // 校验
																					{
																						Annotation[] annotations = methodParam.getParameterAnnotations();
																						for (Annotation ann : annotations) {
																							Validated validatedAnn = AnnotationUtils.getAnnotation(ann, Validated.class); // 注解类上的注解
																							if (validatedAnn != null || ann.annotationType().getSimpleName().startsWith("Valid")) {
																								Object hints = (validatedAnn != null ? validatedAnn.value() : AnnotationUtils.getValue(ann));
																								Object[] validationHints = (hints instanceof Object[] ? (Object[]) hints : new Object[] {hints});
																								// binder === org.springframework.web.bind.support.WebRequestDataBinder
																								binder.validate(validationHints);
																								{
																									org.springframework.validation.DataBinder.validate(Object... validationHints)
																									{
																										for (Validator validator : getValidators()) {
																											if (!ObjectUtils.isEmpty(validationHints) && validator instanceof SmartValidator) {
																												((SmartValidator) validator).validate(getTarget(), getBindingResult(), validationHints); // 迭代调用在“有@InitBinder注解”方法中注入的校验器
																											}
																											else if (validator != null) {
																												validator.validate(getTarget(), getBindingResult());
																											}
																										}
																									}
																								}
																								break;
																							}
																						}
																					}
																					if (binder.getBindingResult().hasErrors() && isBindExceptionRequired(binder, parameter){
																						int i = methodParam.getParameterIndex();
																						Class<?>[] paramTypes = methodParam.getMethod().getParameterTypes(); // 参数列表
																						boolean hasBindingResult = (paramTypes.length > (i + 1) && Errors.class.isAssignableFrom(paramTypes[i + 1])); // 绑定对象的“后一个”参数是“存放绑定结果”的对象
																						return !hasBindingResult;
																					}) { // 如果校验不通过
																						throw new BindException(binder.getBindingResult());
																					}
																				}
																		
																				// Add resolved attribute and BindingResult at the end of the model
																				// binder === org.springframework.web.bind.support.WebRequestDataBinder
																				// binder.getBindingResult() === org.springframework.validation.BeanPropertyBindingResult
																				Map<String, Object> bindingResultModel = binder.getBindingResult().getModel(); // model.put(attribute,webRequest);  // model.put("org.springframework.validation.BindingResult.attribute",org.springframework.validation.BeanPropertyBindingResult对象)
																				mavContainer.removeAttributes(bindingResultModel);
																				mavContainer.addAllAttributes(bindingResultModel); // 绑定结果放入mavContainer
																		
																				return binder.convertIfNecessary(binder.getTarget(), parameter.getParameterType(), parameter); // 返回值是binder.getTarget()
																			}
																		}
																	}
																	continue;
																}
															}
														}
														return args;
													}
													Object returnValue = doInvoke(args); // 传递参数，调用方法  --- 调用“业务方法”
													{
														ReflectionUtils.makeAccessible(getBridgedMethod());
														try {
															return getBridgedMethod().invoke(getBean(), args); // 调用方法
														}
													}
													return returnValue;
												}
											}
											setResponseStatus(webRequest); // 发送响应状态
									
											if (returnValue == null) { // 没有返回值
												if (isRequestNotModified(webRequest) || hasResponseStatus() || mavContainer.isRequestHandled()) {
													mavContainer.setRequestHandled(true);
													return;
												}
											}
											else if (StringUtils.hasText(this.responseReason)) {
												mavContainer.setRequestHandled(true);
												return;
											}
									
											mavContainer.setRequestHandled(false);
											try {
												// returnValueHandlers === org.springframework.web.method.support.HandlerMethodReturnValueHandlerComposite
												this.returnValueHandlers.handleReturnValue(
														returnValue, getReturnValueType(returnValue), mavContainer, webRequest); // 处理返回值
											}
										}
										
										if (asyncManager.isConcurrentHandlingStarted()) {
											return null;
										}
							
										return getModelAndView(mavContainer, modelFactory, webRequest); // 处理返回的 org.springframework.web.servlet.ModelAndView
									}
									finally {
										webRequest.requestCompleted();
									}
								}
							}
							
							if (!response.containsHeader(HEADER_CACHE_CONTROL)) { // 缓存控制
								if (getSessionAttributesHandler(handlerMethod).hasSessionAttributes()) { // 有使用@SessionAttributes注解
									applyCacheSeconds(response, this.cacheSecondsForSessionAttributeHandlers); // 发送控制“浏览器缓存”的Header
								}
								else {
									prepareResponse(response); // 发送控制“浏览器缓存”的Header
								}
							}
		
							return mav;
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
			
			-----------------“参数”的识别--------------------------------
			默认“带@ModelAttribute注解或者带@RequestMapping注解的方法”参数解析器：
				org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.getDefaultArgumentResolvers()
				{
					List<HandlerMethodArgumentResolver> resolvers = new ArrayList<HandlerMethodArgumentResolver>();

					// Annotation-based argument resolution
					resolvers.add(new RequestParamMethodArgumentResolver(getBeanFactory(), false));
					resolvers.add(new RequestParamMapMethodArgumentResolver());
					resolvers.add(new PathVariableMethodArgumentResolver());
					resolvers.add(new PathVariableMapMethodArgumentResolver());
					resolvers.add(new MatrixVariableMethodArgumentResolver());
					resolvers.add(new MatrixVariableMapMethodArgumentResolver());
					resolvers.add(new ServletModelAttributeMethodProcessor(false)); // !!! 需要@ModelAttribute注解
					resolvers.add(new RequestResponseBodyMethodProcessor(getMessageConverters(), this.requestResponseBodyAdvice)); // !!! 读取http中body的数据，要首先配置转换器
					resolvers.add(new RequestPartMethodArgumentResolver(getMessageConverters(), this.requestResponseBodyAdvice));
					resolvers.add(new RequestHeaderMethodArgumentResolver(getBeanFactory()));
					resolvers.add(new RequestHeaderMapMethodArgumentResolver());
					resolvers.add(new ServletCookieValueMethodArgumentResolver(getBeanFactory()));
					resolvers.add(new ExpressionValueMethodArgumentResolver(getBeanFactory()));
					resolvers.add(new SessionAttributeMethodArgumentResolver());
					resolvers.add(new RequestAttributeMethodArgumentResolver());
			
					// Type-based argument resolution
					resolvers.add(new ServletRequestMethodArgumentResolver());
					resolvers.add(new ServletResponseMethodArgumentResolver());
					resolvers.add(new HttpEntityMethodProcessor(getMessageConverters(), this.requestResponseBodyAdvice));
					resolvers.add(new RedirectAttributesMethodArgumentResolver());
					resolvers.add(new ModelMethodProcessor());
					resolvers.add(new MapMethodProcessor());
					resolvers.add(new ErrorsMethodArgumentResolver());
					resolvers.add(new SessionStatusMethodArgumentResolver());
					resolvers.add(new UriComponentsBuilderMethodArgumentResolver());
			
					// Custom arguments
					if (getCustomArgumentResolvers() != null) {
						resolvers.addAll(getCustomArgumentResolvers());
					}
			
					// Catch-all
					resolvers.add(new RequestParamMethodArgumentResolver(getBeanFactory(), true));
					resolvers.add(new ServletModelAttributeMethodProcessor(true)); // !!!! 不需要@ModelAttribute注解
			
					return resolvers;
				}
			
			默认“带@InitBinder注解的方法”参数解析器：
				org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.getDefaultInitBinderArgumentResolvers()
				{
					List<HandlerMethodArgumentResolver> resolvers = new ArrayList<HandlerMethodArgumentResolver>();

					// Annotation-based argument resolution
					resolvers.add(new RequestParamMethodArgumentResolver(getBeanFactory(), false));
					resolvers.add(new RequestParamMapMethodArgumentResolver());
					resolvers.add(new PathVariableMethodArgumentResolver());
					resolvers.add(new PathVariableMapMethodArgumentResolver());
					resolvers.add(new MatrixVariableMethodArgumentResolver());
					resolvers.add(new MatrixVariableMapMethodArgumentResolver());
					resolvers.add(new ExpressionValueMethodArgumentResolver(getBeanFactory()));
					resolvers.add(new SessionAttributeMethodArgumentResolver());
					resolvers.add(new RequestAttributeMethodArgumentResolver());
			
					// Type-based argument resolution
					resolvers.add(new ServletRequestMethodArgumentResolver());
					resolvers.add(new ServletResponseMethodArgumentResolver());
			
					// Custom arguments
					if (getCustomArgumentResolvers() != null) {
						resolvers.addAll(getCustomArgumentResolvers());
					}
			
					// Catch-all
					resolvers.add(new RequestParamMethodArgumentResolver(getBeanFactory(), true));
			
					return resolvers;
				}
			------
			使用参数解析器：
				org.springframework.web.method.support.InvocableHandlerMethod.getMethodArgumentValues(...)
			
			----------------“返回值”的处理---------------------
			默认“返回值”解析器：
				org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.getDefaultReturnValueHandlers()
				{
					List<HandlerMethodReturnValueHandler> handlers = new ArrayList<HandlerMethodReturnValueHandler>();

					// Single-purpose return value types
					handlers.add(new ModelAndViewMethodReturnValueHandler());
					handlers.add(new ModelMethodProcessor());
					handlers.add(new ViewMethodReturnValueHandler());
					handlers.add(new ResponseBodyEmitterReturnValueHandler(getMessageConverters()));
					handlers.add(new StreamingResponseBodyReturnValueHandler());
					handlers.add(new HttpEntityMethodProcessor(getMessageConverters(),
							this.contentNegotiationManager, this.requestResponseBodyAdvice));
					handlers.add(new HttpHeadersReturnValueHandler());
					handlers.add(new CallableMethodReturnValueHandler());
					handlers.add(new DeferredResultMethodReturnValueHandler());
					handlers.add(new AsyncTaskMethodReturnValueHandler(this.beanFactory));
			
					// Annotation-based return value types
					handlers.add(new ModelAttributeMethodProcessor(false));
					handlers.add(new RequestResponseBodyMethodProcessor(getMessageConverters(),
							this.contentNegotiationManager, this.requestResponseBodyAdvice));
			
					// Multi-purpose return value types
					handlers.add(new ViewNameMethodReturnValueHandler());
					handlers.add(new MapMethodProcessor());
			
					// Custom return value types
					if (getCustomReturnValueHandlers() != null) {
						handlers.addAll(getCustomReturnValueHandlers());
					}
			
					// Catch-all
					if (!CollectionUtils.isEmpty(getModelAndViewResolvers())) {
						handlers.add(new ModelAndViewResolverMethodReturnValueHandler(getModelAndViewResolvers()));
					}
					else {
						handlers.add(new ModelAttributeMethodProcessor(true));
					}
			
					return handlers;
				}
			------
			使用“返回值”解析器：
		 	org.springframework.web.method.support.HandlerMethodReturnValueHandlerComposite.handleReturnValue(...)
	
		 */
	}

}
