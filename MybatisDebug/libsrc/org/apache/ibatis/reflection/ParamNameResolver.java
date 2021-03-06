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

package org.apache.ibatis.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.binding.MapperMethod.ParamMap;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

public class ParamNameResolver {

  private static final String GENERIC_NAME_PREFIX = "param";
  private static final String PARAMETER_CLASS = "java.lang.reflect.Parameter";
  private static Method GET_NAME = null;
  private static Method GET_PARAMS = null;

  static {
    try {
      Class<?> paramClass = Resources.classForName(PARAMETER_CLASS);
      GET_NAME = paramClass.getMethod("getName");
      GET_PARAMS = Method.class.getMethod("getParameters");
    } catch (Exception e) {
      // ignore
    }
  }

  /**
   * <p>
   * The key is the index and the value is the name of the parameter.<br />
   * The name is obtained from {@link Param} if specified. When {@link Param} is not specified,
   * the parameter index is used. Note that this index could be different from the actual index
   * when the method has special parameters (i.e. {@link RowBounds} or {@link ResultHandler}).
   * </p>
   * <ul>
   * <li>aMethod(@Param("M") int a, @Param("N") int b) -&gt; {{0, "M"}, {1, "N"}}</li>
   * <li>aMethod(int a, int b) -&gt; {{0, "0"}, {1, "1"}}</li>
   * <li>aMethod(int a, RowBounds rb, int b) -&gt; {{0, "0"}, {2, "1"}}</li>
   * </ul>
   */
  private final SortedMap<Integer, String> names;

  private boolean hasParamAnnotation;

  public ParamNameResolver(Configuration config, Method method) {
    final Class<?>[] paramTypes = method.getParameterTypes();
    final Annotation[][] paramAnnotations = method.getParameterAnnotations(); // 参数上的注解
    final SortedMap<Integer, String> map = new TreeMap<Integer, String>();
    int paramCount = paramAnnotations.length;
    // get names from @Param annotations
    for (int paramIndex = 0; paramIndex < paramCount; paramIndex++) {
      if (isSpecialParameter(paramTypes[paramIndex])) { // RowBounds 或者 ResultHandler 的子类型
        // skip special parameters
        continue;
      }
      String name = null;
      for (Annotation annotation : paramAnnotations[paramIndex]) { // 某个参数的注解 @Param
    	  /**
	       	{
	      		0 : "userid", //第一个参数  @Param("userid")
	      		1 : "username", //第二个参数 @Param("username")
	      	}
	       */
    	if (annotation instanceof Param) {
          hasParamAnnotation = true;
          name = ((Param) annotation).value();
          break;
        }
      }
      if (name == null) {
        // @Param was not specified.
        if (config.isUseActualParamName()) {
        	/**
	         	{
	        		0 : "arg0", //第一个参数
	        		1 : "arg1", //第二个参数
	        	}
	         */
          name = getActualParamName(method, paramIndex);
        }
        if (name == null) {
          // use the parameter index as the name ("0", "1", ...)
          // gcode issue #71
        /**
         	{
        		0 : "0", //第一个参数
        		1 : "1", //第二个参数
        	}
         */
          name = String.valueOf(map.size());
        }
      }
      map.put(paramIndex, name); // 第几个参数的参数名
    }
    names = Collections.unmodifiableSortedMap(map);
  }

  private String getActualParamName(Method method, int paramIndex) {
    if (GET_PARAMS == null) {
      return null;
    }
    try {
      Object[] params = (Object[]) GET_PARAMS.invoke(method);
      return (String) GET_NAME.invoke(params[paramIndex]);
    } catch (Exception e) {
      throw new ReflectionException("Error occurred when invoking Method#getParameters().", e);
    }
  }

  private static boolean isSpecialParameter(Class<?> clazz) {
    return RowBounds.class.isAssignableFrom(clazz) || ResultHandler.class.isAssignableFrom(clazz);
  }

  /**
   * Returns parameter names referenced by SQL providers.
   */
  public String[] getNames() {
    return names.values().toArray(new String[0]);
  }

  /**
   * <p>
   * A single non-special parameter is returned without a name.<br />
   * Multiple parameters are named using the naming rule.<br />
   * In addition to the default names, this method also adds the generic names (param1, param2,
   * ...).
   * </p>
   */
  public Object getNamedParams(Object[] args) {
    final int paramCount = names.size();
    if (args == null || paramCount == 0) {
      return null;
    } else if (!hasParamAnnotation && paramCount == 1) {
      return args[names.firstKey()];
    } else {
      final Map<String, Object> param = new ParamMap<Object>();
      int i = 0;
      for (Map.Entry<Integer, String> entry : names.entrySet()) {
//      param.put("参数名", "传递进来的参数值");
        param.put(entry.getValue(), args[entry.getKey()]);
        // add generic param names (param1, param2, ...)
        final String genericParamName = GENERIC_NAME_PREFIX + String.valueOf(i + 1); // param1,param2
        // ensure not to overwrite parameter named with @Param
        if (!names.containsValue(genericParamName)) {
          param.put(genericParamName, args[entry.getKey()]); // 会多增加一个通用名
        }
        i++;
      }
//      param = {
//    	  param1 : 参数值0，自动增加的参数通用名
//    	  userid : 参数值0
//    	  param2 : 参数值1，自动增加的参数通用名
//    	  username : 参数值1
//      }
      return param;
    }
  }
}
