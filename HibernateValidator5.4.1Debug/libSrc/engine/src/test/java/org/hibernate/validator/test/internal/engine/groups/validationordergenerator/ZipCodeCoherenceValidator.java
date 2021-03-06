/*
 * Hibernate Validator, declare and validate application constraints
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.validator.test.internal.engine.groups.validationordergenerator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author Hardy Ferentschik
 */
public class ZipCodeCoherenceValidator implements ConstraintValidator<ZipCodeCoherenceChecker, Address> {

	@Override
	public void initialize(ZipCodeCoherenceChecker parameters) {
	}

	@Override
	public boolean isValid(Address value, ConstraintValidatorContext constraintValidatorContext) {
		return false;
	}
}
