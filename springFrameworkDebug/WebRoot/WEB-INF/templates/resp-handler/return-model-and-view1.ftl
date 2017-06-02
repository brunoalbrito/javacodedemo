<#include "/common/common-header.ftl">
<!--
模板中数据的访问是通过：freemarker.ext.servlet.AllHttpScopesHashModel
	访问顺序是：
		用户赋值的 -- > request中的attributes--- > Session中的attributes --- > Application中的attributes
	其中还注入的对象：
		JspTaglibs、Application(访问的是attributes)、Session(访问的是attributes)、Request(访问的是attributes)、RequestParameters(访问的是attributes)
-->
this is FreeMarker template.<br />
hello , attr0 = ${Request["attr0"]} = ${Request.attr0}, attr1 = ${Request["attr1"]} = ${Request.attr1} <br />
hello , attr0 = ${attr0} , attr1 = ${attr1}
<#--
${Application.attr0}<br />
${Session.attr0}<br />
${Request.attr0}<br />
${RequestParameters.attr0}<br />
-->
<#include "/common/common-footer.ftl">
