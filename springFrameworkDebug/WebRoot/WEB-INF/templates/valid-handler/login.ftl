 <#include "/common/common-header.ftl">
 <form id="login-form" class="login-form" action="${requestContext.getContextUrl("${requestContext.pathToServlet}/valid-handler/login")}" method="post">
	 <input  name="username" placeholder="用户名" type="text" value="username1" tabindex="1" >
	 <input  name="password" placeholder="密码" type="password" value="password1" tabindex="2" >
	 <button type="submit" name="submit"> 
	 	登 录
	 </button>
 </form>
 <#include "/common/common-footer.ftl">