package cn.java.demo.webmvc.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import cn.java.demo.webmvc.form.RestForm;

public class RestFormValidator implements Validator {

	private static final int MINIMUM_RESTNAME_LENGTH = 6;

	boolean isRequestMethodPut = false; // 是否是添加操作

	public boolean isRequestMethodPut() {
		return isRequestMethodPut;
	}

	public void setRequestMethodPut(boolean isRequestMethodPut) {
		this.isRequestMethodPut = isRequestMethodPut;
	}

	public boolean supports(Class clazz) {
		return RestForm.class.isAssignableFrom(clazz);
	}

	public void validate(Object target, Errors errors) {
		RestForm restForm = (RestForm) target;
		if (!isRequestMethodPut) {
			if (restForm.getRestId() == null) { // 修改操作必须提供ID
				ValidationUtils.rejectIfEmptyOrWhitespace(errors, "restId", "restFormValidator.restId.required");
			}
		}

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "restName", "restFormValidator.restName.required");
		if ((restForm.getRestName() != null) && (restForm.getRestName().trim().length() < MINIMUM_RESTNAME_LENGTH)) { // restName长度
			errors.rejectValue("restName", "restFormValidator.restName.minLength",
					new Object[] { Integer.valueOf(MINIMUM_RESTNAME_LENGTH) },
					"restName至少需要[" + MINIMUM_RESTNAME_LENGTH + "]个字符");
		}
	}
}