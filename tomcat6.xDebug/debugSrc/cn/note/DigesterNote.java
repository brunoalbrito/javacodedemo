package cn.note;

import org.apache.tomcat.util.digester.ObjectCreateRule;
import org.apache.tomcat.util.digester.Rules;
import org.apache.tomcat.util.digester.RulesBase;
import org.apache.tomcat.util.digester.SetNextRule;
import org.apache.tomcat.util.digester.SetPropertiesRule;

public class DigesterNote {

/**

通过源代码知道：
	server.xml只有一个Server标签
	


 */
/*

addObjectCreate 
	addRule(pattern,new ObjectCreateRule(className, attributeName));
addSetProperties	 
	addRule(pattern,new SetPropertiesRule());
addSetNext
	addRule(pattern,new SetNextRule(methodName, paramType));
addRule
	addRule(pattern,new SetNextRule(methodName, paramType));
	
addRuleSet（自定义的规则设置对象，要继承RuleSetBase）
	调用"自定义的规则设置对象".addRuleInstances(this),来反向调用addObjectCreate、addSetProperties、addSetNext、addRule
	
//解析开始
  matches：用来存放指定标签事件的触发对象列表

class digester
{
	digester.push(instance)//把元素压入站
	digester.peek();//取得栈顶元素，没有弹出
	digester.pop();//把元素弹出
	
}

在startElement的时候，调用“标签事件的触发对象列表”的begin方法
如：
	ObjectCreateRule.begin()
		创建对象，并推入堆栈  digester.push(instance)===stack.push(object)===ArrayStack.push()===ArrayList.add()::::stack：用来存放创建的对象
	SetPropertiesRule.begin()
		把匹配到的开始标签中的属性反射到栈顶的对象属性中 digester.peek().....IntrospectionUtils.setProperty(top, name, value)
	SetNextRule.begin()
	没有执行方法
	
在endElement的时候，调用“标签事件的触发对象列表”的body方法和end方法

                     * 迭代执行规则执行对象列表中的body方法
                     *  如：ObjectCreateRule.body()
                     * 			没有执行内容
                     * 	  SetPropertiesRule.body()
                     * 			没有执行内容
                     * 	  SetNextRule.body()
                     * 			没有执行内容


                     * 迭代执行规则执行对象列表中的end方法，和声明的顺序相反，逆序迭代
                     *  如：
                     * 	  SetNextRule.end()
                     * 			IntrospectionUtils.callMethod1(parent, methodName,child, paramType, digester.getClassLoader());
                     * 	  SetPropertiesRule.end()
                     * 			没有执行内容
                     *  ObjectCreateRule.end()
                     * 			digester.pop()，把对象弹出来
                    
                    
	 */
	public static void main(String[] args) {

	}

	
}
