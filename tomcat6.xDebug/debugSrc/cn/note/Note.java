package cn.note;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.catalina.connector.CoyoteAdapter;
import org.apache.coyote.ProtocolHandler;
import org.apache.juli.logging.LogFactory;
import org.apache.tomcat.util.IntrospectionUtils;
import org.apache.tomcat.util.log.SystemLogHandler;
import org.apache.tomcat.util.net.JIoEndpoint.Acceptor;

/**
 * 一个server.xml文件只能有一个Server子标签  	org.apache.catalina.Server
 * 一个server标签可以有多个Service子标签		org.apache.catalina.core.StandardService
 * 一个Service标签可以有多个Executor子标签		org.apache.catalina.core.StandardThreadExecutor
 * 一个Service标签可以有多个Connector子标签  	org.apache.catalina.connector.Connector
 * 一个Service标签可以有一个Engine子标签  		org.apache.catalina.core.StandardEngine
 * 一个Engine标签可以有一个Cluster子标签
 * 一个Engine标签可以有多个Host子标签  			org.apache.catalina.core.StandardHost


 <Server>
 	<Service>
	 	<Executor></Executor>
	 	<Executor></Executor>
	 	...
	 	<Connector org.apache.coyote.http11.Http11Protocol HTTP/1.1 ></Connector>
	 	<Connector org.apache.jk.server.JkCoyoteHandler AJP/1.3></Connector>
	 	...
	 	<Engine>
			<Cluster></Cluster>
		</Engine>
 	</Service>
 	<Service>
 		<Executor></Executor>
	 	<Executor></Executor>
	 	...
	 	<Connector></Connector>
	 	<Connector></Connector>
	 	...
		<Engine>
			<Cluster></Cluster>
		</Engine>
 	</Service>
 </Server>
 
 Executor
 	WorkerStack
 进入核心：
Server
	Service
		Connector.initialize()
		 * org.apache.coyote.http11.Http11Protocol,通过反射映射标签中的属性，动态创建对象protocolHandler = new Http11Protocol()
         * org.apache.jk.server.JkCoyoteHandler，通过反射映射标签中的属性，动态创建对象protocolHandler
         * adapter = new CoyoteAdapter(Connector);
         * protocolHandler.setAdapter(adapter);
 
 */
/**
		2014-9-2 0:05:59 org.apache.catalina.core.AprLifecycleListener init
		信息: The APR based Apache Tomcat Native library which allows optimal performanc
		e in production environments was not found on the java.library.path: E:\jdk1.6.0
		_45\bin;C:\Windows\Sun\Java\bin;C:\Windows\system32;C:\Windows;C:\Program Files
		(x86)\NVIDIA Corporation\PhysX\Common;.;E:\jdk1.6.0_45\bin;C:\Windows\system32;C
		:\Windows;C:\Windows\System32\Wbem;C:\Windows\System32\WindowsPowerShell\v1.0\;C
		:\Program Files (x86)\Intel\OpenCL SDK\3.0\bin\x86;C:\Program Files (x86)\Intel\
		OpenCL SDK\3.0\bin\x64;c:\Program Files (x86)\Microsoft SQL Server\90\Tools\binn
		\;D:\cygwin\bin;D:\vs2008\Microsoft Visual Studio 9.0\VC\bin;C:\wamp\bin\mysql\m
		ysql5.1.32\bin;C:\Program Files (x86)\MIT\Kerberos\bin;C:\wamp\bin\php\php-5.4.2
		8-Win32-VC9-x86;D:\Git\bin;E:\VisualSVN Server\bin;E:\TortoiseSVN\bin;.
		
		2014-9-2 0:06:00 org.apache.coyote.http11.Http11Protocol init
		信息: Initializing Coyote HTTP/1.1 on http-8080
		
		2014-9-2 0:06:00 org.apache.catalina.startup.Catalina load
		信息: Initialization processed in 1317 ms  //org.apache.catalina.startup.Catalina.load()结尾
		
		2014-9-2 0:06:01 org.apache.catalina.core.StandardService start
		信息: Starting service Catalina
		
		2014-9-2 0:06:01 org.apache.catalina.core.StandardEngine start
		信息: Starting Servlet Engine: Apache Tomcat/6.0.41
		
		2014-9-2 0:06:01 org.apache.catalina.startup.HostConfig deployDirectory
		信息: Deploying web application directory docs
		2014-9-2 0:06:01 org.apache.catalina.startup.HostConfig deployDirectory
		信息: Deploying web application directory examples
		2014-9-2 0:06:02 org.apache.catalina.startup.HostConfig deployDirectory
		信息: Deploying web application directory host-manager
		2014-9-2 0:06:02 org.apache.catalina.startup.HostConfig deployDirectory
		信息: Deploying web application directory manager
		2014-9-2 0:06:02 org.apache.catalina.startup.HostConfig deployDirectory
		信息: Deploying web application directory ROOT
		
		2014-9-2 0:06:02 org.apache.coyote.http11.Http11Protocol start
		信息: Starting Coyote HTTP/1.1 on http-8080
		
		2014-9-2 0:06:02 org.apache.jk.common.ChannelSocket init
		信息: JK: ajp13 listening on /0.0.0.0:8009
		
		2014-9-2 0:06:02 org.apache.jk.server.JkMain start
		信息: Jk running ID=0 time=0/32  config=null
		
		2014-9-2 0:06:02 org.apache.catalina.startup.Catalina start
		信息: Server startup in 1572 ms
 */
public class Note {
	System.arraycopy(oldMap, 0, newMap, 0, pos + 1);
	//目录名  和文件名
	defaultContextFile.getParentFile(), defaultContextFile.getName()
	public static void main(String[] args) throws Exception {
		
		//取得类加载器
		ClassLoader classLoader=Thread.currentThread().getContextClassLoader();
       
		//动态创建对象
		Class clazz = Class.forName(protocolHandlerClassName);
        this.protocolHandler = (ProtocolHandler) clazz.newInstance();
		
        //反射注入
        IntrospectionUtils.setProperty(protocolHandler, "jkHome",
                                       System.getProperty("catalina.base"));
        
        // Start acceptor threads 开始N个接受线程
        for (int i = 0; i < acceptorThreadCount; i++) {
        	//org.apache.tomcat.util.net.JIoEndpoint.Acceptor 处理线程
            Thread acceptorThread = new Thread(new Acceptor(), getName() + "-Acceptor-" + i);
            acceptorThread.setPriority(threadPriority);
            acceptorThread.setDaemon(daemon);
            acceptorThread.start();
        }
        
		long t1 = System.currentTimeMillis();
		long t2 = System.nanoTime();
		Thread awaitThread = Thread.currentThread();
		Thread.sleep(10000);
		// Runtime.getRuntime().addShutdownHook(shutdownHook);
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
        SystemLogHandler systemlog = new SystemLogHandler(System.out);
        System.setOut(systemlog);
        System.setErr(systemlog);
        System.arraycopy(services, 0, results, 0, services.length);
        
        //使用日志系统
        log = LogFactory.getLog("org.apache.tomcat.util.digester.Digester");
        saxLog = LogFactory.getLog("org.apache.tomcat.util.digester.Digester.sax");
	}

	public void socketDemo() throws Exception {
		ServerSocket serverSocket = new ServerSocket(8005, 1,               InetAddress.getByName("localhost"));//创建Socket 端口是：8005;
		Socket socket = serverSocket.accept();//监听socket
		socket.setSoTimeout(10 * 1000);  // Ten seconds  十秒
		InputStream stream = socket.getInputStream();//读取输入流
		serverSocket.close();
		
		
	}
}

// Policy.getPolicy();
Class<?> policyClass = Class
        .forName("javax.security.auth.Policy");
Method method = policyClass.getMethod("getPolicy");
method.invoke(null);

Class<?> clazz = Class.forName("sun.misc.GC");//内存回收
Method method = clazz.getDeclaredMethod(
        "requestLatency",
        new Class[] {long.class});
method.invoke(null, Long.valueOf(Long.MAX_VALUE - 1));


Class.forName("javax.security.auth.login.Configuration", true, ClassLoader.getSystemClassLoader());



/**
 CURRENT_DIR=%cd% = F:\apache-tomcat-6.0.41\bin

CATALINA_HOME = F:\apache-tomcat-6.0.41

EXECUTABLE=%CATALINA_HOME%\bin\catalina.bat = F:\apache-tomcat-6.0.41\bin\catalina.bat

CMD_LINE_ARGS=

call "%EXECUTABLE%" start %CMD_LINE_ARGS%
//---------------------
CATALINA_BASE = CATALINA_HOME = F:\apache-tomcat-6.0.41


BASEDIR=%CATALINA_HOME% = F:\apache-tomcat-6.0.41

call "%CATALINA_HOME%\bin\setclasspath.bat" %1
{
	JRE_HOME=%JAVA_HOME%
	JAVA_ENDORSED_DIRS=%BASEDIR%\endorsed = F:\apache-tomcat-6.0.41\endorsed
	_RUNJAVA="%JRE_HOME%\bin\java"
	_RUNJDB="%JAVA_HOME%\bin\jdb"
}
CATALINA_TMPDIR=%CATALINA_BASE%\temp
CLASSPATH=%CLASSPATH%;
CLASSPATH=%CLASSPATH%%CATALINA_HOME%\bin\bootstrap.jar
LOGGING_CONFIG=-Dnop
LOGGING_CONFIG=-Djava.util.logging.config.file="%CATALINA_BASE%\conf\logging.properties" = F:\apache-tomcat-6.0.41\conf\logging.properties

JAVA_OPTS=%JAVA_OPTS% %LOGGING_CONFIG%

LOGGING_MANAGER=-Djava.util.logging.manager=org.apache.juli.ClassLoaderLogManager
JAVA_OPTS=%JAVA_OPTS% %LOGGING_MANAGER%

//-------------------
set _EXECJAVA=%_RUNJAVA%
set MAINCLASS=org.apache.catalina.startup.Bootstrap
set ACTION=start
set SECURITY_POLICY_FILE=
set DEBUG_OPTS=
set JPDA=

TITLE=Tomcat

_EXECJAVA=start "%TITLE%" %_RUNJAVA%

CMD_LINE_ARGS=

CMD_LINE_ARGS=%CMD_LINE_ARGS% %1

%_EXECJAVA% %JAVA_OPTS% %CATALINA_OPTS% %DEBUG_OPTS% -Djava.endorsed.dirs="%JAVA_ENDORSED_DIRS%" -classpath "%CLASSPATH%" -Dcatalina.base="%CATALINA_BASE%" -Dcatalina.home="%CATALINA_HOME%" -Djava.io.tmpdir="%CATALINA_TMPDIR%" %MAINCLASS% %CMD_LINE_ARGS% %ACTION%

//执行命令
%JRE_HOME%\bin\java 
	-Djava.util.logging.config.file="%CATALINA_BASE%\conf\logging.properties" //%JAVA_OPTS%
	-Djava.util.logging.manager=org.apache.juli.ClassLoaderLogManager	
	//%CATALINA_OPTS%
	//%DEBUG_OPTS%
	-Djava.endorsed.dirs="%JAVA_ENDORSED_DIRS%"
	-classpath "%CLASSPATH%"
	-Dcatalina.base="%CATALINA_BASE%"
	-Dcatalina.home="%CATALINA_HOME%"
	-Djava.io.tmpdir="%CATALINA_TMPDIR%"
	org.apache.catalina.startup.Bootstrap
	//%CMD_LINE_ARGS% 
	start

 
 */
