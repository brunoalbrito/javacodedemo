package cn.java.demo.webmvc.bean.handler.byannotation;

import java.util.List;

import org.springframework.http.HttpMethod;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ExtendedServletRequestDataBinder;

import cn.java.demo.web.util.WebUtilx;
import cn.java.demo.webmvc.form.UserLoginForm;
import cn.java.demo.webmvc.validator.UserLoginFormValidator;

/**
 * @author zhouzhian
 *
 */
@RequestMapping(path = { "/valid-handler" })
public class ValidHandler {

	/**
	 * @param binder
	 */
	@InitBinder(value={"userLoginForm","objectName1"}) // 要@ModelAttribute的name值，在集合{"objectName0","objectName1"}中
	public void initBinder(DataBinder binder,HttpMethod httpMethod) {
		if(binder instanceof ExtendedServletRequestDataBinder){
			System.out.println("-->binder instanceof ExtendedServletRequestDataBinder");
			if(binder.getTarget() instanceof UserLoginForm){
				
			}
			if("objectName0".equals(binder.getObjectName())){
				
			}
		}
		if(RequestMethod.POST.name().equals(httpMethod.name())){ // post的提交方式才需要校验
			binder.setValidator(new UserLoginFormValidator()); // 添加校验器
		}
	}
	
	/**
	 * get请求
	 * /valid-handler/login
	 * @return
	 */
	@RequestMapping(path = { "/*" }, method = {RequestMethod.GET})
	public String login(){
		return "valid-handler/login";
	}
	
	/**
	 * post请求
	 */
	@RequestMapping(path = { "/*" }, method = {RequestMethod.POST})
	public String login(
		@ModelAttribute(name="userLoginForm") @Validated() UserLoginForm userLoginForm, 
		BindingResult result,
		WebRequest webRequest
	) throws Exception {
		
		{
			UserLoginFormValidator.debugBeanPropertyBindingResult(result);
		}
		
		if (result.hasErrors()) {
			return "redirect:login";
		}
		if("username1".equals(userLoginForm.getUsername()) && "password1".equals(userLoginForm.getPassword())){
			System.out.println("登录成功...");
			return "redirect:" + WebUtilx.getContextUrl(WebUtilx.getPathToServlet(webRequest) + "/", webRequest);
		}
		else{
			return "redirect:login";
		}
	}

}
