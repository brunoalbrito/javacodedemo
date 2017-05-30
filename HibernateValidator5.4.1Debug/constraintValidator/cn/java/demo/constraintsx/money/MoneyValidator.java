package cn.java.demo.constraintsx.money;

import java.math.BigDecimal;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MoneyValidator implements ConstraintValidator<Money, BigDecimal> {

	@Override
	public void initialize(Money arg0) {
		
	}

	@Override
	public boolean isValid(BigDecimal arg0, ConstraintValidatorContext arg1) {
		return false;
	}

}
