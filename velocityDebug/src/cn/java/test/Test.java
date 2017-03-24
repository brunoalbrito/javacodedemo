package cn.java.test;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.runtime.RuntimeSingleton;

public class Test {
	
	private static final String templateFile = "templates/test.vm";
	
	public static void main(String[] args) throws Exception{
		testAll();
//		test1_merge();
//		test2_mergeTemplate();
//		test3_evaluate();
	}

	private static VelocityContext getVelocityContext(){
		// 数据容器
		VelocityContext context = new VelocityContext();
		context.put("name", "Velocity");
		context.put("project", "Jakarta");
		context.put("username", "username1");
		List list  = new ArrayList();
		list.add("item1Value");
		list.add("item2Value");
		list.add("item3Value");
		context.put("list",list);
		
		Map map = new HashMap();
		map.put("key1", "key1Value");
		map.put("key2", "key2Value");
		context.put("map",map);
		
		Object bean = new Object(){
			private String property1 = "property1";

			public String getProperty1() {
				return property1;
			}

			public void setProperty1(String property1) {
				this.property1 = property1;
			}
			
		};
		User user = new User(1, "username1");
		context.put("user",user);
		context.put("bean",bean);
		context.put("intvar",1);
		return context;
	}
	
	public static void testAll() throws Exception{
		Velocity.init();

		// 扩展自己的指令
		RuntimeSingleton.loadDirective("cn.java.velocity.directive.FooDirective");
		
		// 数据容器
		VelocityContext context = getVelocityContext();

		// 加载模板文件，并解析模板文件成抽象语法树，语法树的节点都存有对org.apache.velocity.runtime.RuntimeInstance的引用
		Template template = Velocity.getTemplate(templateFile,"UTF-8");

		// 渲染
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));
		
		template.merge(context, writer); // org.apache.velocity.Template

		writer.flush();
		writer.close();
	}
	
	/**
	 * 文件渲染方式1.1
	 * @throws Exception
	 */
	
	public static void test1_merge() throws Exception{
		Velocity.init();
//		Velocity.init("velocity.properties");

		// 数据容器
		VelocityContext context = getVelocityContext();

		// 加载模板文件，并解析模板文件成抽象语法树，语法树的节点都存有对org.apache.velocity.runtime.RuntimeInstance的引用
		Template template = Velocity.getTemplate(templateFile,"UTF-8");

		// 渲染
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));
		
		template.merge(context, writer); // org.apache.velocity.Template

		writer.flush();
		writer.close();
	}
	
	/**
	 * 文件渲染方式1.2
	 * @throws Exception
	 */
	public static void test1_macro() throws Exception{
		Velocity.init();
		
		// 数据容器
		VelocityContext context = getVelocityContext();
		
		// 渲染
		BufferedWriter writer = writer = new BufferedWriter(
				new OutputStreamWriter(System.out));
		List macroLibraries = new ArrayList(); // 子模板
		macroLibraries.add("templdate/macro1.vm");
		macroLibraries.add("templdate/macro2.vm");
		
		Template template = Velocity.getTemplate(templateFile,"UTF-8");
		template.merge(context, writer,macroLibraries);
		writer.flush();
		writer.close();
	}

	/**
	 * 以下写法全部等价
	 * @throws Exception
	 */
	public static void test2_eq() throws Exception{
		
		VelocityContext context = getVelocityContext();
		StringWriter stringWriter = new StringWriter();
		
		//　写法1
		Template template = Velocity.getTemplate(templateFile);
		template.merge(context, stringWriter);
		
		// 写法2
		Velocity.mergeTemplate(templateFile, "UTF-8", context, stringWriter ); // 单不
		
	}
	
	/**
	 * 文件渲染方式二
	 * @throws Exception
	 */
	public static void test2_mergeTemplate() throws Exception{
		Velocity.init();

		// 数据容器
		VelocityContext context = getVelocityContext();

		StringWriter stringWriter = new StringWriter();
		
		// 渲染
		Velocity.mergeTemplate(templateFile, "UTF-8", context, stringWriter ); // 单不
		System.out.println(" string : " + stringWriter );
	}

	/**
	 * 字符串渲染方式三
	 * @throws Exception
	 */
	public static void test3_evaluate() throws Exception{
		// 数据容器
		VelocityContext context = getVelocityContext();
		
		StringWriter stringWriter = new StringWriter();
		// 模板内容
		String instring = "We are using $project $name to render this.";
		// 渲染
		Velocity.evaluate(context, stringWriter, "mystring", instring );
		System.out.println(" string : " + stringWriter );
	}
}
