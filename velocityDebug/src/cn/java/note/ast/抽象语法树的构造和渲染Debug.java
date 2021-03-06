package cn.java.note.ast;

public class 抽象语法树的构造和渲染Debug {
	public static void main(String[] args){
		
		
		/*
		 	1.0 #
		 		2.0 #   
		 			jjStopAtPos
		 		2.1 [
		 		2.2 \
		 	1.1 \
		 	
		 	
		 	
		 	
		 */
		/*
		 
		 
		 	org.apache.velocity.runtime.parser.Parser.parse(reader, templateName);
		 	org.apache.velocity.runtime.parser.node.SimpleNode.init( ica, rsvc); // 初始化抽象语法树
		 	org.apache.velocity.runtime.parser.node.SimpleNode.render( ica, writer); // 渲染
		 	
		 	-----------------------解析模板文件，构造抽象语法树--------------------------
			org.apache.velocity.Template.process(){
				name = "templates/test.vm";
				is = resourceLoader.getResourceStream(name); 
				BufferedReader br = new BufferedReader( new InputStreamReader( is, encoding ) );
				// rsvc == org.apache.velocity.runtime.RuntimeInstance
				// data == SimpleNode
				data = rsvc.parse( br, name);{ //  解析模板
					Parser parser = (Parser) parserPool.get(); // org.apache.velocity.runtime.ParserPoolImpl 取出一个解析器
					return parser.parse(reader, templateName); // org.apache.velocity.runtime.parser.Parser 这个是类是使用JavaCC生成出来的
				}
	            initDocument(); { // 初始化抽象语法树
		            InternalContextAdapterImpl ica = new InternalContextAdapterImpl(  new VelocityContext() );
		            ica.pushCurrentTemplateName( name );
		            ica.setCurrentResource( this );
		            ((SimpleNode)data).init( ica, rsvc); // 初始化抽象语法树
	
		            String property = scopeName+'.'+RuntimeConstants.PROVIDE_SCOPE_CONTROL;
		            provideScope = rsvc.getBoolean(property, provideScope);
		            ica.popCurrentTemplateName();
		            ica.setCurrentResource( null );
	            }
			}
			
			org.apache.velocity.runtime.parser.Parser类的注释如下：
			使用JavaCC生成，使用JJTree扩展生成抽象语法树
			This class was generated by JavaCC using the JJTree extension to produce an Abstract Syntax Tree (AST) of the template.
			
			-----------------------渲染抽象语法树--------------------------
			org.apache.velocity.Template.merge( Context context, Writer writer, List macroLibraries){
				InternalContextAdapterImpl ica = new InternalContextAdapterImpl( context ); // 适配传入的上下文
				( (SimpleNode) data ).render( ica, writer); // 渲染  org.apache.velocity.runtime.parser.node.SimpleNode
			}
			
		 */
	}
}
