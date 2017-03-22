package cn.java.note.configure;

public class 关于ResultMap {

	public static void main(String[] args) {

		/*
		  	----------------------------ResultMap , org.apache.ibatis.mapping.ResultMap-----------------------------------
		  	-- MappedStatement 描述的是SQL语句的信息
		  	-- ResultMap 描述的是某一行数据怎么映射的信息
		  	
		  	创建方式一：  
		  	-- 只有注解 @ResultMap，有返回值类型
		  	// 直接能获取到 @ResultMap({"resultMapId1","resultMapId2"})的信息创建  ResultMap 对象
		  	ResultMap对象  
		  	{
				resultMap.id = id;
				resultMap.type = type; 
				resultMap.resultMappings = [
					空
				];
				resultMap.autoMapping = autoMapping;
				resultMap.mappedColumns = new HashSet<String>();
				resultMap.idResultMappings = new ArrayList<ResultMapping>();
				resultMap.constructorResultMappings = new ArrayList<ResultMapping>();
				resultMap.propertyResultMappings = new ArrayList<ResultMapping>();
				resultMap.discriminator = null;
		  	}
		  	
		  	创建方式二：  
		  	-- 没有注解 @ResultMap、@ConstructorArgs、@Results、@TypeDiscriminator，只有返回值类型
		  	ResultMap对象
		  	{
				resultMap.id = id;
				resultMap.type = type; // 返回值类型 cn.java.mybatis.bean.User（通过反射方法得到））
				resultMap.resultMappings = [
					空
				];
				resultMap.autoMapping = autoMapping;
				resultMap.mappedColumns = new HashSet<String>();
				resultMap.idResultMappings = new ArrayList<ResultMapping>();
				resultMap.constructorResultMappings = new ArrayList<ResultMapping>();
				resultMap.propertyResultMappings = new ArrayList<ResultMapping>();
				resultMap.discriminator = null;
		  	}
		  	
		  	创建方式三：  
		  	-- 没有注解 @ResultMap，有注解@ConstructorArgs、@Results、@TypeDiscriminator，有返回值类型
		  		// 通过解析注解@ConstructorArgs、@Results、@TypeDiscriminator
			 	---- 情况_0
			 	ResultMap对象 // （@ConstructorArgs + @Results）
			  	{
					resultMap.id = id;  // （@ConstructorArgs + @Results）对应"cn.java.mapper.FooMapper.method1-param0-param1"
					resultMap.type = type; // 返回值类型cn.java.mybatis.bean.User（通过反射方法得到））
					resultMap.resultMappings = [
						{ // @Arg
							resultMapping.column = nullOrEmpty(arg.column()); // 列名1
							...
						},
						{ // @Result
							resultMapping.column = nullOrEmpty(arg.column()); // 列名2
							....
						}
					];
					resultMap.autoMapping = autoMapping;
					resultMap.mappedColumns = [列名1,列名2];
					resultMap.idResultMappings = new ArrayList<ResultMapping>();
					resultMap.constructorResultMappings = new ArrayList<ResultMapping>();
					resultMap.propertyResultMappings = new ArrayList<ResultMapping>();
					resultMap.discriminator = { // Discriminator
				    	discriminator.resultMapping = { // ResultMapping
				    		resultMapping.configuration = configuration;
							resultMapping.property = null;
							resultMapping.flags = new ArrayList<ResultFlag>();
							resultMapping.composites = new ArrayList<ResultMapping>();
							resultMapping.lazy = configuration.isLazyLoadingEnabled();
							  
							resultMapping.column = discriminator.column(); // 列名
							resultMapping.javaType = java.lang.String.class;  //  由  javaType="" 或 反射的返回值中是否有setter方法决定
							
							resultMapping.jdbcType = VARCHAR;
							resultMapping.nestedQueryId = null;
							resultMapping.nestedResultMapId = null;
							resultMapping.resultSet = null;
							resultMapping.typeHandler = org.apache.ibatis.type.StringTypeHandler;
							resultMapping.flags = new ArrayList<ResultFlag>();
							resultMapping.composites = new ArrayList<ResultMapping>();
							resultMapping.notNullColumns = null;
							resultMapping.columnPrefix = null;
							resultMapping.foreignColumn = null;
							resultMapping.lazy = false;
				    	},
					    discriminator.discriminatorMap = { // Map<String, String>
					    	CaseValue0 : resultMapId + "-" + CaseValue0
					    	CaseValue1 : resultMapId + "-" + CaseValue1
					    }
				    }
			  	}
			  	
			  	---- 情况_1
			 	ResultMap对象 //  @Case
			  	{
					resultMap.id = id;  //  @TypeDiscriminator对应"cn.java.mapper.FooMapper.method1-param0-param1-CaseValue0"
					resultMap.type = type; // 返回值类型cn.java.mybatis.bean.User（通过反射方法得到））
					resultMap.resultMappings = [
						{ // @Arg
							resultMapping.column = nullOrEmpty(arg.column()); // 列名1
							....
						},
						{ // @Result
							resultMapping.column = nullOrEmpty(arg.column()); // 列名2
							....
						}
					];
					resultMap.autoMapping = autoMapping;
					resultMap.mappedColumns = new HashSet<String>();
					resultMap.idResultMappings = new ArrayList<ResultMapping>();
					resultMap.constructorResultMappings = new ArrayList<ResultMapping>();
					resultMap.propertyResultMappings = new ArrayList<ResultMapping>();
					resultMap.discriminator = null;
			  	}
		  	
		  	----------------------------ResultMapping-----------------------------------
		  	ResultMapping 描述的是：参数和表中某一列的对应关系
		  	ResultMapping 由什么触发创建：@ConstructorArgs、@Results、@TypeDiscriminator
		  	----- @Arg ----- 
		  	//  @Arg(id=false,column="",javaType=String.class,jdbcType=VARCHAR,typeHandler=org.apache.ibatis.type.StringTypeHandler,select="",resultMap="")
		  	ResultMapping = { 
				resultMapping.configuration = configuration;
				resultMapping.property = null;
				resultMapping.flags = new ArrayList<ResultFlag>();
				resultMapping.composites = new ArrayList<ResultMapping>();
				resultMapping.lazy = configuration.isLazyLoadingEnabled();
				  
				resultMapping.column = nullOrEmpty(arg.column()); // 列名
				resultMapping.javaType = java.lang.String.class; //  由  javaType="" 或 反射的返回值中是否有setter方法决定
				
				resultMapping.jdbcType = VARCHAR;
				resultMapping.nestedQueryId = nullOrEmpty(arg.select());
				resultMapping.nestedResultMapId = nullOrEmpty(arg.resultMap());
				resultMapping.resultSet = null;
				resultMapping.typeHandler = org.apache.ibatis.type.StringTypeHandler;
				resultMapping.flags = ArrayList(ResultFlag.CONSTRUCTOR,);
				resultMapping.composites = new ArrayList<ResultMapping>();
				resultMapping.notNullColumns = new HashSet<String>();
				resultMapping.columnPrefix = null;
				resultMapping.foreignColumn = null;
				resultMapping.lazy = false;
	       }
	       ----- @Result -----
	       // @Result(id=false,column="",property="",javaType=String.class,jdbcType=VARCHAR,typeHandler=org.apache.ibatis.type.StringTypeHandler,one={select="",fetchType=DEFAULT},many={select="",fetchType=DEFAULT})
	       ResultMapping = { 
	       		resultMapping.configuration = configuration;
				resultMapping.property = nullOrEmpty(result.property());
				resultMapping.flags = new ArrayList<ResultFlag>();
				resultMapping.composites = new ArrayList<ResultMapping>();
				resultMapping.lazy = configuration.isLazyLoadingEnabled();
				  
				resultMapping.column = nullOrEmpty(result.column()); // 列名
				resultMapping.javaType = java.lang.String.class;
				
				resultMapping.jdbcType = VARCHAR;
				resultMapping.nestedQueryId = nestedSelectId(result);
				resultMapping.nestedResultMapId = null;
				resultMapping.resultSet = null;
				resultMapping.typeHandler = org.apache.ibatis.type.StringTypeHandler;
				resultMapping.flags = new ArrayList<ResultFlag>();
				resultMapping.composites = new ArrayList<ResultMapping>();
				resultMapping.notNullColumns = null;
				resultMapping.columnPrefix = null;
				resultMapping.foreignColumn = null;
				resultMapping.lazy = isLazy(result);
       		}
       		----- @TypeDiscriminator -----
       		Discriminator = {
		    	discriminator.resultMapping = { // ResultMapping
		    		resultMapping.configuration = configuration;
					resultMapping.property = null;
					resultMapping.flags = new ArrayList<ResultFlag>();
					resultMapping.composites = new ArrayList<ResultMapping>();
					resultMapping.lazy = configuration.isLazyLoadingEnabled();
					  
					resultMapping.column = discriminator.column(); // 列名
					resultMapping.javaType = java.lang.String.class;  //  由  javaType="" 或 反射的返回值中是否有setter方法决定
					
					resultMapping.jdbcType = VARCHAR;
					resultMapping.nestedQueryId = null;
					resultMapping.nestedResultMapId = null;
					resultMapping.resultSet = null;
					resultMapping.typeHandler = org.apache.ibatis.type.StringTypeHandler;
					resultMapping.flags = new ArrayList<ResultFlag>();
					resultMapping.composites = new ArrayList<ResultMapping>();
					resultMapping.notNullColumns = null;
					resultMapping.columnPrefix = null;
					resultMapping.foreignColumn = null;
					resultMapping.lazy = false;
		    	},
			    discriminator.discriminatorMap = { // Map<String, String>
			    	CaseValue0 : resultMapId + "-" + CaseValue0
			    	CaseValue1 : resultMapId + "-" + CaseValue1
			    }
		    }
		  	
		 */
	}

}
