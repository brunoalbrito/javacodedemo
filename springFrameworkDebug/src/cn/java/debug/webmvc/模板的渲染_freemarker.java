package cn.java.debug.webmvc;

import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.support.RequestContext;
import org.springframework.web.servlet.support.RequestContextUtils;

import freemarker.template.SimpleHash;

public class 模板的渲染_freemarker {
	/*
		org.springframework.web.servlet.view.AbstractView.render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response)
		{
			Map<String, Object> mergedModel = createMergedOutputModel(model, request, response);
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
					mergedModel.put(this.requestContextAttribute, createRequestContext(request, response, mergedModel));
				}
		
				return mergedModel;
			}
			prepareResponse(request, response);
			{
				if (generatesDownloadContent()) {
					response.setHeader("Pragma", "private");
					response.setHeader("Cache-Control", "private, must-revalidate");
				}
			}
			renderMergedOutputModel(mergedModel, getRequestToExpose(request), response);
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
			
					applyContentType(response);
					{
						if (response.getContentType() == null) {
							response.setContentType(getContentType());
						}
					}
			
					renderMergedTemplateModel(model, request, response);
					{
						org.springframework.web.servlet.view.freemarker.FreeMarkerView.renderMergedTemplateModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) 
						{
							FreeMarkerView.exposeHelpers(model, request); // !!!
							FreeMarkerView.doRender(model, request, response); // !!!
							{
								// Expose model to JSP tags (as request attributes).
								exposeModelAsRequestAttributes(model, request);
								// Expose all standard FreeMarker hash models.
								SimpleHash fmModel = buildTemplateModel(model, request, response);
						
								if (logger.isDebugEnabled()) {
									logger.debug("Rendering FreeMarker template [" + getUrl() + "] in FreeMarkerView '" + getBeanName() + "'");
								}
								// Grab the locale-specific version of the template.
								Locale locale = RequestContextUtils.getLocale(request);
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
