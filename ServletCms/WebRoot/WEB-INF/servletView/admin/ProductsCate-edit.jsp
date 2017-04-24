<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ page import="cn.java.core.helper.url.UrlHelper,cn.java.core.helper.url.JspPageHelper,java.util.HashMap,java.util.ArrayList,java.util.Date" %><div>
<% 
HashMap item = (HashMap)request.getAttribute("item");
ArrayList<HashMap> selectList = (ArrayList)request.getAttribute("selectList");
%>
<form id="formTemp" action="<%=UrlHelper.url("admin","products-cate", "edit") %>&id=<%=item.get("id") %>" method="post"  enctype="application/x-www-form-urlencoded">
<input type="hidden" name="id" value="<%=item.get("id") %>" />
		<ul class="lauer_classification">
			<% if(selectList!=null){ %>
				<li> 
					<span>父级分类：</span>
					<select name="parent_id">
						<option value="0">请选择分类</option>
						<% for (HashMap itemTemp : selectList) { %>
							<option value="<%=JspPageHelper.getString("id", itemTemp) %>" <% if(item!=null && item.get("parent_id")==itemTemp.get("id")){ %>selected="selected"<% } %>>
								<%=itemTemp.get("cate_name_x") %>
							</option>
						<% } %>
					</select>
				</li>
			<% }else{ %>
				<li> 
					<span>父级分类：</span>
					<input type="hidden"  name="parent_id" value="0" />
					<input type="text"  name="cate_name" value="没有父级分类" />
				</li>
			<% } %>
			
			<li> <span>分类名称：</span>
				<input type="text"  name="cate_name" value="<%=JspPageHelper.getString("cate_name", item) %>" />
			</li>
		</ul>
	</form>
</div>