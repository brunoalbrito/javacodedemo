package cn.java.demo.webmvc.bean.handler.byannotation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import cn.java.demo.webmvc.form.SpringformTagForm;

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
		return "theme-jstl-handler/method0";
		/*
		 	
		 	<spring:argument value="" /> <br />
<spring:hasBindErrors value="" name="field0" /> <br />
<spring:nestedPath path=""  /> <br />
<spring:bind path=""  ignoreNestedPath="false" htmlEscape="false" /> <br />
<spring:transform scope="request" var="commentContext" value="value0..."   /> <br />
<spring:url value="value0" context="">
	<spring:param name="param0" value="param0val" />
	<spring:param name="param1" value="param1val" />
</spring:url> <br />
<spring:eval expression="value0" var="eval0" />
${spring:mvcUrl('controller0/method0').arg(0,'username').build()}

<spring:nestedPath path="userLoginForm">
	<spring:bind path="userLoginForm"  ignoreNestedPath="false" htmlEscape="false">
		<spring:transform  value="value0..."   />
	</spring:bind>
</spring:nestedPath>

<spring:nestedPath path="userLoginForm.">
	<spring:bind path="property0"  ignoreNestedPath="false" htmlEscape="false">
		<spring:transform  value="value0..."   />
	</spring:bind>
</spring:nestedPath>

<spring:hasBindErrors name="userLoginForm">
	<spring:nestedPath path="userLoginForm.">
		<spring:bind path="property0*"  ignoreNestedPath="false" htmlEscape="false">
			${status.getErrorMessage()}
		</spring:bind>
	</spring:nestedPath>
</spring:hasBindErrors>

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
			// ----------------------------------------
			org.springframework.web.servlet.tags.form.FormTag
			org.springframework.web.servlet.tags.form.InputTag
			org.springframework.web.servlet.tags.form.PasswordInputTag
			org.springframework.web.servlet.tags.form.HiddenInputTag
	
	
	
	
	<errors path="description" />
	
		 */
	}
	
	@RequestMapping(path={"/method1/{pathVar0}/{pathVar1}"},method={RequestMethod.GET})
	public String method1(
			@PathVariable(name="pathVar0") String pathVar0,
			@PathVariable(name="pathVar1") String pathVar1,
			@RequestParam(name="reqParam0") String reqParam0,
			@RequestParam(name="reqParam1") String reqParam1,
			HttpServletRequest request,HttpServletResponse response) throws Exception{ 
		return null;
	}
	
	@InitBinder(value={"springformTagForm","objectName1"}) 
	public void initBinder(DataBinder binder) {
	}
	
	@RequestMapping(path={"/form0"},method={RequestMethod.GET})
	public ModelAndView form0(
			@ModelAttribute(name="springformTagForm") @Validated() SpringformTagForm springformTagForm,
			BindingResult result,
			HttpServletRequest request,HttpServletResponse response) throws Exception{
		ModelAndView modelAndView = new ModelAndView();
		{
			modelAndView.setViewName("theme-jstl-handler/form0");
			ModelMap modelMap = new ModelMap();
			modelMap.put("springformTagForm", springformTagForm);
			modelAndView.addAllObjects(modelMap);
			return modelAndView;
		}
		
	}
	
	
}
