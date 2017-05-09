package cn.java.note;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ResourceBundle;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.core.StandardWrapperFacade;

/**
org.apache.catalina.core.StandardEngine
org.apache.catalina.core.StandardHost
org.apache.catalina.core.StandardContext
org.apache.catalina.core.StandardWrapper

org.apache.catalina.connector.Connector


org.apache.catalina.connector.CoyoteAdapter.service()
{
	第一  container = new org.apache.catalina.core.StandardEngine()
	第二    standardPipeline = container.getPipeline() === new StandardPipeline(this)// 类内部实例化好的
	第三    valve = standardPipeline.getFirst() === StandardEngineValve(); //类内部实例化好的
	第四    valve.invoke(request, response);
	connector.getContainer().getPipeline().getFirst().invoke(request, response);//链条式层级调用
}

org.apache.catalina.core.StandardWrapperValve.invoke()
{
	servlet = org.apache.catalina.core.StandardWrapper.allocate();//根据<servlet>的描述信息，加载并创建Servlet实体
	{
		org.apache.catalina.core.StandardWrapper.loadServlet()
		{
			Class classClass = null;
			classClass = classLoader.loadClass(actualClass);//加载类
			servlet = (Servlet) classClass.newInstance();//实例化Servlet
            // StandardWrapperFacade facade = new StandardWrapperFacade(org.apache.catalina.core.StandardWrapper);
            // servlet中的config，其实就是 org.apache.catalina.core.StandardWrapper 对象
            servlet.init(facade);//Servlet初始化  
		}
	}
	
	ApplicationFilterChain filterChain = factory.createFilterChain(request, wrapper, servlet);
	//org.apache.catalina.core.ApplicationFilterChain.doFilter
	filterChain.doFilter(request.getRequest(), response.getResponse()); //触发过滤器链条，在链条的末尾才调用的Servlet
	{
		org.apache.catalina.core.ApplicationFilterChain.internalDoFilter()
		{
			if (pos < n) {//
				//继续 ，触发过滤器
				filter.doFilter(request, response, this);//触发过滤器
			}
			servlet.service((HttpServletRequest) request,(HttpServletResponse) response);//调用Servlet
		}
	}
}


org.apache.catalina.servlets.DefaultServlet
org.apache.jasper.servlet.JspServlet
 */
public class HelloWorldExample extends HttpServlet {


	/**
	  
	   ServletConfig config === new StandardWrapperFacade(org.apache.catalina.core.StandardWrapper);
	  
	 */
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
        throws IOException, ServletException
    {
    
        ResourceBundle rb =
            ResourceBundle.getBundle("LocalStrings",request.getLocale());
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<html>");
        out.println("<head>");

	    String title = rb.getString("helloworld.title");

	    out.println("<title>" + title + "</title>");
        out.println("</head>");
        out.println("<body bgcolor=\"white\">");

	// note that all links are created to be relative. this
	// ensures that we can move the web application that this
	// servlet belongs to to a different place in the url
	// tree and not have any harmful side effects.

        // XXX
        // making these absolute till we work out the
        // addition of a PathInfo issue

	    out.println("<a href=\"../helloworld.html\">");
        out.println("<img src=\"../images/code.gif\" height=24 " +
                    "width=24 align=right border=0 alt=\"view code\"></a>");
        out.println("<a href=\"../index.html\">");
        out.println("<img src=\"../images/return.gif\" height=24 " +
                    "width=24 align=right border=0 alt=\"return\"></a>");
        out.println("<h1>" + title + "</h1>");
        out.println("</body>");
        out.println("</html>");
    }
}
