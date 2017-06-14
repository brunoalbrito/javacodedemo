package cn.java.demo.webmvc.validator.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

public class MineValidationUtils extends ValidationUtils {
	
	/**
	 * 只能中文
	 * 
	 * <p> 
	 * http://blog.csdn.net/yigelangmandeshiren/article/details/6414563 <br />
	 * Unicode表 ：http://blog.csdn.net/hherima/article/details/9045861	
	 * </p>
	 */
	public static void rejectIfNotOnlyChinese(Errors errors, String field, String errorCode) {
		rejectIfNotMatchPattern("^[\u4e00-\u9fa5]$",errors,field,errorCode,null,"只能中文");
	}
	
	/**
	 * 域名
	 */
	public static void rejectIfNotDomain(Errors errors, String field, String errorCode) {
		rejectIfNotMatchPattern("^([a-z0-9d\u4E00-\u9FA5]([a-z0-9\u4E00-\u9FA5-]*))?[a-z0-9\u4E00-\u9FA5]((\\.[a-z0-9\u4E00-\u9FA5]+)?\\.([a-z0-9\u4E00-\u9FA5]+))$",errors,field,errorCode,null,"不是有效域名");
	}
	
	/**
	 * 带小数点的钱
	 */
	public static void rejectIfNotMoneyDecimal(Errors errors, String field, String errorCode) {
		rejectIfNotDecimal(2,errors,field,errorCode);
	}
	
	/**
	 * 小数
	 */
	public static void rejectIfNotDecimal(Integer scale,Errors errors, String field, String errorCode) {
		rejectIfNotMatchPattern("^-?(?:0-9+)?(?:\\.0-9{0,"+scale+"})?$",errors,field,errorCode,null,"不是有效域名");
	}
	
	/**
	 * 中国身份证
	 */
	public static void rejectIfNotChineseIdCard(Errors errors, String field, String errorCode) {
		Object value = errors.getFieldValue(field);
		if (value == null || !StringUtils.hasLength(value.toString())) {
			errors.rejectValue(field, errorCode, null,"不是有效的中国身份证");
		}
		if(!IdCardValidator.isValidatedAllIdcard(value.toString())){
			errors.rejectValue(field, errorCode, null,"不是有效的中国身份证");
		}
	}
	
	/**
	 * 只能英文小写
	 */
	public static void rejectIfNotOnlyEnglishLowerCase(Errors errors, String field, String errorCode) {
		rejectIfNotMatchPattern("^[a-z]$",errors,field,errorCode,null,"只能英文小写");
	}
	
	/**
	 * 只能英文大写
	 */
	public static void rejectIfNotOnlyEnglishUpperCase(Errors errors, String field, String errorCode) {
		rejectIfNotMatchPattern("^[A-Z]$",errors,field,errorCode,null,"只能英文大写");
	}
	
	/**
	 * 只能数字
	 */
	public static void rejectIfNotOnlyDigit(Errors errors, String field, String errorCode) {
		rejectIfNotMatchPattern("^[0-9]$",errors,field,errorCode,null,"只能数字");
	}
	
	/**
	 * 只能电话
	 */
	public static void rejectIfNotTelephone(Errors errors, String field, String errorCode) {
		rejectIfNotMatchPattern("^((\\+[0-9]{2,4})?\\(?[0-9]+\\)?-?)?[0-9]{7,8}$",errors,field,errorCode,null,"只能电话");
	}
	
	/**
	 * 只能电话或者手机号
	 */
	public static void rejectIfNotTelephoneOrMobile(Errors errors, String field, String errorCode) {
		rejectIfNotMatchPattern("^(13[4,5,6,7,8,9]|15[0,8,9,1,7]|188|187)\\d{8}|((\\+[0-9]{2,4})?\\(?[0-9]+\\)?-?)?[0-9]{7,8}$",errors,field,errorCode,null,"只能电话或者手机号");
	}
	
	
	/**
	 * 只能手机号
	 */
	public static void rejectIfNotMobile(Errors errors, String field, String errorCode) {
		rejectIfNotMatchPattern("^(13[4,5,6,7,8,9]|15[0,8,9,1,7]|188|187)\\d{8}$",errors,field,errorCode,null,"只能手机号");
	}
	
	/**
	 * 只能邮箱
	 */
	public static void rejectIfNotEmail(Errors errors, String field, String errorCode) {
		rejectIfNotMatchPattern("^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$",errors,field,errorCode,null,"只能邮箱");
	}
	
	/**
	 * 必须“大于等于”最小长度
	 */
	public static void rejectIfNotEgtMinLength(Integer minLength,Errors errors, String field, String errorCode) {
		Object value = errors.getFieldValue(field);
		if (value == null || !StringUtils.hasLength(value.toString())) {
			errors.rejectValue(field, errorCode, new Object[]{Integer.valueOf(minLength)},"至少要[" + minLength + "]个字符");
		}
		if(value.toString().trim().length() < minLength){
			errors.rejectValue(field, errorCode, new Object[]{Integer.valueOf(minLength)},"至少要[" + minLength + "]个字符");
		}
	}
	
	/**
	 * 必须“小于等于”最大长度
	 */
	public static void rejectIfNotEltMaxLength(Integer maxLength,Errors errors, String field, String errorCode) {
		Object value = errors.getFieldValue(field);
		if (value == null || !StringUtils.hasLength(value.toString())) {
			errors.rejectValue(field, errorCode, new Object[]{Integer.valueOf(maxLength)},"最多只能[" + maxLength + "]个字符");
		}
		if(value.toString().trim().length() > maxLength){
			errors.rejectValue(field, errorCode, new Object[]{Integer.valueOf(maxLength)},"最多只能[" + maxLength + "]个字符");
		}
	}
	

	/**
	 * 不符合表达式
	 * @param regex 正则表达式
	 * @param errors
	 * @param field
	 * @param errorCode
	 * @param errorArgs
	 * @param defaultMessage
	 */
	public static void rejectIfNotMatchPattern(String regex,Errors errors, String field, String errorCode, Object[] errorArgs, String defaultMessage) {
		Object value = errors.getFieldValue(field);
		if (value == null || !StringUtils.hasLength(value.toString())) {
			errors.rejectValue(field, errorCode, errorArgs, defaultMessage);
		}
		Pattern pattern = Pattern.compile(regex); 
		Matcher matcher = pattern.matcher(value.toString());  
		if(!matcher.matches()){
			errors.rejectValue(field, errorCode, errorArgs, defaultMessage);
		}
	}
}
