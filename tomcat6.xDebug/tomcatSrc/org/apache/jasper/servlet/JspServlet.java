/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.jasper.servlet;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.PeriodicEventListener;
import org.apache.jasper.Constants;
import org.apache.jasper.EmbeddedServletOptions;
import org.apache.jasper.Options;
import org.apache.jasper.compiler.JspRuntimeContext;
import org.apache.jasper.compiler.Localizer;
import org.apache.jasper.security.SecurityUtil;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;

/**
 * The JSP engine (a.k.a Jasper).
 *
 * The servlet container is responsible for providing a
 * URLClassLoader for the web application context Jasper
 * is being used in. Jasper will try get the Tomcat
 * ServletContext attribute for its ServletContext class
 * loader, if that fails, it uses the parent class loader.
 * In either case, it must be a URLClassLoader.
 *
 * @author Anil K. Vijendran
 * @author Harish Prabandham
 * @author Remy Maucherat
 * @author Kin-man Chung
 * @author Glenn Nielsen
 */
public class JspServlet extends HttpServlet implements PeriodicEventListener {

    // Logger
    private Log log = LogFactory.getLog(JspServlet.class);

    private ServletContext context;
    private ServletConfig config;
    private Options options;
    private JspRuntimeContext rctxt;


    /*
     * Initializes this JspServlet.
     */
    public void init(ServletConfig config) throws ServletException {
        
        super.init(config);
        this.config = config;//<servlet>节点的包装信息  config = org.apache.catalina.core.StandardWrapperFacade
        this.context = config.getServletContext();//被包装后的  context == org.apache.catalina.core.ApplicationContextFacade
        
        // Initialize the JSP Runtime Context
        // Check for a custom Options implementation
        String engineOptionsName = 
            config.getInitParameter("engineOptionsClass");
        if (engineOptionsName != null) {
            // Instantiate the indicated Options implementation
            try {
                ClassLoader loader = Thread.currentThread()
                        .getContextClassLoader();
                Class engineOptionsClass = loader.loadClass(engineOptionsName);
                Class[] ctorSig = { ServletConfig.class, ServletContext.class };
                Constructor ctor = engineOptionsClass.getConstructor(ctorSig);
                Object[] args = { config, context };
                options = (Options) ctor.newInstance(args);
            } catch (Throwable e) {
                // Need to localize this.
                log.warn("Failed to load engineOptionsClass", e);
                // Use the default Options implementation
                options = new EmbeddedServletOptions(config, context);
            }
        } else {
        	System.out.println("YES---------->org.apache.jasper.servlet.JspServlet.init()");
            // Use the default Options implementation
        	/**
        	 	org.apache.catalina.core.StandardWrapperFacade@77468bd9
				org.apache.catalina.core.ApplicationContextFacade@12bb4df8
        	 */
//        	System.out.println(config);//org.apache.catalina.core.StandardWrapperFacade@77468bd9
//        	System.out.println(context);//org.apache.catalina.core.ApplicationContextFacade@12bb4df8
            options = new EmbeddedServletOptions(config, context);
        }
      
        /**
         * JSP运行时上下文
         */
        rctxt = new JspRuntimeContext(context, options);//.......
        
        if (log.isDebugEnabled()) {
            log.debug(Localizer.getMessage("jsp.message.scratch.dir.is",
                    options.getScratchDir().toString()));
            log.debug(Localizer.getMessage("jsp.message.dont.modify.servlets"));
        }
    }


    /**
     * Returns the number of JSPs for which JspServletWrappers exist, i.e.,
     * the number of JSPs that have been loaded into the webapp with which
     * this JspServlet is associated.
     *
     * <p>This info may be used for monitoring purposes.
     *
     * @return The number of JSPs that have been loaded into the webapp with
     * which this JspServlet is associated
     */
    public int getJspCount() {
        return this.rctxt.getJspCount();
    }


    /**
     * Resets the JSP reload counter.
     *
     * @param count Value to which to reset the JSP reload counter
     */
    public void setJspReloadCount(int count) {
        this.rctxt.setJspReloadCount(count);
    }


    /**
     * Gets the number of JSPs that have been reloaded.
     *
     * <p>This info may be used for monitoring purposes.
     *
     * @return The number of JSPs (in the webapp with which this JspServlet is
     * associated) that have been reloaded
     */
    public int getJspReloadCount() {
        return this.rctxt.getJspReloadCount();
    }


    /**
     * <p>Look for a <em>precompilation request</em> as described in
     * Section 8.4.2 of the JSP 1.2 Specification.  <strong>WARNING</strong> -
     * we cannot use <code>request.getParameter()</code> for this, because
     * that will trigger parsing all of the request parameters, and not give
     * a servlet the opportunity to call
     * <code>request.setCharacterEncoding()</code> first.</p>
     *
     * @param request The servlet requset we are processing
     *
     * @exception ServletException if an invalid parameter value for the
     *  <code>jsp_precompile</code> parameter name is specified
     */
    boolean preCompile(HttpServletRequest request) throws ServletException {

        String queryString = request.getQueryString();
        if (queryString == null) {
            return (false);
        }
        int start = queryString.indexOf(Constants.PRECOMPILE);//jsp_precompile
        if (start < 0) {
            return (false);
        }
        queryString =
            queryString.substring(start + Constants.PRECOMPILE.length());
        if (queryString.length() == 0) {
            return (true);             // ?jsp_precompile
        }
        if (queryString.startsWith("&")) {
            return (true);             // ?jsp_precompile&foo=bar...
        }
        if (!queryString.startsWith("=")) {
            return (false);            // part of some other name or value
        }
        int limit = queryString.length();
        int ampersand = queryString.indexOf("&");
        if (ampersand > 0) {
            limit = ampersand;
        }
        String value = queryString.substring(1, limit);
        if (value.equals("true")) {
            return (true);             // ?jsp_precompile=true
        } else if (value.equals("false")) {
	    // Spec says if jsp_precompile=false, the request should not
	    // be delivered to the JSP page; the easiest way to implement
	    // this is to set the flag to true, and precompile the page anyway.
	    // This still conforms to the spec, since it says the
	    // precompilation request can be ignored.
            return (true);             // ?jsp_precompile=false
        } else {
            throw new ServletException("Cannot have request parameter " +
                                       Constants.PRECOMPILE + " set to " +
                                       value);
        }

    }
    

    public void service (HttpServletRequest request, 
    			 HttpServletResponse response)
                throws ServletException, IOException {

        String jspUri = null;

        String jspFile = (String) request.getAttribute(Constants.JSP_FILE);//JSP文件的路径  org.apache.catalina.jsp_file
        if (jspFile != null) {
            // JSP is specified via <jsp-file> in <servlet> declaration
            jspUri = jspFile;
        } else {
            /*
             * Check to see if the requested JSP has been the target of a
             * RequestDispatcher.include()
             */
            jspUri = (String) request.getAttribute(Constants.INC_SERVLET_PATH);// javax.servlet.include.servlet_path
            if (jspUri != null) {
                /*
		 * Requested JSP has been target of
                 * RequestDispatcher.include(). Its path is assembled from the
                 * relevant javax.servlet.include.* request attributes
                 */
                String pathInfo = (String) request.getAttribute(
                                    "javax.servlet.include.path_info");
                if (pathInfo != null) {
                    jspUri += pathInfo;
                }
            } else {
                /*
                 * Requested JSP has not been the target of a 
                 * RequestDispatcher.include(). Reconstruct its path from the
                 * request's getServletPath() and getPathInfo()
                 */
                jspUri = request.getServletPath(); // /index.jsp  !!!!!!!!!!!!!!!JSP请求地址!!!!!!!!!!!!!!!!!
                String pathInfo = request.getPathInfo();
                if (pathInfo != null) {
                    jspUri += pathInfo;
                }
            }
        }

        if (log.isDebugEnabled()) {	    
            log.debug("JspEngine --> " + jspUri);
            log.debug("\t     ServletPath: " + request.getServletPath());
            log.debug("\t        PathInfo: " + request.getPathInfo());
            log.debug("\t        RealPath: " + context.getRealPath(jspUri));
            log.debug("\t      RequestURI: " + request.getRequestURI());
            log.debug("\t     QueryString: " + request.getQueryString());
        }

        try {
        	System.out.println("org.apache.jasper.servlet.JspServlet.service(...) 执行这里吗？  YES");
            boolean precompile = preCompile(request);//通过URL地址判断，是否执行执行预编译的文件，而不重新检查编译
            serviceJspFile(request, response, jspUri, null, precompile);//处理JSP文件!!!!!!!!!!!!
        } catch (RuntimeException e) {
            throw e;
        } catch (ServletException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        } catch (Throwable e) {
            throw new ServletException(e);
        }

    }

    public void destroy() {
        if (log.isDebugEnabled()) {
            log.debug("JspServlet.destroy()");
        }

        rctxt.destroy();
    }


    public void periodicEvent() {
        rctxt.checkCompile();
    }

    // -------------------------------------------------------- Private Methods

    private void serviceJspFile(HttpServletRequest request,
                                HttpServletResponse response, String jspUri,
                                Throwable exception, boolean precompile)
        throws ServletException, IOException {
//    	org.apache.jasper.compiler.JspRuntimeContext.getWrapper("/index.jsp");

        JspServletWrapper wrapper = rctxt.getWrapper(jspUri);//包装JSP文件
        if (wrapper == null) {
            synchronized(this) {
                wrapper = rctxt.getWrapper(jspUri);
                if (wrapper == null) {//执行进来
                    // Check if the requested JSP page exists, to avoid
                    // creating unnecessary directories and files.
                    if (null == context.getResource(jspUri)) {
                        handleMissingResource(request, response, jspUri);
                        return;
                    }
                    
                    System.out.println("---------org.apache.jasper.servlet.JspServlet.serviceJspFile()---------执行这里吗？YES");
                    
                    boolean isErrorPage = exception != null;
                    // !!!!!!!!!!!!! 包括JSP文件的请求信息 !!!!!!!!!!!!!
                    wrapper = new JspServletWrapper(config, options, jspUri,
                                                    isErrorPage, rctxt);//jsp的配置信息，和 ServletWrapper存储的是<servlet>标签的意义一样
                    
                    rctxt.addWrapper(jspUri,wrapper);//添加进去
                }
            }
        }

        try {
            wrapper.service(request, response, precompile);//执行JSP文件的service  org.apache.jasper.servlet.JspServletWrapper
        } catch (FileNotFoundException fnfe) {
            handleMissingResource(request, response, jspUri);
        }

    }


    private void handleMissingResource(HttpServletRequest request,
            HttpServletResponse response, String jspUri)
            throws ServletException, IOException {

        String includeRequestUri =
            (String)request.getAttribute("javax.servlet.include.request_uri");

        if (includeRequestUri != null) {
            // This file was included. Throw an exception as
            // a response.sendError() will be ignored
            String msg =
                Localizer.getMessage("jsp.error.file.not.found",jspUri);
            // Strictly, filtering this is an application
            // responsibility but just in case...
            throw new ServletException(SecurityUtil.filter(msg));
        } else {
            try {
                response.sendError(HttpServletResponse.SC_NOT_FOUND,
                        request.getRequestURI());
            } catch (IllegalStateException ise) {
                log.error(Localizer.getMessage("jsp.error.file.not.found",
                        jspUri));
            }
        }
        return;
    }


}
