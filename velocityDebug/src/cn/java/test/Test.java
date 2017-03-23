package cn.java.test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.OutputStreamWriter;
import java.io.StringWriter;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

public class Test {
	
	private static final String templateFile = "templates/test.vm";
	
	public static void main(String[] args) throws Exception{
		test1_merge();
		test2_mergeTemplate();
		test3_evaluate();
	}

	
	/**
	 * 渲染方式一
	 * @throws Exception
	 */
	public static void test1_merge() throws Exception{
		Velocity.init();
//		Velocity.init("velocity.properties");

		// 数据容器
		VelocityContext context = new VelocityContext();
		context.put("name", "Velocity");
		context.put("project", "Jakarta");

		// 加载模板文件，并解析模板文件
		Template template = Velocity.getTemplate(templateFile);

		// 渲染
		BufferedWriter writer = writer = new BufferedWriter(
				new OutputStreamWriter(System.out));
		
		template.merge(context, writer);

		writer.flush();
		writer.close();
	}

	/**
	 * 渲染方式二
	 * @throws Exception
	 */
	public static void test2_mergeTemplate() throws Exception{
		Velocity.init();

		// 数据容器
		VelocityContext context = new VelocityContext();
		context.put("name", "Velocity");
		context.put("project", "Jakarta");

		StringWriter stringWriter = new StringWriter();
		// 渲染
		Velocity.mergeTemplate(templateFile, "UTF-8", context, stringWriter );
		System.out.println(" string : " + stringWriter );
	}

	/**
	 * 渲染方式三
	 * @throws Exception
	 */
	public static void test3_evaluate() throws Exception{
		// 数据容器
		VelocityContext context = new VelocityContext();
		context.put("name", "Velocity");
		context.put("project", "Jakarta");
		StringWriter stringWriter = new StringWriter();
		// 模板内容
		String instring = "We are using $project $name to render this.";
		// 渲染
		Velocity.evaluate(context, stringWriter, "mystring", instring );
		System.out.println(" string : " + stringWriter );
	}
}
