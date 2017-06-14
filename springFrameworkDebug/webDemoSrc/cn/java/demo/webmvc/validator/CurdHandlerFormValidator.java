package cn.java.demo.webmvc.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import cn.java.demo.webmvc.form.CurdHandlerForm;
import cn.java.demo.webmvc.form.RestForm;

public class CurdHandlerFormValidator implements Validator {

	private static final int MINIMUM_FOONAME_LENGTH = 6;

	boolean isDoAdd = false; // 是否是添加操作

	public boolean isDoAdd() {
		return isDoAdd;
	}

	public void setDoAdd(boolean isDoAdd) {
		this.isDoAdd = isDoAdd;
	}

	public boolean supports(Class clazz) {
		return CurdHandlerForm.class.isAssignableFrom(clazz);
	}

	public void validate(Object target, Errors errors) {
		CurdHandlerForm curdHandlerForm = (CurdHandlerForm) target;
		if (!isDoAdd) {
			if (curdHandlerForm.getFooId() == null) { // 修改操作必须提供ID
				ValidationUtils.rejectIfEmptyOrWhitespace(errors, "fooId", "validator.emptyOrWhitespace");
			}
		}
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "fooName", "validator.emptyOrWhitespace");
		if ((curdHandlerForm.getFooName() != null) && (curdHandlerForm.getFooName().trim().length() < MINIMUM_FOONAME_LENGTH)) { // restName长度
			errors.rejectValue("fooName", "validator.minLength",
					new Object[] { Integer.valueOf(MINIMUM_FOONAME_LENGTH) },
					"fooName至少需要[" + MINIMUM_FOONAME_LENGTH + "]个字符");
		}
	}
}