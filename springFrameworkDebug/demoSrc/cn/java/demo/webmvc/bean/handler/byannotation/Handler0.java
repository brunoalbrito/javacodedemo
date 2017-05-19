package cn.java.demo.webmvc.bean.handler.byannotation;

import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.HandlerMapping;

@RequestMapping(name="Handler1",
	path={"/hadler1","/hadler1_alias"},
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
	public void model0(){
		
	}
	
	@ModelAttribute
	public void model1(){
		
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
	public void method0(){
		HttpServletRequest request = null;
		{
			// 匹配的表达式
			String bestPattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE); 
		}
		{
			// 地址  param0=value0&param1=value1
			Map<String, String> decodedUriVariables = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE); 
		}
		{
			Map<String, MultiValueMap<String, String>> matrixVars =  (Map<String, MultiValueMap<String, String>>) request.getAttribute(HandlerMapping.MATRIX_VARIABLES_ATTRIBUTE); 
			/*
			 	当访问地址：http://www.domain.com/foovalue/key00;key000=value000;key001=value001
			 	decodedUriVariables = {
			 		fookey_patt : foovalue
			 		key0_patt : key00
			 	}
			 	matrixVars = {
			 		key0_patt : {
			 			key000 : value000,
			 			key001 : value001
			 		},
			 	}
			 	-----
			 	当访问地址：http://www.domain.com/foovalue/;key000=value000;key001=value001
			 	decodedUriVariables = {
			 		fookey_patt : foovalue
			 		key0_patt : 没值
			 	}
			 	matrixVars = {
			 		key0_patt : {
			 			key000 : value000,
			 			key001 : value001
			 		},
			 	}
			 */
		}
		{
			Set<MediaType> mediaTypes = (Set<MediaType>) request.getAttribute(HandlerMapping.PRODUCIBLE_MEDIA_TYPES_ATTRIBUTE);
		}
	}
}
