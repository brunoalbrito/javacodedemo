package cn.java.servlet.tags;

import java.util.HashMap;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

/**

1 创建自定义标签类
<cc:showUserInfo user="${pageScope.userinfo }" />

*************************************
2 在Web-Inf创建标签库描述文件.tdl(Tag Library Description)
<?xml version="1.0" encoding="UTF-8"?>
<taglib version="2.0" xmlns="http://java.sun.com/xml/ns/j2ee"
 xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
 xsi:schemaLocation="http://java.sun.com/xml/ns/j2ee web-jsptaglibrary_2_0.xsd">
<tlib-version>1.0</tlib-version>
<jsp-version>2.0</jsp-version>
<short-name>cc</short-name>
<uri>/mytaglib</uri>
<tag>
    <name>showUserInfo</name>
    <tag-class>com.mytags.UserInfoTag</tag-class>
    <body-content>empty</body-content>
    <attribute>
        <name>user</name>
         <required>false</required>
         <rtexprvalue>true</rtexprvalue>
    </attribute>
 </tag>
</taglib>
*************************************
3 配置web.xml
	 <jsp-config>
		 <taglib>
		 	<taglib-uri>/mytaglib</taglib-uri>
		 	<taglib-location>/WEB-INF/mytaglib.tld</taglib-location>
		 </taglib>
	 </jsp-config>
*************************************
4 在需要使用此标签的jsp页面头部引入
	<%@ taglib uri="/mytaglib" prefix="cc"%>
	<!-- 创建需要展现UserInfo的实例（用于测试数据） -->
5 在页面中这样使用
    <%
	    UserInfo user = new UserInfo();
	    user.setUserName("Xuwei");
	    user.setAge(33);
	    user.setEmail("test@test.test");
	    pageContext.setAttribute("userinfo", user);  
    %>

    <!-- 给标签设置user属性绑定要展现的UserInfo对象  -->
    <cc:showUserInfo user="${pageScope.userinfo }" />
 */
/**
 * 
 * @author Administrator
 *
 */
public class UserInfoTag extends TagSupport {

	private HashMap hashMapList = new HashMap();
	private String keyName = "";
	private String itemName = "";

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public HashMap getHashMapList() {
		return hashMapList;
	}

	public void setHashMapList(HashMap hashMapList) {
		this.hashMapList = hashMapList;
	}

	@Override
	public int doStartTag() throws JspException {
		try {
			JspWriter out = this.pageContext.getOut();
			return SKIP_BODY;
		} catch (Exception e) {
			throw new JspException(e.getMessage());
		}

	}

	@Override
	public int doEndTag() throws JspException {

		return EVAL_PAGE;
	}

	@Override
	public void release() {
		super.release();
	}

}
