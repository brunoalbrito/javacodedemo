package cn.java.demo.constraintsx.address;

import java.lang.annotation.Annotation;

import javax.validation.Constraint;
public @interface Address {

	public static interface List extends Annotation {

		public abstract Address[] value();
	}

	public abstract String message();

	public abstract Class[] groups();

	public abstract Class[] payload();

	public abstract String[] value();
}