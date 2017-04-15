package cn.java.note.jsp.visitor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

abstract class Node {
	protected String qName;
	protected String localName;
	protected HashMap attrs;
	protected Node parent;
	protected Nodes body;
	public abstract void accept(Visitor v);
	public Node(String qName, String localName,HashMap attrs,Node parent){
		this.qName = qName;
		this.localName = localName;
		this.attrs = attrs;
		addToParent(parent);
	}
	public Node(){
	}

	public Nodes getBody() {
		return body;
	}

	public void setBody(Nodes body) {
		this.body = body;
	}

	private void addToParent(Node parent) {
		if (parent != null) {
			this.parent = parent; // 保存对父节点的依赖
			Nodes parentBody = parent.getBody();
			if (parentBody == null) {
				parentBody = new Nodes();
				parent.setBody(parentBody);
			}
			parentBody.add(this); // 保存父节点对子节点的依赖
		}
	}

	@Override
	public String toString() {
		return "Node [qName=" + qName + ", localName=" + localName + ", attrs=" + attrs + "]";
	}

	/**
	 * 节点列表
	 */
	public static class Nodes {
		private final List<Node> list;
		private Node.Root root;

		public Nodes() {
			list = new Vector<>();
		}

		public Nodes(Node.Root root) {
			this.root = root;
			list = new Vector<>();
			list.add(root);
		}

		public void add(Node n) {
			list.add(n);
			root = null;
		}

		public void remove(Node n) {
			list.remove(n);
		}

		public int size() {
			return list.size();
		}

		public void visit(Visitor v)  {
			Iterator<Node> iter = list.iterator();
			while (iter.hasNext()) {
				Node n = iter.next();
				n.accept(v);
			}
		}
	}

	/**
	 * 访问器
	 */
	public static class Visitor {
		public void visit(CustomTag node){
		}

		public void visit(IncludeAction node){

		}

		public void visit(NodeType1 node){

		}

		protected void doVisit(Node n)  {
		}

		protected void visitBody(Node n){
			if (n.getBody() != null) {
				n.getBody().visit(this);
			}
		}

		public void visit(Root n)  {
			doVisit(n);
			visitBody(n);
		}
	}

	/**
	 * 根节点
	 * @author Administrator
	 */
	public static class Root extends Node {

		public Root() {
		}

		@Override
		public void accept(Visitor v){
			v.visit(this);
		}
	}

	/**
	 * 节点类型1
	 * @author Administrator
	 *
	 */
	public static class CustomTag extends Node {
		private final String prefix;

		public CustomTag(String qName, String prefix, String localName,HashMap attrs ,Node parent){
			super(qName, localName,attrs,parent);
			this.prefix = prefix;
		}

		@Override
		public void accept(Visitor v) {
			v.visit(this);
		}

		@Override
		public String toString() {
			return "CustomTag [qName=" + qName + ", localName=" + localName + ", prefix=" + prefix + ", attrs=" + attrs + "]";
		}


	}

	/**
	 * 节点类型2
	 * @author Administrator
	 *
	 */
	public static class IncludeAction extends Node {

		public IncludeAction(String qName, String localName,HashMap attrs,Node parent) {
			super(qName, localName,attrs,parent);
		}

		@Override
		public void accept(Visitor v) {
			v.visit(this);
		}
		
	}

	/**
	 * 节点类型3
	 * @author Administrator
	 *
	 */
	public static class NodeType1 extends Node {
		public NodeType1(String qName, String localName,HashMap attrs ,Node parent) {
			super(qName, localName,attrs,parent);
		}

		@Override
		public void accept(Visitor v) {
			v.visit(this);
		}
	}
}
