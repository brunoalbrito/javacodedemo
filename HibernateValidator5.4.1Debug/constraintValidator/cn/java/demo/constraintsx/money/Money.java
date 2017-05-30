package cn.java.demo.constraintsx.money;

import java.lang.annotation.Annotation;

import javax.validation.Constraint;
@Constraint(validatedBy={MoneyValidator.class})
public @interface Money {

	public static interface List extends Annotation {

		public abstract Money[] value();
	}

	public abstract String message();

	public abstract Class[] groups();

	public abstract Class[] payload();

	public abstract String[] value();
}