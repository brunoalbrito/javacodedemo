package cn.java.demo.constraintsx.username;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UserNameValidator implements ConstraintValidator<UserName, String> {

	@Override
	public void initialize(UserName arg0) {
		
	}

	@Override
	public boolean isValid(String arg0, ConstraintValidatorContext arg1) {
		return false;
	}

}
