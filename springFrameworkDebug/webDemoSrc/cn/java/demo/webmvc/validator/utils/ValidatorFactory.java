package cn.java.demo.webmvc.validator.utils;

import java.util.Iterator;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.springframework.validation.Errors;

public class ValidatorFactory {

	public static org.springframework.validation.Validator getHibernateValidator() {
		return new HibernateValidator();
	}
	
	public static org.springframework.validation.Validator getMineValidator() {
		return new MineValidator();
	}

	/**
	 * hibernate的校验器
	 */
	public static class HibernateValidator implements org.springframework.validation.Validator {
		private Validator validator;
		public HibernateValidator() {
			this.validator = Validation.buildDefaultValidatorFactory().getValidator();
		}

		public boolean supports(Class clazz) {
			return true;
		}

		public void validate(Object target, Errors errors) {
			Set<ConstraintViolation<Object>> constraintViolations = validator.validate(target);
			Iterator<ConstraintViolation<Object>> iter = constraintViolations  
					.iterator();  
			while (iter.hasNext()) {  
				String message = iter.next().getMessage();  
			}  
		}
	}
	
	/**
	 * 个人校验器
	 */
	public static class MineValidator implements org.springframework.validation.Validator {
		
		@Override
		public boolean supports(Class<?> clazz) {
			return true;
		}

		@Override
		public void validate(Object target, Errors errors) {
			
		}
		
	}
}
