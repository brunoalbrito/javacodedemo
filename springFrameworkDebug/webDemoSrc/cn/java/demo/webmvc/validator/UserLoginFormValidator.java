package cn.java.demo.webmvc.validator;

import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.web.context.request.ServletWebRequest;

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

		}
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "field.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "field.required");
		UserLoginForm login = (UserLoginForm) target;
		if (login.getPassword() != null
				&& login.getPassword().trim().length() < MINIMUM_PASSWORD_LENGTH) {
			errors.rejectValue("password", "field.min.length",
					new Object[]{Integer.valueOf(MINIMUM_PASSWORD_LENGTH)},
					"The password must be at least [" + MINIMUM_PASSWORD_LENGTH + "] characters in length.");
		}
	}
}