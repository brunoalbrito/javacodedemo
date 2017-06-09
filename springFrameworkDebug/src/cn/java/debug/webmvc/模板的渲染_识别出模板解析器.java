package cn.java.debug.webmvc;

import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.util.PatternMatchUtils;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.support.RequestContext;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.AbstractTemplateView;
import org.springframework.web.servlet.view.AbstractUrlBasedView;
import org.springframework.web.servlet.view.InternalResourceView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.servlet.view.jasperreports.AbstractJasperReportsView;

import freemarker.ext.servlet.AllHttpScopesHashModel;
import freemarker.ext.servlet.FreemarkerServlet;
import freemarker.ext.servlet.HttpRequestHashModel;
import freemarker.ext.servlet.HttpRequestParametersHashModel;
import freemarker.template.SimpleHash;

public class 模板的渲染_识别出模板解析器 {
	/*
		org.springframework.web.servlet.DispatcherServlet.resolveViewName(String viewName, Map<String, Object> model, Locale locale, HttpServletRequest request)
		{
			for (ViewResolver viewResolver : this.viewResolvers) {
				// 如：viewResolver == org.springframework.web.servlet.view.InternalResourceViewResolver
				View view = viewResolver.resolveViewName(viewName, locale);
				if (view != null) {
					return view;
				}
			}
			return null;
		}
		
		// --------freemarker 模板解析器---------------
		// viewResolver == org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver
		{
			org.springframework.web.servlet.view.AbstractCachingViewResolver.resolveViewName(String viewName, Locale locale)
			{
				// org.springframework.web.servlet.view.UrlBasedViewResolver
				return UrlBasedViewResolver.createView(viewName, locale);
				{
					// If this resolver is not supposed to handle the given view,
					// return null to pass on to the next resolver in the chain.
					if (!canHandle(viewName, locale){
						String[] viewNames = getViewNames();
						return (viewNames == null || PatternMatchUtils.simpleMatch(viewNames, viewName));
					}) { // !!!!
						return null;
					}
					
					// Check for special "redirect:" prefix.
					if (viewName.startsWith(REDIRECT_URL_PREFIX)) { // 重定向
						String redirectUrl = viewName.substring(REDIRECT_URL_PREFIX.length());
						// org.springframework.web.servlet.view.RedirectView
						RedirectView view = new RedirectView(redirectUrl, isRedirectContextRelative(), isRedirectHttp10Compatible());
						view.setHosts(getRedirectHosts());
						return applyLifecycleMethods(viewName, view);
					}
					// Check for special "forward:" prefix.
					if (viewName.startsWith(FORWARD_URL_PREFIX)) { // 转发
						String forwardUrl = viewName.substring(FORWARD_URL_PREFIX.length());
						// org.springframework.web.servlet.view.InternalResourceView
						return new InternalResourceView(forwardUrl); // 只支持JSP
					}
					// Else fall back to superclass implementation: calling loadView.
					return super.createView(viewName, locale); // !!!!
					{
						return UrlBasedViewResolver.loadView(viewName, locale);
						{
							// view === org.springframework.web.servlet.view.freemarker.FreeMarkerView
							AbstractUrlBasedView view = AbstractTemplateViewResolver.buildView(viewName); // !!!
							{
								AbstractTemplateView view = (AbstractTemplateView) super.buildView(viewName);
								{
									AbstractUrlBasedView view = (AbstractUrlBasedView) BeanUtils.instantiateClass(getViewClass()); // 实例化对象
									view.setUrl(getPrefix() + viewName + getSuffix()); // 设置模板路径
							
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
								view.setExposeRequestAttributes(this.exposeRequestAttributes);
								view.setAllowRequestOverride(this.allowRequestOverride);
								view.setExposeSessionAttributes(this.exposeSessionAttributes);
								view.setAllowSessionOverride(this.allowSessionOverride);
								view.setExposeSpringMacroHelpers(this.exposeSpringMacroHelpers);
								return view;
							}
							View result = UrlBasedViewResolver.applyLifecycleMethods(viewName, view);
							{
								// org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.initializeBean(view, viewName);
								return (View) getApplicationContext().getAutowireCapableBeanFactory().initializeBean(view, viewName); //  给view对注入感知对象/ 调用view的初始化方法
							}
							return (view.checkResource(locale) ? result : null); // 检查模板的存在性 , FreeMarkerView.checkResource(locale)，会匹配!!!
						}
					}
				}
			}
		}

		// --------jsp、jstl 模板解析器---------------
		// viewResolver == org.springframework.web.servlet.view.InternalResourceViewResolver
		{
		  	org.springframework.web.servlet.view.AbstractCachingViewResolver.resolveViewName(String viewName, Locale locale)
			{
				// org.springframework.web.servlet.view.UrlBasedViewResolver
				return UrlBasedViewResolver.createView(viewName, locale);
				{
					// If this resolver is not supposed to handle the given view,
					// return null to pass on to the next resolver in the chain.
					if (!canHandle(viewName, locale){
						String[] viewNames = getViewNames();
						return (viewNames == null || PatternMatchUtils.simpleMatch(viewNames, viewName));
					}) { // !!!!
						return null;
					}
					// Check for special "redirect:" prefix.
					if (viewName.startsWith(REDIRECT_URL_PREFIX)) { // 重定向
						String redirectUrl = viewName.substring(REDIRECT_URL_PREFIX.length());
						RedirectView view = new RedirectView(redirectUrl, isRedirectContextRelative(), isRedirectHttp10Compatible());
						view.setHosts(getRedirectHosts());
						return applyLifecycleMethods(viewName, view);
					}
					// Check for special "forward:" prefix.
					if (viewName.startsWith(FORWARD_URL_PREFIX)) { // 转发
						String forwardUrl = viewName.substring(FORWARD_URL_PREFIX.length());
						return new InternalResourceView(forwardUrl); // 只支持JSP
					}
					// Else fall back to superclass implementation: calling loadView.
					return super.createView(viewName, locale); // !!!!
					{
						return UrlBasedViewResolver.loadView(viewName, locale);
						{
							// view === org.springframework.web.servlet.view.JstlView 
							// view === org.springframework.web.servlet.view.InternalResourceView 
							AbstractUrlBasedView view = InternalResourceView.buildView(viewName); // !!!
							{
								InternalResourceView view = (InternalResourceView) super.buildView(viewName);
								{
									AbstractUrlBasedView view = (AbstractUrlBasedView) BeanUtils.instantiateClass(getViewClass()); // 实例化对象
									view.setUrl(getPrefix() + viewName + getSuffix()); // 设置模板路径
							
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
								if (this.alwaysInclude != null) {
									view.setAlwaysInclude(this.alwaysInclude);
								}
								view.setPreventDispatchLoop(true);
								return view;
							}
							View result = UrlBasedViewResolver.applyLifecycleMethods(viewName, view);
							{
								// org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.initializeBean(view, viewName);
								return (View) getApplicationContext().getAutowireCapableBeanFactory().initializeBean(view, viewName); //  给view对注入感知对象/ 调用view的初始化方法
							}
							return (view.checkResource(locale) ? result : null); // 检查模板的存在性，始终返回为true!!!
						}
					}
				}
			}
		}
		
		// --------jasper 模板解析器---------------
		// viewResolver == org.springframework.web.servlet.view.jasperreports.JasperReportsViewResolver
		{
			org.springframework.web.servlet.view.AbstractCachingViewResolver.resolveViewName(String viewName, Locale locale)
			{
				// org.springframework.web.servlet.view.UrlBasedViewResolver
				return UrlBasedViewResolver.createView(viewName, locale);
				{
					// If this resolver is not supposed to handle the given view,
					// return null to pass on to the next resolver in the chain.
					if (!canHandle(viewName, locale){
						String[] viewNames = getViewNames();
						return (viewNames == null || PatternMatchUtils.simpleMatch(viewNames, viewName));
					}) { // !!!!
						return null;
					}
					// Check for special "redirect:" prefix.
					if (viewName.startsWith(REDIRECT_URL_PREFIX)) { // 重定向
						String redirectUrl = viewName.substring(REDIRECT_URL_PREFIX.length());
						RedirectView view = new RedirectView(redirectUrl, isRedirectContextRelative(), isRedirectHttp10Compatible());
						view.setHosts(getRedirectHosts());
						return applyLifecycleMethods(viewName, view);
					}
					// Check for special "forward:" prefix.
					if (viewName.startsWith(FORWARD_URL_PREFIX)) { // 转发
						String forwardUrl = viewName.substring(FORWARD_URL_PREFIX.length());
						return new InternalResourceView(forwardUrl); // 只支持JSP
					}
					
					// Else fall back to superclass implementation: calling loadView.
					return super.createView(viewName, locale); // !!!!
					{
						return UrlBasedViewResolver.loadView(viewName, locale);
						{
							// view === org.springframework.web.servlet.view.jasperreports.JasperReportsViewResolver
							AbstractUrlBasedView view = JasperReportsViewResolver.buildView(viewName); // !!!
							{
								AbstractJasperReportsView view = (AbstractJasperReportsView) super.buildView(viewName);
								{
									AbstractUrlBasedView view = (AbstractUrlBasedView) BeanUtils.instantiateClass(getViewClass()); // 实例化对象
									view.setUrl(getPrefix() + viewName + getSuffix()); // 设置模板路径
							
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
								view.setReportDataKey(this.reportDataKey);
								view.setSubReportUrls(this.subReportUrls);
								view.setSubReportDataKeys(this.subReportDataKeys);
								view.setHeaders(this.headers);
								view.setExporterParameters(this.exporterParameters);
								view.setJdbcDataSource(this.jdbcDataSource);
								return view;
							}
							View result = UrlBasedViewResolver.applyLifecycleMethods(viewName, view);
							{
								// org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.initializeBean(view, viewName);
								return (View) getApplicationContext().getAutowireCapableBeanFactory().initializeBean(view, viewName); //  给view对注入感知对象/ 调用view的初始化方法
							}
							return (view.checkResource(locale) ? result : null); // 检查模板的存在性，始终返回为true!!!
						}
					}
				}
			}
		}
		
		
	 */
}
