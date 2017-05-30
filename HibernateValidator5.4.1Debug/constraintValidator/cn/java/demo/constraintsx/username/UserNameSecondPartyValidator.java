package cn.java.demo.constraintsx.username;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UserNameSecondPartyValidator implements ConstraintValidator<UserNameSecondPart, String>{

	@Override
	public void initialize(UserNameSecondPart arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isValid(String arg0, ConstraintValidatorContext arg1) {
		// TODO Auto-generated method stub
		return false;
	}

}
