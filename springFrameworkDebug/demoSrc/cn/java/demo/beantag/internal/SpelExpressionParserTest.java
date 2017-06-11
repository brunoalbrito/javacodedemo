package cn.java.demo.beantag.internal;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.el.VariableResolver;

import org.springframework.context.expression.EnvironmentAccessor;
import org.springframework.context.expression.MapAccessor;
import org.springframework.expression.AccessException;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.PropertyAccessor;
import org.springframework.expression.TypedValue;
import org.springframework.expression.spel.standard.SpelExpression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

public class SpelExpressionParserTest {

	public static void testSpelExpressionParser(String[] args) {
		new SpelExpressionParserTest().getValue("");
	}
	public static void main(String[] args) {
		SpelExpressionParserTest parser = new SpelExpressionParserTest();
		System.out.println(parser.getValue("T(Integer).MAX_VALUE"));
		System.out.println("--------自定义变量--------------");
		System.out.println(parser.getValue("#variable0"));
		System.out.println(parser.getValue("#variable1"));
		
		System.out.println("--------自定义函数--------------");
		System.out.println(parser.getValue("#parseInt('3')"));
		
		System.out.println("--------list--------------");
		List<Integer> list0 = (List<Integer>) parser.getValue("{1,2,3}");
		System.out.println(list0.get(0));
		
		List<Integer> list1 = (List<Integer>) parser.getValue("{'item0','item1','item2'}");
		System.out.println(list1.get(1));
		
		System.out.println("--------map--------------");
		Map map = (Map) parser.getValue("{'key0':'key0_val','key1':'key1_val','key2':'key2_val'}");
		System.out.println(map.get("key1"));
		
		System.out.println("--------对象--------------");
		System.out.println(parser.getValue("attributes_Key0"));
		System.out.println(parser.getValue("attributes_Key2"));
		System.out.println(parser.getValue("attributes_Key2.attributes_Key2_0"));
		System.out.println(parser.getValue("request_Key0"));
		System.out.println(parser.getValue("session_Key0"));
		System.out.println(parser.getValue("context_Key0"));
	}
	
	public Object getValue(String expressionString) {
		// 解析表达式
		Expression expression = parseExpression(expressionString);
		// 创建执行上下
		EvaluationContext evaluationContext = createEvaluationContext();
		// 获取值
		return expression.getValue(evaluationContext);
	}
	
	/**
	 * 解析表达式成抽象语法树
	 */
	private Expression parseExpression(String expressionString) {
		ExpressionParser expressionParser = new SpelExpressionParser();
		Expression expression = expressionParser.parseExpression(expressionString);
		if(expression instanceof SpelExpression){
			
		}
		return expression;
	}
	
	/**
	 * 创建执行上下文
	 */
	private EvaluationContext createEvaluationContext() {
		StandardEvaluationContext context = new StandardEvaluationContext(); // 标准执行上下文
		// 自定义变量
		context.setVariable("variable0", "variable0_val");
	    context.setVariable("variable1", "variable1_val");
	    
	    // 自定义函数
	    Method parseInt;
		try {
			parseInt = Integer.class.getDeclaredMethod("parseInt", String.class);
			context.registerFunction("parseInt", parseInt);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		{
			context.addPropertyAccessor(new JspPropertyAccessor(new PageContextImpl(null,null,null,null)));
			context.addPropertyAccessor(new MapAccessor());
			context.addPropertyAccessor(new EnvironmentAccessor());
//			ConfigurableListableBeanFactory beanFactory = null;
//			context.setBeanResolver(new BeanFactoryResolver(beanFactory));
		}
		return context;
		
	}
	
	private static interface  PageContextMine {
		public abstract Object findAttribute(String paramString);
		public abstract VariableResolver getVariableResolver();
	}
	
	private static class PageContextImpl implements PageContextMine {
		private Map attributes = new HashMap(); // pageScope
		private Map request = new HashMap();
		private Map session = new HashMap();
		private Map context = new HashMap();
		public PageContextImpl(Map attributesx,Map requestx,Map sessionx,Map contextx){
			attributes.put("attributes_Key0", "attributes_Key0Value");
			attributes.put("attributes_Key1", "attributes_Key1Value");
			attributes.put("attributes_Key2", new HashMap(){
				{
					put("attributes_Key2_0","attributes_Key2_0_Value");
					put("attributes_Key2_1","attributes_Key2_1_Value");
				}
			});
			request.put("request_Key0", "request_Key0Value");
			request.put("request_Key1", "request_Key1Value");
			session.put("session_Key0", "session_Key0Value");
			session.put("session_Key1", "session_Key1Value");
			context.put("context_Key0", "context_Key0Value");
			context.put("context_Key1", "context_Key1Value");
		}
		@Override
		public Object findAttribute(String name) {
			 Object o = attributes.get(name);
		        if (o != null)
		            return o;

		        o = request.get(name); // 尝试获取
		        if (o != null)
		            return o;

		        if (session != null) {
		            try {
		                o = session.get(name); // 尝试获取
		            } catch(IllegalStateException ise) {
		                // Session has been invalidated.
		                // Ignore and fall through to application scope.
		            }
		            if (o != null)
		                return o;
		        }

		        return context.get(name); // 尝试获取
		}

		@Override
		public VariableResolver getVariableResolver() {
			return null;
		}
		
	}
	
	@SuppressWarnings("deprecation")
	private static class JspPropertyAccessor implements PropertyAccessor {

		private final PageContextMine pageContext;

		private final javax.servlet.jsp.el.VariableResolver variableResolver;

		public JspPropertyAccessor(PageContextMine pageContext) {
			this.pageContext = pageContext;
			this.variableResolver = pageContext.getVariableResolver();
		}

		@Override
		public Class<?>[] getSpecificTargetClasses() {
			return null;
		}

		@Override
		public boolean canRead(EvaluationContext context, Object target, String name) throws AccessException {
			return (target == null &&
					(resolveImplicitVariable(name) != null || this.pageContext.findAttribute(name) != null));
		}

		@Override
		public TypedValue read(EvaluationContext context, Object target, String name) throws AccessException {
			Object implicitVar = resolveImplicitVariable(name);
			if (implicitVar != null) {
				return new TypedValue(implicitVar);
			}
			return new TypedValue(this.pageContext.findAttribute(name));
		}

		@Override
		public boolean canWrite(EvaluationContext context, Object target, String name) {
			return false;
		}

		@Override
		public void write(EvaluationContext context, Object target, String name, Object newValue) {
			throw new UnsupportedOperationException();
		}

		private Object resolveImplicitVariable(String name) throws AccessException {
			if (this.variableResolver == null) {
				return null;
			}
			try {
				return this.variableResolver.resolveVariable(name);
			}
			catch (Exception ex) {
				throw new AccessException(
						"Unexpected exception occurred accessing '" + name + "' as an implicit variable", ex);
			}
		}
	}

}
