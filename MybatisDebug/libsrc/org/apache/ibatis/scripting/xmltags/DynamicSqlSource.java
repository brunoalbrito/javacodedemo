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
package org.apache.ibatis.scripting.xmltags;

import java.util.Map;

import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.session.Configuration;

/**
 * @author Clinton Begin
 */
public class DynamicSqlSource implements SqlSource {

  private Configuration configuration;
  private SqlNode rootSqlNode;

  public DynamicSqlSource(Configuration configuration, SqlNode rootSqlNode) {
    this.configuration = configuration;
    this.rootSqlNode = rootSqlNode; // org.apache.ibatis.scripting.xmltags.TextSqlNode
  }

  @Override
  public BoundSql getBoundSql(Object parameterObject) {
	// parameterObject 实际调用时传递进来的参数
    DynamicContext context = new DynamicContext(configuration, parameterObject);
    rootSqlNode.apply(context); // 解析 OGNL 表达式
    
    SqlSourceBuilder sqlSourceParser = new SqlSourceBuilder(configuration); // 解析其他参数
    Class<?> parameterType = parameterObject == null ? Object.class : parameterObject.getClass();
    SqlSource sqlSource = sqlSourceParser.parse(context.getSql(), parameterType, context.getBindings());
    
    /*
	    context.getBindings() = {
	   		// 如果不是map类型，还会包装改对象的所有属性访问的方法
	   		"property..." : '....'
	   		// 其他
	   		"_parameter" ：实际调用方法传递进来的参数
	   		"_databaseId" : configuration.getDatabaseId()
	    }
	 */
    BoundSql boundSql = sqlSource.getBoundSql(parameterObject); 
    for (Map.Entry<String, Object> entry : context.getBindings().entrySet()) {
      boundSql.setAdditionalParameter(entry.getKey(), entry.getValue());
    }
    return boundSql;
  }

}
