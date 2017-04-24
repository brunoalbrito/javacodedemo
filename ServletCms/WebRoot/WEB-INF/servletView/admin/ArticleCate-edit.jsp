<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%><div><%@ page import="cn.java.core.helper.url.UrlHelper,java.util.HashMap,java.util.ArrayList,cn.java.core.helper.url.JspPageHelper" %>
	<form id="formTemp" action="<%=UrlHelper.url("admin","article-cate","edit","") %>" method="post"  enctype="application/x-www-form-urlencoded">
		<ul class="lauer_classification">
			<li> 
				<span>父级分类：</span>
				<% HashMap itemInfo = (HashMap)request.getAttribute("item"); %>
				<input type="hidden" name="parent_id"  value="<%=JspPageHelper.getString("parentId", itemInfo) %>" />
				<% 
				if(itemInfo.containsKey("parentId")&&!"0".equals(itemInfo.get("parentId").toString())){
					if(request.getAttribute("cateItemList")!=null){ 
						ArrayList mArrayList1= (ArrayList)request.getAttribute("cateItemList");
						for(int index=0;index<mArrayList1.size();index++){
							HashMap mHashMap1 = (HashMap)mArrayList1.get(index);
							if(mHashMap1.get("id").toString().equals(itemInfo.get("parentId").toString())){
				%>
						<input type="text"  readonly="readonly" value="<%=JspPageHelper.getString("cateName", mHashMap1) %>" />
				<%
							}
						}
					}
				%>
			 	<%	
				 }else{%> 
					 <input type="text"  readonly="readonly" value="没有父级" />
				<%} %>
			</li>
			<li> <span>分类名称：</span>
				<input type="text"  name="cate_name" value="<%=JspPageHelper.getString("cateName", itemInfo) %>" />
				<input type="hidden"  name="id" value="<%=JspPageHelper.getString("id", itemInfo) %>" />
			</li>
		</ul>
	</form>
</div>