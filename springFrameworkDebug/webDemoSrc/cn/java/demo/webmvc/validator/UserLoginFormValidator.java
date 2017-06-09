package cn.java.demo.webmvc.validator;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.ConfigurablePropertyAccessor;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import cn.java.demo.webmvc.form.UserLoginForm;

public class UserLoginFormValidator implements Validator {

	private static final int MINIMUM_PASSWORD_LENGTH = 6;

	public boolean supports(Class clazz) {
		return UserLoginForm.class.isAssignableFrom(clazz);
	}

	public void validate(Object target, Errors errors) {
		
		if(target instanceof UserLoginForm){
			
		}
		
		if(errors instanceof BeanPropertyBindingResult){
//			BeanPropertyBindingResult beanPropertyBindingResult = (BeanPropertyBindingResult)errors;
//			ConfigurablePropertyAccessor configurablePropertyAccessor = beanPropertyBindingResult.getPropertyAccessor();
//			if(configurablePropertyAccessor instanceof BeanWrapper){
//				
//			}
//			configurablePropertyAccessor.setPropertyValue("propertyName0", "propertyName0Value");
//			configurablePropertyAccessor.getPropertyValue("propertyName0");
		}
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "userLoginFormValidator.username.required"); // 必须通过用户名
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "userLoginFormValidator.password.required"); // 必须通过密码
		
		UserLoginForm userLoginForm = (UserLoginForm) target;
		if((userLoginForm.getPassword() != null)&&(userLoginForm.getPassword().trim().length() < MINIMUM_PASSWORD_LENGTH)){ // 密码长度
			errors.rejectValue("password", "userLoginFormValidator.username.minLength",
					new Object[]{Integer.valueOf(MINIMUM_PASSWORD_LENGTH)},
					"密码至少需要[" + MINIMUM_PASSWORD_LENGTH + "]个字符");
		}
		
	}
}