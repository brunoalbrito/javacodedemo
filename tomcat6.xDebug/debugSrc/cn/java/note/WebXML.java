package cn.java.note;

import org.apache.catalina.startup.CallMethodMultiRule;
import org.apache.catalina.startup.CallParamMultiRule;
import org.apache.catalina.startup.IgnoreAnnotationsRule;
import org.apache.catalina.startup.SecurityRoleRefCreateRule;
import org.apache.catalina.startup.ServiceQnameRule;
import org.apache.catalina.startup.SetAuthConstraintRule;
import org.apache.catalina.startup.SetDistributableRule;
import org.apache.catalina.startup.SetJspConfig;
import org.apache.catalina.startup.SetLoginConfig;
import org.apache.catalina.startup.SetNextNamingRule;
import org.apache.catalina.startup.SetPublicIdRule;
import org.apache.catalina.startup.SetSessionConfig;
import org.apache.catalina.startup.SoapHeaderRule;
import org.apache.catalina.startup.WrapperCreateRule;
import org.apache.tomcat.util.digester.Digester;
import org.apache.tomcat.util.digester.SetNextRule;

/**
 web.xml配置文件
 */
public class WebXML {
	
    /**
    org.apache.catalina.startup.WebRuleSet


    <web-app>
         <context-param>
	         <param-name></param-name>
	         <param-value></param-value>
         </context-param>
         <display-name></display-name>
         <distributable></distributable>
         <ejb-local-ref>
	         <description></description>
	         <ejb-link></ejb-link>
	         <ejb-ref-name></ejb-ref-name>
	         <ejb-ref-type></ejb-ref-type>
	         <local></local>
	         <local-home></local-home>
         </ejb-local-ref>
         <ejb-ref>
	         <description></description>
	         <ejb-link></ejb-link>
	         <ejb-ref-name></ejb-ref-name>
	         <ejb-ref-type></ejb-ref-type>
	         <home></home>
	         <remote></remote>
         </ejb-ref>
    	 <env-entry>
    	 	<description></description>
    	 	<env-entry-name></env-entry-name>
    	 	<env-entry-type></env-entry-type>
    	 	<env-entry-value></env-entry-value>
    	 </env-entry>
    	 <error-page>
    	 	<error-code></error-code>
    	 	<error-type></error-type>
    	 	<location></location>
    	 </error-page>
    	 <filter>
    	 	<description></description>
    	 	<display-name></display-name>
    	 	<filter-class></filter-class>
    	 	<filter-name></filter-name>
    	 	<large-icon></large-icon>
    	 	<small-icon></small-icon>
    	 	<init-param>
    	 		<param-name></param-name>
	         	<param-value></param-value>
    	 	</init-param>
    	 	<></>
    	 </filter>
    	 <filter-mapping>
    	 	<filter-name></filter-name>
    	 	<servlet-name></servlet-name>
    	 	<url-pattern></url-pattern>
    	 	<dispatcher></dispatcher>
    	 	<></>
    	 	<></>
    	 </filter-mapping>
    	 <listener>
    	 	<listener-class></listener-class>
    	 </listener>
    	 <jsp-config>
    	 	<jsp-config>
    	 		<jsp-property-group>
    	 			<url-pattern></url-pattern>
    	 		</jsp-property-group>
    	 	</jsp-config>
    	 </jsp-config>
    	 <login-config>
    	 	<auth-method></auth-method>
    	 	<realm-name></realm-name>
    	 	<form-login-config>
    	 		<form-error-page></form-error-page>
    	 		<form-login-page></form-login-page>
    	 	</form-login-config>
    	 </login-config>
    	 <mime-mapping>
    	 	<extension></extension>
    	 	<mime-type></mime-type>
    	 </mime-mapping>
		 <resource-env-ref>
		 	<resource-env-ref-name></resource-env-ref-name>
		 	<resource-env-ref-type></resource-env-ref-type>
		 </resource-env-ref>
		 <message-destination>
		 	<description></description>
		 	<display-name></display-name>
		 	<message-destination-name></message-destination-name>
		 	<icon>
		 		<large-icon></large-icon>
		 		<small-icon></small-icon>
		 	</icon>
		 </message-destination>
    	 <message-destination-ref></message-destination-ref>
    </web-app>
    
    */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public void addRuleInstances(Digester digester) {
        sessionConfig = new SetSessionConfig();
        jspConfig = new SetJspConfig();
        loginConfig = new SetLoginConfig();
        
       
        digester.addRule(prefix + "web-app",
                         new SetPublicIdRule("setPublicId"));
        digester.addRule(prefix + "web-app",
                         new IgnoreAnnotationsRule());

        digester.addCallMethod(prefix + "web-app/context-param",
                               "addParameter", 2);
        digester.addCallParam(prefix + "web-app/context-param/param-name", 0);
        digester.addCallParam(prefix + "web-app/context-param/param-value", 1);

        digester.addCallMethod(prefix + "web-app/display-name",
                               "setDisplayName", 0);

        digester.addRule(prefix + "web-app/distributable",
                         new SetDistributableRule());

        digester.addObjectCreate(prefix + "web-app/ejb-local-ref",
                                 "org.apache.catalina.deploy.ContextLocalEjb");
        digester.addRule(prefix + "web-app/ejb-local-ref",
                new SetNextNamingRule("addLocalEjb",
                            "org.apache.catalina.deploy.ContextLocalEjb"));

        digester.addCallMethod(prefix + "web-app/ejb-local-ref/description",
                               "setDescription", 0);
        digester.addCallMethod(prefix + "web-app/ejb-local-ref/ejb-link",
                               "setLink", 0);
        digester.addCallMethod(prefix + "web-app/ejb-local-ref/ejb-ref-name",
                               "setName", 0);
        digester.addCallMethod(prefix + "web-app/ejb-local-ref/ejb-ref-type",
                               "setType", 0);
        digester.addCallMethod(prefix + "web-app/ejb-local-ref/local",
                               "setLocal", 0);
        digester.addCallMethod(prefix + "web-app/ejb-local-ref/local-home",
                               "setHome", 0);

        digester.addObjectCreate(prefix + "web-app/ejb-ref",
                                 "org.apache.catalina.deploy.ContextEjb");
        digester.addRule(prefix + "web-app/ejb-ref",
                new SetNextNamingRule("addEjb",
                            "org.apache.catalina.deploy.ContextEjb"));

        digester.addCallMethod(prefix + "web-app/ejb-ref/description",
                               "setDescription", 0);
        digester.addCallMethod(prefix + "web-app/ejb-ref/ejb-link",
                               "setLink", 0);
        digester.addCallMethod(prefix + "web-app/ejb-ref/ejb-ref-name",
                               "setName", 0);
        digester.addCallMethod(prefix + "web-app/ejb-ref/ejb-ref-type",
                               "setType", 0);
        digester.addCallMethod(prefix + "web-app/ejb-ref/home",
                               "setHome", 0);
        digester.addCallMethod(prefix + "web-app/ejb-ref/remote",
                               "setRemote", 0);
      
        digester.addObjectCreate(prefix + "web-app/env-entry",
                                 "org.apache.catalina.deploy.ContextEnvironment");
        digester.addRule(prefix + "web-app/env-entry",
                new SetNextNamingRule("addEnvironment",
                            "org.apache.catalina.deploy.ContextEnvironment"));

        digester.addCallMethod(prefix + "web-app/env-entry/description",
                               "setDescription", 0);
        digester.addCallMethod(prefix + "web-app/env-entry/env-entry-name",
                               "setName", 0);
        digester.addCallMethod(prefix + "web-app/env-entry/env-entry-type",
                               "setType", 0);
        digester.addCallMethod(prefix + "web-app/env-entry/env-entry-value",
                               "setValue", 0);

        digester.addObjectCreate(prefix + "web-app/error-page",
                                 "org.apache.catalina.deploy.ErrorPage");
        digester.addSetNext(prefix + "web-app/error-page",
                            "addErrorPage",
                            "org.apache.catalina.deploy.ErrorPage");

        digester.addCallMethod(prefix + "web-app/error-page/error-code",
                               "setErrorCode", 0);
        digester.addCallMethod(prefix + "web-app/error-page/exception-type",
                               "setExceptionType", 0);
        digester.addCallMethod(prefix + "web-app/error-page/location",
                               "setLocation", 0);

        digester.addObjectCreate(prefix + "web-app/filter",
                                 "org.apache.catalina.deploy.FilterDef");
        digester.addSetNext(prefix + "web-app/filter",
                            "addFilterDef",
                            "org.apache.catalina.deploy.FilterDef");

        digester.addCallMethod(prefix + "web-app/filter/description",
                               "setDescription", 0);
        digester.addCallMethod(prefix + "web-app/filter/display-name",
                               "setDisplayName", 0);
        digester.addCallMethod(prefix + "web-app/filter/filter-class",
                               "setFilterClass", 0);
        digester.addCallMethod(prefix + "web-app/filter/filter-name",
                               "setFilterName", 0);
        digester.addCallMethod(prefix + "web-app/filter/large-icon",
                               "setLargeIcon", 0);
        digester.addCallMethod(prefix + "web-app/filter/small-icon",
                               "setSmallIcon", 0);

        digester.addCallMethod(prefix + "web-app/filter/init-param",
                               "addInitParameter", 2);
        digester.addCallParam(prefix + "web-app/filter/init-param/param-name",
                              0);
        digester.addCallParam(prefix + "web-app/filter/init-param/param-value",
                              1);

        digester.addObjectCreate(prefix + "web-app/filter-mapping",
                                 "org.apache.catalina.deploy.FilterMap");
        digester.addSetNext(prefix + "web-app/filter-mapping",
                                 "addFilterMap",
                                 "org.apache.catalina.deploy.FilterMap");

        digester.addCallMethod(prefix + "web-app/filter-mapping/filter-name",
                               "setFilterName", 0);
        digester.addCallMethod(prefix + "web-app/filter-mapping/servlet-name",
                               "addServletName", 0);
        digester.addCallMethod(prefix + "web-app/filter-mapping/url-pattern",
                               "addURLPattern", 0);

        digester.addCallMethod(prefix + "web-app/filter-mapping/dispatcher",
                               "setDispatcher", 0);

        digester.addCallMethod(prefix + "web-app/listener/listener-class",
                                "addApplicationListener", 0);
         
        digester.addRule(prefix + "web-app/jsp-config",
                         jspConfig);
        
        digester.addCallMethod(prefix + "web-app/jsp-config/jsp-property-group/url-pattern",
                               "addJspMapping", 0);

        digester.addRule(prefix + "web-app/login-config",
                         loginConfig);

        digester.addObjectCreate(prefix + "web-app/login-config",
                                 "org.apache.catalina.deploy.LoginConfig");
        digester.addSetNext(prefix + "web-app/login-config",
                            "setLoginConfig",
                            "org.apache.catalina.deploy.LoginConfig");

        digester.addCallMethod(prefix + "web-app/login-config/auth-method",
                               "setAuthMethod", 0);
        digester.addCallMethod(prefix + "web-app/login-config/realm-name",
                               "setRealmName", 0);
        digester.addCallMethod(prefix + "web-app/login-config/form-login-config/form-error-page",
                               "setErrorPage", 0);
        digester.addCallMethod(prefix + "web-app/login-config/form-login-config/form-login-page",
                               "setLoginPage", 0);

        digester.addCallMethod(prefix + "web-app/mime-mapping",
                               "addMimeMapping", 2);
        digester.addCallParam(prefix + "web-app/mime-mapping/extension", 0);
        digester.addCallParam(prefix + "web-app/mime-mapping/mime-type", 1);

        digester.addObjectCreate(prefix + "web-app/resource-env-ref",
            "org.apache.catalina.deploy.ContextResourceEnvRef");
        digester.addRule(prefix + "web-app/resource-env-ref",
                    new SetNextNamingRule("addResourceEnvRef",
                        "org.apache.catalina.deploy.ContextResourceEnvRef"));

        digester.addCallMethod(prefix + "web-app/resource-env-ref/resource-env-ref-name",
                "setName", 0);
        digester.addCallMethod(prefix + "web-app/resource-env-ref/resource-env-ref-type",
                "setType", 0);

        digester.addObjectCreate(prefix + "web-app/message-destination",
                                 "org.apache.catalina.deploy.MessageDestination");
        digester.addSetNext(prefix + "web-app/message-destination",
                            "addMessageDestination",
                            "org.apache.catalina.deploy.MessageDestination");

        digester.addCallMethod(prefix + "web-app/message-destination/description",
                               "setDescription", 0);
        digester.addCallMethod(prefix + "web-app/message-destination/display-name",
                               "setDisplayName", 0);
        digester.addCallMethod(prefix + "web-app/message-destination/icon/large-icon",
                               "setLargeIcon", 0);
        digester.addCallMethod(prefix + "web-app/message-destination/icon/small-icon",
                               "setSmallIcon", 0);
        digester.addCallMethod(prefix + "web-app/message-destination/message-destination-name",
                               "setName", 0);

        digester.addObjectCreate(prefix + "web-app/message-destination-ref",
                                 "org.apache.catalina.deploy.MessageDestinationRef");
        digester.addSetNext(prefix + "web-app/message-destination-ref",
                            "addMessageDestinationRef",
                            "org.apache.catalina.deploy.MessageDestinationRef");

        digester.addCallMethod(prefix + "web-app/message-destination-ref/description",
                               "setDescription", 0);
        digester.addCallMethod(prefix + "web-app/message-destination-ref/message-destination-link",
                               "setLink", 0);
        digester.addCallMethod(prefix + "web-app/message-destination-ref/message-destination-ref-name",
                               "setName", 0);
        digester.addCallMethod(prefix + "web-app/message-destination-ref/message-destination-type",
                               "setType", 0);
        digester.addCallMethod(prefix + "web-app/message-destination-ref/message-destination-usage",
                               "setUsage", 0);

        digester.addObjectCreate(prefix + "web-app/resource-ref",
                                 "org.apache.catalina.deploy.ContextResource");
        digester.addRule(prefix + "web-app/resource-ref",
                new SetNextNamingRule("addResource",
                            "org.apache.catalina.deploy.ContextResource"));

        digester.addCallMethod(prefix + "web-app/resource-ref/description",
                               "setDescription", 0);
        digester.addCallMethod(prefix + "web-app/resource-ref/res-auth",
                               "setAuth", 0);
        digester.addCallMethod(prefix + "web-app/resource-ref/res-ref-name",
                               "setName", 0);
        digester.addCallMethod(prefix + "web-app/resource-ref/res-sharing-scope",
                               "setScope", 0);
        digester.addCallMethod(prefix + "web-app/resource-ref/res-type",
                               "setType", 0);

        digester.addObjectCreate(prefix + "web-app/security-constraint",
                                 "org.apache.catalina.deploy.SecurityConstraint");
        digester.addSetNext(prefix + "web-app/security-constraint",
                            "addConstraint",
                            "org.apache.catalina.deploy.SecurityConstraint");

        digester.addRule(prefix + "web-app/security-constraint/auth-constraint",
                         new SetAuthConstraintRule());
        digester.addCallMethod(prefix + "web-app/security-constraint/auth-constraint/role-name",
                               "addAuthRole", 0);
        digester.addCallMethod(prefix + "web-app/security-constraint/display-name",
                               "setDisplayName", 0);
        digester.addCallMethod(prefix + "web-app/security-constraint/user-data-constraint/transport-guarantee",
                               "setUserConstraint", 0);

        digester.addObjectCreate(prefix + "web-app/security-constraint/web-resource-collection",
                                 "org.apache.catalina.deploy.SecurityCollection");
        digester.addSetNext(prefix + "web-app/security-constraint/web-resource-collection",
                            "addCollection",
                            "org.apache.catalina.deploy.SecurityCollection");
        digester.addCallMethod(prefix + "web-app/security-constraint/web-resource-collection/http-method",
                               "addMethod", 0);
        digester.addCallMethod(prefix + "web-app/security-constraint/web-resource-collection/url-pattern",
                               "addPattern", 0);
        digester.addCallMethod(prefix + "web-app/security-constraint/web-resource-collection/web-resource-name",
                               "setName", 0);

        digester.addCallMethod(prefix + "web-app/security-role/role-name",
                               "addSecurityRole", 0);

        digester.addObjectCreate(prefix + "web-app/service-ref",
                                 "org.apache.catalina.deploy.ContextService");
        digester.addRule(prefix + "web-app/service-ref",
                         new SetNextNamingRule("addService",
                         "org.apache.catalina.deploy.ContextService"));

        digester.addCallMethod(prefix + "web-app/service-ref/description",
                               "setDescription", 0);
        digester.addCallMethod(prefix + "web-app/service-ref/display-name",
                               "setDisplayname", 0);
        digester.addCallMethod(prefix + "web-app/service-ref/icon",
                               "setIcon", 0);
        digester.addCallMethod(prefix + "web-app/service-ref/service-ref-name",
                               "setName", 0);
        digester.addCallMethod(prefix + "web-app/service-ref/service-interface",
                               "setType", 0);
        digester.addCallMethod(prefix + "web-app/service-ref/wsdl-file",
                               "setWsdlfile", 0);
        digester.addCallMethod(prefix + "web-app/service-ref/jaxrpc-mapping-file",
                               "setJaxrpcmappingfile", 0);
        digester.addRule(prefix + "web-app/service-ref/service-qname", new ServiceQnameRule());

        digester.addRule(prefix + "web-app/service-ref/port-component-ref",
                               new CallMethodMultiRule("addPortcomponent", 2, 1));
        digester.addCallParam(prefix + "web-app/service-ref/port-component-ref/service-endpoint-interface", 0);
        digester.addRule(prefix + "web-app/service-ref/port-component-ref/port-component-link", new CallParamMultiRule(1));

        digester.addObjectCreate(prefix + "web-app/service-ref/handler",
                                 "org.apache.catalina.deploy.ContextHandler");
        digester.addRule(prefix + "web-app/service-ref/handler",
                         new SetNextRule("addHandler",
                         "org.apache.catalina.deploy.ContextHandler"));
        
        digester.addCallMethod(prefix + "web-app/service-ref/handler/handler-name",
                               "setName", 0);
        digester.addCallMethod(prefix + "web-app/service-ref/handler/handler-class",
                               "setHandlerclass", 0);

        digester.addCallMethod(prefix + "web-app/service-ref/handler/init-param",
                               "setProperty", 2);
        digester.addCallParam(prefix + "web-app/service-ref/handler/init-param/param-name",
                              0);
        digester.addCallParam(prefix + "web-app/service-ref/handler/init-param/param-value",
                              1);

        digester.addRule(prefix + "web-app/service-ref/handler/soap-header", new SoapHeaderRule());

        digester.addCallMethod(prefix + "web-app/service-ref/handler/soap-role",
                               "addSoapRole", 0);
        digester.addCallMethod(prefix + "web-app/service-ref/handler/port-name",
                               "addPortName", 0);
        
        digester.addRule(prefix + "web-app/servlet",
                         new WrapperCreateRule());
        digester.addSetNext(prefix + "web-app/servlet",
                            "addChild",
                            "org.apache.catalina.Container");

        digester.addCallMethod(prefix + "web-app/servlet/init-param",
                               "addInitParameter", 2);
        digester.addCallParam(prefix + "web-app/servlet/init-param/param-name",
                              0);
        digester.addCallParam(prefix + "web-app/servlet/init-param/param-value",
                              1);

        digester.addCallMethod(prefix + "web-app/servlet/jsp-file",
                               "setJspFile", 0);
        digester.addCallMethod(prefix + "web-app/servlet/load-on-startup",
                               "setLoadOnStartupString", 0);
        digester.addCallMethod(prefix + "web-app/servlet/run-as/role-name",
                               "setRunAs", 0);

        digester.addRule(prefix + "web-app/servlet/security-role-ref",
                new SecurityRoleRefCreateRule());
        digester.addCallMethod(
                prefix + "web-app/servlet/security-role-ref/role-link",
                "setLink", 0);
        digester.addCallMethod(
                prefix + "web-app/servlet/security-role-ref/role-name",
                "setName", 0);

        digester.addCallMethod(prefix + "web-app/servlet/servlet-class",
                              "setServletClass", 0);
        digester.addCallMethod(prefix + "web-app/servlet/servlet-name",
                              "setName", 0);

        digester.addRule(prefix + "web-app/servlet-mapping",
                               new CallMethodMultiRule("addServletMapping", 2, 0));
        digester.addCallParam(prefix + "web-app/servlet-mapping/servlet-name", 1);
        digester.addRule(prefix + "web-app/servlet-mapping/url-pattern", new CallParamMultiRule(0));

        digester.addRule(prefix + "web-app/session-config",
                         sessionConfig);
        
        digester.addCallMethod(prefix + "web-app/session-config/session-timeout",
                               "setSessionTimeout", 1,
                               new Class[] { Integer.TYPE });
        digester.addCallParam(prefix + "web-app/session-config/session-timeout", 0);

        digester.addCallMethod(prefix + "web-app/taglib",
                               "addTaglib", 2);
        digester.addCallParam(prefix + "web-app/taglib/taglib-location", 1);
        digester.addCallParam(prefix + "web-app/taglib/taglib-uri", 0);

        digester.addCallMethod(prefix + "web-app/welcome-file-list/welcome-file",
                               "addWelcomeFile", 0);

        digester.addCallMethod(prefix + "web-app/locale-encoding-mapping-list/locale-encoding-mapping",
                              "addLocaleEncodingMappingParameter", 2);
        digester.addCallParam(prefix + "web-app/locale-encoding-mapping-list/locale-encoding-mapping/locale", 0);
        digester.addCallParam(prefix + "web-app/locale-encoding-mapping-list/locale-encoding-mapping/encoding", 1);

    }
}
