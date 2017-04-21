package cn.java.demo.mock.mytag.handles;

import java.util.List;

import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.aspectj.autoproxy.AspectJAwareAdvisorAutoProxyCreator;
import org.springframework.aop.config.AdvisorComponentDefinition;
import org.springframework.aop.config.AdvisorEntry;
import org.springframework.aop.config.PointcutComponentDefinition;
import org.springframework.aop.config.PointcutEntry;
import org.springframework.aop.support.DefaultBeanFactoryPointcutAdvisor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanNameReference;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.parsing.BeanComponentDefinition;
import org.springframework.beans.factory.parsing.CompositeComponentDefinition;
import org.springframework.beans.factory.parsing.ParseState;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.core.Ordered;
import org.springframework.util.StringUtils;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

public class Tag1BeanDefinitionParser  implements BeanDefinitionParser{

	// 顶级标签
	public static final String FOO_CREATOR_BEAN_NAME = "cn.java.demo.mytag.config.internalFooCreator";
	public static final String PROXY_TARGET_CLASS_ATTRIBUTE = "proxy-target-class";
	
	// 子标签1
	private static final String SUBTAG_LOCALNAME_TAG1SUB1 = "tag1sub1";
	private static final String SUBTAG_LOCALNAME_TAG1SUB1_ATTR1 = "attr1";
	
	// 子标签2
	private static final String SUBTAG_LOCALNAME_TAG1SUB2 = "tag1sub2";
	private static final String SUBTAG_LOCALNAME_TAG1SUB2_ATTR1 = "attr1";
	private static final String SUBTAG_LOCALNAME_TAG1SUB2_ATTR2 = "attr2";
	private static final String SUBTAG_LOCALNAME_TAG1SUB2_ATTR3 = "attr3";
	private static final String SUBTAG_LOCALNAME_TAG1SUB2_ATTR1_BEAN_NAME = "tag1sub2Attr1";
	
	private static final String ID = "id";
	private static final String ORDER_PROPERTY = "order";
	
	private ParseState parseState = new ParseState();
	
	@Override
	public BeanDefinition parse(Element element, ParserContext parserContext) {
		
		/*
		 	element.getTagName() ===  "myaoptag:tag1"
		 	element.getLocalName() === "tag1"
		 	
		 	-------------
		 	<myaoptag:tag1 order="1" proxy-target-class="true">
				<myaoptag:tag1sub1 id="" attr1="" />
				<myaoptag:tag1sub2 id="" attr1="" attr2="" attr3="" />
			</myaoptag:tag1>
			
		 */

		{
			boolean debug = true;
			if(debug == true){
				System.out.println("--bof->"+this.getClass().getName()+":parse(...)");
				System.out.println("element.getNodeName() = " + element.getNodeName() + " , element.getTagName() = " + element.getLocalName());
				System.out.println("--eof->"+this.getClass().getName()+":parse(...)");
				return null;
			}
		}
		
		System.out.println("element.getNodeName()" + element.getNodeName() + " , element.getTagName()" + element.getLocalName());
		
		CompositeComponentDefinition compositeDef =
				new CompositeComponentDefinition(element.getTagName(), parserContext.extractSource(element));
		parserContext.pushContainingComponent(compositeDef); // 入栈

		configureFooCreator(parserContext, element); 

		// 子元素
		List<Element> childElts = DomUtils.getChildElements(element);
		for (Element elt: childElts) {
			String localName = parserContext.getDelegate().getLocalName(elt);
			if (SUBTAG_LOCALNAME_TAG1SUB1.equals(localName)) {
				parseTag1sub1(elt, parserContext);
			}
			else if (SUBTAG_LOCALNAME_TAG1SUB2.equals(localName)) {
				parseTag1sub2(elt, parserContext);
			}
		}

		parserContext.popAndRegisterContainingComponent(); // 出栈、并注册
		return null;
	}

	/**
	 * 处理顶级标签
	 * @param parserContext
	 * @param element
	 */
	private void configureFooCreator(ParserContext parserContext, Element element) {
		//
		Class<?> cls = AspectJAwareAdvisorAutoProxyCreator.class;
		BeanDefinitionRegistry registry = parserContext.getRegistry();
		Object source = parserContext.extractSource(element);
		RootBeanDefinition beanDefinition = new RootBeanDefinition(cls);
		beanDefinition.setSource(source);
		beanDefinition.getPropertyValues().add("order", Ordered.HIGHEST_PRECEDENCE);
		beanDefinition.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
		registry.registerBeanDefinition(FOO_CREATOR_BEAN_NAME, beanDefinition);

		//
		if (element != null) {
			boolean proxyTargetClass = Boolean.valueOf(element.getAttribute(PROXY_TARGET_CLASS_ATTRIBUTE));
			if (proxyTargetClass) {
				if (registry.containsBeanDefinition(FOO_CREATOR_BEAN_NAME)) {
					BeanDefinition definition = registry.getBeanDefinition(FOO_CREATOR_BEAN_NAME);
					definition.getPropertyValues().add("proxyTargetClass", Boolean.TRUE);
				}
			}
		}

		//
		if (beanDefinition != null) {
			BeanComponentDefinition componentDefinition =
					new BeanComponentDefinition(beanDefinition, FOO_CREATOR_BEAN_NAME);
			parserContext.registerComponent(componentDefinition);  // 注册成组件到 ParserContext
		}
	}

	/**
	 * 处理子标签1
	 * @param tagsub1Element
	 * @param parserContext
	 * @return
	 */
	private AbstractBeanDefinition parseTag1sub1(Element tagsub1Element, ParserContext parserContext) {
		
		String id = tagsub1Element.getAttribute(ID);
		String attr1 = tagsub1Element.getAttribute(SUBTAG_LOCALNAME_TAG1SUB1_ATTR1);
		AbstractBeanDefinition tag1sub1Definition = null;

		try {
			this.parseState.push(new PointcutEntry(id));
			
			tag1sub1Definition = new RootBeanDefinition(AspectJExpressionPointcut.class);
			tag1sub1Definition.setScope(BeanDefinition.SCOPE_PROTOTYPE);
			tag1sub1Definition.setSynthetic(true);
			tag1sub1Definition.getPropertyValues().add(SUBTAG_LOCALNAME_TAG1SUB1_ATTR1, attr1);
			tag1sub1Definition.setSource(parserContext.extractSource(tagsub1Element));
			
			String tagsub1BeanName =id;
			if (StringUtils.hasText(tagsub1BeanName)) {
				parserContext.getRegistry().registerBeanDefinition(tagsub1BeanName, tag1sub1Definition); // 注册bean定义
			}
			else {
				tagsub1BeanName = parserContext.getReaderContext().registerWithGeneratedName(tag1sub1Definition);
			}
			parserContext.registerComponent(
					new PointcutComponentDefinition(tagsub1BeanName, tag1sub1Definition, attr1)); // 维护和父级标签的关系
		}
		finally {
			this.parseState.pop();
		}

		return tag1sub1Definition;
	}
	
	/**
	 * 处理子标签2
	 * @param tagsub1Element
	 * @param parserContext
	 * @return
	 */
	private void parseTag1sub2(Element tag1sub2Element, ParserContext parserContext) {
		RootBeanDefinition tag1sub2Definition = new RootBeanDefinition(DefaultBeanFactoryPointcutAdvisor.class);
		tag1sub2Definition.setSource(parserContext.extractSource(tag1sub2Element));

		String attr1 = tag1sub2Element.getAttribute(SUBTAG_LOCALNAME_TAG1SUB2_ATTR1);
		if (!StringUtils.hasText(attr1)) {
			parserContext.getReaderContext().error(
					"'advice-ref' attribute contains empty value.", tag1sub2Element, this.parseState.snapshot());
		}
		else {
			tag1sub2Definition.getPropertyValues().add(
					SUBTAG_LOCALNAME_TAG1SUB2_ATTR1_BEAN_NAME, new RuntimeBeanNameReference(attr1));
		}
		if (tag1sub2Element.hasAttribute(ORDER_PROPERTY)) {
			tag1sub2Definition.getPropertyValues().add(
					ORDER_PROPERTY, tag1sub2Definition.getAttribute(ORDER_PROPERTY));
		}
		
		String id = tag1sub2Element.getAttribute(ID);
		try {
			this.parseState.push(new AdvisorEntry(id));
			String tag1sub2BeanName = id;
			if (StringUtils.hasText(tag1sub2BeanName)) {
				parserContext.getRegistry().registerBeanDefinition(tag1sub2BeanName, tag1sub2Definition); // 注册bean定义 
			}
			else {
				tag1sub2BeanName = parserContext.getReaderContext().registerWithGeneratedName(tag1sub2Definition);
			}
			
			Object tag1sub2 = parseTag1sub2Property(tag1sub2Element, parserContext);
			if (tag1sub2 instanceof BeanDefinition) {
				tag1sub2Definition.getPropertyValues().add(SUBTAG_LOCALNAME_TAG1SUB2, tag1sub2);
				parserContext.registerComponent(
						new AdvisorComponentDefinition(tag1sub2BeanName, tag1sub2Definition, (BeanDefinition) tag1sub2)); // 维护和父级标签的关系
			}
			else if (tag1sub2 instanceof String) {
				tag1sub2Definition.getPropertyValues().add(SUBTAG_LOCALNAME_TAG1SUB2, new RuntimeBeanReference((String) tag1sub2));
				parserContext.registerComponent(
						new AdvisorComponentDefinition(tag1sub2BeanName, tag1sub2Definition));
			}
		}
		finally {
			this.parseState.pop();
		}
	}
	
	/**
	 * 处理子标签2的属性
	 * @param tagsub1Element
	 * @param parserContext
	 * @return
	 */
	private Object parseTag1sub2Property(Element element, ParserContext parserContext) {
		
		if (element.hasAttribute(SUBTAG_LOCALNAME_TAG1SUB2_ATTR2) && element.hasAttribute(SUBTAG_LOCALNAME_TAG1SUB2_ATTR3)) {
			parserContext.getReaderContext().error(
					"Cannot define both 'pointcut' and 'pointcut-ref' on <advisor> tag.",
					element, this.parseState.snapshot());
			return null;
		}
		else if (element.hasAttribute(SUBTAG_LOCALNAME_TAG1SUB2_ATTR2)) {
			String tag1sub2Attr2 = element.getAttribute(SUBTAG_LOCALNAME_TAG1SUB2_ATTR2);
			RootBeanDefinition pointcutDefinition = new RootBeanDefinition(AspectJExpressionPointcut.class);
			pointcutDefinition.setScope(BeanDefinition.SCOPE_PROTOTYPE);
			pointcutDefinition.setSynthetic(true);
			pointcutDefinition.getPropertyValues().add(SUBTAG_LOCALNAME_TAG1SUB2_ATTR2, tag1sub2Attr2);
			pointcutDefinition.setSource(parserContext.extractSource(element));
			return pointcutDefinition;
		}
		else if (element.hasAttribute(SUBTAG_LOCALNAME_TAG1SUB2_ATTR3)) {
			String tag1sub2Attr3 = element.getAttribute(SUBTAG_LOCALNAME_TAG1SUB2_ATTR3);
			if (!StringUtils.hasText(tag1sub2Attr3)) {
				parserContext.getReaderContext().error(
						"'pointcut-ref' attribute contains empty value.", element, this.parseState.snapshot());
				return null;
			}
			return tag1sub2Attr3;
		}
		else {
			parserContext.getReaderContext().error(
					"Must define one of 'pointcut' or 'pointcut-ref' on <advisor> tag.",
					element, this.parseState.snapshot());
			return null;
		}
	}
	
}
