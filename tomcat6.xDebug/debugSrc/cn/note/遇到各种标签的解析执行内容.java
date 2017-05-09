package cn.note;

import org.apache.catalina.connector.Connector;

public class 遇到各种标签的解析执行内容 {

	/**
	<!--
    <Connector executor="tomcatThreadPool"
               port="8080" protocol="HTTP/1.1" 
               connectionTimeout="20000" 
               redirectPort="8443" />
    -->
	 <Connector port="8080" protocol="HTTP/1.1" 
               connectionTimeout="20000" 
               redirectPort="8443" />
     <Connector port="8009" protocol="AJP/1.3" redirectPort="8443" />
	 */
	Connector con = new Connector(attributes.getValue("protocol"));
	
	
/**
   request.setRequestedSessionId
                        (scookie.getValue().toString());
                    request.setRequestedSessionCookie(true);//说明，取得的sessionid是保存在cookie中的
                    request.setRequestedSessionURL(false);//不是保存在URL中
 */
}
