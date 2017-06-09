package cn.java.demo.webmvc.bean.handler.byannotation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.java.demo.webmvc.form.RestConditionForm;
import cn.java.demo.webmvc.form.RestForm;
import cn.java.demo.webmvc.validator.RestFormValidator;

/**
 * @author zhouzhian
 *
 */
@RequestMapping(
	path={"/rest-handler"},
	method={RequestMethod.GET,RequestMethod.POST}
)
public class RestHandler {
	
	private static final String REST_FROM_PUT = "restFormPut"; // 添加
	private static final String REST_FROM_POST = "restFormPost";
	
	@InitBinder(value={REST_FROM_PUT,REST_FROM_POST})
	public void initBinder(DataBinder binder) {
		RestFormValidator restFormValidator = new RestFormValidator();
		System.out.println(binder.getObjectName());
		if(REST_FROM_PUT.equals(binder.getObjectName())){ // 是添加操作
			restFormValidator.setRequestMethodPut(true);
		}
		else if(REST_FROM_POST.equals(binder.getObjectName())){
			restFormValidator.setRequestMethodPut(false);
		}
		binder.setValidator(restFormValidator); // 添加校验器
	}
	
	private void debugPrint(HttpServletRequest request){
		String bestMatchingPattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
		String pathWithinMapping = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
		System.out.println("bestMatchingPattern : " + bestMatchingPattern); // 内部路由路径  “/valid-handler/*”
		System.out.println("pathWithinMapping : " + pathWithinMapping); // 用户访问路径  “/valid-handler/login”
	}
	
	/**
	 * 添加
	 */
	@RequestMapping(path={"/put"},method={RequestMethod.PUT})
	public ModelAndView put(
			@ModelAttribute(name=REST_FROM_PUT) @Validated() RestForm restForm,
			BindingResult result, // Errors result, 必须紧跟在@ModelAttribute后面 - 存放校验结果
			HttpServletRequest request,HttpServletResponse response) throws Exception{ 
		debugPrint(request);
		if (result.hasErrors()) {
			
		}
		return null;
	}
	
	/**
	 * 读取
	 * /rest-handler/list
	 * /rest-handler/list/
	 * /rest-handler/list/1
	 * /rest-handler/list/1/2
	 */
	@RequestMapping(path={"/list","/list/{length:[0-9]*}","/list/{length:[0-9]*}/{offset:[0-9]*}"},method={RequestMethod.GET})
	public ModelAndView list(
			@PathVariable(name="length",required=false)Integer length,
			@PathVariable(name="offset",required=false)Integer offset,
			RestConditionForm restConditionForm,
			HttpServletRequest request,HttpServletResponse response	) throws Exception{
		debugPrint(request);
		if ((offset == null)  || (offset < 0)) { // 没有指定起始记录
			offset = 0;
		}
		if ((length == null) || (length < 0) ) { // 没有指定长度
			length = 10;
		}
		if (length > 300) { // 最多能查询300条
			length = 300;
		}
		
		System.out.println("length = " + length);
		System.out.println("offset = " + offset);
		System.out.println("restConditionForm = " + restConditionForm);
		return null;
	}
	
	/**
	 * 读取
	 * /rest-handler/get/1
	 */
	@RequestMapping(path={"/get/{itemId:[0-9]*}"},method={RequestMethod.GET})
	public ModelAndView get(@PathVariable(name="itemId",required=false)Integer itemId,
			RestConditionForm restConditionForm,
			HttpServletRequest request,HttpServletResponse response) throws Exception{
		debugPrint(request);
		System.out.println("itemId = " + itemId);
		System.out.println("restConditionForm = " + restConditionForm);
		return null;
	}
	
	/**
	 * 修改
	 */
	@RequestMapping(path={"/post"},method={RequestMethod.POST})
	public ModelAndView post(@ModelAttribute(name=REST_FROM_POST) @Validated()RestForm restForm,
			BindingResult result, // Errors result, 必须紧跟在@ModelAttribute后面 - 存放校验结果
			HttpServletRequest request,HttpServletResponse response) throws Exception{ 
		debugPrint(request);
		if (result.hasErrors()) {
			
		}
		return null;
	}
	
	/**
	 * 删除
	 */
	@RequestMapping(path={"/{itemId:[0-9]*}"},method={RequestMethod.DELETE})
	public ModelAndView delete(@PathVariable(name="itemId",required=true)Integer itemId,HttpServletRequest request,HttpServletResponse response) throws Exception{ 
		debugPrint(request);
		return null;
	}

}
