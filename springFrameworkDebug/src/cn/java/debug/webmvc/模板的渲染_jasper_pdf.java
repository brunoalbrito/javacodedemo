package cn.java.debug.webmvc;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.springframework.context.ApplicationContextException;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.MessageSourceResourceBundle;
import org.springframework.core.io.Resource;
import org.springframework.ui.jasperreports.JasperReportsUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.support.RequestContext;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.util.WebUtils;

import freemarker.ext.servlet.AllHttpScopesHashModel;
import freemarker.ext.servlet.FreemarkerServlet;
import freemarker.ext.servlet.HttpRequestHashModel;
import freemarker.ext.servlet.HttpRequestParametersHashModel;
import freemarker.template.SimpleHash;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRDataSourceProvider;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

public class 模板的渲染_jasper_pdf {
	/*
		org.springframework.web.servlet.view.jasperreports.JasperReportsPdfView
		
		// ------------实例化report对象--------------------
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
							org.springframework.web.servlet.view.jasperreports.AbstractJasperReportsView.initApplicationContext() 
							{
								this.report = loadReport(); // !!!! 实例化report对象
								{
									String url = getUrl();
									if (url == null) {
										return null;
									}
									Resource mainReport = getApplicationContext().getResource(url);
									return loadReport(mainReport);
									{
										String filename = resource.getFilename();
										if (filename != null) {
											if (filename.endsWith(".jasper")) {
												// Load pre-compiled report.
												InputStream is = resource.getInputStream();
												try {
													return (JasperReport) JRLoader.loadObject(is); // "xxxx.jasper"
												}
												finally {
													is.close();
												}
											}
											else if (filename.endsWith(".jrxml")) {
												// Compile report on-the-fly.
												InputStream is = resource.getInputStream();
												try {
													JasperDesign design = JRXmlLoader.load(is); // "xxxx.jrxml"
													return JasperCompileManager.compileReport(design);
												}
												finally {
													is.close();
												}
											}
										}
									}
								}
						
								// Load sub reports if required, and check data source parameters.
								if (this.subReportUrls != null) {
									if (this.subReportDataKeys != null && this.subReportDataKeys.length > 0 && this.reportDataKey == null) {
										throw new ApplicationContextException(
												"'reportDataKey' for main report is required when specifying a value for 'subReportDataKeys'");
									}
									this.subReports = new HashMap<String, JasperReport>(this.subReportUrls.size());
									for (Enumeration<?> urls = this.subReportUrls.propertyNames(); urls.hasMoreElements();) {
										String key = (String) urls.nextElement();
										String path = this.subReportUrls.getProperty(key);
										Resource resource = getApplicationContext().getResource(path);
										this.subReports.put(key, loadReport(resource));
									}
								}
						
								// Convert user-supplied exporterParameters.
								convertExporterParameters();
							}
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
		
		// ------------渲染模板--------------------
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
			
			// org.springframework.web.servlet.view.AbstractJasperReportsView
			AbstractJasperReportsView.renderMergedOutputModel(mergedModel, getRequestToExpose(request), response);
			{
				if (this.subReports != null) {
					// Expose sub-reports as model attributes.
					model.putAll(this.subReports);
		
					// Transform any collections etc into JRDataSources for sub reports.
					if (this.subReportDataKeys != null) {
						for (String key : this.subReportDataKeys) {
							model.put(key, convertReportData(model.get(key)));
						}
					}
				}
		
				// Expose Spring-managed Locale and MessageSource.
				AbstractJasperReportsView.exposeLocalizationContext(model, request);
				{
					RequestContext rc = new RequestContext(request, getServletContext());
					Locale locale = rc.getLocale();
					if (!model.containsKey(JRParameter.REPORT_LOCALE)) {
						model.put(JRParameter.REPORT_LOCALE, locale);
					}
					TimeZone timeZone = rc.getTimeZone();
					if (timeZone != null && !model.containsKey(JRParameter.REPORT_TIME_ZONE)) {
						model.put(JRParameter.REPORT_TIME_ZONE, timeZone);
					}
					JasperReport report = getReport();
					{
						return this.report;
					}
					
					if ((report == null || report.getResourceBundle() == null) &&
							!model.containsKey(JRParameter.REPORT_RESOURCE_BUNDLE)) {
						model.put(JRParameter.REPORT_RESOURCE_BUNDLE,
								new MessageSourceResourceBundle(rc.getMessageSource(), locale));
					}
				}
		
				// Fill the report.
				JasperPrint filledReport = AbstractJasperReportsView.fillReport(model); // 填充报表
				{
					// Determine main report.
					JasperReport report = getReport();
					if (report == null) {
						throw new IllegalStateException("No main report defined for 'fillReport' - " +
								"specify a 'url' on this view or override 'getReport()' or 'fillReport(Map)'");
					}
			
					JRDataSource jrDataSource = null;
					DataSource jdbcDataSourceToUse = null;
			
					// Try model attribute with specified name.
					if (this.reportDataKey != null) { // 指定了数据源的键
						Object reportDataValue = model.get(this.reportDataKey);
						if (reportDataValue instanceof DataSource) {
							jdbcDataSourceToUse = (DataSource) reportDataValue;
						}
						else {
							jrDataSource = AbstractJasperReportsView.convertReportData(reportDataValue);
							{
								if (value instanceof JRDataSourceProvider) {
									return createReport((JRDataSourceProvider) value);
									{
										JasperReport report = getReport();
										return provider.create(report);
									}
								}
								else {
									return JasperReportsUtils.convertReportData(value);
									{
										if (value instanceof JRDataSource) {
											return (JRDataSource) value;
										}
										else if (value instanceof Collection) {
											return new JRBeanCollectionDataSource((Collection<?>) value);
										}
										else if (value instanceof Object[]) {
											return new JRBeanArrayDataSource((Object[]) value);
										}
										else {
											throw new IllegalArgumentException("Value [" + value + "] cannot be converted to a JRDataSource");
										}
									}
								}
							}
						}
					}
					else {
						Collection<?> values = model.values();
						jrDataSource = CollectionUtils.findValueOfType(values, JRDataSource.class); // 扫描实现JRDataSource接口的对象（数据源）
						if (jrDataSource == null) {
							JRDataSourceProvider provider = CollectionUtils.findValueOfType(values, JRDataSourceProvider.class);
							if (provider != null) {
								jrDataSource = createReport(provider);
							}
							else {
								jdbcDataSourceToUse = CollectionUtils.findValueOfType(values, DataSource.class);
								if (jdbcDataSourceToUse == null) {
									jdbcDataSourceToUse = this.jdbcDataSource;
								}
							}
						}
					}
			
					if (jdbcDataSourceToUse != null) {
						return doFillReport(report, model, jdbcDataSourceToUse);
					}
					else {
						// Determine JRDataSource for main report.
						if (jrDataSource == null) {
							jrDataSource = getReportData(model);
						}
						if (jrDataSource != null) {
							// Use the JasperReports JRDataSource.
							return JasperFillManager.fillReport(report, model, jrDataSource);
						}
						else {
							// Assume that the model contains parameters that identify
							// the source for report data (e.g. Hibernate or JPA queries).
							logger.debug("Filling report with plain model");
							return JasperFillManager.fillReport(report, model);
						}
					}
				}
				AbstractJasperReportsView.postProcessReport(filledReport, model); // 空方法
		
				// Prepare response and render report.
				AbstractJasperReportsView.populateHeaders(response); // 填充头
				{
					// Apply the headers to the response.
					for (Enumeration<?> en = this.headers.propertyNames(); en.hasMoreElements();) {
						String key = (String) en.nextElement();
						response.addHeader(key, this.headers.getProperty(key));
					}
				}
				AbstractJasperReportsSingleFormatView.renderReport(filledReport, model, response);
				{
					net.sf.jasperreports.engine.JRExporter exporter = createExporter(); // 创建生成器
					{
						org.springframework.web.servlet.view.jasperreports.JasperReportsPdfView.createExporter()
						{
							return new JRPdfExporter();
						}
					}

					Map<net.sf.jasperreports.engine.JRExporterParameter, Object> mergedExporterParameters = getConvertedExporterParameters();
					if (!CollectionUtils.isEmpty(mergedExporterParameters)) {
						exporter.setParameters(mergedExporterParameters);
					}
			
					if (useWriter()) {
						renderReportUsingWriter(exporter, populatedReport, response); // 使用writer写数据
						{
							// Copy the encoding configured for the report into the response.
							String contentType = getContentType();
							String encoding = (String) exporter.getParameter(net.sf.jasperreports.engine.JRExporterParameter.CHARACTER_ENCODING);
							if (encoding != null) {
								// Only apply encoding if content type is specified but does not contain charset clause already.
								if (contentType != null && !contentType.toLowerCase().contains(WebUtils.CONTENT_TYPE_CHARSET_PREFIX)) {
									contentType = contentType + WebUtils.CONTENT_TYPE_CHARSET_PREFIX + encoding;
								}
							}
							response.setContentType(contentType);
					
							// Render report into HttpServletResponse's Writer.
							JasperReportsUtils.render(exporter, populatedReport, response.getWriter()); // 渲染
						}
					}
					else {
						renderReportUsingOutputStream(exporter, populatedReport, response); // 使用流写数据
						{
							// IE workaround: write into byte array first.
							ByteArrayOutputStream baos = createTemporaryOutputStream();
							JasperReportsUtils.render(exporter, populatedReport, baos);
							writeToResponse(response, baos);
							{
								org.springframework.web.servlet.view.AbstractView.writeToResponse(HttpServletResponse response, ByteArrayOutputStream baos) 
								{
									// Write content type and also length (determined via byte array).
									response.setContentType(getContentType());
									response.setContentLength(baos.size());
							
									// Flush byte array to servlet output stream.
									ServletOutputStream out = response.getOutputStream();
									baos.writeTo(out);
									out.flush();
								}
							}
						}
					}
				}
			}
		}

	 */
}
