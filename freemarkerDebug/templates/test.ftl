hello freemark
--------注释------------
<#-- 
注释部分
--> 

---------输出简单类型-------------
输出字符串：${username}
输出数值：${intvar}

-------模板中赋值------------
<#assign param0=0 />
<#assign param1="foo_param" /> 
${param0}
${param1} 

------输出list-------------
${list[0]}

-------输出map------------
${map["key1"]}

-------输出对象------------
不支持的访问方式：object["property1"]
${user["username"]} , ${user.username}

-------指令------------
-->指令 if
<#assign age=23>
<#if (age>60)>老年人
	<#elseif (age>40)>中年人
	<#elseif (age>20)>青年人
	<#else> 少年人
</#if> 

-->指令 list、break
->迭代list
<#list list as itemValue>
	<#if (itemValue=="item3Value")>
		<#break>
	</#if>
	${itemValue}
</#list>
->迭代map
<#assign keys = map?keys>
<#list keys as key>
	${key} ---> ${map[key]!("null")}
</#list>

<#list map?keys as key>
	${key} ---> ${map[key]!("null")}
</#list>

-->指令 include
include 指令的作用类似于JSP的包含指令
<#include "./footer.ftl">

-->指令 include
import指令用于导入FreeMarker模板中的所有变量,并将该变量放置在指定的Map对象中
<#import "./commonMap.ftl" as import0> 
<@import0.common_macro common_macro_param0="common_macro_param0_value" /> 

-->指令 Macro宏、return
定义
<#macro macroName1 macroParam0>
	<#if (username==macroParam0)>
		<img src="http://www.xxx.com/ico/${username}.gif">
	</#if>
	<#return>
</#macro>
使用
<font color="#ffff">头衔<@macroName1 macroParam0="username1" /></font> 
<font color="#ffff">头衔<@macroName1 macroParam0="username2" /></font> 

-->指令 escape , noescape
<#assign htmlcode="<test>">
${htmlcode}<#-- 默认会自动转义--> 
