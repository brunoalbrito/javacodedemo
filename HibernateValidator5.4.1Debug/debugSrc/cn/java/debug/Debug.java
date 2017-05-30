package cn.java.debug;

import java.util.Iterator;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidationProviderResolver;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.Validation.DefaultValidationProviderResolver;
import javax.validation.Validation.GetValidationProviderListAction;
import javax.validation.spi.ValidationProvider;

import org.hibernate.validator.internal.engine.ConfigurationImpl;

import cn.java.demo.entity.User;

//http://www.ibm.com/developerworks/cn/java/j-lo-beanvalid/
//This is the reference implementation of JSR-349 - Bean Validation 1.1. 
public class Debug {
	
	{
		/*
		 	
		 	实现的地方
		 		hibernate-validator-5.4.1.Final.jar!!META-INF/services/javax.validation.spi.ValidationProvider
		 		{
		 			org.hibernate.validator.HibernateValidator
		 		}
		 	Validation.buildDefaultValidatorFactory()
		 	{
		 		new  javax.validation.Validation.GenericBootstrapImpl().configure(){
		 			ValidationProviderResolver resolver = this.resolver != null ? this.resolver : getDefaultValidationProviderResolver();{
			 			if(defaultResolver == null)
			                defaultResolver = new javax.validation.Validation.DefaultValidationProviderResolver();
			            return defaultResolver;
		 			}
		 			// resolver === javax.validation.Validation.DefaultValidationProviderResolver
		 			validationProviders = resolver.getValidationProviders();
		 			{
		 				return javax.validation.Validation.GetValidationProviderListAction.getValidationProviderList();
		 				{
		 					 javax.validation.Validation.GetValidationProviderListAction.run();
				 			 {
				 			 	if(cachedContextClassLoaderProviderList != null)
                					return cachedContextClassLoaderProviderList;
				 			 	ClassLoader classloader = Thread.currentThread().getContextClassLoader();
					            List validationProviderList = loadProviders(classloader);
					            {
					            	ServiceLoader loader = ServiceLoader.load(javax.validation.spi.ValidationProvider, classloader); // 发现服务
						            Iterator providerIterator = loader.iterator();
						            List validationProviderList = new ArrayList();
						            while(providerIterator.hasNext()) 
					                try
					                {
					                    validationProviderList.add(providerIterator.next());
					                }
					                catch(ServiceConfigurationError e) { }
						            return validationProviderList;
					            }
					            
					            if(validationProviderList.isEmpty())
					            {
					                classloader = javax.validation.Validation$DefaultValidationProviderResolver.getClassLoader();
					                validationProviderList = loadProviders(classloader);
					                {
					                	ServiceLoader loader = ServiceLoader.load(javax.validation.spi.ValidationProvider, classloader); // 发现服务
							            Iterator providerIterator = loader.iterator();
							            List validationProviderList = new ArrayList();
							            while(providerIterator.hasNext()) 
						                try
						                {
						                    validationProviderList.add(providerIterator.next());
						                }
						                catch(ServiceConfigurationError e) { }
							            return validationProviderList;
					                }
					            }
					            return validationProviderList;
				 			 }
		 				}
		 			}
		 			
		 			// resolver === javax.validation.Validation.DefaultValidationProviderResolver
		 			config = ((ValidationProvider)resolver.getValidationProviders().get(0)).createGenericConfiguration(this);
		 			{
			 			// this ===  javax.validation.Validation.GenericBootstrapImpl
		 				return org.hibernate.validator.HibernateValidator.createGenericConfiguration(this);
		 				{
		 					// state === javax.validation.Validation.GenericBootstrapImpl
		 					return new org.hibernate.validator.internal.engine.ConfigurationImpl( state );
		 				}
		 			}
		 			return config;
		 		}.buildValidatorFactory();{
		 			
		 		}
		 	
		 	}
		 	发现 ValidationProvider
		 	
			 	ValidatorFactory factory = javax.validation.Validation.buildDefaultValidatorFactory();
			 	{
				 	List validationProviders = javax.validation.Validation.DefaultValidationProviderResolver.getValidationProviders();
				 	{
				 		return GetValidationProviderListAction.getValidationProviderList();
				 		{
				 			
				 		}
				 	}
				 	// (ValidationProvider)validationProviders.get(0) === org.hibernate.validator.HibernateValidator
					// config === org.hibernate.validator.internal.engine.ConfigurationImpl
		 			Configuration config = ((ValidationProvider)validationProviders.get(0)).createGenericConfiguration(javax.validation.Validation.GenericBootstrapImpl);
					ValidatorFactory factory = config.buildValidatorFactory(); 
					// factory === org.hibernate.validator.internal.engine.ValidatorFactoryImpl
					return factory;
				}
				// factory === org.hibernate.validator.internal.engine.ValidatorFactoryImpl
				validator = factory.getValidator(); // validator === org.hibernate.validator.internal.engine.ValidatorImpl
		 
		 	-------------------------
		 	内建校验器
		 	org.hibernate.validator.internal.metadata.core.ConstraintHelper.ConstraintHelper()

		 	
		 */
	}
	public static void main(String[] args) {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();

		//要校验的对象
		User car = new User(null, "DD-AB-123", 1);

		//进行校验
		Set<ConstraintViolation<User>> constraintViolations = validator
				.validate(car);

		//错误的数量
		System.out.println("错误的数量是： " + constraintViolations.size());

		//输出错误信息
		System.out.println("错误消息：");
		Iterator<ConstraintViolation<User>> iterator = constraintViolations
				.iterator();
		while (iterator.hasNext()) {
			ConstraintViolation constraintViolation = iterator.next();
			System.out.println("\t" + constraintViolation.getMessage());

		}
		//		System.out.println(constraintViolations.iterator().next().getMessage());
	}
}
