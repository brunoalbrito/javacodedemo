package cn.java.demo.beantag.internal;

import org.aspectj.bridge.IMessageHandler;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.World;
import org.aspectj.weaver.bcel.BcelWorld;
import org.aspectj.weaver.patterns.Bindings;
import org.aspectj.weaver.patterns.FormalBinding;
import org.aspectj.weaver.patterns.IScope;
import org.aspectj.weaver.patterns.PatternParser;
import org.aspectj.weaver.patterns.SimpleScope;
import org.aspectj.weaver.patterns.TypePattern;

public class AspectJTypeTest {

	public static abstract class FooService{
		
	}
	public static class FooServiceImpl extends FooService{
		
	}
	
	public static void main(String[] args) {
		{
			String typePatternExpression = "* cn.java.demo..*ServiceImpl"; // 类型匹配表达式
			String className = FooServiceImpl.class.getName();
			testMatch(className, typePatternExpression);
		}
		
		// 会自动识别父类的是否匹配
		{
			String typePatternExpression = "* cn.java.demo..*FooService"; // 类型匹配表达式
			String className = FooServiceImpl.class.getName();
			testMatch(className, typePatternExpression);
		}
	}
	public static void testMatch(String className,String typePatternExpression) {
		
		
		TypePattern typePattern = null;
		World world = null;
		{
			world = new BcelWorld(AspectJTypeTest.class.getClassLoader(), IMessageHandler.THROW, null);
			world.setBehaveInJava5Way(true);
			
			PatternParser patternParser = new PatternParser(typePatternExpression); // 解析表达式
			TypePattern typePatternTemp = patternParser.parseTypePattern();
			typePatternTemp.resolve(world);
			IScope scope = new SimpleScope(world, new FormalBinding[0]);
			typePattern = typePatternTemp.resolveBindings(scope, Bindings.NONE, false, false);
		}
		
		{
			ResolvedType resolvedType = world.resolve(className);
			System.out.println("resolvedType.getClassName() = " + resolvedType.getClassName());
			if(typePattern.matchesStatically(resolvedType)){
				System.out.println(className + " is matches " + typePatternExpression);
			}
			else{
				System.out.println(className + " is not matches " + typePatternExpression);
			}
		}
	}

}
