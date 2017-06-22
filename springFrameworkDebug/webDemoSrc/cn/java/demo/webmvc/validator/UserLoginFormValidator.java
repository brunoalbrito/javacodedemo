package cn.java.demo.webmvc.validator;

import java.util.List;

import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.ConfigurablePropertyAccessor;
import org.springframework.util.StringUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.DefaultMessageCodesResolver;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.MessageCodesResolver;
import org.springframework.validation.ObjectError;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.support.RequestContext;

import cn.java.demo.webmvc.form.UserLoginForm;
import cn.java.demo.webmvc.validator.utils.ValidatorFactory;

public class UserLoginFormValidator implements Validator {

	private static final int MINIMUM_PASSWORD_LENGTH = 6;

	public boolean supports(Class clazz) {
		return UserLoginForm.class.isAssignableFrom(clazz);
	}

	public void validate(Object target, Errors errors) {
		
		ValidationUtils.invokeValidator(ValidatorFactory.getMineValidator(),target,errors);  // 使用自定义校验器校验
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "validator.emptyOrWhitespace"); // 必须通过用户名
		{
			/*
			 	在“错误消息模板”里面“键名”可以有三种写法
			 		emptyOrWhitespace.userLoginForm.username="用户名必须填写"
			 		emptyOrWhitespace.username="用户名必须填写"
			 		emptyOrWhitespace="必须填写"
			 */
		}
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "validator.emptyOrWhitespace"); // 必须通过密码
		UserLoginForm userLoginForm = (UserLoginForm) target;
		if((userLoginForm.getPassword() != null)&&(userLoginForm.getPassword().trim().length() < MINIMUM_PASSWORD_LENGTH)){ // 密码长度
			errors.rejectValue("password", "validator.minLength",	new Object[]{Integer.valueOf(MINIMUM_PASSWORD_LENGTH)},"密码至少需要[" + MINIMUM_PASSWORD_LENGTH + "]个字符"); 
			/*
			 	在“错误消息模板”里面“键名”可以有三种写法
			 		minLength.userLoginForm.password="密码至少需要{0}个字符"
			 		minLength.password="密码至少需要{0}个字符"
			 		minLength="密码至少需要{0}个字符"
			 */
		}
		
		// 代码展开
		{
			if(target instanceof UserLoginForm){
				
			}

			{
				UserLoginFormValidator.debugBeanPropertyBindingResult(errors);
			}
		}
		
	}
	
	/**
	 * 打印出对象
	 * @param errors
	 */
	public static void debugBeanPropertyBindingResult(Errors errors){
		if(errors instanceof BeanPropertyBindingResult){
			BeanPropertyBindingResult beanPropertyBindingResult = (BeanPropertyBindingResult)errors;
			
			boolean runMineCode = false;
			
			if(runMineCode){
				System.out.println("beanPropertyBindingResult.getTarget() = " + beanPropertyBindingResult.getTarget());
				System.out.println("beanPropertyBindingResult.getObjectName() = " + beanPropertyBindingResult.getObjectName());	 // == "userLoginForm" 
			}
			
			// 校验username是否为空
			if(runMineCode){
				Object value = errors.getFieldValue("username");
				if (value == null ||!StringUtils.hasText(value.toString())) {
					errors.rejectValue("username", "emptyOrWhitespace", null, null);
				}
			}
			
			// 访问自定义对象的属性
			if(runMineCode){
				ConfigurablePropertyAccessor configurablePropertyAccessor = beanPropertyBindingResult.getPropertyAccessor();
				if(configurablePropertyAccessor instanceof BeanWrapperImpl){
					configurablePropertyAccessor.setPropertyValue("username", "username1");
					configurablePropertyAccessor.getPropertyValue("username");
				}
			}
			
			// 错误消息模板
			if(runMineCode){
				MessageCodesResolver messageCodesResolver = beanPropertyBindingResult.getMessageCodesResolver();
				if(messageCodesResolver instanceof DefaultMessageCodesResolver){
					String[] codes = messageCodesResolver.resolveMessageCodes("minLength","userLoginForm", "password", String.class);
					/*
					 	code是自动生成的
					 	{
					 		"errorCode.objectName.field" -----> minLength.userLoginForm.password
					 		"errorCode.field" -----> minLength.password
					 		"errorCode" -----> minLength
					 	}
					 */
				}
			}
			
			// 打印错误消息
			if(runMineCode){
				List<ObjectError> allErrors = errors.getAllErrors();
				List<ObjectError> globalErrors = errors.getGlobalErrors();
				List<FieldError> fieldErrors = errors.getFieldErrors();
				List<FieldError> fieldErrorsOfUsername = errors.getFieldErrors("username");
				for (ObjectError objectError : allErrors) {
					objectError.getObjectName();// == "userLoginForm"
					String[] codes = objectError.getCodes(); // 消息编码
					objectError.getArguments(); // 模板参数
					objectError.getDefaultMessage(); // 默认消息模板
				}
				for (FieldError fieldError : fieldErrors) {
					System.out.println("fieldError.getObjectName() = " + fieldError.getObjectName()); // == "userLoginForm"  
					fieldError.getField(); // 字段名 === "username"
					fieldError.getRejectedValue();  // 用户传递的值
					String[] codes = fieldError.getCodes(); // 消息编码
					fieldError.getArguments(); // 模板参数
					fieldError.getDefaultMessage(); // 默认消息模板
					
					// 消息转换
					{
						RequestContext requestContext = null;
						String errorMessages = requestContext.getMessage(fieldError, false);
						// org.springframework.web.context.support.XmlWebApplicationContext.getMessage(fieldError, locale)
						System.out.println("errorMessages = " + errorMessages);
					}
				}
			}
		}
		
		/*
		 	校验结束后，mavContainer会增加两个元素
		 	mavContainer = {
		 		...
				"userLoginForm" : cn.java.demo.webmvc.form.UserLoginForm对象
				"org.springframework.validation.BindingResult.userLoginForm" : org.springframework.validation.BeanPropertyBindingResult对象
		 		...
			}
		 */
	}
}