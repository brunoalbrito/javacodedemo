package cn.java.test;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.StringWriter;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

public class Test {
	public static void main(String[] args) throws Exception{
		test1_merge();
	}

	public static void debug() throws Exception{
		// 默认配置文件  /org/apache/velocity/runtime/defaults/velocity.properties
		/* 
		指令配置文件  /org/apache/velocity/runtime/defaults/directive.properties
		{
			directive.1=org.apache.velocity.runtime.directive.Foreach
			directive.2=org.apache.velocity.runtime.directive.Include
			directive.3=org.apache.velocity.runtime.directive.Parse
			directive.4=org.apache.velocity.runtime.directive.Macro
			directive.5=org.apache.velocity.runtime.directive.Literal
			directive.6=org.apache.velocity.runtime.directive.Evaluate
			directive.7=org.apache.velocity.runtime.directive.Break
			directive.8=org.apache.velocity.runtime.directive.Define
			directive.9=org.apache.velocity.runtime.directive.Stop
		}
		 */
		

	}
	public static void test1_merge() throws Exception{
		String templateFile = "test.vm";
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


	public static void test2_mergeTemplate() throws Exception{
		String templateFile = "test.vm";
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
