package cn.java.demo.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.cors.CorsUtils;

import cn.java.demo.util.ApplicationContextUtil;
import cn.java.demo.web.bean.NeedAwareBean;

@SuppressWarnings(value={  "serial" })
public class HelloServlet extends HttpServlet   {
	ThreadLocal<ServletContext> servletContextLocal = new ThreadLocal<ServletContext>();
	
	public void init(ServletConfig config) throws ServletException {
		servletContextLocal.set(config.getServletContext());
		super.init(config);
	}
	
	public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
		super.service(req, res);
		
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if(servletContextLocal.get()!=null){
			ServletContext servletContext = servletContextLocal.get();
			
			// Spring默认会把XmlWebApplicationContext对象放入servletContext的 WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE 属性中
			XmlWebApplicationContext context = (XmlWebApplicationContext) servletContext.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
			
			// 测试 - 获取bean实例
			{ 
				NeedAwareBean needAwareBean0 = (NeedAwareBean) context.getBean("needAwareBean0");
				needAwareBean0.testMethod();
			}
			
			// 测试（context是共享对象，在实际的编码中，不应该动态添加bean的定义） - 动态添加bean的定义、获取bean实例
			{
				BeanDefinitionRegistry registry = ApplicationContextUtil.getBeanFactoryAndTryCastTypeToBeanDefinitionRegistry(context);
				ConfigurableListableBeanFactory beanFactory = ApplicationContextUtil.getBeanFactoryAndTryCastTypeToConfigurableListableBeanFactory(context);
				if(registry!=null && registry!=beanFactory){
					final String beanNameInternal0 = this.getClass().getSimpleName()+"_beanName0";
					
					// 添加bean的定义
					RootBeanDefinition beanDefinitionInternal0 = new RootBeanDefinition(NeedAwareBean.class);
					beanDefinitionInternal0.setSource(null);
					beanDefinitionInternal0.setAutowireMode(RootBeanDefinition.AUTOWIRE_BY_NAME);
					beanDefinitionInternal0.getPropertyValues().add("field1", this.getClass().getSimpleName()+"beanDefinition0_field2Value");
					beanDefinitionInternal0.setRole(BeanDefinition.ROLE_APPLICATION);
					registry.registerBeanDefinition(beanNameInternal0, beanDefinitionInternal0);
					
					// 获取bean实例
					NeedAwareBean needAwareBeanInternal0 = (NeedAwareBean)beanFactory.getBean(beanNameInternal0);
					needAwareBeanInternal0.testMethod();
				}
			}
			
			// 是跨域请求
			{
				if(CorsUtils.isCorsRequest(req)){ 
					List<String> allowOriginList = new ArrayList<String>();
					allowOriginList.add("http://www.domain0.com");
					allowOriginList.add("http://www.domain1.com");
					String originReq = req.getHeader(HttpHeaders.ORIGIN);
					if(allowOriginList.contains(originReq)){
						resp.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
						resp.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, originReq);
						resp.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "GET, POST, PUT, DELETE, OPTIONS");
					}
					
				}
			}
			
			// 缓存控制
			{
				// http://baike.baidu.com/item/Cache-control
				final String HEADER_PRAGMA = "Pragma";
				final String HEADER_EXPIRES = "Expires";
				final String HEADER_CACHE_CONTROL = "Cache-Control";
				CacheControl cacheControl;
				int cacheSeconds = 3600;
				int cacheTypeId = 0;
				if(cacheTypeId == 0){
					cacheControl = CacheControl.maxAge(cacheSeconds, TimeUnit.SECONDS); // 缓存时长
				}
				else if(cacheTypeId == 1){
					// 不保存文件，所有内容都不会被缓存到缓存或 Internet 临时文件中（浏览器访问页面时，会在临时文件夹下保存页面的html，js，图片等）
					cacheControl = CacheControl.noStore(); 
				}
				else if(cacheTypeId == 2){
					// 会保存文件，只不过每次在向浏览器提供响应数据时，缓存都要向服务器评估缓存响应的有效性
					// 必须先与服务器确认返回的响应是否被更改，然后才能使用该响应来满足后续对同一个网址的请求。因此，如果存在合适的验证令牌 (ETag)，no-cache 会发起往返通信来验证缓存的响应，如果资源未被更改，可以避免下载。
					cacheControl = CacheControl.noCache(); 
				}
				else{
					cacheControl = CacheControl.empty(); // 空
				}
				
				{
					String ccValue = cacheControl.getHeaderValue();
					if (ccValue != null) {
						// Set computed HTTP 1.1 Cache-Control header
						resp.setHeader(HEADER_CACHE_CONTROL, ccValue);

						if (resp.containsHeader(HEADER_PRAGMA)) {
							// Reset HTTP 1.0 Pragma header if present
							resp.setHeader(HEADER_PRAGMA, "");
						}
						if (resp.containsHeader(HEADER_EXPIRES)) {
							// Reset HTTP 1.0 Expires header if present
							resp.setHeader(HEADER_EXPIRES, "");
						}
					}
				}
			}
			
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
	}
}
