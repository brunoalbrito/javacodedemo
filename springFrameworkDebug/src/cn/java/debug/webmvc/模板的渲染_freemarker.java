package cn.java.debug.webmvc;

import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.support.RequestContext;
import org.springframework.web.servlet.support.RequestContextUtils;

import freemarker.ext.servlet.AllHttpScopesHashModel;
import freemarker.ext.servlet.FreemarkerServlet;
import freemarker.ext.servlet.HttpRequestHashModel;
import freemarker.ext.servlet.HttpRequestParametersHashModel;
import freemarker.template.SimpleHash;

public class 模板的渲染_freemarker {
	/*
		org.springframework.web.servlet.view.freemarker.FreeMarkerView
		
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
		
		//---------------------渲染的过程----------------------
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
					mergedModel.put(this.requestContextAttribute, createRequestContext(request, response, mergedModel)); // !!!! 设置请求上下文
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
			// org.springframework.web.servlet.view.AbstractTemplateView
			AbstractTemplateView.renderMergedOutputModel(mergedModel, getRequestToExpose(request), response);
			{
				org.springframework.web.servlet.view.AbstractTemplateView.renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) 
				{
					if (this.exposeRequestAttributes) {
						for (Enumeration<String> en = request.getAttributeNames(); en.hasMoreElements();) {
							String attribute = en.nextElement();
							Object attributeValue = request.getAttribute(attribute);
							model.put(attribute, attributeValue);
						}
					}
			
					if (this.exposeSessionAttributes) {
						HttpSession session = request.getSession(false);
						if (session != null) {
							for (Enumeration<String> en = session.getAttributeNames(); en.hasMoreElements();) {
								String attribute = en.nextElement();
								Object attributeValue = session.getAttribute(attribute);
								model.put(attribute, attributeValue);
							}
						}
					}
			
					if (this.exposeSpringMacroHelpers) {
						// Expose RequestContext instance for Spring macros.
						model.put(SPRING_MACRO_REQUEST_CONTEXT_ATTRIBUTE,
								new RequestContext(request, response, getServletContext(), model));
					}
			
					AbstractTemplateView.applyContentType(response); // 输出内容类型
					{
						if (response.getContentType() == null) {
							response.setContentType(getContentType());
						}
					}
			
					FreeMarkerView.renderMergedTemplateModel(model, request, response);
					{
						FreeMarkerView.renderMergedTemplateModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) 
						{
							FreeMarkerView.exposeHelpers(model, request); // !!! 空方法
							FreeMarkerView.doRender(model, request, response); // !!!
							{
								// Expose model to JSP tags (as request attributes).
								AbstractView.exposeModelAsRequestAttributes(model, request);
								{
									for (Map.Entry<String, Object> entry : model.entrySet()) {
										String modelName = entry.getKey();
										Object modelValue = entry.getValue();
										if (modelValue != null) {
											request.setAttribute(modelName, modelValue); // !!!! 所有的属性值，放入request
										}
										else {
											request.removeAttribute(modelName);
										}
									}
								}
								
								// Expose all standard FreeMarker hash models.
								SimpleHash fmModel = buildTemplateModel(model, request, response); // 内置的一些对象
								{
									AllHttpScopesHashModel fmModel = new AllHttpScopesHashModel(getObjectWrapper(), getServletContext(), request);
									// JspTaglibs、Application、Session、Request、RequestParameters
									fmModel.put(FreemarkerServlet.KEY_JSP_TAGLIBS, this.taglibFactory);
									fmModel.put(FreemarkerServlet.KEY_APPLICATION, this.servletContextHashModel);
									fmModel.put(FreemarkerServlet.KEY_SESSION, buildSessionModel(request, response));
									fmModel.put(FreemarkerServlet.KEY_REQUEST, new HttpRequestHashModel(request, response, getObjectWrapper()));
									fmModel.put(FreemarkerServlet.KEY_REQUEST_PARAMETERS, new HttpRequestParametersHashModel(request));
									fmModel.putAll(model);
									return fmModel;
								}
								// Grab the locale-specific version of the template.
								Locale locale = RequestContextUtils.getLocale(request); // 识别出本地语言
								FreeMarkerView.processTemplate(getTemplate(locale){
									return FreeMarkerView.getTemplate(getUrl(), locale);
									{
										return (getEncoding() != null ?
											getConfiguration().getTemplate(name, locale, getEncoding()) :
											getConfiguration().getTemplate(name, locale)); //!!!!
									}
								}, fmModel, response); // !!!!
								{
									template.process(model, response.getWriter()); // !!!
								}
							}
						}
					}
				}
			}
		}

	 */
}
