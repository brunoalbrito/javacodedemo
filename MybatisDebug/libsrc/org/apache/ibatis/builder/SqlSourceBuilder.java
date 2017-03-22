/**
 *    Copyright 2009-2015 the original author or authors.
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
package org.apache.ibatis.builder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.parsing.GenericTokenParser;
import org.apache.ibatis.parsing.TokenHandler;
import org.apache.ibatis.reflection.MetaClass;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.JdbcType;

/**
 * @author Clinton Begin
 */
public class SqlSourceBuilder extends BaseBuilder {

  private static final String parameterProperties = "javaType,jdbcType,mode,numericScale,resultMap,typeHandler,jdbcTypeName";

  public SqlSourceBuilder(Configuration configuration) {
    super(configuration);
  }

  public SqlSource parse(String originalSql, Class<?> parameterType, Map<String, Object> additionalParameters) {
//	originalSql == "select * from user where field1 = #{field1_property} AND field2 = #{field1_property,:jdbcType,optionsname1=value1,optionsname2=value2}"
//  #{field1_property,:jdbcType,optionsname1=value1,optionsname2=value2}
//  #{field1_property,:JdbcType.VARCHAR,javaType=java.lang.String,mode=ParameterMode.IN,numericScale=2,resultMap=resultMapId,typeHandler=typeHandlerAlias,jdbcTypeName=jdbcTypeName}
    
	  
//	  有多个参数时，参数的类型称为：parameterType === org.apache.ibatis.binding.MapperMethod.ParamMap  
	  
	ParameterMappingTokenHandler handler = new ParameterMappingTokenHandler(configuration, parameterType, additionalParameters);
    GenericTokenParser parser = new GenericTokenParser("#{", "}", handler);
    String sql = parser.parse(originalSql);
    /*
      	sqlSource == org.apache.ibatis.builder.StaticSqlSource 
	  	{
			// select * from user where field1 = #{field1_property} AND field2 = #{field2_property,:JdbcType.VARCHAR,javaType=java.lang.String,mode=ParameterMode.IN,numericScale=2,resultMap=resultMapId,typeHandler=typeHandlerAlias,jdbcTypeName=jdbcTypeName}
			sql = "select * from user where field1 = ? AND field2 = ?",
			
			handler.getParameterMappings() = parameterMappings = {
				{
					parameterMapping.configuration = configuration;
					parameterMapping.property = "property1";
					parameterMapping.javaType = java.lang.String; // 接口上方法参数的类型
					parameterMapping.mode = ParameterMode.IN;
					parameterMapping.jdbcType = org.apache.ibatis.type.JdbcType.VARCHAR;
					parameterMapping.numericScale = 0; // 精度
					parameterMapping.resultMapId = "resultMapId";
					parameterMapping.jdbcTypeName = "VARCHAR";
					parameterMapping.typeHandler = cn.java.typehandler.StringTypeHandler_obj;
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
					parameterMapping.typeHandler = cn.java.typehandler.StringTypeHandler_obj;
				},
				{
					parameterMapping.configuration = configuration;
					parameterMapping.property = "property3";
					parameterMapping.javaType = org.apache.ibatis.binding.MapperMethod.ParamMap; // 接口上方法参数的类型
					parameterMapping.mode = ParameterMode.IN;
					parameterMapping.jdbcType = org.apache.ibatis.type.JdbcType.VARCHAR;
					parameterMapping.numericScale = 0; // 精度
					parameterMapping.resultMapId = "resultMapId";
					parameterMapping.jdbcTypeName = "VARCHAR";
					parameterMapping.typeHandler = cn.java.typehandler.StringTypeHandler_obj;
				}
			},
			configuration : org.apache.ibatis.session.Configuration
	  	}
	*/
    return new StaticSqlSource(configuration, sql, handler.getParameterMappings());
  }

  private static class ParameterMappingTokenHandler extends BaseBuilder implements TokenHandler {

    private List<ParameterMapping> parameterMappings = new ArrayList<ParameterMapping>();
    private Class<?> parameterType;
    private MetaObject metaParameters;

    public ParameterMappingTokenHandler(Configuration configuration, Class<?> parameterType, Map<String, Object> additionalParameters) {
      super(configuration);
      this.parameterType = parameterType; // Mapper接口中的方法参数类型列表
      this.metaParameters = configuration.newMetaObject(additionalParameters); // HashMap<String, Object>
    }

    public List<ParameterMapping> getParameterMappings() {
      return parameterMappings;
    }

    @Override
    public String handleToken(String content) {
      // "select * from user where field1 = #{field1}" ---> "select * from user where field1 = ?"
      parameterMappings.add(buildParameterMapping(content));
      return "?";
    }

    private ParameterMapping buildParameterMapping(String content) {
      Map<String, String> propertiesMap = parseParameterMapping(content); // 参数信息
//	  ParameterExpression propertiesMap = {
//		  property ： "username",	  
//		  jdbcType ： "jdbcType",	  
//		  option1 ： "option1Value",	  
//		  option2： "option2Value",	  
//		  optionX： "...",	  
//	  }
//    #{field2:jdbcType,optionsname1=value1,optionsname2=value2}
//    #{field2:VARCHAR,javaType=java.lang.String,mode=ParameterMode.IN,numericScale=2,resultMap=resultMapId,typeHandler=typeHandlerAlias,jdbcTypeName="VARCHAR"}
      String property = propertiesMap.get("property"); //  属性名
      Class<?> propertyType;
      if (metaParameters.hasGetter(property)) { // issue #448 get type from additional params  
        propertyType = metaParameters.getGetterType(property); // javabean的属性类型
      } else if (typeHandlerRegistry.hasTypeHandler(parameterType)) {
        propertyType = parameterType; // org.apache.ibatis.binding.MapperMethod.ParamMap
      } else if (JdbcType.CURSOR.name().equals(propertiesMap.get("jdbcType"))) {
        propertyType = java.sql.ResultSet.class;
      } else if (property != null) { // 反射
        MetaClass metaClass = MetaClass.forClass(parameterType, configuration.getReflectorFactory());
        if (metaClass.hasGetter(property)) {
          propertyType = metaClass.getGetterType(property);
        } else {
          propertyType = Object.class;
        }
      } else {
        propertyType = Object.class;
      }
      /*
			{
				field1 = {
					parameterMapping.configuration = configuration;
					parameterMapping.property = "property1";
					parameterMapping.javaType = java.lang.String; // 接口上方法参数的类型
					parameterMapping.mode = ParameterMode.IN;
					parameterMapping.jdbcType = org.apache.ibatis.type.JdbcType.VARCHAR;
					parameterMapping.numericScale = 0; // 整型
					parameterMapping.resultMapId = "resultMapId";
					parameterMapping.jdbcTypeName = "VARCHAR";
					parameterMapping.typeHandler = cn.java.typehandler.StringTypeHandler_obj;
				},
				field2 = {
					parameterMapping.configuration = configuration;
					parameterMapping.property = "property2";
					parameterMapping.javaType = java.lang.Object; // 接口上方法参数的类型
					parameterMapping.mode = ParameterMode.IN;
					parameterMapping.jdbcType = org.apache.ibatis.type.JdbcType.VARCHAR;
					parameterMapping.numericScale = 0; // 整型
					parameterMapping.resultMapId = "resultMapId";
					parameterMapping.jdbcTypeName = "VARCHAR";
					parameterMapping.typeHandler = cn.java.typehandler.StringTypeHandler_obj;
				},
				field3 = {
					parameterMapping.configuration = configuration;
					parameterMapping.property = "property3";
					parameterMapping.javaType = org.apache.ibatis.binding.MapperMethod.ParamMap; // 接口上方法参数的类型
					parameterMapping.mode = ParameterMode.IN;
					parameterMapping.jdbcType = org.apache.ibatis.type.JdbcType.VARCHAR;
					parameterMapping.numericScale = 0; // 整型
					parameterMapping.resultMapId = "resultMapId";
					parameterMapping.jdbcTypeName = "VARCHAR";
					parameterMapping.typeHandler = cn.java.typehandler.StringTypeHandler_obj;
				}
			}
       */
      ParameterMapping.Builder builder = new ParameterMapping.Builder(configuration, property, propertyType);
      Class<?> javaType = propertyType; // java的类型
      String typeHandlerAlias = null;
      for (Map.Entry<String, String> entry : propertiesMap.entrySet()) {
        String name = entry.getKey();
        String value = entry.getValue();
        if ("javaType".equals(name)) {
          javaType = resolveClass(value);
          builder.javaType(javaType);
        } else if ("jdbcType".equals(name)) { // 数据库的类型
          builder.jdbcType(resolveJdbcType(value)); // JdbcType.INTEGER
        } else if ("mode".equals(name)) {
          builder.mode(resolveParameterMode(value));
        } else if ("numericScale".equals(name)) {
          builder.numericScale(Integer.valueOf(value));
        } else if ("resultMap".equals(name)) {
          builder.resultMapId(value);
        } else if ("typeHandler".equals(name)) { // 类型处理器
          typeHandlerAlias = value;
        } else if ("jdbcTypeName".equals(name)) {
          builder.jdbcTypeName(value);
        } else if ("property".equals(name)) {
          // Do Nothing
        } else if ("expression".equals(name)) {
          throw new BuilderException("Expression based parameters are not supported yet");
        } else {
          throw new BuilderException("An invalid property '" + name + "' was found in mapping #{" + content + "}.  Valid properties are " + parameterProperties);
        }
      }
      if (typeHandlerAlias != null) {
        builder.typeHandler(resolveTypeHandler(javaType, typeHandlerAlias)); // builder.typeHandler(new MyTypeHandlerClass(JavaTypeClass.class))
      }
      return builder.build();
    }

    private Map<String, String> parseParameterMapping(String content) {
      try {
//    	  ParameterExpression = {
//			  property ： "username",	  
//			  jdbcType ： "jdbcType",	  
//			  option1 ： "option1Value",	  
//			  option2： "option2Value",	  
//    	  }
        return new ParameterExpression(content);
      } catch (BuilderException ex) {
        throw ex;
      } catch (Exception ex) {
        throw new BuilderException("Parsing error was found in mapping #{" + content + "}.  Check syntax #{property|(expression), var1=value1, var2=value2, ...} ", ex);
      }
    }
  }

}
