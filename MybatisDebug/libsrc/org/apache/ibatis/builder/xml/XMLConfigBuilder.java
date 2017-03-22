/**
 *    Copyright 2009-2016 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.apache.ibatis.builder.xml;

import java.io.InputStream;
import java.io.Reader;
import java.util.Properties;
import javax.sql.DataSource;

import org.apache.ibatis.builder.BaseBuilder;
import org.apache.ibatis.builder.BuilderException;
import org.apache.ibatis.datasource.DataSourceFactory;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.executor.loader.ProxyFactory;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.io.VFS;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.parsing.XPathParser;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaClass;
import org.apache.ibatis.reflection.ReflectorFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.apache.ibatis.session.AutoMappingBehavior;
import org.apache.ibatis.session.AutoMappingUnknownColumnBehavior;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.LocalCacheScope;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.type.JdbcType;

/**
 * @author Clinton Begin
 * @author Kazuki Shimizu
 */
public class XMLConfigBuilder extends BaseBuilder {

  private boolean parsed;
  private XPathParser parser;
  private String environment;
  private ReflectorFactory localReflectorFactory = new DefaultReflectorFactory();

  public XMLConfigBuilder(Reader reader) {
    this(reader, null, null);
  }

  public XMLConfigBuilder(Reader reader, String environment) {
    this(reader, environment, null);
  }

  public XMLConfigBuilder(Reader reader, String environment, Properties props) {
    this(new XPathParser(reader, true, props, new XMLMapperEntityResolver()), environment, props);
  }

  public XMLConfigBuilder(InputStream inputStream) {
    this(inputStream, null, null);
  }

  public XMLConfigBuilder(InputStream inputStream, String environment) {
    this(inputStream, environment, null);
  }

  public XMLConfigBuilder(InputStream inputStream, String environment, Properties props) {
    this(new XPathParser(inputStream, true, props, new XMLMapperEntityResolver()), environment, props);
  }

  private XMLConfigBuilder(XPathParser parser, String environment, Properties props) {
    super(new Configuration());
    ErrorContext.instance().resource("SQL Mapper Configuration");
    this.configuration.setVariables(props);
    this.parsed = false;
    this.environment = environment;
    this.parser = parser;
  }

  public Configuration parse() {
    if (parsed) {
      throw new BuilderException("Each XMLConfigBuilder can only be used once.");
    }
    parsed = true;
    parseConfiguration(parser.evalNode("/configuration"));
    return configuration;
  }

  private void parseConfiguration(XNode root) {
    try {
    	/*
	    	 <configuration>
				<settings>
					<property name="propertie1" value="propertie1Value" />
					<property name="propertie2" value="propertie2Value" />
					<property name="vfsImpl" value="cn.java.MyVfsImpl" />
				</settings>
				
				<properties resource=""><!-- configuration.setVariables(defaults); -->
					<property name="propertie1" value="propertie1Value" />
					<property name="propertie2" value="propertie2Value" />
				</properties>
				
				<typeAliases>
					<package name=""></package> <!-- configuration.getTypeAliasRegistry().registerAliases(typeAliasPackage); -->
					<package name=""></package>
					<typeAlias alias="User" type="cn.java.entity.User" />  <!-- configuration.getTypeAliasRegistry().registerAlias(alias, clazz);-->
					<typeAlias alias="User" type="cn.java.entity.User" />  
				</typeAliases>
				
				<plugins> <!--  configuration.addInterceptor(interceptorInstance); -->
					<plugin interceptor="">
						<property name="propertie1" value="propertie1Value" />
						<property name="propertie2" value="propertie2Value" />
					</plugin>
					<plugin interceptor="">
						<property name="propertie1" value="propertie1Value" />
						<property name="propertie2" value="propertie2Value" />
					</plugin>
				</plugins>
				
				<objectFactory type=""> <!-- configuration.setObjectFactory(factory); -->
					<property name="propertie1" value="propertie1Value" />
					<property name="propertie2" value="propertie2Value" />
				</objectFactory>
			
				<objectWrapperFactory type="" /> <!-- configuration.setObjectWrapperFactory(factory); -->
				
				<reflectorFactory type="" /> <!-- configuration.setReflectorFactory(factory); -->
				
				<environments default="">
					<environment id="demo">  <!-- configuration.setEnvironment(environmentBuilder.build()); -->
						<transactionManager type="JDBC" />
						<dataSource type="POOLED">
							<property value="${driver}" name="driver" />
							<property value="${url}" name="url" />
							<property value="${username}" name="username" />
							<property value="${password}" name="password" />
						</dataSource>
					</environment>
				</environments>
			
				<databaseIdProvider type="VENDOR"> <!--  configuration.setDatabaseId(databaseId); -->
					<properties name="" value="" />
					<properties name="" value="" />
					<properties name="" value="" />
				</databaseIdProvider>
			
				<typeHandlers>
					<package name="" /> <!--  configuration.getTypeHandlerRegistry.register(typeHandlerPackage); -->
					<package name="" />
					<typeHandler javaType="" jdbcType="" handler="" /> <!--  configuration.getTypeHandlerRegistry.register(javaTypeClass, jdbcType, typeHandlerClass); -->
					<typeHandler javaType="" jdbcType="" handler="" />
				</typeHandlers>
				
				<mappers>
					<package name="" /> <!-- configuration.addMappers(mapperPackage); -->
					<package name="" />
					<mapper resource="cn/java/mapper/UserMapper.xml" />
					<mapper url="cn/java/mapper/UserMapper.xml" />
					<mapper class="" /> <!-- configuration.addMapper(mapperInterface); -->
				</mappers>
	    	 </configuration>
	    	 
    	 */
      Properties settings = settingsAsPropertiess(root.evalNode("settings"));
      //issue #117 read properties first
      propertiesElement(root.evalNode("properties"));
      loadCustomVfs(settings);
      typeAliasesElement(root.evalNode("typeAliases"));
      pluginElement(root.evalNode("plugins"));
      objectFactoryElement(root.evalNode("objectFactory"));
      objectWrapperFactoryElement(root.evalNode("objectWrapperFactory"));
      reflectorFactoryElement(root.evalNode("reflectorFactory"));
      settingsElement(settings);
      // read it after objectFactory and objectWrapperFactory issue #631
      environmentsElement(root.evalNode("environments"));
      databaseIdProviderElement(root.evalNode("databaseIdProvider"));
      typeHandlerElement(root.evalNode("typeHandlers"));
      mapperElement(root.evalNode("mappers"));
    } catch (Exception e) {
      throw new BuilderException("Error parsing SQL Mapper Configuration. Cause: " + e, e);
    }
  }

  private Properties settingsAsPropertiess(XNode context) {
    if (context == null) {
      return new Properties();
    }
    Properties props = context.getChildrenAsProperties();
    // Check that all settings are known to the configuration class
    MetaClass metaConfig = MetaClass.forClass(Configuration.class, localReflectorFactory);
    for (Object key : props.keySet()) {
      if (!metaConfig.hasSetter(String.valueOf(key))) {
        throw new BuilderException("The setting " + key + " is not known.  Make sure you spelled it correctly (case sensitive).");
      }
    }
    return props;
  }

  private void loadCustomVfs(Properties props) throws ClassNotFoundException {
    String value = props.getProperty("vfsImpl");
    if (value != null) {
      String[] clazzes = value.split(",");
      for (String clazz : clazzes) {
        if (!clazz.isEmpty()) {
          @SuppressWarnings("unchecked")
          Class<? extends VFS> vfsImpl = (Class<? extends VFS>)Resources.classForName(clazz);
          configuration.setVfsImpl(vfsImpl);
        }
      }
    }
  }

  private void typeAliasesElement(XNode parent) {
    if (parent != null) {
      /*
	       <typeAliases>
		       <package name=""></package>
		       <package name=""></package>
		       <typeAlias alias="User" type="cn.java.entity.User" />
		       <typeAlias alias="User" type="cn.java.entity.User" />
		   </typeAliases>
       */
      for (XNode child : parent.getChildren()) {
        if ("package".equals(child.getName())) {
          String typeAliasPackage = child.getStringAttribute("name");
          configuration.getTypeAliasRegistry().registerAliases(typeAliasPackage);
        } else {
          String alias = child.getStringAttribute("alias");
          String type = child.getStringAttribute("type");
          try {
            Class<?> clazz = Resources.classForName(type);
            if (alias == null) {
              typeAliasRegistry.registerAlias(clazz);
            } else {
              typeAliasRegistry.registerAlias(alias, clazz);
            }
          } catch (ClassNotFoundException e) {
            throw new BuilderException("Error registering typeAlias for '" + alias + "'. Cause: " + e, e);
          }
        }
      }
    }
  }

  private void pluginElement(XNode parent) throws Exception {
    if (parent != null) {
      for (XNode child : parent.getChildren()) {
        String interceptor = child.getStringAttribute("interceptor");
        Properties properties = child.getChildrenAsProperties();
        Interceptor interceptorInstance = (Interceptor) resolveClass(interceptor).newInstance();
        interceptorInstance.setProperties(properties);
        configuration.addInterceptor(interceptorInstance);
      }
    }
  }

  private void objectFactoryElement(XNode context) throws Exception {
    if (context != null) {
      String type = context.getStringAttribute("type");
      Properties properties = context.getChildrenAsProperties();
      ObjectFactory factory = (ObjectFactory) resolveClass(type).newInstance();
      factory.setProperties(properties);
      configuration.setObjectFactory(factory);
    }
  }

  private void objectWrapperFactoryElement(XNode context) throws Exception {
    if (context != null) {
      String type = context.getStringAttribute("type");
      ObjectWrapperFactory factory = (ObjectWrapperFactory) resolveClass(type).newInstance();
      configuration.setObjectWrapperFactory(factory);
    }
  }

  private void reflectorFactoryElement(XNode context) throws Exception {
    if (context != null) {
       String type = context.getStringAttribute("type");
       ReflectorFactory factory = (ReflectorFactory) resolveClass(type).newInstance();
       configuration.setReflectorFactory(factory);
    }
  }

  private void propertiesElement(XNode context) throws Exception {
    if (context != null) {
      Properties defaults = context.getChildrenAsProperties();
      String resource = context.getStringAttribute("resource");
      String url = context.getStringAttribute("url");
      if (resource != null && url != null) {
        throw new BuilderException("The properties element cannot specify both a URL and a resource based property file reference.  Please specify one or the other.");
      }
      if (resource != null) {
        defaults.putAll(Resources.getResourceAsProperties(resource));
      } else if (url != null) {
        defaults.putAll(Resources.getUrlAsProperties(url));
      }
      Properties vars = configuration.getVariables();
      if (vars != null) {
        defaults.putAll(vars);
      }
      parser.setVariables(defaults);
      configuration.setVariables(defaults);
    }
  }

  private void settingsElement(Properties props) throws Exception {
    configuration.setAutoMappingBehavior(AutoMappingBehavior.valueOf(props.getProperty("autoMappingBehavior", "PARTIAL")));
    configuration.setAutoMappingUnknownColumnBehavior(AutoMappingUnknownColumnBehavior.valueOf(props.getProperty("autoMappingUnknownColumnBehavior", "NONE")));
    configuration.setCacheEnabled(booleanValueOf(props.getProperty("cacheEnabled"), true));
    configuration.setProxyFactory((ProxyFactory) createInstance(props.getProperty("proxyFactory")));
    configuration.setLazyLoadingEnabled(booleanValueOf(props.getProperty("lazyLoadingEnabled"), false));
    configuration.setAggressiveLazyLoading(booleanValueOf(props.getProperty("aggressiveLazyLoading"), true));
    configuration.setMultipleResultSetsEnabled(booleanValueOf(props.getProperty("multipleResultSetsEnabled"), true));
    configuration.setUseColumnLabel(booleanValueOf(props.getProperty("useColumnLabel"), true));
    configuration.setUseGeneratedKeys(booleanValueOf(props.getProperty("useGeneratedKeys"), false));
    configuration.setDefaultExecutorType(ExecutorType.valueOf(props.getProperty("defaultExecutorType", "SIMPLE")));
    configuration.setDefaultStatementTimeout(integerValueOf(props.getProperty("defaultStatementTimeout"), null));
    configuration.setDefaultFetchSize(integerValueOf(props.getProperty("defaultFetchSize"), null));
    configuration.setMapUnderscoreToCamelCase(booleanValueOf(props.getProperty("mapUnderscoreToCamelCase"), false));
    configuration.setSafeRowBoundsEnabled(booleanValueOf(props.getProperty("safeRowBoundsEnabled"), false));
    configuration.setLocalCacheScope(LocalCacheScope.valueOf(props.getProperty("localCacheScope", "SESSION")));
    configuration.setJdbcTypeForNull(JdbcType.valueOf(props.getProperty("jdbcTypeForNull", "OTHER")));
    configuration.setLazyLoadTriggerMethods(stringSetValueOf(props.getProperty("lazyLoadTriggerMethods"), "equals,clone,hashCode,toString"));
    configuration.setSafeResultHandlerEnabled(booleanValueOf(props.getProperty("safeResultHandlerEnabled"), true));
    configuration.setDefaultScriptingLanguage(resolveClass(props.getProperty("defaultScriptingLanguage")));
    configuration.setCallSettersOnNulls(booleanValueOf(props.getProperty("callSettersOnNulls"), false));
    configuration.setUseActualParamName(booleanValueOf(props.getProperty("useActualParamName"), false));
    configuration.setLogPrefix(props.getProperty("logPrefix"));
    @SuppressWarnings("unchecked")
    Class<? extends Log> logImpl = (Class<? extends Log>)resolveClass(props.getProperty("logImpl"));
    configuration.setLogImpl(logImpl);
    configuration.setConfigurationFactory(resolveClass(props.getProperty("configurationFactory")));
  }

  private void environmentsElement(XNode context) throws Exception {
    if (context != null) {
    	/*
	      <environments default="">
	      	<environment id="" > <!-- configuration.setEnvironment(environmentBuilder.build()); -->
	      		<transactionManager></transactionManager>
	      		<dataSource></dataSource>
	      	</environment>
	      </environments>
    	 */
      if (environment == null) {
        environment = context.getStringAttribute("default");
      }
      for (XNode child : context.getChildren()) {
        String id = child.getStringAttribute("id");
        if (isSpecifiedEnvironment(id)) {
          TransactionFactory txFactory = transactionManagerElement(child.evalNode("transactionManager")); // 如 org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory
          DataSourceFactory dsFactory = dataSourceElement(child.evalNode("dataSource")); // 如 org.apache.ibatis.datasource.pooled.PooledDataSourceFactory
          DataSource dataSource = dsFactory.getDataSource();
          Environment.Builder environmentBuilder = new Environment.Builder(id)
              .transactionFactory(txFactory)
              .dataSource(dataSource);
          configuration.setEnvironment(environmentBuilder.build());
        }
      }
    }
  }

  private void databaseIdProviderElement(XNode context) throws Exception {
	 /**
	  <databaseIdProvider type="VENDOR">
	  	<properties name="" value="" />
	  	<properties name="" value="" />
	  	<properties name="" value="" />
	  </databaseIdProvider>
	  */
    DatabaseIdProvider databaseIdProvider = null;
    if (context != null) {
      String type = context.getStringAttribute("type");
      // awful patch to keep backward compatibility
      if ("VENDOR".equals(type)) {
          type = "DB_VENDOR"; // org.apache.ibatis.mapping.VendorDatabaseIdProvider
      }
      Properties properties = context.getChildrenAsProperties();
      databaseIdProvider = (DatabaseIdProvider) resolveClass(type).newInstance();
      databaseIdProvider.setProperties(properties);
    }
    Environment environment = configuration.getEnvironment();
    if (environment != null && databaseIdProvider != null) {
      String databaseId = databaseIdProvider.getDatabaseId(environment.getDataSource());
      configuration.setDatabaseId(databaseId);
    }
  }

  private TransactionFactory transactionManagerElement(XNode context) throws Exception {
    if (context != null) {
      String type = context.getStringAttribute("type");
      Properties props = context.getChildrenAsProperties();
      TransactionFactory factory = (TransactionFactory) resolveClass(type).newInstance();
      factory.setProperties(props);
      return factory;
    }
    throw new BuilderException("Environment declaration requires a TransactionFactory.");
  }

  private DataSourceFactory dataSourceElement(XNode context) throws Exception {
    if (context != null) {
      String type = context.getStringAttribute("type");
      Properties props = context.getChildrenAsProperties();
      DataSourceFactory factory = (DataSourceFactory) resolveClass(type).newInstance();
      factory.setProperties(props);
      return factory;
    }
    throw new BuilderException("Environment declaration requires a DataSourceFactory.");
  }

  private void typeHandlerElement(XNode parent) throws Exception {
	 /*
	  <typeHandlers>
	  	<package name="" />
	  	<package name="" />
	  	<typeHandler javaType="" jdbcType="" handler="" />
	  	<typeHandler javaType="" jdbcType="" handler="" />
	  </typeHandlers>
	  */
    if (parent != null) {
      for (XNode child : parent.getChildren()) {
        if ("package".equals(child.getName())) {
          String typeHandlerPackage = child.getStringAttribute("name");
          typeHandlerRegistry.register(typeHandlerPackage);
        } else {
          String javaTypeName = child.getStringAttribute("javaType");
          String jdbcTypeName = child.getStringAttribute("jdbcType");
          String handlerTypeName = child.getStringAttribute("handler");
          Class<?> javaTypeClass = resolveClass(javaTypeName);
          JdbcType jdbcType = resolveJdbcType(jdbcTypeName);
          Class<?> typeHandlerClass = resolveClass(handlerTypeName);
          if (javaTypeClass != null) {
            if (jdbcType == null) {
              typeHandlerRegistry.register(javaTypeClass, typeHandlerClass);
            } else {
              typeHandlerRegistry.register(javaTypeClass, jdbcType, typeHandlerClass);
            }
          } else {
            typeHandlerRegistry.register(typeHandlerClass);
          }
        }
      }
    }
  }

  private void mapperElement(XNode parent) throws Exception {
	 /*
	  	<mappers>
	  		<package name="" /> <!-- configuration.addMappers(mapperPackage); -->
	  		<package name="" />
	  		<mapper resource="cn/java/mapper/UserMapper.xml" />
	  		<mapper url="cn/java/mapper/UserMapper.xml" />
	  		<mapper class="" /> <!-- configuration.addMapper(mapperInterface); -->
	  	</mappers>
	  */
    if (parent != null) {
      for (XNode child : parent.getChildren()) {
        if ("package".equals(child.getName())) {
          String mapperPackage = child.getStringAttribute("name");
          configuration.addMappers(mapperPackage);
        } else {
          String resource = child.getStringAttribute("resource");
          String url = child.getStringAttribute("url");
          String mapperClass = child.getStringAttribute("class");
          if (resource != null && url == null && mapperClass == null) {
            ErrorContext.instance().resource(resource);
            InputStream inputStream = Resources.getResourceAsStream(resource);
            /*
             ------ mapper.xml ------
             <mapper namespace="namespace1"> <!-- builderAssistant.setCurrentNamespace("namespace1"); -->
		    	<cache-ref namespace="cacheRefNamespace1" /> <!-- configuration.addCacheRef("namespace1","cacheRefNamespace1"); builderAssistant.useCacheRef("cacheRefNamespace1"); -->
		    	<cache type="PERPETUAL" eviction="LRU" flushInterval="" size="" readOnly="false" blocking="false"  > <!-- builderAssistant.useNewCache(typeClass, evictionClass, flushInterval, size, readWrite, blocking, props); -->
			 		<property name="" value="" />
			 		<property name="" value="" />
			 	</cache>
			 	
			 	<!-- parameterMap -->
			 	<parameterMap id="" type=""> <!-- builderAssistant.addParameterMap(id, parameterClass, parameterMappings); -->
			 		<parameter property="" javaType="" jdbcType="" resultMap="" mode="" typeHandler="" numericScale="" />
			 		<parameter property="" javaType="" jdbcType="" resultMap="" mode="" typeHandler="" numericScale="" />
			 	</parameterMap>
			 	<parameterMap id="" type="">
			 		<parameter property="" javaType="" jdbcType="" resultMap="" mode="" typeHandler="" numericScale="" />
			 		<parameter property="" javaType="" jdbcType="" resultMap="" mode="" typeHandler="" numericScale="" />
			 	</parameterMap>
			 	
			 	<!-- resultMap -->
			 	<resultMap id="" type="类型优先级1" ofType="类型优先级2" resultType="类型优先级3" javaType="类型优先级4" extends="" autoMapping=""> <!-- assistant.addResultMap(this.id, this.type, this.extend, this.discriminator, this.resultMappings, this.autoMapping); -->
		   			<constructor></constructor>
		   			<discriminator></discriminator>
		   			<id></id>
		   		</resultMap>
			 	<resultMap id="" type="类型优先级1" ofType="类型优先级2" resultType="类型优先级3" javaType="类型优先级4" extends="" autoMapping="">
		   			<constructor></constructor>
		   			<discriminator></discriminator>
		   			<id></id>
		   		</resultMap>
		   		
		   		<!-- sql -->
		   		<sql databaseId="" id="namespace1.sql1" /> <!-- sqlFragments.put("namespace1.sql1", context); -->
		   		<sql databaseId="" id="namespace1.sql1" />
		   		
		   		<!-- select|insert|update|delete -->
		   		<select id="" databaseId="" fetchSize="" timeout="" parameterMap="" parameterType="" resultMap="" resultType="" lang="" resultSetType="" statementType="" 
			 		flushCache="" useCache="" resultOrdered="" resultSets="" resultOrdered="" resultSets="" keyProperty="" keyColumn="" useGeneratedKeys=""> <!-- builderAssistant.addMappedStatement(...); -->
				 	<include refid="" />
				 	<selectKey resultType="" statementType="" keyProperty="" keyColumn="" order="BEFORE"></selectKey>
				 	<selectKey resultType="" statementType="" keyProperty="" keyColumn="" order="BEFORE"></selectKey>
				 </select>
			 	<insert id="" databaseId="" fetchSize="" timeout="" parameterMap="" parameterType="" resultMap="" resultType="" lang="" resultSetType="" statementType="" 
				 		flushCache="" useCache="" resultOrdered="" resultSets="" resultOrdered="" resultSets="" keyProperty="" keyColumn="" useGeneratedKeys="">
				 	<include refid="" />
				 	<selectKey resultType="" statementType="" keyProperty="" keyColumn="" order="BEFORE"></selectKey>
				 	<selectKey resultType="" statementType="" keyProperty="" keyColumn="" order="BEFORE"></selectKey>
				 </insert>
			 	<update id="" databaseId="" fetchSize="" timeout="" parameterMap="" parameterType="" resultMap="" resultType="" lang="" resultSetType="" statementType="" 
				 		flushCache="" useCache="" resultOrdered="" resultSets="" resultOrdered="" resultSets="" keyProperty="" keyColumn="" useGeneratedKeys="">
				 	<include refid="" />
				 	<selectKey resultType="" statementType="" keyProperty="" keyColumn="" order="BEFORE"></selectKey>
				 	<selectKey resultType="" statementType="" keyProperty="" keyColumn="" order="BEFORE"></selectKey>
				 </update>
			 	<delete id="" databaseId="" fetchSize="" timeout="" parameterMap="" parameterType="" resultMap="" resultType="" lang="" resultSetType="" statementType="" 
				 		flushCache="" useCache="" resultOrdered="" resultSets="" resultOrdered="" resultSets="" keyProperty="" keyColumn="" useGeneratedKeys="">
				 	<include refid="" />
				 	<selectKey resultType="" statementType="" keyProperty="" keyColumn="" order="BEFORE"></selectKey>
				 	<selectKey resultType="" statementType="" keyProperty="" keyColumn="" order="BEFORE"></selectKey>
				 </delete>
		     </mapper>
             */
            XMLMapperBuilder mapperParser = new XMLMapperBuilder(inputStream, configuration, resource, configuration.getSqlFragments());
            mapperParser.parse();
          } else if (resource == null && url != null && mapperClass == null) {
            ErrorContext.instance().resource(url);
            InputStream inputStream = Resources.getUrlAsStream(url);
            XMLMapperBuilder mapperParser = new XMLMapperBuilder(inputStream, configuration, url, configuration.getSqlFragments());
            mapperParser.parse();
          } else if (resource == null && url == null && mapperClass != null) {
            Class<?> mapperInterface = Resources.classForName(mapperClass);
            configuration.addMapper(mapperInterface);
          } else {
            throw new BuilderException("A mapper element may only specify a url, resource or class, but not more than one.");
          }
        }
      }
    }
  }

  private boolean isSpecifiedEnvironment(String id) {
    if (environment == null) {
      throw new BuilderException("No environment specified.");
    } else if (id == null) {
      throw new BuilderException("Environment requires an id attribute.");
    } else if (environment.equals(id)) {
      return true;
    }
    return false;
  }

}
