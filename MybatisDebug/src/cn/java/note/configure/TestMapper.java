package cn.java.note.configure;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Lang;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Options.FlushCachePolicy;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.cache.decorators.LruCache;
import org.apache.ibatis.cache.impl.PerpetualCache;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.mapping.ResultSetType;
import org.apache.ibatis.mapping.StatementType;
import org.apache.ibatis.scripting.xmltags.XMLLanguageDriver;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import cn.java.entity.User;

/**
 * 必须是接口，
 * 	配置以注解的方式设置
 * @author zhouzhian
 */
// 缓存的配置
@CacheNamespace(implementation=PerpetualCache.class,eviction=LruCache.class,flushInterval=0,size=1024,readWrite=true,blocking=false)
//　使用Test2Mapper的缓存
//@CacheNamespaceRef(value=Test2Mapper.class)
public interface TestMapper {  
	

	/*
	 	@Select 和 @SelectProvider 不能同时存在
	 	
	 */
	
	
	@Lang(value=XMLLanguageDriver.class)
	@Select(value={
         	"select * from table1 where field1 = ${parampre\\${param_middle\\}paramafter}",
         	"select * from table1 where field1 = #{field1_property} AND field2 = #{field2_property:JdbcType.VARCHAR,javaType=java.lang.String,"
         				+ "mode=ParameterMode.IN,numericScale=2,resultMap=resultMapId,typeHandler=typeHandlerAlias,jdbcTypeName=jdbcTypeName}",
         	"select * from ${tablePrefix}table1 where field1 = ${field1_property}",
         	"select * from ${tablePrefix}table1 where field1 = ${_parameter.field1}",
     }) 
	@Options(useCache=true,flushCache=FlushCachePolicy.DEFAULT,resultSetType=ResultSetType.FORWARD_ONLY,statementType=StatementType.PREPARED,
		fetchSize=-1,timeout=-1,useGeneratedKeys=false,keyProperty="id",keyColumn="",resultSets="resultSets1,resultSets2")
	// @INSERT 或者 @UPDATE 注解时，才会处理 @SelectKey 
	@SelectKey(statement={
		},keyProperty="",keyColumn="",before=true,resultType=org.apache.ibatis.mapping.ResultMap.class,statementType=StatementType.PREPARED) // mappedStatementId = "cn.java.TestMappper.mehtod1"!selectKey", configuration.addMappedStatement(statement); configuration.addKeyGenerator(id, answer);
	@ResultMap(value={"resultMap1","resultMap2"}) // 
	public List<User> mehtod1(String name); // mappedStatementId = "cn.java.TestMappper.mehtod1",  configuration.addMappedStatement(statement);
	
	public List<User> mehtod2(@Param(value="param")String name);
	
	@Select(value={
			// sql包含非全局变量的 ${} 表达式，表示此句子为 DynamicSqlSource ，使用的是OGNL的解析方式，直接替换${_parameter.key1}字符串（${...}是简单识别替换，没有做安全处理的）
         	"select * from ${tablePrefix}table1 where field1 = ${_parameter.key1} AND field2=${_parameter.list1[0]}  AND field3=${_parameter.map['key1']}  AND field4=${_parameter.object1['property1']}", // 支持OGNL表达式
     }) 
	public List<User> mehtod3(Map param0);
	
	
	// 返回值的定义，有没有 ResultHandler
	public void select0_0(String param0,Object param1,ResultHandler result); // 没有放回值，使用引用传递参数
	public List select0_1(); // 返回值类型是Collection的子类
	@MapKey(value="key1") public Map select0_2(); // // 返回值类型是Map的子类，并且有@MapKey(value="key1")注解
	public Cursor select0_3(); // 返回值类型是Cursor
	
	// 参数的定义，有没有RowBounds参数（限制分页的参数）
	public void select1_0(String param0,Object param1,ResultHandler result);
	public void select1_1(String param0,Object param1,RowBounds rowBounds,ResultHandler result);
	
}  