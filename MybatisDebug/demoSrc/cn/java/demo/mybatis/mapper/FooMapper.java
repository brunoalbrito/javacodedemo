package cn.java.demo.mybatis.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Arg;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.CacheNamespaceRef;
import org.apache.ibatis.annotations.ConstructorArgs;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Options.FlushCachePolicy;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.cache.decorators.LruCache;
import org.apache.ibatis.cache.impl.PerpetualCache;
import org.apache.ibatis.mapping.ResultSetType;
import org.apache.ibatis.mapping.StatementType;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.JdbcType;

import cn.java.demo.mybatis.mapper.bean.Foo;
import cn.java.demo.mybatis.mapper.bean.FooConstructAnnotaionMap;
import cn.java.demo.mybatis.mapper.bean.FooConstructAutoMap;
import cn.java.demo.mybatis.mapper.resulthandler.FooResultHandler;

// 创建缓存对象
// @CacheNamespace(implementation=PerpetualCache.class,eviction=LruCache.class,flushInterval=0,size=1024,readWrite=true,blocking=false)
@CacheNamespace(implementation=cn.java.demo.mybatis.cache.RedisCache.class,eviction=LruCache.class,flushInterval=0,size=1024,readWrite=true,blocking=false)
// 使用哪个缓存
@CacheNamespaceRef(cn.java.demo.mybatis.mapper.FooMapper.class)
public interface FooMapper {  
	
    public final static String SELECT_FIELDS_STR = "id,account,password";
	// --------------------- 关于返回值 - N中写法 ----------------------
    // case 0.没有返回值，使用参数获取返回值，这种方式不会触发缓存
    @Select(value={
         	"select "+SELECT_FIELDS_STR+" from ${tablePrefix}" + TableNameRegistry.TABLE_FOO + "  where "
         		+ "id >= #{userid:INTEGER,javaType=java.lang.Integer,mode=IN,numericScale=0,typeHandler=org.apache.ibatis.type.IntegerTypeHandler,jdbcTypeName=INTEGER} "
         		+ "AND account >= #{account:VARCHAR,javaType=java.lang.String,mode=IN,numericScale=0,typeHandler=org.apache.ibatis.type.StringTypeHandler,jdbcTypeName=VARCHAR} "
         		+ "AND id > 0 "
         		+ "AND id > ${onglValue} ",
    })
    @ResultType(value=cn.java.demo.mybatis.mapper.bean.Foo.class) // 
    public void selectListReturnVoidGetResultByParam(@Param("userid")Integer userid,@Param("account")String account,@Param("onglValue")String onglValue,@Param("result")FooResultHandler result);
    
    // case 1.返回值类型是Collection的子类或者数组
    @Select(value={"select "+SELECT_FIELDS_STR+" from ${tablePrefix}" + TableNameRegistry.TABLE_FOO + " WHERE id >= #{userid} ",})
    public Foo[] selectListReturnArray(Integer userid);
    
    @Select(value={"select "+SELECT_FIELDS_STR+" from ${tablePrefix}" + TableNameRegistry.TABLE_FOO + " WHERE id >= #{userid} ",})
    public List<Foo> selectListReturnList(Integer userid);
    
    // case 2.返回值类型是Map的子类，使用某个字段作为索引
    @Select(value={"select "+SELECT_FIELDS_STR+" from ${tablePrefix}" + TableNameRegistry.TABLE_FOO + " WHERE id >= #{userid} ",})
    @MapKey(value="account") // 用account列作为索引
    public Map selectListReturnMapWithMapKey(Integer userid);
    
    // case 3.返回值类型是Cursor，《不支持Cursor类型的返回值！！！》 org.apache.ibatis.reflection.factory.DefaultObjectFactory.resolveInterface(...)没有相关的自适应代码块
//  @Select(value={"select "+SELECT_FIELDS_STR+" from ${tablePrefix}" + TableNameRegistry.TABLE_FOO + " WHERE id = #{userid} ",})
//  public Cursor selectAboutResult4(Integer userid);
 
    // case 4.返回值类型是其他
    @Select(value={"select "+SELECT_FIELDS_STR+" from ${tablePrefix}" + TableNameRegistry.TABLE_FOO + " WHERE id = #{userid} LIMIT 1",})
    public Foo selectOneReturnBeanObject(Integer userid);
    
    // case 5.返回值类型是Map的子类
    @Select(value={"select "+SELECT_FIELDS_STR+" from ${tablePrefix}" + TableNameRegistry.TABLE_FOO + " WHERE id >= #{userid} LIMIT 1",})
    public Map selectOneReturnMapWithoutMapKey(Integer userid);
   
    // --------------------- 关于参数 - N中写法 ----------------------
    // case 0. 对返回的结果集进行筛选（RowBounds 是对结果集的筛选，不是决定数据库的返回结果集）
    @Select(value={"select "+SELECT_FIELDS_STR+" from ${tablePrefix}" + TableNameRegistry.TABLE_FOO + " WHERE id >= #{userid}",})
    public List<Foo> selectListAndPaginationOnLocalResultSet(Integer userid,RowBounds rowBounds); 
  
    // case 1. 分页
    @Select(value={"select "+SELECT_FIELDS_STR+" from ${tablePrefix}" + TableNameRegistry.TABLE_FOO + " WHERE id >= #{userid} LIMIT #{limit} OFFSET #{offset}",})
    public List<Foo> selectListAndPaginationOnDbServer(@Param("userid")Integer userid,@Param("offset")Integer offset,@Param("limit")int limit); 

    // case 2. ${...} 写法是不安全的
    @Select(value={"select "+SELECT_FIELDS_STR+" from ${tablePrefix}" + TableNameRegistry.TABLE_FOO + " WHERE id >= #{userid} ${ognlUnsafeValue} ${_parameter.ognlUnsafeValue}",})
    public List<Foo> selectListPassParamUnsafe(@Param("userid")Integer userid,@Param("ognlUnsafeValue")String ognlUnsafeValue); 
    
    // case 3. 参数是自定义对象
    @Select(value={"select "+SELECT_FIELDS_STR+" from ${tablePrefix}" + TableNameRegistry.TABLE_FOO + " WHERE id >= #{id}",})
    public List<Foo> selectListPassBeanObject(Foo foo);
    
    // case 4. 参数是自定义对象
    @Select(value={"select "+SELECT_FIELDS_STR+" from ${tablePrefix}" + TableNameRegistry.TABLE_FOO + " WHERE id >= #{foo.id} AND id >= #{userid} ",})
    public List<Foo> selectListPassParamNamed(@Param("foo")Foo foo,@Param("userid")int userid);
    
    // case 5. 使用通用名获取参数，从param1开始
    @Select(value={"select "+SELECT_FIELDS_STR+" from ${tablePrefix}" + TableNameRegistry.TABLE_FOO + " WHERE id >= #{param1.id} AND id >= #{param2} ",})
    public List<Foo> selectListPassParamWithGenericNamed(Foo foo,int userid);
    
    // --------------------- 关于Bean - N中写法 ----------------------
    // case 0.通过setter方法注入返回值
    @Select(value={"select "+SELECT_FIELDS_STR+" from ${tablePrefix}" + TableNameRegistry.TABLE_FOO + " WHERE id = #{userid} LIMIT 1",})
    public Foo selectOneFillBeanObjectBySetter(Integer userid);
    
    // case 1.通过构造函数创建bean，使用注解（不要求bean构造函数有查询结果的所有字段，但是要配置注解）
    @ConstructorArgs(value={ // 构造函数传参实例化
			@Arg(id=false,column="id",javaType=Integer.class,jdbcType=JdbcType.INTEGER,typeHandler=org.apache.ibatis.type.IntegerTypeHandler.class,select="",resultMap=""),
			@Arg(id=false,column="account",javaType=String.class,jdbcType=JdbcType.VARCHAR,typeHandler=org.apache.ibatis.type.StringTypeHandler.class,select="",resultMap="")
		}
	)
    @Select(value={"select * from ${tablePrefix}" + TableNameRegistry.TABLE_FOO + " WHERE id = #{userid} LIMIT 1",})
    public FooConstructAnnotaionMap selectOneFillBeanObjectByConstructorAccordingAnnotation(Integer userid);
  
    // case 2.通过构造函数创建bean，不使用注解（要求bean构造函数有查询结果的所有字段，不然自动映射不了）
    @Select(value={"select id,account from ${tablePrefix}" + TableNameRegistry.TABLE_FOO + " WHERE id = #{userid} LIMIT 1",})
    public FooConstructAutoMap selectOneFillBeanObjectByConstructorWithoutAccordingAnnotation(Integer userid);
   
    
    // --------------------- 查询 ----------------------
	/*
	
	#{field2_property // java的变量名称
		:VARCHAR, // 对应数据库上的类型  org.apache.ibatis.type.JdbcType
		javaType=java.lang.String, // 接口上方法参数的类型 ，如有配置，就使用这边的类型，不使用自动识别到的类型。java.lang.Object
    	mode=IN,  // org.apache.ibatis.mapping.ParameterMode
    	numericScale=2, // 对应精度
    	resultMap=resultMapId, // 返回类型处理类
    	typeHandler=typeHandlerAlias, // 类型处理器别名
    	jdbcTypeName=VARCHAR // 对应数据库上的类型的名称
    }
	
	// 执行 case.0，配置模板
	@Lang(value=XMLLanguageDriver.class)
	@Select(value={
         	"select * from table1 where field1 = ${parampre\\${param_middle\\}paramafter}",
         	"select * from table1 where field1 = #{field1_property} AND field2 = #{field2_property:VARCHAR,javaType=java.lang.String,"
         				+ "mode=IN,numericScale=2,resultMap=resultMapId,typeHandler=typeHandlerAlias,jdbcTypeName=jdbcTypeName}",
         	"select * from ${tablePrefix}table1 where field1 = ${field1_property}",
         	"select * from ${tablePrefix}table1 where field1 = ${_parameter.field1}",
     }) 
	@Options(useCache=true,flushCache=FlushCachePolicy.DEFAULT,resultSetType=ResultSetType.FORWARD_ONLY,statementType=StatementType.PREPARED,
		fetchSize=-1,timeout=-1,useGeneratedKeys=false,keyProperty="id",keyColumn="",resultSets="")
	// @INSERT 或者 @UPDATE 注解时，才会处理 @SelectKey 
	@SelectKey(statement={
		},keyProperty="",keyColumn="",before=true,resultType=org.apache.ibatis.mapping.ResultMap.class,statementType=StatementType.PREPARED) // mappedStatementId = "cn.java.TestMappper.mehtod1"!selectKey", configuration.addMappedStatement(statement); configuration.addKeyGenerator(id, answer);
	@ResultMap(value={"resultMap1","resultMap2"}) // 
	
//    @ConstructorArgs(value={ // 构造函数传参实例化
//			@Arg(id=false,column="",javaType=String.class,jdbcType=JdbcType.VARCHAR,typeHandler=org.apache.ibatis.type.StringTypeHandler.class,select="",resultMap=""),
//			@Arg(id=false,column="",javaType=String.class,jdbcType=JdbcType.VARCHAR,typeHandler=org.apache.ibatis.type.StringTypeHandler.class,select="",resultMap="")
//		}
//	)
//	@Results(id="",value={ // 结果
//			@Result(id=false,column="",property="",javaType=String.class,jdbcType=JdbcType.VARCHAR,typeHandler=org.apache.ibatis.type.StringTypeHandler.class,
//					one=@One(select="",fetchType=FetchType.DEFAULT),
//					many=@Many(select="",fetchType=FetchType.DEFAULT)
//			),
//			@Result(id=false,column="",property="",javaType=String.class,jdbcType=JdbcType.VARCHAR,typeHandler=org.apache.ibatis.type.StringTypeHandler.class,
//					one=@One(select="",fetchType=FetchType.DEFAULT),
//					many=@Many(select="",fetchType=FetchType.DEFAULT)
//			)
//		}
//	)
//	@TypeDiscriminator(column="列名",javaType=String.class,jdbcType=JdbcType.VARCHAR,typeHandler=org.apache.ibatis.type.StringTypeHandler.class,
//		cases={
//			@Case(value="casevalue0",type=cn.java.mybatis.mapper.bean.Foo.class, // resultMaps.put("cn.java.mapper.FooMapper.method1-param0-param1-casevalue0",..)
//				 results={
//					@Result(id=false,column="",property="",javaType=String.class,jdbcType=JdbcType.VARCHAR,typeHandler=org.apache.ibatis.type.StringTypeHandler.class,
//							one=@One(select="",fetchType=FetchType.DEFAULT),
//							many=@Many(select="",fetchType=FetchType.DEFAULT)
//					),
//					@Result(id=false,column="",property="",javaType=String.class,jdbcType=JdbcType.VARCHAR,typeHandler=org.apache.ibatis.type.StringTypeHandler.class,
//							one=@One(select="",fetchType=FetchType.DEFAULT),
//							many=@Many(select="",fetchType=FetchType.DEFAULT)
//					)
//				 },
//				 constructArgs={
//					@Arg(id=false,column="",javaType=String.class,jdbcType=JdbcType.VARCHAR,typeHandler=org.apache.ibatis.type.StringTypeHandler.class,select="",resultMap=""),
//					@Arg(id=false,column="",javaType=String.class,jdbcType=JdbcType.VARCHAR,typeHandler=org.apache.ibatis.type.StringTypeHandler.class,select="",resultMap="")
//				 }
//			 ),
//			@Case(value="casevalue1",type=cn.java.mybatis.mapper.bean.Foo.class,
//				 results={
//					@Result(id=false,column="",property="",javaType=String.class,jdbcType=JdbcType.VARCHAR,typeHandler=org.apache.ibatis.type.StringTypeHandler.class,
//							one=@One(select="",fetchType=FetchType.DEFAULT),
//							many=@Many(select="",fetchType=FetchType.DEFAULT)
//					),
//					@Result(id=false,column="",property="",javaType=String.class,jdbcType=JdbcType.VARCHAR,typeHandler=org.apache.ibatis.type.StringTypeHandler.class,
//							one=@One(select="",fetchType=FetchType.DEFAULT),
//							many=@Many(select="",fetchType=FetchType.DEFAULT)
//					)
//				 },
//				 constructArgs={
//					@Arg(id=false,column="",javaType=String.class,jdbcType=JdbcType.VARCHAR,typeHandler=org.apache.ibatis.type.StringTypeHandler.class,select="",resultMap=""),
//					@Arg(id=false,column="",javaType=String.class,jdbcType=JdbcType.VARCHAR,typeHandler=org.apache.ibatis.type.StringTypeHandler.class,select="",resultMap="")
//				 }
//			 )
//		}
//	)
	public User selectTpl(String id);
*/	
	
    // 执行 case.1，指定要查询的列
    // select * from tb_user where id >= ? AND account >= ? AND id > 0 AND id > 0 Limit 1 
    @Select(value={
    		"select * from ${tablePrefix}" + TableNameRegistry.TABLE_FOO + "  where "
    				+ "id >= #{userid:INTEGER,javaType=java.lang.Integer,mode=IN,numericScale=0,typeHandler=org.apache.ibatis.type.IntegerTypeHandler,jdbcTypeName=INTEGER} "
    				+ "AND account >= #{account:VARCHAR,javaType=java.lang.String,mode=IN,numericScale=0,typeHandler=org.apache.ibatis.type.StringTypeHandler,jdbcTypeName=VARCHAR} "
    				+ "AND id > 0 "
    				+ "AND id > ${onglValue}  Limit 1 ",
    })
    public List<Foo> selectWithSomeFileds(@Param("userid")Integer userid,@Param("account")String account,@Param("onglValue")String onglValue); // 有多个参数时，参数的类型称为：org.apache.ibatis.binding.MapperMethod.ParamMap
	
    // 执行 case.2,指定别名
    // select id As id_alias,account,password from tb_user WHERE id >= ? Limit 3 
    @Select(value={"select id As id_alias,account,password from ${tablePrefix}" + TableNameRegistry.TABLE_FOO + " WHERE id >= #{userid} Limit 3",})
    @MapKey(value="account") // 用account列作为map索引(键)
    public Map selectWithAlias(Integer userid);
    
	// 执行 case.3，分组、排序、分页限制 
	//  select id As id_alias,account,password from tb_user WHERE id >= ? GROUP BY id,id ORDER BY id ASC,account DESC LIMIT 3 OFFSET 0 
    @Select(value={
    		"select id As id_alias,account,password from ${tablePrefix}" + TableNameRegistry.TABLE_FOO 
    		+ " WHERE id >= #{userid} GROUP BY id,id ORDER BY id ASC,account DESC LIMIT 3 OFFSET 0",
    })
    @MapKey(value="account") 
    public Map selectWithGroupOrderPagination(Integer userid);
    
	// ---关联---
	//  SELECT tb1.id As id_alias,tb2.account,tb3.password FROM tb_user AS tb1 
    //		LEFT JOIN tb_user AS tb2 ON tb1.id=tb2.id 
    //		LEFT JOIN tb_user AS tb3 ON tb2.id=tb3.id 
    // 	WHERE tb1.id >= ? GROUP BY tb1.id,tb1.id ORDER BY tb1.id ASC,tb2.account DESC LIMIT 3 OFFSET 0 
    @Select(value={
    		"SELECT tb1.id As id_alias,tb2.account,tb3.password FROM ${tablePrefix}" + TableNameRegistry.TABLE_FOO +" AS tb1",
    		"LEFT JOIN ${tablePrefix}"+ TableNameRegistry.TABLE_FOO + " AS tb2 ON tb1.id=tb2.id",
    		"LEFT JOIN ${tablePrefix}"+ TableNameRegistry.TABLE_FOO + " AS tb3 ON tb2.id=tb3.id",
    		"WHERE tb1.id >= #{userid} ",
    		"GROUP BY tb1.id,tb1.id ",
    		"ORDER BY tb1.id ASC,tb2.account DESC ",
    		"LIMIT 3 OFFSET 0",
    })
    @MapKey(value="account") 
    public Map selectWithLeftJoin(Integer userid);
    
	// ---子查询---
	//  执行 case.1，【子查询作为条件】
	//  SELECT *  from pg_user where id in(SELECT id  from pg_user);
    @Select(value={
    		"SELECT id As id_alias,account,password FROM ${tablePrefix}" + TableNameRegistry.TABLE_FOO +" AS tb1",
    		"WHERE id IN (SELECT id  from ${tablePrefix}" + TableNameRegistry.TABLE_FOO +"  WHERE id >= #{userid} )",
    		"LIMIT 3 OFFSET 0",
    })
    @MapKey(value="account") 
    public Map selectSubQueryAsParam(Integer userid);
    
	//  执行 case.2，【子查询作为表】子查询出列表，再从列表中查询
	// SELECT id As id_alias,account,password FROM tb_user AS tb1 WHERE id IN (SELECT id from tb_user WHERE id >= ? ) LIMIT 3 OFFSET 0 
    @Select(value={
    		"SELECT id As id_alias,account,password FROM ",
    		" 	(SELECT *  from ${tablePrefix}" + TableNameRegistry.TABLE_FOO +" WHERE id >= #{userid} LIMIT 10) AS tb_temp",
    		"LIMIT 3 OFFSET 0",
    })
    @MapKey(value="account") 
    public Map selectSubQueryAsTempTable(Integer userid);
    
	//  执行 case.3，【子查询作为字段】子查询只能返回单条
	//  SELECT id As id_alias,account,password,(SELECT id AS id_alias2 FROM tb_user as tb1 LIMIT 1) as id_alias2_sub FROM tb_user WHERE id >= ? LIMIT 10 
    @Select(value={
    		"SELECT id As id_alias,account,password,(SELECT id AS id_alias2 FROM ${tablePrefix}" + TableNameRegistry.TABLE_FOO +" as tb1 LIMIT 1) as id_alias2_sub FROM ${tablePrefix}" + TableNameRegistry.TABLE_FOO +" WHERE id >= #{userid} LIMIT 10",
    })
    @MapKey(value="account") 
    public Map selectSubQueryAsField(Integer userid);
    
	// ---联合查询---
	//  (SELECT id As id_alias,account,password FROM tb_user WHERE id >= ? LIMIT 1) UNION ALL (SELECT id As id_alias,account,password FROM tb_user WHERE id >= ? LIMIT 2) 
    @Select(value={
    		"(SELECT id As id_alias,account,password FROM ${tablePrefix}" + TableNameRegistry.TABLE_FOO +" WHERE id >= #{userid} LIMIT 1)",
    		"UNION ALL  (SELECT id As id_alias,account,password FROM ${tablePrefix}" + TableNameRegistry.TABLE_FOO +" WHERE id >= #{userid} LIMIT 2)",
    })
    @MapKey(value="account") 
    public Map selectUnion(Integer userid);
    
	//　---统计---
	//  SELECT "album"."count""("*")" AS "count_total" FROM "album"
    @Select(value={
    		" SELECT count(*) FROM ${tablePrefix}" + TableNameRegistry.TABLE_FOO +" WHERE id >= #{userid}",
    })
    public long selectCount(Integer userid);
    
    // ---------------------缓存--------------------------------
	// 执行 case.0
    // select * from tb_user where id >= ? AND account >= ? AND id > 0 AND id > 0 Limit 1 
    @Select(value={
         	"select * from ${tablePrefix}" + TableNameRegistry.TABLE_FOO + "  where "
         		+ "id >= #{userid:INTEGER,javaType=java.lang.Integer,mode=IN,numericScale=0,typeHandler=org.apache.ibatis.type.IntegerTypeHandler,jdbcTypeName=INTEGER} "
         		+ "AND account >= #{account:VARCHAR,javaType=java.lang.String,mode=IN,numericScale=0,typeHandler=org.apache.ibatis.type.StringTypeHandler,jdbcTypeName=VARCHAR} "
         		+ "AND id > 0 "
         		+ "AND id > ${onglValue}  Limit 1 ",
    })
	@Options(useCache=true,flushCache=FlushCachePolicy.DEFAULT,resultSetType=ResultSetType.FORWARD_ONLY,statementType=StatementType.PREPARED,
		fetchSize=-1,timeout=-1,useGeneratedKeys=false,keyProperty="id",keyColumn="id,account,password",resultSets="")
	public List<Foo> selectListThroughCache(@Param("userid")Integer userid,@Param("account")String account,@Param("onglValue")String onglValue); 
    
    // ---------------------添加--------------------------------
    // 一次添加一条记录
    @Insert(value={
    		"INSERT INTO ${tablePrefix}" + TableNameRegistry.TABLE_FOO +"(id,account,password) ",
    		"VALUES (#{foo.id:INTEGER,javaType=java.lang.Integer,mode=IN,numericScale=0,typeHandler=org.apache.ibatis.type.IntegerTypeHandler,jdbcTypeName=INTEGER},#{foo.account},#{foo.password})",
    })
    public int insertOne(@Param("foo")Foo foo);
    
    // 一次添加多条记录
    @Insert(value={
    	"INSERT INTO ${tablePrefix}" + TableNameRegistry.TABLE_FOO +"(id,account,password) ",
    	"VALUES (#{list[0].id:INTEGER,javaType=java.lang.Integer,mode=IN,numericScale=0,typeHandler=org.apache.ibatis.type.IntegerTypeHandler,jdbcTypeName=INTEGER},#{list[0].account},#{list[0].password})",
    	",(#{list[1].id:INTEGER,javaType=java.lang.Integer,mode=IN,numericScale=0,typeHandler=org.apache.ibatis.type.IntegerTypeHandler,jdbcTypeName=INTEGER},#{list[1].account},#{list[1].password})"
    })
    public int insertBatch(@Param("list") List<Foo> list);
    
    // ---------------------删除--------------------------------
    @Delete(value={
    		"DELETE FROM ${tablePrefix}" + TableNameRegistry.TABLE_FOO +"  ",
    		" WHERE id = #{foo.id} ",
    })
    public int delete0(@Param("foo")Foo foo);
    
    // ---------------------修改--------------------------------
    @Update(value={
    		"UPDATE ${tablePrefix}" + TableNameRegistry.TABLE_FOO,
    		"SET  account = #{foo.account}",
    		",password = #{foo.password}",
    		"WHERE id = #{foo.id}",
    })
    public int update0(@Param("foo")Foo foo);
}  