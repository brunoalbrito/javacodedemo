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


package org.apache.catalina.connector;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.catalina.CometEvent;
import org.apache.catalina.Context;
import org.apache.catalina.Globals;
import org.apache.catalina.Host;
import org.apache.catalina.Pipeline;
import org.apache.catalina.Valve;
import org.apache.catalina.Wrapper;
import org.apache.catalina.core.StandardPipeline;
import org.apache.catalina.util.StringManager;
import org.apache.catalina.util.ServerInfo;
import org.apache.catalina.util.URLEncoder;
import org.apache.coyote.ActionCode;
import org.apache.coyote.Adapter;
import org.apache.coyote.RequestInfo;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.apache.tomcat.util.ExceptionUtils;
import org.apache.tomcat.util.buf.B2CConverter;
import org.apache.tomcat.util.buf.ByteChunk;
import org.apache.tomcat.util.buf.CharChunk;
import org.apache.tomcat.util.buf.MessageBytes;
import org.apache.tomcat.util.http.Cookies;
import org.apache.tomcat.util.http.ServerCookie;
import org.apache.tomcat.util.net.SocketStatus;


/**
 * Implementation of a request processor which delegates the processing to a
 * Coyote processor.
 *
 * @author Craig R. McClanahan
 * @author Remy Maucherat
 *
 */

public class CoyoteAdapter implements Adapter {

    private static Log log = LogFactory.getLog(CoyoteAdapter.class);

    // -------------------------------------------------------------- Constants

    private static final String POWERED_BY = "Servlet/2.5 JSP/2.1 " +
            "(" + ServerInfo.getServerInfo() + " Java/" +
            System.getProperty("java.vm.vendor") + "/" +
            System.getProperty("java.runtime.version") + ")";

    public static final int ADAPTER_NOTES = 1;


    protected static final boolean ALLOW_BACKSLASH =
        Boolean.valueOf(System.getProperty("org.apache.catalina.connector.CoyoteAdapter.ALLOW_BACKSLASH", "false")).booleanValue();


    // ----------------------------------------------------------- Constructors


    /**
     * Construct a new CoyoteProcessor associated with the specified connector.
     *
     * @param connector CoyoteConnector that owns this processor
     */
    public CoyoteAdapter(Connector connector) {

        super();
        this.connector = connector;

    }


    // ----------------------------------------------------- Instance Variables


    /**
     * The CoyoteConnector with which this processor is associated.
     */
    private Connector connector = null;


    /**
     * The string manager for this package.
     */
    protected StringManager sm =
        StringManager.getManager(Constants.Package);


    /**
     * Encoder for the Location URL in HTTP redirects.
     */
    protected static URLEncoder urlEncoder;


    // ----------------------------------------------------- Static Initializer


    /**
     * The safe character set.
     */
    static {
        urlEncoder = new URLEncoder();
        urlEncoder.addSafeCharacter('-');
        urlEncoder.addSafeCharacter('_');
        urlEncoder.addSafeCharacter('.');
        urlEncoder.addSafeCharacter('*');
        urlEncoder.addSafeCharacter('/');
    }


    // -------------------------------------------------------- Adapter Methods


    /**
     * Event method.
     *
     * @return false to indicate an error, expected or not
     */
    public boolean event(org.apache.coyote.Request req,
            org.apache.coyote.Response res, SocketStatus status) {

        Request request = (Request) req.getNote(ADAPTER_NOTES);
        Response response = (Response) res.getNote(ADAPTER_NOTES);

        if (request.getWrapper() != null) {

            boolean error = false;
            boolean read = false;
            try {
                if (status == SocketStatus.OPEN) {
                    if (response.isClosed()) {
                        // The event has been closed asynchronously, so call end instead of
                        // read to cleanup the pipeline
                        request.getEvent().setEventType(CometEvent.EventType.END);
                        request.getEvent().setEventSubType(null);
                    } else {
                        try {
                            // Fill the read buffer of the servlet layer
                            if (request.read()) {
                                read = true;
                            }
                        } catch (IOException e) {
                            error = true;
                        }
                        if (read) {
                            request.getEvent().setEventType(CometEvent.EventType.READ);
                            request.getEvent().setEventSubType(null);
                        } else if (error) {
                            request.getEvent().setEventType(CometEvent.EventType.ERROR);
                            request.getEvent().setEventSubType(CometEvent.EventSubType.CLIENT_DISCONNECT);
                        } else {
                            request.getEvent().setEventType(CometEvent.EventType.END);
                            request.getEvent().setEventSubType(null);
                        }
                    }
                } else if (status == SocketStatus.DISCONNECT) {
                    request.getEvent().setEventType(CometEvent.EventType.ERROR);
                    request.getEvent().setEventSubType(CometEvent.EventSubType.CLIENT_DISCONNECT);
                    error = true;
                } else if (status == SocketStatus.ERROR) {
                    request.getEvent().setEventType(CometEvent.EventType.ERROR);
                    request.getEvent().setEventSubType(CometEvent.EventSubType.IOEXCEPTION);
                    error = true;
                } else if (status == SocketStatus.STOP) {
                    request.getEvent().setEventType(CometEvent.EventType.END);
                    request.getEvent().setEventSubType(CometEvent.EventSubType.SERVER_SHUTDOWN);
                } else if (status == SocketStatus.TIMEOUT) {
                    if (response.isClosed()) {
                        // The event has been closed asynchronously, so call end instead of
                        // read to cleanup the pipeline
                        request.getEvent().setEventType(CometEvent.EventType.END);
                        request.getEvent().setEventSubType(null);
                    } else {
                        request.getEvent().setEventType(CometEvent.EventType.ERROR);
                        request.getEvent().setEventSubType(CometEvent.EventSubType.TIMEOUT);
                    }
                }

                req.getRequestProcessor().setWorkerThreadName(Thread.currentThread().getName());

                // Calling the container
                connector.getContainer().getPipeline().getFirst().event(request, response, request.getEvent());

                if (!error && !response.isClosed() && (request.getAttribute(Globals.EXCEPTION_ATTR) != null)) {
                    // An unexpected exception occurred while processing the event, so
                    // error should be called
                    request.getEvent().setEventType(CometEvent.EventType.ERROR);
                    request.getEvent().setEventSubType(null);
                    error = true;
                    connector.getContainer().getPipeline().getFirst().event(request, response, request.getEvent());
                }
                if (response.isClosed() || !request.isComet()) {
                    if (status==SocketStatus.OPEN) {
                        //CometEvent.close was called during an event.
                        request.getEvent().setEventType(CometEvent.EventType.END);
                        request.getEvent().setEventSubType(null);
                        error = true;
                        connector.getContainer().getPipeline().getFirst().event(request, response, request.getEvent());
                    }
                    res.action(ActionCode.ACTION_COMET_END, null);
                } else if (!error && read && request.getAvailable()) {
                    // If this was a read and not all bytes have been read, or if no data
                    // was read from the connector, then it is an error
                    request.getEvent().setEventType(CometEvent.EventType.ERROR);
                    request.getEvent().setEventSubType(CometEvent.EventSubType.IOEXCEPTION);
                    error = true;
                    connector.getContainer().getPipeline().getFirst().event(request, response, request.getEvent());
                }
                return (!error);
            } catch (Throwable t) {
                if (!(t instanceof IOException)) {
                    log.error(sm.getString("coyoteAdapter.service"), t);
                }
                error = true;
                return false;
            } finally {
                req.getRequestProcessor().setWorkerThreadName(null);
                // Recycle the wrapper request and response
                if (error || response.isClosed() || !request.isComet()) {
                    request.recycle();
                    request.setFilterChain(null);
                    response.recycle();
                }
            }

        } else {
            return false;
        }
    }


    /**
     * Service method.
     */
    public void service(org.apache.coyote.Request req,
    	                org.apache.coyote.Response res)
        throws Exception {

    	/**
    	 * 请求对象，响应对象
    	 * req.notes[1]
    	 */
        Request request = (Request) req.getNote(ADAPTER_NOTES);//req.notes[1]
        Response response = (Response) res.getNote(ADAPTER_NOTES);//req.notes[1]
        if (request == null) {//执行这边

            // Create objects 重新赋予request新的意义
        	/**
        	 	connector === org.apache.catalina.connector.Connector
        	 	Request request = new Request();
        		(org.apache.catalina.connector.Request)request.setConnector(org.apache.catalina.connector.Connector);
        	 */
            request = (Request) connector.createRequest();
            request.setCoyoteRequest(req);
            //重新赋予request新的意义
            response = (Response) connector.createResponse();
            response.setCoyoteResponse(res);

            // Link objects 相互交叉关联 
            request.setResponse(response);
            response.setRequest(request);

            // Set as notes 设置进req，和res
            req.setNote(ADAPTER_NOTES, request);
            res.setNote(ADAPTER_NOTES, response);

            // Set query string encoding
            req.getParameters().setQueryStringEncoding
                (connector.getURIEncoding());

        }

        if (connector.getXpoweredBy()) {//是否在返回内容中添加 如下头部。
            response.addHeader("X-Powered-By", POWERED_BY);
        }

        boolean comet = false;

        try {

            // Parse and set Catalina and configuration specific
            // request parameters
        	/**
        	 * new RequestInfo(org.apache.coyote.Request).setWorkerThreadName(Thread.currentThread().getName());
        	 */
            req.getRequestProcessor().setWorkerThreadName(Thread.currentThread().getName());
            /**
		     		解析，并把路径中参数放入request
					解析，并把上下文参数Context和Wrap放入request.getMappingData()
					request.setContext
					request.setContext
					解析URL，或者cookie中SessionID参数：request.setRequestedSessionId(scookie.getValue().toString());

					postParseRequest(org.apache.coyote.Request,org.apache.catalina.connector.Request,org.apache.coyote.Response,org.apache.catalina.connector.Response)
             */
            if (postParseRequest(req, request, res, response)) {//处理映射关系。。。。。。。。。。。。。。。
                // Calling the container
            	/**
            	  
            	  通过查找mserver对象管理器，提取Container对象，在mserver里面Container对象就是Engine对象
            	  container = new org.apache.catalina.core.StandardEngine()
            	  standardPipeline = container.getPipeline() === new StandardPipeline(this)// 类内部实例化好的
            	  valve = standardPipeline.getFirst() === StandardEngineValve(); //类内部实例化好的
            	  valve.invoke(request, response);
            	 
            	 class StandardPipeline
            	 {
            	 	 public StandardPipeline()
            	 	 {
            	 	 	this.container = container
            	 	 }
            	 	 
	            	 public void setBasic(Valve valve) 
	            	 {
	            	 	this.basic = valve;
	            	 }
            	 	 
            	 	 public Valve getFirst() 
            	 	 {
					    //.....
					    return this.basic;// this.basic == new StandardEngineValve()
					    //.....
					 }
            	 }

            	 class StandardEngine
            	 {
            	 	protected Pipeline pipeline = new StandardPipeline(this);// 当调用 object.getPipeline() 的时候，会返回这个对象
            	 	
            	 	public  StandardEngine()//构造函数中设置pipeline
            	 	{
            	 		pipeline.setBasic(new StandardEngineValve());//  在构造函数内设置 StandardPipeline.basic ,会成为 StandardPipeline.getFirst() 的返回结果
            	 	}
            	 	
            	 	public  Pipeline getPipeline()
            	 	{
            	 		return this.pipeline;
            	 	}
            	 }
            	 
            	 
            	 引擎（StandardEngine）->管道（StandardPipeline）->第一个环节（StandardEngineValve）->调用
            	 
            		第一  container = new org.apache.catalina.core.StandardEngine()
	            	第二    standardPipeline = container.getPipeline() === new StandardPipeline(this)// 类内部实例化好的
	            	第三    valve = standardPipeline.getFirst() === StandardEngineValve(); //类内部实例化好的
	            	第四    valve.invoke(request, response);
	            
	            
	           	 org.apache.catalina.connector.Request  request,  已经是二级的对象了
	           	 org.apache.catalina.connector.Response  response 已经是二级的对象了
            	 */
                connector.getContainer().getPipeline().getFirst().invoke(request, response);//链条式层级调用

                if (request.isComet()) {//如果是Comet traffic请求
                    if (!response.isClosed() && !response.isError()) {
                        if (request.getAvailable() || (request.getContentLength() > 0 && (!request.isParametersParsed()))) {
                            // Invoke a read event right away if there are available bytes
                            if (event(req, res, SocketStatus.OPEN)) {
                                comet = true;
                                res.action(ActionCode.ACTION_COMET_BEGIN, null);
                            }
                        } else {
                            comet = true;
                            res.action(ActionCode.ACTION_COMET_BEGIN, null);
                        }
                    } else {//Chain 设置成 null
                        // Clear the filter chain, as otherwise it will not be reset elsewhere
                        // since this is a Comet request
                        request.setFilterChain(null);
                    }
                }

            }

            if (!comet) {
                response.finishResponse();
                req.action(ActionCode.ACTION_POST_REQUEST , null);
            }

        } catch (IOException e) {
            ;
        } finally {
            req.getRequestProcessor().setWorkerThreadName(null);
            // Recycle the wrapper request and response
            if (!comet) {
                request.recycle();
                response.recycle();
            } else {
                // Clear converters so that the minimum amount of memory
                // is used by this processor
                request.clearEncoders();
                response.clearEncoders();
            }
        }

    }


    public void log(org.apache.coyote.Request req,
            org.apache.coyote.Response res, long time) {

        Request request = (Request) req.getNote(ADAPTER_NOTES);
        Response response = (Response) res.getNote(ADAPTER_NOTES);

        if (request == null) {
            // Create objects
            request = connector.createRequest();
            request.setCoyoteRequest(req);
            response = connector.createResponse();
            response.setCoyoteResponse(res);

            // Link objects
            request.setResponse(response);
            response.setRequest(request);

            // Set as notes
            req.setNote(ADAPTER_NOTES, request);
            res.setNote(ADAPTER_NOTES, response);

            // Set query string encoding
            req.getParameters().setQueryStringEncoding
                (connector.getURIEncoding());
        }

        try {
            // Log at the lowest level available. logAccess() will be
            // automatically called on parent containers.
            boolean logged = false;
            if (request.mappingData != null) {
                if (request.mappingData.context != null) {
                    logged = true;
                    ((Context) request.mappingData.context).logAccess(
                            request, response, time, true);
                } else if (request.mappingData.host != null) {
                    logged = true;
                    ((Host) request.mappingData.host).logAccess(
                            request, response, time, true);
                }
            }
            if (!logged) {
                connector.getService().getContainer().logAccess(
                        request, response, time, true);
            }
        } catch (Throwable t) {
            ExceptionUtils.handleThrowable(t);
            log.warn(sm.getString("coyoteAdapter.accesslogFail"), t);
        } finally {
            request.recycle();
            response.recycle();
        }
    }


    // ------------------------------------------------------ Protected Methods


    /**
     * Parse additional request parameters.
     */
    protected boolean postParseRequest(org.apache.coyote.Request req,
                                       Request request,
    		                       org.apache.coyote.Response res,
                                       Response response)
            throws Exception {

        // XXX the processor needs to set a correct scheme and port prior to this point,
        // in ajp13 protocols dont make sense to get the port from the connector..
        // XXX the processor may have set a correct scheme and port prior to this point,
        // in ajp13 protocols dont make sense to get the port from the connector...
        // otherwise, use connector configuration
        if (! req.scheme().isNull()) {//有提供访问协议
            // use processor specified scheme to determine secure state
            request.setSecure(req.scheme().equals("https"));
        } else {
            // use connector scheme and secure configuration, (defaults to
            // "http" and false respectively)
            req.scheme().setString(connector.getScheme());
            request.setSecure(connector.getSecure());
        }

        // FIXME: the code below doesnt belongs to here,
        // this is only have sense
        // in Http11, not in ajp13..
        // At this point the Host header has been processed.
        // Override if the proxyPort/proxyHost are set
        String proxyName = connector.getProxyName();//是用代理访问？
        int proxyPort = connector.getProxyPort();
        if (proxyPort != 0) {
            req.setServerPort(proxyPort);
        }
        if (proxyName != null) {
            req.serverName().setString(proxyName);
        }

        // Copy the raw URI to the decoded URI
        MessageBytes decodedURI = req.decodedURI();
        decodedURI.duplicate(req.requestURI());

        // Parse the path parameters. This will:
        //   - strip out the path parameters
        //   - convert the decodedURI to bytes
        /**
         * 迭代把req的key-value放入request中
         */
        parsePathParameters(req, request);//解析路径的参数!!!!!!!!!!
        
        // URI decoding
        // %xx decoding of the URL
        //转码
        try {
            req.getURLDecoder().convert(decodedURI, false);
        } catch (IOException ioe) {
            res.setStatus(400);
            res.setMessage("Invalid URI: " + ioe.getMessage());
            connector.getService().getContainer().logAccess(
                    request, response, 0, true);
            return false;
        }
        // Normalization 标准化资源请求地址
        if (!normalize(req.decodedURI())) {
            res.setStatus(400);
            res.setMessage("Invalid URI");
            connector.getService().getContainer().logAccess(
                    request, response, 0, true);
            return false;
        }
        
        // Character decoding
        convertURI(decodedURI, request);
        // Check that the URI is still normalized
        if (!checkNormalize(req.decodedURI())) {//检查是否已经标准化
            res.setStatus(400);
            res.setMessage("Invalid URI character encoding");
            connector.getService().getContainer().logAccess(
                    request, response, 0, true);
            return false;
        }

        // Set the remote principal
        String principal = req.getRemoteUser().toString();//远程用户
        if (principal != null) {
            request.setUserPrincipal(new CoyotePrincipal(principal));
        }

        // Set the authorization type
        String authtype = req.getAuthType().toString();//使用授权
        if (authtype != null) {
            request.setAuthType(authtype);
        }

        // Request mapping.
        MessageBytes serverName;
        if (connector.getUseIPVHosts()) {//使用IP访问
            serverName = req.localName();
            if (serverName.isNull()) {
                // well, they did ask for it
                res.action(ActionCode.ACTION_REQ_LOCAL_NAME_ATTRIBUTE, null);
            }
        } else {
            serverName = req.serverName();//使用域名访问
        }
        
        /**
         * 在org.apache.catalina.connector.Connector.start() 中初始化了mapperListener
         * 
         * 
         * 
         * 映射器，设置上下文
         * org.apache.tomcat.util.http.mapper.Mapper
         * new Mapper().map(serverName, decodedURI,request.getMappingData())
         * 
         * 把context设置进request.getMappingData
         * 把wrapper设置进request.getMappingData
         * 
         * * * * * * * * * * * * * * 
         * 			处理映射，匹配 ，保存匹配的数据,定位到Servlet
         * * * * * * * * * * * * * * 
         * 
         */
        System.out.println("decodedURI:"+decodedURI);//decodedURI:/docs
        connector.getMapper().map(serverName, decodedURI,request.getMappingData());
        
        /**
         	request.getMappingData().context: StandardEngine[Catalina].StandardHost[localhost].StandardContext[]
 			request.getMappingData().wrapper.toString(): StandardEngine[Catalina].StandardHost[localhost].StandardContext[].StandardWrapper[default]
         */
        System.out.println("request.getMappingData().context: "+request.getMappingData().context);
        System.out.println("request.getMappingData().wrapper.toString(): "+ request.getMappingData().wrapper);
        /***************Context在这边设置*************************/
        /**
         				objectName === Catalina:j2eeType=WebModule,name=//localhost/,J2EEApplication=none,J2EEServer=none
				     	objectName === Catalina:j2eeType=WebModule,name=//localhost/docs,J2EEApplication=none,J2EEServer=none
				     	
				     	相应的对象是：org.apache.catalina.core.StandardContext
         */
        request.setContext((Context) request.getMappingData().context);//!!!!!!!!!!!!!!!!!!!!!!!!!!!

        // If there is no context at this point, it is likely no ROOT context
        // has been deployed
        if (request.getContext() == null) {
            res.setStatus(404);
            res.setMessage("Not found");
            connector.getService().getContainer().logAccess(request, response, 0, true);
            return false;
        }
        /***************Wrapper在这边设置*************************/
        /**
          			  Catalina:j2eeType=Servlet,name=default,WebModule=//localhost/,J2EEApplication=none,J2EEServer=none
				      Catalina:j2eeType=Servlet,name=jsp,WebModule=//localhost/,J2EEApplication=none,J2EEServer=none
				      Catalina:j2eeType=Servlet,name=default,WebModule=//localhost/docs,J2EEApplication=none,J2EEServer=none
				           
				             相应的对象是： org.apache.catalina.core.StandardWrapper ,就是匹配到<servlet>中定义的类
         */
        request.setWrapper((Wrapper) request.getMappingData().wrapper);//有多少匹配模式：1、前缀、2、文件扩展名、3、欢迎文件 4、tomcat默认 5、.....
        
        System.out.println("---->"+request.getWrapper().getClass());
        // Filter trace method
        if (!connector.getAllowTrace()//允许跟踪
                && req.method().equalsIgnoreCase("TRACE")) {
            Wrapper wrapper = request.getWrapper();
            String header = null;
            if (wrapper != null) {
                String[] methods = wrapper.getServletMethods();
                if (methods != null) {
                    for (int i=0; i<methods.length; i++) {
                        if ("TRACE".equals(methods[i])) {
                            continue;
                        }
                        if (header == null) {
                            header = methods[i];
                        } else {
                            header += ", " + methods[i];
                        }
                    }
                }
            }
            res.setStatus(405);
            res.addHeader("Allow", header);
            res.setMessage("TRACE method is not allowed");
            request.getContext().logAccess(request, response, 0, true);
            return false;
        }

        // Parse session Id
        /**
         * session_id名称，默认使用jsessionid
         * 
         */
        String sessionID =
            request.getPathParameter(Globals.SESSION_PARAMETER_NAME);//jsessionid
        if (sessionID != null && !isURLRewritingDisabled(request)) {//配置读取URL中的jsessionid参数
            request.setRequestedSessionId(sessionID);
            request.setRequestedSessionURL(true);
        }
        /**
         * 从req取得cookie的，如果存在jsessionid的cookie信息，就把COOKIE ID设置进入request
         */
        parseSessionCookiesId(req, request);//解析SESSION ID，取得SessionID

        // Possible redirect
        MessageBytes redirectPathMB = request.getMappingData().redirectPath;//重定向
        if (!redirectPathMB.isNull()) {
            String redirectPath = urlEncoder.encode(redirectPathMB.toString());
            String query = request.getQueryString();
            if (request.isRequestedSessionIdFromURL()) {
                // This is not optimal, but as this is not very common, it
                // shouldn't matter
                redirectPath = redirectPath + ";" + Globals.SESSION_PARAMETER_NAME + "="
                    + request.getRequestedSessionId();
            }
            if (query != null) {
                // This is not optimal, but as this is not very common, it
                // shouldn't matter
                redirectPath = redirectPath + "?" + query;
            }
            response.sendRedirect(redirectPath);
            request.getContext().logAccess(request, response, 0, true);
            return false;
        }

        return true;
    }

    private boolean isURLRewritingDisabled(Request request) {
        Context context = (Context) request.getMappingData().context;
        if (context != null)
            return (context.isDisableURLRewriting());
        else
            return (false);
    }


    /**
     * Extract the path parameters from the request. This assumes parameters are
     * of the form /path;name=value;name2=value2/ etc. Currently only really
     * interested in the session ID that will be in this form. Other parameters
     * can safely be ignored.
     *
     * @param req
     * @param request
     */
    protected void parsePathParameters(org.apache.coyote.Request req,
            Request request) {

        // Process in bytes (this is default format so this is normally a NO-OP
        req.decodedURI().toBytes();

        ByteChunk uriBC = req.decodedURI().getByteChunk();
        int semicolon = uriBC.indexOf(';', 0);

        // What encoding to use? Some platforms, eg z/os, use a default
        // encoding that doesn't give the expected result so be explicit
        String enc = connector.getURIEncoding();
        if (enc == null) {
            enc = "ISO-8859-1";
        }

        if (log.isDebugEnabled()) {
            log.debug(sm.getString("coyoteAdapter.debug", "uriBC",
                    uriBC.toString()));
            log.debug(sm.getString("coyoteAdapter.debug", "semicolon",
                    String.valueOf(semicolon)));
            log.debug(sm.getString("coyoteAdapter.debug", "enc", enc));
        }

        boolean warnedEncoding = false;

        while (semicolon > -1) {
            // Parse path param, and extract it from the decoded request URI
            int start = uriBC.getStart();
            int end = uriBC.getEnd();

            int pathParamStart = semicolon + 1;
            int pathParamEnd = ByteChunk.findBytes(uriBC.getBuffer(),
                    start + pathParamStart, end,
                    new byte[] {';', '/'});

            String pv = null;

            if (pathParamEnd >= 0) {
                try {
                    pv = (new String(uriBC.getBuffer(), start + pathParamStart,
                                pathParamEnd - pathParamStart, enc));
                } catch (UnsupportedEncodingException e) {
                    if (!warnedEncoding) {
                        log.warn(sm.getString("coyoteAdapter.parsePathParam",
                                enc));
                        warnedEncoding = true;
                    }
                }
                // Extract path param from decoded request URI
                byte[] buf = uriBC.getBuffer();
                for (int i = 0; i < end - start - pathParamEnd; i++) {
                    buf[start + semicolon + i]
                        = buf[start + i + pathParamEnd];
                }
                uriBC.setBytes(buf, start,
                        end - start - pathParamEnd + semicolon);
            } else {
                try {
                    pv = (new String(uriBC.getBuffer(), start + pathParamStart,
                                (end - start) - pathParamStart, enc));
                } catch (UnsupportedEncodingException e) {
                    if (!warnedEncoding) {
                        log.warn(sm.getString("coyoteAdapter.parsePathParam",
                                enc));
                        warnedEncoding = true;
                    }
                }
                uriBC.setEnd(start + semicolon);
            }

            if (log.isDebugEnabled()) {
                log.debug(sm.getString("coyoteAdapter.debug", "pathParamStart",
                        String.valueOf(pathParamStart)));
                log.debug(sm.getString("coyoteAdapter.debug", "pathParamEnd",
                        String.valueOf(pathParamEnd)));
                log.debug(sm.getString("coyoteAdapter.debug", "pv", pv));
            }

            if (pv != null) {
                int equals = pv.indexOf('=');
                if (equals > -1) {
                    String name = pv.substring(0, equals);//参数名
                    String value = pv.substring(equals + 1);//参数值
                    request.addPathParameter(name, value);//!!!!!!!!!!!
                    if (log.isDebugEnabled()) {
                        log.debug(sm.getString("coyoteAdapter.debug", "equals",
                                String.valueOf(equals)));
                        log.debug(sm.getString("coyoteAdapter.debug", "name",
                                name));
                        log.debug(sm.getString("coyoteAdapter.debug", "value",
                                value));
                    }
                }
            }

            semicolon = uriBC.indexOf(';', semicolon);
        }
    }


    /**
     * Parse session id in URL.
     * @deprecated Not used since 6.0.33
     */
    @Deprecated
    protected void parseSessionId(org.apache.coyote.Request req, Request request) {

        parsePathParameters(req, request);

        String sessionID =
            request.getPathParameter(Globals.SESSION_PARAMETER_NAME);
        if (sessionID != null) {
            request.setRequestedSessionId(sessionID);
            request.setRequestedSessionURL(true);
        } else {
            clearRequestedSessionURL(request);
        }

    }


    private void clearRequestedSessionURL(Request request) {
        request.setRequestedSessionId(null);
        request.setRequestedSessionURL(false);
    }


    /**
     * Parse session id in URL.
     */
    protected void parseSessionCookiesId(org.apache.coyote.Request req, Request request) {

        // If session tracking via cookies has been disabled for the current
        // context, don't go looking for a session ID in a cookie as a cookie
        // from a parent context with a session ID may be present which would
        // overwrite the valid session ID encoded in the URL
        Context context = (Context) request.getMappingData().context;
        if (context != null && !context.getCookies())
            return;

        // Parse session id from cookies
        Cookies serverCookies = req.getCookies();//使用第一层req解析
        int count = serverCookies.getCookieCount();
        if (count <= 0)
            return;

        String sessionCookieName = getSessionCookieName(context);//如果没有设置，就使用：JSESSIONID

        for (int i = 0; i < count; i++) {
            ServerCookie scookie = serverCookies.getCookie(i);
            if (scookie.getName().equals(sessionCookieName)) {//在cookie中找到JSESSIONID ===JSESSIONID
                // Override anything requested in the URL
                if (!request.isRequestedSessionIdFromCookie()) {
                    // Accept only the first session id cookie
                    convertMB(scookie.getValue());
                    request.setRequestedSessionId
                        (scookie.getValue().toString());//保存JSESSIONID的值
                    request.setRequestedSessionCookie(true);//说明，取得的sessionid是保存在cookie中的
                    request.setRequestedSessionURL(false);//不是保存在URL中
                    if (log.isDebugEnabled())
                        log.debug(" Requested cookie session id is " +
                            request.getRequestedSessionId());
                } else {
                    if (!request.isRequestedSessionIdValid()) {
                        // Replace the session id until one is valid
                        convertMB(scookie.getValue());
                        request.setRequestedSessionId
                            (scookie.getValue().toString());//保存JSESSIONID的值
                    }
                }
            }
        }

    }


    /**
     * Character conversion of the URI.
     */
    protected void convertURI(MessageBytes uri, Request request)
        throws Exception {

        ByteChunk bc = uri.getByteChunk();
        int length = bc.getLength();
        CharChunk cc = uri.getCharChunk();
        cc.allocate(length, -1);

        String enc = connector.getURIEncoding();
        if (enc != null) {
            B2CConverter conv = request.getURIConverter();
            try {
                if (conv == null) {
                    conv = new B2CConverter(enc);
                    request.setURIConverter(conv);
                }
            } catch (IOException e) {
                // Ignore
                log.error("Invalid URI encoding; using HTTP default");
                connector.setURIEncoding(null);
            }
            if (conv != null) {
                try {
                    conv.convert(bc, cc);
                    uri.setChars(cc.getBuffer(), cc.getStart(),
                                 cc.getLength());
                    return;
                } catch (IOException e) {
                    log.error("Invalid URI character encoding; trying ascii");
                    cc.recycle();
                }
            }
        }

        // Default encoding: fast conversion
        byte[] bbuf = bc.getBuffer();
        char[] cbuf = cc.getBuffer();
        int start = bc.getStart();
        for (int i = 0; i < length; i++) {
            cbuf[i] = (char) (bbuf[i + start] & 0xff);
        }
        uri.setChars(cbuf, 0, length);

    }


    /**
     * Character conversion of the a US-ASCII MessageBytes.
     */
    protected void convertMB(MessageBytes mb) {

        // This is of course only meaningful for bytes
        if (mb.getType() != MessageBytes.T_BYTES)
            return;

        ByteChunk bc = mb.getByteChunk();
        CharChunk cc = mb.getCharChunk();
        int length = bc.getLength();
        cc.allocate(length, -1);

        // Default encoding: fast conversion
        byte[] bbuf = bc.getBuffer();
        char[] cbuf = cc.getBuffer();
        int start = bc.getStart();
        for (int i = 0; i < length; i++) {
            cbuf[i] = (char) (bbuf[i + start] & 0xff);
        }
        mb.setChars(cbuf, 0, length);

    }


    /**
     * Normalize URI.
     * <p>
     * This method normalizes "\", "//", "/./" and "/../". This method will
     * return false when trying to go above the root, or if the URI contains
     * a null byte.
     *
     * @param uriMB URI to be normalized
     */
    public static boolean normalize(MessageBytes uriMB) {

        ByteChunk uriBC = uriMB.getByteChunk();
        final byte[] b = uriBC.getBytes();
        final int start = uriBC.getStart();
        int end = uriBC.getEnd();

        // An empty URL is not acceptable
        if (start == end)
            return false;

        // URL * is acceptable
        if ((end - start == 1) && b[start] == (byte) '*')
          return true;

        int pos = 0;
        int index = 0;

        // Replace '\' with '/'
        // Check for null byte
        for (pos = start; pos < end; pos++) {
            if (b[pos] == (byte) '\\') {
                if (ALLOW_BACKSLASH) {
                    b[pos] = (byte) '/';
                } else {
                    return false;
                }
            }
            if (b[pos] == (byte) 0) {
                return false;
            }
        }

        // The URL must start with '/'
        if (b[start] != (byte) '/') {
            return false;
        }

        // Replace "//" with "/"
        for (pos = start; pos < (end - 1); pos++) {
            if (b[pos] == (byte) '/') {
                while ((pos + 1 < end) && (b[pos + 1] == (byte) '/')) {
                    copyBytes(b, pos, pos + 1, end - pos - 1);
                    end--;
                }
            }
        }

        // If the URI ends with "/." or "/..", then we append an extra "/"
        // Note: It is possible to extend the URI by 1 without any side effect
        // as the next character is a non-significant WS.
        if (((end - start) >= 2) && (b[end - 1] == (byte) '.')) {
            if ((b[end - 2] == (byte) '/')
                || ((b[end - 2] == (byte) '.')
                    && (b[end - 3] == (byte) '/'))) {
                b[end] = (byte) '/';
                end++;
            }
        }

        uriBC.setEnd(end);

        index = 0;

        // Resolve occurrences of "/./" in the normalized path
        while (true) {
            index = uriBC.indexOf("/./", 0, 3, index);
            if (index < 0)
                break;
            copyBytes(b, start + index, start + index + 2,
                      end - start - index - 2);
            end = end - 2;
            uriBC.setEnd(end);
        }

        index = 0;

        // Resolve occurrences of "/../" in the normalized path
        while (true) {
            index = uriBC.indexOf("/../", 0, 4, index);
            if (index < 0)
                break;
            // Prevent from going outside our context
            if (index == 0)
                return false;
            int index2 = -1;
            for (pos = start + index - 1; (pos >= 0) && (index2 < 0); pos --) {
                if (b[pos] == (byte) '/') {
                    index2 = pos;
                }
            }
            copyBytes(b, start + index2, start + index + 3,
                      end - start - index - 3);
            end = end + index2 - index - 3;
            uriBC.setEnd(end);
            index = index2;
        }

        return true;

    }


    /**
     * Check that the URI is normalized following character decoding.
     * <p>
     * This method checks for "\", 0, "//", "/./" and "/../". This method will
     * return false if sequences that are supposed to be normalized are still
     * present in the URI.
     *
     * @param uriMB URI to be checked (should be chars)
     */
    public static boolean checkNormalize(MessageBytes uriMB) {

        CharChunk uriCC = uriMB.getCharChunk();
        char[] c = uriCC.getChars();
        int start = uriCC.getStart();
        int end = uriCC.getEnd();

        int pos = 0;

        // Check for '\' and 0
        for (pos = start; pos < end; pos++) {
            if (c[pos] == '\\') {
                return false;
            }
            if (c[pos] == 0) {
                return false;
            }
        }

        // Check for "//"
        for (pos = start; pos < (end - 1); pos++) {
            if (c[pos] == '/') {
                if (c[pos + 1] == '/') {
                    return false;
                }
            }
        }

        // Check for ending with "/." or "/.."
        if (((end - start) >= 2) && (c[end - 1] == '.')) {
            if ((c[end - 2] == '/')
                    || ((c[end - 2] == '.')
                    && (c[end - 3] == '/'))) {
                return false;
            }
        }

        // Check for "/./"
        if (uriCC.indexOf("/./", 0, 3, 0) >= 0) {
            return false;
        }

        // Check for "/../"
        if (uriCC.indexOf("/../", 0, 4, 0) >= 0) {
            return false;
        }

        return true;

    }


    /**
     * Copy an array of bytes to a different position. Used during
     * normalization.
     */
    protected static void copyBytes(byte[] b, int dest, int src, int len) {
        for (int pos = 0; pos < len; pos++) {
            b[pos + dest] = b[pos + src];
        }
    }
    private String getSessionCookieName(Context context) {

        String result = null;

        if (context != null) {
            result = context.getSessionCookieName();
        }

        if (result == null) {
            result = Globals.SESSION_COOKIE_NAME;
        }

        return result;
    }
}
