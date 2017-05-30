package cn.java.demo.test;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.BeforeClass;
import org.junit.Test;

import cn.java.demo.entity.User;

public class UserTest {

	private static Validator validator;

	@BeforeClass
	public static void setUp() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	@Test
	public void userNameIsNull() {
		User car = new User(null, "DD-AB-123", 4);
		Set<ConstraintViolation<User>> constraintViolations = validator
				.validate(car);

		assertEquals(1, constraintViolations.size());
		assertEquals("may not be null", constraintViolations.iterator().next()
				.getMessage());
	}

	@Test
	public void passwordTooShort() {
		User car = new User("Morris", "D", 4);

		Set<ConstraintViolation<User>> constraintViolations = validator
				.validate(car);

		assertEquals(1, constraintViolations.size());
		assertEquals("size must be between 2 and 14", constraintViolations
				.iterator().next().getMessage());
	}

	@Test
	public void ageTooLow() {
		User car = new User("Morris", "DD-AB-123", 1);

		Set<ConstraintViolation<User>> constraintViolations = validator
				.validate(car);

		assertEquals(1, constraintViolations.size());
		assertEquals("must be greater than or equal to 2", constraintViolations
				.iterator().next().getMessage());
	}

	@Test
	public void userIsValid() {
		User car = new User("Morris", "DD-AB-123", 2);

		Set<ConstraintViolation<User>> constraintViolations = validator
				.validate(car);

		assertEquals(0, constraintViolations.size());
	}

}
