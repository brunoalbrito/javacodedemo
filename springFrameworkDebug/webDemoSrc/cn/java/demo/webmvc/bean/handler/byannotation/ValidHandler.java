package cn.java.demo.webmvc.bean.handler.byannotation;

import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.java.demo.webmvc.form.UserLoginForm;
import cn.java.demo.webmvc.validator.UserLoginFormValidator;

/**
 * @author zhouzhian
 *
 */
@RequestMapping(path = { "/valid-handler" }, method = { RequestMethod.GET, RequestMethod.POST })
public class ValidHandler {

	/**
	 * @param binder
	 */
	@InitBinder(value={"objectName0","objectName1"}) // 要@ModelAttribute的name值，在集合{"userLognForm","userLognForm0"}中
	public void initBinder(DataBinder binder) {
		binder.setValidator(new UserLoginFormValidator()); // 添加校验器
	}

	/**
	 */
	@RequestMapping(path = { "/*" }, method = { RequestMethod.GET })
	public String login(
		@ModelAttribute(name="objectName0") @Validated() UserLoginForm userLogin,
		BindingResult result
	) throws Exception {
		
		if(result instanceof BeanPropertyBindingResult){
			
		}
		
		if (result.hasErrors()) {
			return "redirect:user/login";
		}
		return "redirect:/";
	}

}
