package cn.java.demo.webmvc.bean.handler.byannotation;

import java.util.List;

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
	@InitBinder(value={"objectName0","objectName1"}) // 要@ModelAttribute的name值，在集合{"objectName0","objectName1"}中
	public void initBinder(DataBinder binder) {
		if(binder instanceof WebDataBinder){
			
		}
		binder.setValidator(new UserLoginFormValidator()); // 添加校验器
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
		@ModelAttribute(name="objectName0") @Validated() UserLoginForm userLoginForm, 
		BindingResult result,
		WebRequest webRequest
	) throws Exception {
		
		System.out.println(userLoginForm);
		if(result instanceof BeanPropertyBindingResult){
		}
		
		if (result.hasErrors()) {
			System.out.println("--> 错误信息");
			List<ObjectError> errorList = result.getAllErrors();
			for (ObjectError objectError : errorList) {
				if (objectError instanceof FieldError) {
					FieldError fieldError = (FieldError)objectError;
					System.out.println(fieldError.getField() +" = "+ fieldError.getDefaultMessage());
				}
				else{
					System.out.println(objectError.getDefaultMessage());
				}
			}
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
