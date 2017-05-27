package cn.java.demo.webmvc.bean.handler.byannotation;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

@RequestMapping(name="Handler0",
	path={"/hadler0","/hadler0_alias"},
	method={RequestMethod.GET,RequestMethod.POST}
)
@CrossOrigin(origins={"http://localhost","http://www.domain1.com"},
	allowedHeaders={},
	exposedHeaders={},
	methods={RequestMethod.GET,RequestMethod.POST},
	allowCredentials="false",
	maxAge=-1
) // 关于跨域的配置
public class Handler0 {
	
	@InitBinder
	public void init0(){
		
	}
	
	@InitBinder
	public void init1(){
		
	}
	
	@ModelAttribute
	public void model0(HttpServletRequest request,HttpServletResponse response){
		
	}
	
	@ModelAttribute
	public void model1(HttpServletRequest request,HttpServletResponse response){
		
	}
	
	/**
	 * 由于在类上面有使用@RequestMapping注解，所以在这个的path路径不要从根路径开始写，会自动合并类上的@RequestMapping注解
	 */
	@RequestMapping(name="H#method0", // name没有提供会自动生成，生成规则是："类名中的每个大写字符"+"#"+"方法名"
			path={"/method0","/method0_alias"},
			method={RequestMethod.GET,RequestMethod.POST},
			params={"param0=value0","param0!=value0"},
			headers={"content-type=text/*"},
			consumes={"text/plain","application/*"},
			produces={"text/plain", "application/*"}
	)
	@CrossOrigin(origins={"http://www.domain0.com","http://www.domain1.com"},
		allowedHeaders={},
		exposedHeaders={},
		methods={RequestMethod.GET,RequestMethod.POST},
		allowCredentials="false",
		maxAge=-1
	) // 关于跨域的配置
	@ResponseStatus(code=HttpStatus.OK,reason=" ok ...")
	public ModelAndView method0(HttpServletRequest request,HttpServletResponse response,WebRequest webRequest) throws Exception{ // 参数是可以有多种类型的，只要“参数解析器”支持
		{
			ModelAndView modelAndView = new ModelAndView();
			// 设置模板
			{
				modelAndView.setViewName("method0.vm");
				// modelAndView.setViewName("redirect:method0.vm");
			}
			// 设置模板值
			Map<String, Object> modelMap = new HashMap();
			modelMap.put("key0", "value0");
			modelAndView.addAllObjects(modelMap);
			// 返回
			return modelAndView;
		}
	}
	
	@RequestMapping(path={"/method1"})
	public void method1(HttpServletRequest request,HttpServletResponse response,WebRequest webRequest) throws Exception{
		System.out.println("hello , i am " + this.getClass().getSimpleName());
		return;
	}
}
