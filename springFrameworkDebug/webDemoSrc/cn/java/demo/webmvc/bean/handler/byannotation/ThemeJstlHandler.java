package cn.java.demo.webmvc.bean.handler.byannotation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author zhouzhian
 *
 */
@RequestMapping(
	path={"/theme-jstl-handler"},
	method={RequestMethod.GET,RequestMethod.POST}
)
public class ThemeJstlHandler {
	
	/**
	 * /theme-jstl-handler/method0
	 */
	@RequestMapping(path={"/method0"},method={RequestMethod.GET})
	public String method0(HttpServletRequest request,HttpServletResponse response) throws Exception{ 
		return "theme-handler/method0";
		/*
		 	
		 	<spring:argument value="" /> <br />
<spring:hasBindErrors value="" name="field0" /> <br />
<spring:nestedPath path=""  /> <br />
<spring:bind path=""  ignoreNestedPath="false" htmlEscape="false" /> <br />
<spring:transform value="value0"   /> <br />
<spring:url value="value0" context="">
	<spring:param name="param0" value="param0val" />
	<spring:param name="param1" value="param1val" />
</spring:url> <br />
<spring:eval expression="value0" var="eval0" />
${spring:mvcUrl('controller0/method0').arg(0,'username').build()}


		 	spring对jstl的支持
			 	org.springframework.web.servlet.tags.HtmlEscapeTag
			 	org.springframework.web.servlet.tags.EscapeBodyTag
			 	org.springframework.web.servlet.tags.MessageTag
			 	org.springframework.web.servlet.tags.ThemeTag
			 	org.springframework.web.servlet.tags.ArgumentTag
			 	org.springframework.web.servlet.tags.BindErrorsTag
			 	org.springframework.web.servlet.tags.NestedPathTag
			 	org.springframework.web.servlet.tags.BindTag
			 	org.springframework.web.servlet.tags.TransformTag
			 	org.springframework.web.servlet.tags.UrlTag
			 	org.springframework.web.servlet.tags.ParamTag
			 	org.springframework.web.servlet.tags.EvalTag
			 	org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder
		 */
	}
	
}
