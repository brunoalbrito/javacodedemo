package cn.java.debug.webmvc.oldhandler;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import cn.java.demo.webmvc.validator.ValidatorRegForm;

/**
 * 支持的请求地址  
 * @author zhouzhian
 */
@Deprecated
@RequestMapping(path={"/","/foo0-with-annotation"},
	method={RequestMethod.GET,RequestMethod.POST},
	params={"param0=param0_value","param1=param1_value"}
)
public class Foo0WithAnnotationHandler{
	
	
	/**
	 * 没有要传递到视图的数据
	 */
	@RequestMapping(path={"method0","method0-alias0"})
	public void method0(){
		/*
		 	http://www.domain/method0
		 	http://www.domain/method0-alias0
		 	http://www.domain/foo0-with-annotation/method0
		 	http://www.domain/foo0-with-annotation/method0-alias0
		 */
	}
	
	/**
	 * 有要传递到视图的数据通过ModelAndView指定
	 * @return
	 */
	@RequestMapping(path={"method1","method1-alias1"})
	public ModelAndView method1(){
		return null;
	}
	
	/**
	 * @param param6
	 * @param param7
	 * @return
	 */
	@RequestMapping(path={"method2","method2-alias1"})
	public ModelAndView method2(
			// param0 配置了获取方式，默认值，校验器
			@RequestParam(name="param0",required=true,defaultValue="defaultValue0") @Value(value="defaultValue") @Validated(value={ValidatorRegForm.class})Object param0,
			// param1 配置了获取方式，默认值，校验器
	 		@RequestHeader(name="param0",required=true,defaultValue="defaultValue0") @Validated(value={ValidatorRegForm.class})Object param1,
	 		@RequestBody() Object param2,
	 		@CookieValue(name="param0",required=true,defaultValue="defaultValue0") Object param3,
	 		@PathVariable() Object param4,
	 		@ModelAttribute() Object param5
	 	){
		/*
		 	一个参数上可以有多个注解，但如下注解是互斥的（即不能在作用在同一个参数上）：
				@RequestParam(name="param0",required=true,defaultValue="defaultValue0")Object param0,
		 		@RequestHeader(name="param0",required=true,defaultValue="defaultValue0") Object param1,
		 		@RequestBody() Object param2,
		 		@CookieValue(name="param0",required=true,defaultValue="defaultValue0") Object param3,
		 		@PathVariable() Object param4,
		 		@ModelAttribute() Object param5
		 */
 		
		return null;
	}

}
