package cn.java.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 */
public class OpenSessionInFilter implements Filter {


	public void init(FilterConfig arg0) throws ServletException {

	} 

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		Session session = null;
		Transaction transaction = null;
		try {
			session = SessionFactoryThreadLocalUtil.getThreadLocalSession();
			transaction = session.beginTransaction(); // 开启事务
			chain.doFilter(request, response);
			transaction.commit(); // 提交事务
		} catch (Exception e) {
			if (transaction != null){
				transaction.rollback(); // 回滚事务
			}
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			SessionFactoryThreadLocalUtil.closeThreadLocalSession();
		}
	}

	public void destroy() {

	}

}
