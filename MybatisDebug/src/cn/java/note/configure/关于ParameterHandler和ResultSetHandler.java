package cn.java.note.configure;

public class 关于ParameterHandler和ResultSetHandler {

	public static void main(String[] args) {

		/*
		
		// --------------------ParameterHandler-------------------------
			调用位置：org.apache.ibatis.executor.statement.PreparedStatementHandler.parameterize(...)，主要进行参数绑定（使用类型处理器、）
			{
		 		org.apache.ibatis.scripting.defaults.DefaultParameterHandler.setParameters(PreparedStatement ps) 
		 		{
		 			List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
		 			for (int i = 0; i < parameterMappings.size(); i++) {
						...
						String propertyName = parameterMapping.getProperty(); // 属性名
						TypeHandler typeHandler = parameterMapping.getTypeHandler(); // 属性的类型处理器
						JdbcType jdbcType = parameterMapping.getJdbcType(); // 数据库的类型 org.apache.ibatis.type.JdbcType.VARCHAR
						if (value == null && jdbcType == null) {
						  jdbcType = configuration.getJdbcTypeForNull(); // JdbcType.OTHER
						}
						...
						typeHandler.setParameter(ps, i + 1, value, jdbcType); // 如 org.apache.ibatis.type.StringTypeHandler
		 			}
		 		}
		 	}
		 	
		// --------------------ResultSetHandler-------------------------
		 	调用位置：org.apache.ibatis.executor.statement.PreparedStatementHandler.query(...)，主要对查询结果进行映射
		 	{
		 		org.apache.ibatis.executor.resultset.DefaultResultSetHandler.handleResultSets(Statement stmt)
		 		{
		 			ResultSetWrapper rsw = getFirstResultSet(stmt); // 执行结果处理 org.apache.ibatis.executor.resultset.ResultSetWrapper
		 			List<ResultMap> resultMaps = mappedStatement.getResultMaps(); // （通过注解 @ResultMap("resultMap1,resultMap2")得到 或者 “解析返回参数并内建” 得到）
		 			while (rsw != null && resultMapCount > resultSetCount) {
				      ResultMap resultMap = resultMaps.get(resultSetCount); // 第N个结果集映射器 org.apache.ibatis.mapping.ResultMap
				      handleResultSet(rsw, resultMap, multipleResults, null);
				      {
				      	if (resultHandler == null) { // 在方法上有定义 ResultHandler 对象
				          DefaultResultHandler defaultResultHandler = new DefaultResultHandler(objectFactory);
				          handleRowValues(rsw, resultMap, defaultResultHandler, rowBounds, null);// !!!
				          {
				          	handleRowValuesForSimpleResultMap(rsw, resultMap, resultHandler, rowBounds, parentMapping);
				          	{
					          	DefaultResultContext<Object> resultContext = new DefaultResultContext<Object>();
							    skipRows(rsw.getResultSet(), rowBounds); // 跳过N行
							    while (shouldProcessMoreRows(resultContext, rowBounds) && rsw.getResultSet().next()) {
							      ResultMap discriminatedResultMap = resolveDiscriminatedResultMap(rsw.getResultSet(), resultMap, null); // org.apache.ibatis.mapping.ResultMap
							      Object rowValue = getRowValue(rsw, discriminatedResultMap); // 获取某行数据 !!!---------------------
							      {
							      		final ResultLoaderMap lazyLoader = new ResultLoaderMap();
									    Object resultObject = createResultObject(rsw, resultMap, lazyLoader, null); //!!! 创建行或者结果对象
									    if (resultObject != null && !hasTypeHandlerForResultObject(rsw, resultMap.getType())) // 没有类型处理器 
									    { 
									      final MetaObject metaObject = configuration.newMetaObject(resultObject);
									      boolean foundValues = !resultMap.getConstructorResultMappings().isEmpty(); // 有构造函数映射
									      if (shouldApplyAutomaticMappings(resultMap, false))  // 自动映射
									      { 
									        foundValues = applyAutomaticMappings(rsw, resultMap, metaObject, null) || foundValues; // 应用映射 !!!----
									        {
									        	List<UnMappedColumnAutoMapping> autoMapping = createAutomaticMappings(rsw, resultMap, metaObject, columnPrefix);//!!!  List<UnMappedColumnAutoMapping> list
											    {
											    	final String mapKey = resultMap.getId() + ":" + columnPrefix; // cn.java.mapper.FooMapper.selectAboutResult0-Foo-FooResultHandler
    												List<UnMappedColumnAutoMapping> autoMapping = autoMappingsCache.get(mapKey);
    												final List<String> unmappedColumnNames = rsw.getUnmappedColumnNames(resultMap, columnPrefix);
      												for (String columnName : unmappedColumnNames) {
												      	final String property = metaObject.findProperty(propertyName, configuration.isMapUnderscoreToCamelCase()); // bean 是否存在指定的属性
												       	if (property != null && metaObject.hasSetter(property)) { // 有setter方法
												          final Class<?> propertyType = metaObject.getSetterType(property); // 获取类型
												          if (typeHandlerRegistry.hasTypeHandler(propertyType, rsw.getJdbcType(columnName))) {
												            final TypeHandler<?> typeHandler = rsw.getTypeHandler(propertyType, columnName);
												            autoMapping.add(new UnMappedColumnAutoMapping(columnName, property, typeHandler, propertyType.isPrimitive()));
												          } else {
												        	// 忽略掉没有类型转换器的
												            configuration.getAutoMappingUnknownColumnBehavior()
												                    .doAction(mappedStatement, columnName, property, propertyType);
												          }
												        }
												     }
												     return autoMapping;
											    }
											    if (autoMapping.size() > 0) {
											      for (UnMappedColumnAutoMapping mapping : autoMapping) { // 迭代映射的属性
											        final Object value = mapping.typeHandler.getResult(rsw.getResultSet(), mapping.column); // 调用类型转换
											        metaObject.setValue(mapping.property, value); // 设置值
											      }
											    }
											    return foundValues;
									        }
											foundValues = applyPropertyMappings(rsw, resultMap, metaObject, lazyLoader, null) || foundValues; // 应用属性
											foundValues = lazyLoader.size() > 0 || foundValues;
											resultObject = foundValues ? resultObject : null;
											return resultObject;
									    }
									    return resultObject;
							      }
							      storeObject(resultHandler, resultContext, rowValue, parentMapping, rsw.getResultSet()); // ...
							      {
							      	callResultHandler(resultHandler, resultContext, rowValue); // 调用回调函数，（方法参数中定义的ResultHandler）
							      	{
							      		resultContext.nextResultObject(rowValue);
    									((ResultHandler<Object>)resultHandler).handleResult(resultContext);
							      	}
							      }
							    }
				          	}
				          }
				          multipleResults.add(defaultResultHandler.getResultList());
				        } else {
				          handleRowValues(rsw, resultMap, resultHandler, rowBounds, null);
				        }
				      }
				      rsw = getNextResultSet(stmt); // 获取下一个结果集
				    }
		 		}
		 	}
		 	
		*/
	}
	

}
