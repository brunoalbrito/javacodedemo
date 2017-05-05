package cn.java.demo.contexttag.internal;

import java.io.IOException;
import java.util.regex.Pattern;

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

public class PatternParserTest {
	public static void main(String[] args) throws IOException {
		String typePatternExpression = "";
		String className = "cn.java.demo.contexttag.internal.asm.HelloService";
		PatternParserTest patternParserTest = new PatternParserTest(typePatternExpression);
		if(patternParserTest.match(className)){
			System.out.println("is match ...");
		}
	}
	
	private final World world;
	private final TypePattern typePattern;
	
	public PatternParserTest(String typePatternExpression) {
		this.world = new BcelWorld(this.getClass().getClassLoader(), IMessageHandler.THROW, null);
		this.world.setBehaveInJava5Way(true);
		PatternParser patternParser = new PatternParser(typePatternExpression); // 表达式解析器
		TypePattern typePattern = patternParser.parseTypePattern(); // 类型解析器
		typePattern.resolve(this.world);
		IScope scope = new SimpleScope(this.world, new FormalBinding[0]);
		this.typePattern = typePattern.resolveBindings(scope, Bindings.NONE, false, false);
	}
	
	public boolean match(String className)
			throws IOException {
		ResolvedType resolvedType = this.world.resolve(className);
		return this.typePattern.matchesStatically(resolvedType);
	}

}
