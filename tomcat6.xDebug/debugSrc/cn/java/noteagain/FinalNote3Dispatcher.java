package cn.java.noteagain;


public class FinalNote3Dispatcher {
	
	/**
	 
	 	内部转发的实质是：
	 		通过org.apache.catalina.core.ApplicationContext.getRequestDispatcher(path)方法，
	 		{
		 		访问到 context.getMapper().map(uriMB, mappingData);//映射要转发的路径，
		 		取得映射路径 Wrapper wrapper = (Wrapper) mappingData.wrapper;//包装器
		 		创建对象new org.apache.catalina.core.ApplicationDispatcher
	 		}
	 		调用对象的forward方法
	 		{
		 		创建目标Servlet对象 servlet = wrapper.allocate();//创建Servlet对象
		 		创建过滤器ApplicationFilterChain filterChain = factory.createFilterChain(request,wrapper,servlet);
		 		触发过滤器 filterChain.doFilter(request, response);
		 		消息资源
	 		}
	 		
	 		
	 		
	 */
	public void note() {
		/**
		
		//内部转发：方式一
		req.getRequestDispatcher("/test.jsp").forward(req, resp);
		//内部转发：方式二
		this.getServletContext().getRequestDispatcher("/test.jsp")//...................................
		{
			return org.apache.catalina.core.ApplicationContextFacade.getRequestDispatcher(path)
			{
				return org.apache.catalina.core.ApplicationContext.getRequestDispatcher(path)
				{
					if (!path.startsWith("/"))//必须以 “/” 开头
			            throw new IllegalArgumentException
			                (sm.getString
			                 ("applicationContext.requestDispatcher.iae", path));
					
					// Get query string
			        String queryString = null;
			        int pos = path.indexOf('?');
			        if (pos >= 0) {
			            queryString = path.substring(pos + 1);
			            path = path.substring(0, pos);
			        }
			        
					MessageBytes uriMB = dd.uriMB;
			        uriMB.recycle();
					MappingData mappingData = dd.mappingData;
					
					CharChunk uriCC = uriMB.getCharChunk();
					
					int semicolon = path.indexOf(';');
		            if (pos >= 0 && semicolon > pos) {
		                semicolon = -1;
		            }
					uriCC.append(path, 0, semicolon > 0 ? semicolon : pos);
					context.getMapper().map(uriMB, mappingData);//映射要转发的路径
					
					Wrapper wrapper = (Wrapper) mappingData.wrapper;//包装器
					String wrapperPath = mappingData.wrapperPath.toString();
					String pathInfo = mappingData.pathInfo.toString();
					
					return new org.apache.catalina.core.ApplicationDispatcher(wrapper, uriCC.toString(), wrapperPath, pathInfo,queryString, null);
				}
				
			}
		}.forward(req, resp);//...................................
		{
			org.apache.catalina.core.ApplicationDispatcher.forward()
			{
				org.apache.catalina.core.ApplicationDispatcher.doForward()
				{
						pplicationHttpRequest wrequest =
			                (ApplicationHttpRequest) wrapRequest(state);
			            String contextPath = context.getPath();
			            HttpServletRequest hrequest = state.hrequest;
			            if (hrequest.getAttribute(Globals.FORWARD_REQUEST_URI_ATTR) == null) {
			                wrequest.setAttribute(Globals.FORWARD_REQUEST_URI_ATTR,
			                                      hrequest.getRequestURI());
			                wrequest.setAttribute(Globals.FORWARD_CONTEXT_PATH_ATTR,
			                                      hrequest.getContextPath());
			                wrequest.setAttribute(Globals.FORWARD_SERVLET_PATH_ATTR,
			                                      hrequest.getServletPath());
			                wrequest.setAttribute(Globals.FORWARD_PATH_INFO_ATTR,
			                                      hrequest.getPathInfo());
			                wrequest.setAttribute(Globals.FORWARD_QUERY_STRING_ATTR,
			                                      hrequest.getQueryString());
			            }
			 
			            wrequest.setContextPath(contextPath);
			            wrequest.setRequestURI(requestURI);
			            wrequest.setServletPath(servletPath);
			            wrequest.setPathInfo(pathInfo);
			            if (queryString != null) {
			                wrequest.setQueryString(queryString);
			                wrequest.setQueryParams(queryString);
			            }

			            org.apache.catalina.core.ApplicationDispatcher.processRequest(request,response,state);
			            {
			            	Integer disInt = (Integer) request.getAttribute
			                        (ApplicationFilterFactory.DISPATCHER_TYPE_ATTR);
			                    if (disInt != null) {
			                        if (disInt.intValue() != ApplicationFilterFactory.ERROR) {
			                            state.outerRequest.setAttribute
			                                (ApplicationFilterFactory.DISPATCHER_REQUEST_PATH_ATTR,
			                                 getCombinedPath());
			                            state.outerRequest.setAttribute
			                                (ApplicationFilterFactory.DISPATCHER_TYPE_ATTR,
			                                 Integer.valueOf(ApplicationFilterFactory.FORWARD));
			                            org.apache.catalina.core.ApplicationDispatcher.invoke(state.outerRequest, response, state);
			                            {
			                            	 servlet = wrapper.allocate();//创建Servlet对象
			                            	
			                            	 // Get the FilterChain Here 取得过滤器链条
			                                 ApplicationFilterFactory factory = ApplicationFilterFactory.getInstance();
			                                 ApplicationFilterChain filterChain = factory.createFilterChain(request,
			                                                                                         wrapper,servlet);
			                                 //如果目标文件是JSP文件
			                                 String jspFile = wrapper.getJspFile();
			                                 if (jspFile != null)
			                                     request.setAttribute(Globals.JSP_FILE_ATTR, jspFile);// org.apache.catalina.jsp_file
			                                 else
			                                     request.removeAttribute(Globals.JSP_FILE_ATTR);
			                                 
			                                 //触发过滤器链条，Servlet放在末尾
			                                 filterChain.doFilter(request, response);
			                            }
			                    }
			            }
				}
			}
		}
		 */
	}
}
