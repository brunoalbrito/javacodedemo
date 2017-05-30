package cn.java.demo.constraintsx.address;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AddressValidator implements ConstraintValidator<Address, String> {

	@Override
	public void initialize(Address arg0) {
		
	}

	@Override
	public boolean isValid(String arg0, ConstraintValidatorContext arg1) {
		return false;
	}

}
