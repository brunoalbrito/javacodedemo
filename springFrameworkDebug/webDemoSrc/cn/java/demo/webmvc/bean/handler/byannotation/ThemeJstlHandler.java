package cn.java.demo.webmvc.bean.handler.byannotation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpMethod;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import cn.java.demo.webmvc.form.SpringformTagForm;
import cn.java.demo.webmvc.validator.SpringformTagFormValidator;

/**
 * @author zhouzhian
 *
 */
@RequestMapping(
	path={"/theme-jstl-handler"},
	method={RequestMethod.GET,RequestMethod.POST}
)
public class ThemeJstlHandler {
	
	/**
	 * /theme-jstl-handler/method0
	 */
	@RequestMapping(path={"/jstltag0"},method={RequestMethod.GET})
	public String jstltag0(HttpServletRequest request,HttpServletResponse response) throws Exception{ 
		return "theme-jstl-handler/jstltag0";
	}
	
	@RequestMapping(path={"/method1/{pathVar0}/{pathVar1}"},method={RequestMethod.GET})
	public String method1(
			@PathVariable(name="pathVar0") String pathVar0,
			@PathVariable(name="pathVar1") String pathVar1,
			@RequestParam(name="reqParam0") String reqParam0,
			@RequestParam(name="reqParam1") String reqParam1,
			HttpServletRequest request,HttpServletResponse response) throws Exception{ 
		return null;
	}
	
	/**
	 * 初始化绑定器
	 * @param binder
	 * @param httpMethod
	 */
	@InitBinder(value={"springformTagForm","springTagForm","objectName1"}) 
	public void initBinder(DataBinder binder,HttpMethod httpMethod) {
		if(RequestMethod.POST.name().equals(httpMethod.name())){ // post的提交方式才需要校验
			if("springformTagForm".equals(binder.getObjectName())){
				binder.addValidators(new SpringformTagFormValidator());
			}
			else if("springTagForm".equals(binder.getObjectName())){
				binder.addValidators(new SpringformTagFormValidator());
			}
		}
	}
	
	@RequestMapping(path={"/springtag-and-theme0"},method={RequestMethod.GET,RequestMethod.POST})
	public ModelAndView springtagAndTheme0(
			@ModelAttribute(name="springTagForm") @Validated() SpringformTagForm springformTagForm,
			BindingResult result,
			HttpMethod httpMethod,
			HttpServletRequest request,HttpServletResponse response) throws Exception{
		if(result.hasErrors() || (RequestMethod.GET.name() == httpMethod.name())){
			System.out.println(result.hasErrors());
			ModelAndView modelAndView = new ModelAndView();
			modelAndView.setViewName("theme-jstl-handler/springtag-and-theme0");
			ModelMap modelMap = new ModelMap();
			modelMap.put("springTagForm", springformTagForm);
			modelAndView.addAllObjects(modelMap);
			return modelAndView;
		}
		else{
			// 业务代码
			System.out.println(springformTagForm);
			return null;
		}
	}
	
	@RequestMapping(path={"/springformtag0"},method={RequestMethod.GET,RequestMethod.POST})
	public ModelAndView springformtag0(
			@ModelAttribute(name="springformTagForm") @Validated() SpringformTagForm springformTagForm,
			BindingResult result,
			HttpMethod httpMethod,
			HttpServletRequest request,HttpServletResponse response) throws Exception{
		if(result.hasErrors() || (RequestMethod.GET.name() == httpMethod.name())){
			ModelAndView modelAndView = new ModelAndView();
			modelAndView.setViewName("theme-jstl-handler/springformtag0");
			ModelMap modelMap = new ModelMap();
			modelMap.put("springformTagForm", springformTagForm);
			modelAndView.addAllObjects(modelMap);
			return modelAndView;
		}
		else{
			// 业务代码
			System.out.println(springformTagForm);
			return null;
		}
		
	}
	
	
}
