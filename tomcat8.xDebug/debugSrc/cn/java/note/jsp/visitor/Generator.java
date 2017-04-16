package cn.java.note.jsp.visitor;

import cn.java.note.jsp.visitor.Node.CustomTag;
import cn.java.note.jsp.visitor.Node.IncludeAction;
import cn.java.note.jsp.visitor.Node.NodeType1;


public class Generator {

	private class GenerateVisitor extends Node.Visitor {
		
		public void visit(CustomTag node){
			System.out.println(node);
		}
		
		public void visit(IncludeAction node){
			System.out.println(node);
		}
		
		public void visit(NodeType1 node){
			System.out.println(node);
		}
		
	}
	
	public static void generate(Node.Nodes page){
		Generator gen = new Generator();
		page.visit(gen.new GenerateVisitor());
	}
			
}
