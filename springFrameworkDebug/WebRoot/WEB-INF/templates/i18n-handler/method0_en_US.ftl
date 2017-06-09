 <#include "/common/common-header.ftl">
 hello, i am "method0_en_US.ftl".<br />
 <#noparse>${requestContext.getMessage("i18n-handler.method0",['arg0','arg1'],"你好, 参数 0是  {0},参数1是 {1}")}</#noparse> 
 	---> ${requestContext.getMessage("i18n-handler.method0",['arg0','arg1'],"你好, 参数 0是  {0},参数1是 {1}")} <br />
 <#include "/common/common-footer.ftl">