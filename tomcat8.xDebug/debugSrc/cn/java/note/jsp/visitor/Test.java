package cn.java.note.jsp.visitor;

import java.util.HashMap;

import cn.java.note.jsp.visitor.Node.CustomTag;
import cn.java.note.jsp.visitor.Node.IncludeAction;

public class Test {

	public static void main(String[] args) {
		
		Node.Root root = new Node.Root();
		new CustomTag("myprefix:tag1","myprefix","tag1" ,new HashMap(){
			{
				put("attr1", "attr1_value");
				put("attr2", "attr2_value");
			}
		},root);
		new CustomTag("myprefix:tag2","myprefix","tag2",null,root);
		new CustomTag("myprefix:tag3","myprefix","tag3",new HashMap(){
			{
				put("attr1", "attr1_value");
				put("attr2", "attr2_value");
			}
		},root);
		new IncludeAction("jsp:include","include",new HashMap(){
			{
				put("attr1", "attr1_value");
				put("attr2", "attr2_value");
			}
		},root);
		new IncludeAction("jsp:include","include",null,root);
		Node.Nodes page = new Node.Nodes(root);
		Generator.generate(page);
	}

}
