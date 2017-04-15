package cn.java.filter;
import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 编码转换
 */
public class CharacterEncodingFilter implements Filter {

	/**
	 * 过滤器初始化
	 */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
//		filterConfig === org.apache.catalina.core.ApplicationFilterConfig
		String filterParam1 = filterConfig.getInitParameter("filterParam1");
		System.out.println("filterParam1 = " + filterParam1);
//		filterConfig.getServletContext() === org.apache.catalina.core.ApplicationContextFacade
	}

	/**
	 * 过滤器执行
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// 执行过滤器业务
		request.setCharacterEncoding("utf-8");
		System.out.println(this.getClass().getCanonicalName());
		// 触发下一个过滤器执行
		chain.doFilter(request, response); 
		
		// Servlet执行后调用
	}

	/**
	 * 销毁过滤器
	 */
	@Override
	public void destroy() {
//		org.apache.catalina.core.StandardContext.stopInternal()
//			org.apache.catalina.core.StandardContext.filterStop()
//				org.apache.catalina.core.ApplicationFilterConfig.release()
//					cn.java.filter.CharacterEncodingFilter.destroy()
		System.out.println("fitler destroy...");
	}

}
