package cn.java.demo.constraintsx.username;

import java.lang.annotation.Annotation;

import javax.validation.Constraint;

public @interface UserNameFirstPart {

	public static interface List extends Annotation {

		public abstract UserNameFirstPart[] value();
	}

	public abstract String message();

	public abstract Class[] groups();

	public abstract Class[] payload();

	public abstract String[] value();
}