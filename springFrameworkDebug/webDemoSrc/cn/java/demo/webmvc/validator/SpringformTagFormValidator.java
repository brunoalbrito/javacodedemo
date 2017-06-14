package cn.java.demo.webmvc.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import cn.java.demo.webmvc.form.SpringformTagForm;

public class SpringformTagFormValidator implements Validator {

	private static final int MINIMUM_PASSWORD_LENGTH = 6;
	private static final int MIN_LIKES_CHECKED_SIZE = 1;

	public boolean supports(Class clazz) {
		return SpringformTagForm.class.isAssignableFrom(clazz);
	}

	public void validate(Object target, Errors errors) {
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "validator.emptyOrWhitespace");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "validator.emptyOrWhitespace");
//		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "csrfToken", "validator.emptyOrWhitespace");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "city", "validator.emptyOrWhitespace");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "sex", "validator.emptyOrWhitespace");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description", "validator.emptyOrWhitespace");
		
		SpringformTagForm springformTagForm = (SpringformTagForm) target;
		
		if((springformTagForm.getPassword() != null)&&(springformTagForm.getPassword().trim().length() < MINIMUM_PASSWORD_LENGTH)){ // 密码长度
			errors.rejectValue("password", "validator.minLength",	new Object[]{Integer.valueOf(MINIMUM_PASSWORD_LENGTH)},"密码至少需要[" + MINIMUM_PASSWORD_LENGTH + "]个字符"); 
		}
		
		if(springformTagForm.getLikes()==null || (springformTagForm.getLikes().size()<=0)){
			errors.rejectValue("likes", "validator.minChecked",	new Object[]{Integer.valueOf(MIN_LIKES_CHECKED_SIZE)},"至少勾选[" + MIN_LIKES_CHECKED_SIZE + "]个选项"); 
		}
	
	}
}