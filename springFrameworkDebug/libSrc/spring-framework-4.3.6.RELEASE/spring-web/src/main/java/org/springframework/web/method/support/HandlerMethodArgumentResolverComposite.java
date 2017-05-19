/*
 * Copyright 2002-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.web.method.support;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;

/**
 * Resolves method parameters by delegating to a list of registered {@link HandlerMethodArgumentResolver}s.
 * Previously resolved method parameters are cached for faster lookups.
 *
 * @author Rossen Stoyanchev
 * @author Juergen Hoeller
 * @since 3.1
 */
public class HandlerMethodArgumentResolverComposite implements HandlerMethodArgumentResolver {

	protected final Log logger = LogFactory.getLog(getClass());

	private final List<HandlerMethodArgumentResolver> argumentResolvers =
			new LinkedList<HandlerMethodArgumentResolver>();

	private final Map<MethodParameter, HandlerMethodArgumentResolver> argumentResolverCache =
			new ConcurrentHashMap<MethodParameter, HandlerMethodArgumentResolver>(256);


	/**
	 * Add the given {@link HandlerMethodArgumentResolver}.
	 */
	public HandlerMethodArgumentResolverComposite addResolver(HandlerMethodArgumentResolver resolver) {
		this.argumentResolvers.add(resolver);
		return this;
	}

	/**
	 * Add the given {@link HandlerMethodArgumentResolver}s.
	 * @since 4.3
	 */
	public HandlerMethodArgumentResolverComposite addResolvers(HandlerMethodArgumentResolver... resolvers) {
		if (resolvers != null) {
			for (HandlerMethodArgumentResolver resolver : resolvers) {
				this.argumentResolvers.add(resolver);
			}
		}
		return this;
	}

	/**
	 * Add the given {@link HandlerMethodArgumentResolver}s.
	 */
	public HandlerMethodArgumentResolverComposite addResolvers(List<? extends HandlerMethodArgumentResolver> resolvers) {
		if (resolvers != null) {
			for (HandlerMethodArgumentResolver resolver : resolvers) {
				this.argumentResolvers.add(resolver);
			}
		}
		return this;
	}

	/**
	 * Return a read-only list with the contained resolvers, or an empty list.
	 */
	public List<HandlerMethodArgumentResolver> getResolvers() {
		return Collections.unmodifiableList(this.argumentResolvers);
	}

	/**
	 * Clear the list of configured resolvers.
	 * @since 4.3
	 */
	public void clear() {
		this.argumentResolvers.clear();
	}


	/**
	 * Whether the given {@linkplain MethodParameter method parameter} is supported by any registered
	 * {@link HandlerMethodArgumentResolver}.
	 */
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return (getArgumentResolver(parameter) != null);
	}

	/**
	 * Iterate over registered {@link HandlerMethodArgumentResolver}s and invoke the one that supports it.
	 * @throws IllegalStateException if no suitable {@link HandlerMethodArgumentResolver} is found.
	 */
	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

		HandlerMethodArgumentResolver resolver = getArgumentResolver(parameter); // 获取参数解析器
		if (resolver == null) {
			throw new IllegalArgumentException("Unknown parameter type [" + parameter.getParameterType().getName() + "]");
		}
		// 如： resolver === org.springframework.web.servlet.mvc.method.annotation.ServletRequestMethodArgumentResolver
		return resolver.resolveArgument(parameter, mavContainer, webRequest, binderFactory); // 获取参数值
	}

	/**
	 * Find a registered {@link HandlerMethodArgumentResolver} that supports the given method parameter.
	 */
	private HandlerMethodArgumentResolver getArgumentResolver(MethodParameter parameter) {
		HandlerMethodArgumentResolver result = this.argumentResolverCache.get(parameter);
		if (result == null) {
			/*
			 	----------有@InitBinder注解的方法的参数的识别------------
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
                
                ----------------业务方法的参数的识别-------------------------
                 // Annotation-based argument resolution
                resolvers.add(new RequestParamMethodArgumentResolver(getBeanFactory(), false));
                resolvers.add(new RequestParamMapMethodArgumentResolver());
                resolvers.add(new PathVariableMethodArgumentResolver());
                resolvers.add(new PathVariableMapMethodArgumentResolver());
                resolvers.add(new MatrixVariableMethodArgumentResolver());
                resolvers.add(new MatrixVariableMapMethodArgumentResolver());
                resolvers.add(new ServletModelAttributeMethodProcessor(false));
                resolvers.add(new RequestResponseBodyMethodProcessor(getMessageConverters(), this.requestResponseBodyAdvice));
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
                resolvers.add(new ServletModelAttributeMethodProcessor(true));

			 */
			for (HandlerMethodArgumentResolver methodArgumentResolver : this.argumentResolvers) {
				if (logger.isTraceEnabled()) {
					logger.trace("Testing if argument resolver [" + methodArgumentResolver + "] supports [" +
							parameter.getGenericParameterType() + "]");
				}
				// 如： methodArgumentResolver === org.springframework.web.servlet.mvc.method.annotation.ServletRequestMethodArgumentResolver
				if (methodArgumentResolver.supportsParameter(parameter)) { // 支持此种类型的参数
					result = methodArgumentResolver;
					this.argumentResolverCache.put(parameter, result);
					break;
				}
			}
		}
		return result;
	}

}
