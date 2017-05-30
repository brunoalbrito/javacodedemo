package cn.java.demo.constraintsx.username;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UserNameFirstPartValidator implements ConstraintValidator<UserNameFirstPart, String>  {

	@Override
	public void initialize(UserNameFirstPart arg0) {
		
	}

	@Override
	public boolean isValid(String arg0, ConstraintValidatorContext arg1) {
		return false;
	}

}
