package cn.java.note.jsp;

import org.apache.jasper.runtime.ServletResponseWrapperInclude;

public class JspObjInPage渲染机制 {

	/**
	 * jsp页面中的对象
	 * @param args
	 */
	public static void main(String[] args) {
		
//		out.write("111");
//		response.getWriter().write("222");
		/**
		 	 JSP 页面中的代码
		 	 ServletResponseWrapperInclude responseTemp = (ServletResponseWrapperInclude)response;
			 responseTemp.getResponse() === org.apache.catalina.core.ApplicationHttpResponse(ResponseFacade,true)
			 responseTemp.getResponse() === org.eclipse.jetty.server.Response(ResponseFacade,true)
			 out.write(responseTemp.getResponse().getClass().toString());
		 
			if(response instanceof ServletResponseWrapperInclude){
				out.write("response is instanceof ServletResponseWrapperInclude ;"); 
			}
			
		*/
		
		/**
		 
		 	JSP编译后的Servlet代码
		 		JspFactory mJspFactory = JspFactory.getDefaultFactory(); // org.apache.jasper.runtime.JspFactoryImpl
				PageContext pageContext = mJspFactory.getPageContext(this, request, response,null, true, 8192, true);
				out = pageContext.getOut(); // org.apache.jasper.runtime.JspWriterImpl

			在JSP页面中做如下操作的等价：
				out.write("123"); // org.apache.jasper.runtime.JspWriterImpl.write("123");  // 内部有使用缓冲区，等缓存区满了之后，才会刷到 response.getWrite()，触发配置jsp的渲染不使用缓冲区
				response.getWrite().write("456"); // 立刻输出
			
			
			问题：
				如果在JSP页面中使用如下代码，为什么输出的内容不是输出111222333。而是输出 222111333
					<% out.write("111"); %>
					<% response.getWriter().write("222"); %>
					<% out.print("333" ); %>
					
				解答：
					因为JSP的JSP_Servlet中始终使用了缓冲区org.apache.jasper.runtime.JspWriterImpl作为页面的out对象。
					在页面中使用 out.write("..."); 是先写入到JSP_Servlet的缓冲区JspWriterImpl，
					等缓冲区满了之后，才会通过  responseServletResponseWrapperInclude.getWriter() 取得输出对象MyJspWriterImpl，
					在把JspWriterImpl中缓冲的数据，刷到 MyJspWriterImpl 中
				
				要是输出一致：
					在JSP页面中都使用一致的风格编码
						<% out.write("111"); %>
						<% out.write("222"); %>
						<% out.print("333" ); %>
					在JSP页面中都使用一致的风格编码
						<% response.getWriter().write("111"); %>
						<% response.getWriter().write("222"); %>
						<% response.getWriter().print("333" ); %>
						
					把JSP内部关于生成JSP_Servlet，关于缓冲区的配置禁用掉：达到如下效果
						mJspFactory.getPageContext(this, request, response,null, true, 0, true);
			扩展认知：
				out.write("111"); 是使用JspWriterImpl缓冲区，到缓冲区满了，使用response.getWriter()，输出到"输出通道"里面
				response.getWriter().write("111"); 是不使用JspWriterImpl缓冲区的输出，直接输出到"输出通道"里面
				
			输出通道：
				可以实现自己的输出通道 ，只要在做JSP跳转的时候，对 response 进行包装，new ServletResponseWrapperInclude(response, mJspWriter)
				Servlet的输出通道是：response.getWriter()
		
		 */
	}
	
}
