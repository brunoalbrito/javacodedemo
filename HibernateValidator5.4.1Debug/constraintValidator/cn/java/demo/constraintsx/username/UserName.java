package cn.java.demo.constraintsx.username;

import java.lang.annotation.Annotation;

import javax.validation.Constraint;

@Constraint(validatedBy={UserNameFirstPartValidator.class,UserNameSecondPartyValidator.class})
public @interface UserName  {

	public static interface List extends Annotation {

		public abstract UserName[] value();
	}

	public abstract String message();

	public abstract Class[] groups();

	public abstract Class[] payload();

	public abstract String[] value();
}