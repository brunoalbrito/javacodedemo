package cn.java.demo.webmvc.bean.handler.byannotation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.HandlerMapping;

import cn.java.demo.webmvc.form.CurdHandlerForm;
import cn.java.demo.webmvc.form.RestForm;
import cn.java.demo.webmvc.validator.CurdHandlerFormValidator;

/**
 * @author zhouzhian
 *
 */
@RequestMapping(path = { "/crud-handler" })
public class CrudHandler {
	private static final String CURD_HANDLER_FROM_ADD = "crudHandlerFormAdd"; // 添加
	private static final String CURD_HANDLER_FROM_UPDATE = "crudHandlerFormEdit";
	
	@InitBinder(value={CURD_HANDLER_FROM_ADD,CURD_HANDLER_FROM_UPDATE})
	public void initBinder(DataBinder binder) {
		CurdHandlerFormValidator curdHandlerFormValidator = new CurdHandlerFormValidator();
		System.out.println(binder.getObjectName());
		if(CURD_HANDLER_FROM_ADD.equals(binder.getObjectName())){ // 是添加操作
			curdHandlerFormValidator.setDoAdd(true);
		}
		else if(CURD_HANDLER_FROM_UPDATE.equals(binder.getObjectName())){
			curdHandlerFormValidator.setDoAdd(false);
		}
		binder.setValidator(curdHandlerFormValidator); // 添加校验器
	}
	
	private void debugPrint(HttpServletRequest request){
		String bestMatchingPattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
		String pathWithinMapping = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
		System.out.println("bestMatchingPattern : " + bestMatchingPattern); // 内部路由路径  “/valid-handler/*”
		System.out.println("pathWithinMapping : " + pathWithinMapping); // 用户访问路径  “/valid-handler/login”
	}
	
	/**
	 * /crud-handler/insert
	 * @return
	 */
	@RequestMapping(path = { "/insert" }, method = {RequestMethod.GET,RequestMethod.POST})
	public String insert(
			@ModelAttribute(name=CURD_HANDLER_FROM_ADD) @Validated() CurdHandlerForm curdHandlerForm,
			BindingResult result, // Errors result, 必须紧跟在@ModelAttribute后面 - 存放校验结果
			HttpServletRequest request,HttpServletResponse response){
		debugPrint(request);
		if (result.hasErrors()) {
			
		}
		return null;
	}
	
	/**
	 * /crud-handler/delete
	 * @return
	 */
	@RequestMapping(path = { "/delete" }, method = {RequestMethod.GET,RequestMethod.POST})
	public String delete(HttpServletRequest request,HttpServletResponse response){
		debugPrint(request);
		return null;
	}
	
	/**
	 * /crud-handler/update
	 * @return
	 */
	@RequestMapping(path = { "/update" }, method = {RequestMethod.GET,RequestMethod.POST})
	public String update(
			@ModelAttribute(name=CURD_HANDLER_FROM_UPDATE) @Validated()CurdHandlerForm curdHandlerForm,
			BindingResult result, // Errors result, 必须紧跟在@ModelAttribute后面 - 存放校验结果
			HttpServletRequest request,HttpServletResponse response){
		debugPrint(request);
		if (result.hasErrors()) {
			
		}
		return null;
	}
	
	/**
	 * /crud-handler/get-one
	 * @return
	 */
	@RequestMapping(path = {"/get-one"}, method = {RequestMethod.GET,RequestMethod.POST})
	public String getOne(HttpServletRequest request,HttpServletResponse response){
		debugPrint(request);
		return null;
	}
	
	/**
	 * /crud-handler/get-list
	 * @return
	 */
	@RequestMapping(path = { "/get-list" }, method = {RequestMethod.GET,RequestMethod.POST})
	public String getList(HttpServletRequest request,HttpServletResponse response){
		debugPrint(request);
		return null;
	}
	
	/**
	 * /crud-handler/get-list-with-condition
	 * @return
	 */
	@RequestMapping(path = { "/get-list-with-condition" }, method = {RequestMethod.GET,RequestMethod.POST})
	public String getListWithCondition(HttpServletRequest request,HttpServletResponse response){
		debugPrint(request);
		return null;
	}
}
