package cn.java.jfinal.handler;



import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletRegistration;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jfinal.handler.Handler;

public class Demo0Handler extends Handler {

	@Override
	public void handle(String target, HttpServletRequest request,
			HttpServletResponse response, boolean[] isHandled) {
		
		System.out.println("Handler:  cn.java.jfinal.handler.Demo0Handler");
		
		//解析用户请求的URL地址
		String contextPath = request.getServletContext().getContextPath();
//		System.out.println("contextPath:"+contextPath);//空
		int contextPathLength = (contextPath == null || "/".equals(contextPath) ? 0
				: contextPath.length());
		String targetTemp = request.getRequestURI();
//		System.out.println("targetTemp:"+targetTemp);//  //test2Servlet/
		if (contextPathLength != 0) {
			targetTemp = targetTemp.substring(contextPathLength);
		}
		targetTemp = targetTemp.replaceFirst("^[/]+/", "/");
//		System.out.println("targetTemp:"+targetTemp);//  /test2Servlet/
		String[] targetArray = targetTemp.split("/");
		String servletTag = "";
		if(targetArray.length>1)
		{
			servletTag = targetArray[1];	//  test2Servlet
		}
//		System.out.println("targetTemp:"+servletTag);
		
		//读取web.xml文件，解析内部配置的 Servlet映射
		ArrayList<String> servletNameList = new ArrayList<String>();
//		System.out.println(request.getServletContext().getServerInfo());//jetty/8.y.z-SNAPSHOT
//		System.out.println(request.getServletContext().getServletContextName());//Servlet and JSP Examples
		Map<String,ServletRegistration> map = (Map<String, ServletRegistration>) request.getServletContext().getServletRegistrations();
		Set keySet = map.keySet();
		Iterator iterator =  keySet.iterator();
		while(iterator.hasNext())
		{
			String keyName = (String) iterator.next();
			String keyNameToLowerCase = keyName.toLowerCase();
//			System.out.println(keyName);  // default、test2Servlet、jsp、testServlet
//			System.out.println("servlet-name_toLowerCase: "+keyNameToLowerCase);
//			keyNameToLowerCase!="default" 此写法有问题
			if((!keyNameToLowerCase.equals("default")) && (!keyNameToLowerCase.equals("jsp")))
			{
				ServletRegistration servletRegistrationTemp = map.get(keyName);
//				System.out.println("servlet-name: "+servletRegistrationTemp.getName()); // default、test2Servlet、jsp、testServlet
//				System.out.println("servlet-class: "+servletRegistrationTemp.getClassName());
				Collection<String> collection = servletRegistrationTemp.getMappings();
				Iterator iteratorTemp =  collection.iterator();
				while(iteratorTemp.hasNext())
				{
					String urlPattern = (String) iteratorTemp.next();
//					System.out.println("url-pattern: "+urlPattern);
					urlPattern = urlPattern.replaceFirst("^[/]+/", "/");
					String[] urlPatternArray = urlPattern.split("/");
					String urlPatternFlag = "";
					if(urlPatternArray.length>1)
					{
						urlPatternFlag = urlPatternArray[1];
//						System.out.println("urlPatternFlag:"+urlPatternFlag);
						servletNameList.add(urlPatternFlag);
					}
				}
			}
		}
		
		if (!servletNameList.contains(servletTag)) {//如果不是Servlet，就进入jfinal
			this.nextHandler.handle(target, request, response, isHandled);
		}
	}

}
