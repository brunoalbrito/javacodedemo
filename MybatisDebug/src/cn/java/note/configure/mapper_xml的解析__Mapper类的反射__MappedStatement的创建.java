package cn.java.note.configure;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.binding.MapperMethod.ParamMap;
import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.mapping.ParameterMap;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.StatementType;

public class mapper_xml的解析__Mapper类的反射__MappedStatement的创建 {

	/**
	 	{{
		 	// TestMapper.xml - xml文件构建器 
		 	// org.apache.ibatis.builder.xml.XMLMapperBuilder
		 	XMLMapperBuilder xmlParser = new XMLMapperBuilder(inputStream, assistant.getConfiguration(), xmlResource, configuration.getSqlFragments(), type.getName());
	    	xmlParser.parse();
    	}}
	 	---------------- <mappers> 标签的解析方式 ----------------------------
	 	<mappers><!-- 包名、XML文件路径、实际Mapper类 -->
			<mapper resource="cn/java/mapper/UserMapper.xml" />
			<mapper class="cn.java.mapper1.UserMapper" /> <!-- configuration.addMapper(mapperInterface); -->
		</mappers>
		
		1、如果mapper指向的是接口 cn.java.mapper1.UserMapper
			- 尝试加载并解析 cn/java/mapper1/UserMapper.xml 文件
			- 反射接口 cn.java.mapper1.UserMapper的注解信息
		2、如果mapper指向的是文件 cn/java/mapper/UserMapper.xml
			- 加载并解析 cn/java/mapper1/UserMapper.xml 文件
			- 尝试加载UserMapper.xml 文件中 <mapper namespace="com.test.bean.TestMapper"> 声明的命名空间，如果可以加载，那么反射该接口的注解信息
		
	 	
	 	
	 	{{
		 	// cn.java.mapper.TestMapper.class - 注解构建器
	    	// org.apache.ibatis.builder.annotation.MapperAnnotationBuilder
	    	MapperAnnotationBuilder parser = new MapperAnnotationBuilder(config, type); // 反射Mapper类中的注解，把配置注入到全局Configuration中
	    	parser.parse();
	 	}}
	 	---------------- 反射 Mapper接口的注解 ----------------------------
	 	1、解析类上的注解 @CacheNamespace，把缓存的信息注入到全局对象Configuration
	 	2、解析类上的注解 @CacheNamespaceRef
	 	3、解析方法参数的类型：（要求单参数，可以有四种可以能的类型）
	 			org.apache.ibatis.session.RowBounds
	 			org.apache.ibatis.session.ResultHandler
	 			org.apache.ibatis.binding.MapperMethod.ParamMap
	 			cn.java.bean.User
	 	4、解析方法上的注解 @Lang
	 	5、解析方法上的注解 @Select 和 注解 @SelectProvider
	 		- 解析SQL语句
	 			- 替换SQL中全局占位符（在<properties..>定义）
	 			- 继续处理SQL语句
	 				- 如果包含标签${...} 表示该SQL语句是动态的 ，用org.apache.ibatis.scripting.xmltags.DynamicSqlSource 包装SQL语句（在实际调用的时候：走OGNL表达式解析(OGNL只是简单识别替换，没有做安全处理)，再走 #{..}表达式解析）
	 				- 如果不包含${}标签，那么解析 #{} 标签 ，用 org.apache.ibatis.scripting.defaults.RawSqlSource 包装SQL语句（内部会解析SQL，并做相关处理）
	 	6、解析方法上的注解 @Options
	 	7、解析方法上的注解 @SelectKey
	 	8、解析方法上的注解 @ResultMap 或者 (@ConstructorArgs、@Results、@TypeDiscriminator)
	 	
	 	configuration = {
       		caches ： { // @CacheNamespace
       			"cn.java.TestMapper" ：new BlockingCache(SynchronizedCache(LoggingCache(SerializedCache(ScheduledCache(new LruCache(new PerpetualCache("com.test.bean.TestMapper")))))))
       		},
       		keyGenerators : { // @SelectKey
       			"cn.java.TestMappper.mehtod1!selectKey" : new SelectKeyGenerator(keyStatement, executeBefore) //new NoKeyGenerator()
       		}
       		mappedStatements : {
       			"cn.java.TestMappper.mehtod1!selectKey" : new MappedStatement()
       			"cn.java.TestMappper.mehtod1" : new MappedStatement()
       		}
       	}
       	
       	---------------- 解析 Mapper.xml 文件 ----------------------------
       	1、解析 Mapper.xml 文件，获取相关配置注入全局对象Configuration
       	2、尝试加载 Mapper.xml 文件中 <mapper namespace="com.test.bean.TestMapper"> 声明的命名空间
       		- 如果该命名空间是有效的类，那么走反射 Mapper接口的注解的流程
       		- 如果该命名空间是无效的类，那么忽略
       		
       	configuration = {
       		caches ： { // <cache>
       			"cn.java.TestMapper" ：new BlockingCache(SynchronizedCache(LoggingCache(SerializedCache(ScheduledCache(new LruCache(new PerpetualCache("com.test.bean.TestMapper")))))))
       		},
       		cacheRefMap ： { // <cache-ref>
       			"cn.java.TestMapper" : "cn.java.Test2Mapper" // TestMapper 引用 Test2Mapper 的缓存
       		},
       		parameterMaps : { // <parameterMap id="parameterMap1" type="">
       			"cn.java.TestMapper.parameterMap1" : new ParameterMap()
       			"cn.java.TestMapper.parameterMap2" : new ParameterMap()
       		}
       		resultMaps : { // <resultMap id="resultMap1">
       			"cn.java.TestMapper.resultMap1" : new ResultMap()
       			"cn.java.TestMapper.resultMap2" : new ResultMap()
       		},
       		sqlFragments : { // <sql databaseId="MySql" id="sql1" />
       			"cn.java.TestMapper.sql1" : new XNode()
       			"cn.java.TestMapper.sql2" : new XNode()
       		},
       		keyGenerators : { // <select id="mehtod1"> <selectKey resultType=""> </select>
       			"cn.java.TestMappper.mehtod1!selectKey" : new SelectKeyGenerator(keyStatement, executeBefore) // new NoKeyGenerator()
       		}
       		mappedStatements : { 
       			"cn.java.TestMappper.mehtod1!selectKey" : new MappedStatement() // <select id="mehtod1"> <selectKey resultType=""> </select>
       			"cn.java.TestMappper.mehtod1" : new MappedStatement() // <select id="mehtod1">
       		}
       	}
       	
        ---------------- 反射注解获得的 org.apache.ibatis.mapping.MappedStatement 对象 ----------------------------
        -- MappedStatement 描述的是SQL语句的信息
		-- ResultMap 描述的是某一行数据怎么映射的信息
        
        MappedStatement = { // 对应 Mapper 接口中的接口，或者Mapper.xml文件中的<select><delete><update><insert>
			mappedStatement.configuration = configuration; // 对全局对象Configuration的依赖
			mappedStatement.id = id;  // "cn.java.mapper.TestMapper.mehtod1"
			mappedStatement.sqlSource = sqlSource; // org.apache.ibatis.scripting.defaults.RawSqlSource
			mappedStatement.statementType = StatementType.PREPARED;
			mappedStatement.parameterMap = new ParameterMap.Builder(configuration, "defaultParameterMap", null, new ArrayList<ParameterMapping>()).build();
			mappedStatement.resultMaps = new ArrayList<ResultMap>();
			mappedStatement.sqlCommandType = sqlCommandType; // SqlCommandType.SELECT
			mappedStatement.keyGenerator = configuration.isUseGeneratedKeys() && SqlCommandType.INSERT.equals(sqlCommandType) ? new Jdbc3KeyGenerator() : new NoKeyGenerator();
			mappedStatement.statementLog = LogFactory.getLog(configuration.getLogPrefix() + id); // 日志记录器
			mappedStatement.lang = configuration.getDefaultScriptingLanuageInstance();
			
			mappedStatement.resource = "com/test/bean/XxxMapper.java (best guess)";
			mappedStatement.fetchSize = 10; // 获取的行数 （通过注解 @Options 得到）
			mappedStatement.timeout = 10;  // 超时 （通过注解 @Options 得到）
			mappedStatement.statementType = StatementType.PREPARED;  //  StatementType.PREPARED （通过注解 @Options 得到）
			mappedStatement.keyGenerator = ...;  // 当添加或者修改的时候（通过 注解@SelectKey 或 注解 @Options 得到）；当删除或者查询（keyProperty="id"）
			mappedStatement.keyProperties = ...; // 当添加或者修改的时候（通过 注解@SelectKey 或 注解 @Options 得到）；当删除或者查询（keyProperty="id"）
			mappedStatement.keyColumns = ...; // 当添加或者修改的时候（通过  注解 @Options 得到）；
			mappedStatement.databaseId = ...; // null
			mappedStatement.lang = org.apache.ibatis.scripting.defaults.RawLanguageDriver; // 通过方法上的注解 @Lang 得到
			mappedStatement.resultOrdered = ...; // false
			mappedStatement.resultSets = "resultSets1,resultSets2"; //通过  注解 @Options得到
			
			mappedStatement.resultMaps = [ // "resultMap1,resultMap2"（通过注解 @ResultMap 得到）  org.apache.ibatis.mapping.ResultMap，（使用地：org.apache.ibatis.executor.resultset.DefaultResultSetHandler.handleResultSets()）
//		    	情况1：有注解  @ResultMap，使用如下配置
		    	configuration.getResultMap("cn.java.mapper.TestMappper.mehtod1.resultMap1"),
	        	configuration.getResultMap("resultMap2")
	        	
//	        	情况2：如果没有注解 @ResultMap、@ConstructorArgs、@Results、@TypeDiscriminator，使用如下配置（即：通过反射返回参数类型内建一个）
//	        	{
//		    		resultMap.id = "cn.java.mapper.TestMappper.mehtod1-Inline";
//		    	    resultMap.type = type; // 返回值类型（通过反射方法得到）
//		    	    resultMap.resultMappings = new ArrayList<ResultMapping>();
//		    	    resultMap.autoMapping = null;
//		    	}

//				情况3：没有注解 @ResultMap，通过解析注解@ConstructorArgs、@Results、@TypeDiscriminator
//				{
//		    		resultMap.id = "cn.java.mapper.TestMappper.mehtod1-Inline";
//		    	    resultMap.type = type; // 返回值类型（通过反射方法得到，可以是接口）
//		    	    resultMap.autoMapping = null;
//					resultMap.resultMappings = [
//						{ // @Arg
//							resultMapping.column = nullOrEmpty(arg.column()); // 列名1
//							...
//						},
//						{ // @Result
//							resultMapping.column = nullOrEmpty(arg.column()); // 列名2
//							....
//						}
//					];
//				}
	    	]; 
			mappedStatement.hasNestedResultMaps = null; 
			
			mappedStatement.resultSetType = ResultSetType.FORWARD_ONLY; //  （通过注解 @Options 得到）（使用地：org.apache.ibatis.executor.resultset.DefaultResultSetHandler.handleResultSets()）
			mappedStatement.flushCacheRequired = ...;  // （通过注解 @Options 得到）
			mappedStatement.useCache = ...; // （通过注解 @Options 得到）
			mappedStatement.cache = BlockingCache(SynchronizedCache(LoggingCache(SerializedCache(ScheduledCache(new LruCache(new PerpetualCache("com.test.bean.TestMapper"))))))); // 通过注解 @CacheNamespace 或者 注解 @CacheNamespaceRef 得到
			
			mappedStatement.parameterMap = {  // 参数 org.apache.ibatis.mapping.ParameterMap
	    	  parameterMap.id = "cn.java.TestMappper.mehtod1-Inline";
	    	  parameterMap.type = type; // org.apache.ibatis.binding.MapperMethod.ParamMap / null / 自定义类型 cn.java.entity.User / java.lang.String （通过反射方法得到）
	    	  parameterMap.parameterMappings = new ArrayList<ParameterMapping>();
	       }
        }
        
	    ---------------- org.apache.ibatis.mapping.BoundSql 对象 ----------------------------	
       	BoundSql = {
       		sql : "select * from table1 where field1 = ? and field2 = ?", // "Mapper方法上传的注解"解析出来的SQL
       		parameterMappings : [ // "Mapper方法上传的注解"解析出来的参数  （使用地：org.apache.ibatis.scripting.defaults.DefaultParameterHandler.setParameters()）
       			{
					parameterMapping.configuration = configuration; // 对全局对象Configuration的依赖
					parameterMapping.property = "property1"; // 属性名
					parameterMapping.javaType = java.lang.String; // 接口上方法参数的类型
					parameterMapping.mode = ParameterMode.IN;
					parameterMapping.jdbcType = org.apache.ibatis.type.JdbcType.VARCHAR; // 对应的数据库类型
					parameterMapping.numericScale = 0; // 精度
					parameterMapping.resultMapId = "resultMapId"; // 
					parameterMapping.jdbcTypeName = "VARCHAR"; // 对应的数据库类型的名称
					parameterMapping.typeHandler = cn.java.typehandler.StringTypeHandler_obj; // 类型处理器
				},
				{
					parameterMapping.configuration = configuration;
					parameterMapping.property = "property2";
					parameterMapping.javaType = java.lang.Object; // 接口上方法参数的类型
					parameterMapping.mode = ParameterMode.IN;
					parameterMapping.jdbcType = org.apache.ibatis.type.JdbcType.VARCHAR;
					parameterMapping.numericScale = 0; // 精度
					parameterMapping.resultMapId = "resultMapId";
					parameterMapping.jdbcTypeName = "VARCHAR";
					parameterMapping.typeHandler = org.apache.ibatis.type.StringTypeHandler; // 实例对象 ，类型处理器注册表 org.apache.ibatis.type.TypeHandlerRegistry
				},
       		],
       		parameterObject : new ParamMap<Object>(){ // 代理调用传递进来的参数
				{
					put("param0","param0Value");
					put("param1",new Object());
				}
			},
       		additionalParameters : new HashMap<String, Object>(), // 附近参数
       		metaParameters : configuration.newMetaObject(additionalParameters),
       	}
       	
	 */
}
