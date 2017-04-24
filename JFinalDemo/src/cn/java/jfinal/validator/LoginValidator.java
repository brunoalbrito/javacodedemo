package cn.java.jfinal.validator;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

/**
 * Validator自身实现了Interceptor接口，所以它也是一个拦截器
 * 
 * 使用方式：
 * 		@Before(LoginValidator.class)  // 配置方式与拦截器一样 
 *
 */
public class LoginValidator extends Validator {
	protected void validate(Controller c) {
		/**
		 * 调用validateXxx(…)系列方法进行后端校验
		 */
		validateRequiredString("name", "nameMsg", "请输入用户名");
		validateRequiredString("pass", "passMsg", "请输入密码");
	}

	protected void handleError(Controller c) {
		// 调用c.keepPara(…)方法将提交的值再传 回页面以便保持原先输入的值
		c.keepPara("name");
		// 调用c.render(…)方法来返回相应的页面
		c.render("login.html");
	}
}