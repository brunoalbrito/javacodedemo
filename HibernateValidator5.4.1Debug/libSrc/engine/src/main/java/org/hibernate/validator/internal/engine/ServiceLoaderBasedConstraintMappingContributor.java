/*
 * Hibernate Validator, declare and validate application constraints
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.validator.internal.engine;

import java.lang.annotation.Annotation;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.ConstraintValidator;

import org.hibernate.validator.cfg.ConstraintMapping;
import org.hibernate.validator.cfg.context.ConstraintDefinitionContext;
import org.hibernate.validator.internal.util.TypeResolutionHelper;
import org.hibernate.validator.internal.util.privilegedactions.GetConstraintValidatorList;
import org.hibernate.validator.spi.cfg.ConstraintMappingContributor;

import com.fasterxml.classmate.ResolvedType;

import static org.hibernate.validator.internal.util.CollectionHelper.newHashMap;

/**
 * Contributor of constraint definitions discovered by the Java service loader mechanism.
 *
 * @author Hardy Ferentschik
 */
public class ServiceLoaderBasedConstraintMappingContributor implements ConstraintMappingContributor {
	/**
	 * Used for resolving type parameters. Thread-safe.
	 */
	private final TypeResolutionHelper typeResolutionHelper;

	public ServiceLoaderBasedConstraintMappingContributor(TypeResolutionHelper typeResolutionHelper) {
		this.typeResolutionHelper = typeResolutionHelper;
	}

	@Override
	public void createConstraintMappings(ConstraintMappingBuilder builder) {
		// !!! builder === org.hibernate.validator.internal.engine.ValidatorFactoryImpl.DefaultConstraintMappingBuilder
		Map<Class<?>, List<Class<?>>> customValidators = newHashMap();

		// find additional constraint validators via the Java ServiceLoader mechanism
		GetConstraintValidatorList constraintValidatorListAction = new GetConstraintValidatorList();
		//  “META-INF/services/javax.validation.ConstraintValidator” 文件内容的实例 constraintValidatorListAction
		List<ConstraintValidator<?, ?>> discoveredConstraintValidators = run( constraintValidatorListAction ); 

		for ( ConstraintValidator<?, ?> constraintValidator : discoveredConstraintValidators ) {
			Class<?> constraintValidatorClass = constraintValidator.getClass();
			Class<?> annotationType = determineAnnotationType( constraintValidatorClass ); // 自定义注解类型

			List<Class<?>> validators = customValidators.get( annotationType );
			if ( annotationType != null && validators == null ) {
				validators = new ArrayList<Class<?>>();
				customValidators.put( annotationType, validators ); // 自定义校验器
			}
			validators.add( constraintValidatorClass );
		}

		// builder === org.hibernate.validator.internal.engine.ValidatorFactoryImpl.DefaultConstraintMappingBuilder
		// constraintMapping === org.hibernate.validator.internal.cfg.context.DefaultConstraintMapping
		ConstraintMapping constraintMapping = builder.addConstraintMapping(); 
		for ( Map.Entry<Class<?>, List<Class<?>>> entry : customValidators.entrySet() ) { // 自定义校验器
			registerConstraintDefinition( constraintMapping, entry.getKey(), entry.getValue() );
		}
	}

	@SuppressWarnings("unchecked")
	private <A extends Annotation> void registerConstraintDefinition(ConstraintMapping constraintMapping,
			Class<?> constraintType,
			List<Class<?>> validatorTypes) {
		{
			/*
			 	constraintType === cn.java.demo.constraintsx.Money
			 	validatorTypes = {
			 		cn.java.demo.constraintsx.MoneyValidator
			 	}
			 */
		}
		
		// constraintMapping === org.hibernate.validator.internal.cfg.context.DefaultConstraintMapping
		ConstraintDefinitionContext<A> context = constraintMapping
				.constraintDefinition( (Class<A>) constraintType )
				.includeExistingValidators( true );
		// context === org.hibernate.validator.internal.cfg.context.ConstraintDefinitionContextImpl
		for ( Class<?> validatorType : validatorTypes ) {
			// validatorType === cn.java.demo.constraintsx.MoneyValidator
			context.validatedBy( (Class<? extends ConstraintValidator<A, ?>>) validatorType );
		}
	}

	private Class<?> determineAnnotationType(Class<?> constraintValidatorClass) {
		ResolvedType resolvedType = typeResolutionHelper.getTypeResolver()
				.resolve( constraintValidatorClass );

		return resolvedType.typeParametersFor( ConstraintValidator.class ).get( 0 ).getErasedType();
	}

	/**
	 * Runs the given privileged action, using a privileged block if required.
	 * <p>
	 * <b>NOTE:</b> This must never be changed into a publicly available method to avoid execution of arbitrary
	 * privileged actions within HV's protection domain.
	 */
	private <T> T run(PrivilegedAction<T> action) {
		return System.getSecurityManager() != null ? AccessController.doPrivileged( action ) : action.run();
	}
}
