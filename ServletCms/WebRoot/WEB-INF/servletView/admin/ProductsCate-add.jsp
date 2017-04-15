<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ page import="cn.java.core.helper.url.UrlHelper,cn.java.core.helper.url.JspPageHelper,java.util.HashMap,java.util.ArrayList,java.util.Date" %><div>
<% 
ArrayList<HashMap> selectList = (ArrayList)request.getAttribute("selectList");
HashMap parentOne = (HashMap)request.getAttribute("parentOne");
%>
<form id="formTemp" action="<%=UrlHelper.url("admin","products-cate", "add") %>" method="post"  enctype="application/x-www-form-urlencoded">
		<ul class="lauer_classification">
			<% if(selectList!=null){ %>
				<li> 
					<span>父级分类：</span>
					<select name="parent_id">
						<option value="0">请选择分类</option>
						<% for (HashMap itemTemp : selectList) { %>
							<option value="<%=JspPageHelper.getString("id", itemTemp) %>" <% if(parentOne!=null && parentOne.get("id")==itemTemp.get("id")){ %>selected="selected"<% } %>>
								<%=itemTemp.get("cate_name_x") %>
							</option>
						<% } %>
					</select>
				</li>
			<% } else if(parentOne!=null){ %>
				<li> 
					<span>父级分类：</span>
					<input type="hidden"  name="parent_id" value="<%=JspPageHelper.getString("id", parentOne) %>" />
					<input type="text"  name="cate_name_p" value="<%=JspPageHelper.getString("cate_name", parentOne) %>" />
				</li>
			<%	} %>
			<li> <span>分类名称：</span>
				<input type="text"  name="cate_name" value="" />
			</li>
		</ul>
	</form>
</div>