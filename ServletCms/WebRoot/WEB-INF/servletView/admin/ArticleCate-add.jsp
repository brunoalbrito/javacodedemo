<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%><div><%@ page import="cn.java.core.helper.url.UrlHelper,java.util.HashMap,java.util.ArrayList,cn.java.core.helper.url.JspPageHelper" %>
	<form id="formTemp" action="<%=UrlHelper.url("admin","article-cate","add","") %>" method="post"  enctype="application/x-www-form-urlencoded">
		<ul class="lauer_classification">
			<li> 
				<span>父级分类：</span>
						<select name="parent_id">
							<option value="0">请选择分类</option>
							<% 
								if(request.getAttribute("cateItemList")!=null){ 
									ArrayList mArrayList2= (ArrayList)request.getAttribute("cateItemList");
									for(int index2=0;index2<mArrayList2.size();index2++){
										HashMap mHashMap2 = (HashMap)mArrayList2.get(index2);
							%>
								<option value="<%=JspPageHelper.getString("id", mHashMap2) %>" ><%=JspPageHelper.getString("cateName", mHashMap2) %></option>
							<%
								
									}
								}
							%>
						</select>
			</li>
			<li> <span>分类名称：</span>
				<input type="text"  name="cate_name" value="" />
			</li>
		</ul>
	</form>
</div>