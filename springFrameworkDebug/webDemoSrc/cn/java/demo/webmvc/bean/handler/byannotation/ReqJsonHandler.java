package cn.java.demo.webmvc.bean.handler.byannotation;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ExtendedServletRequestDataBinder;

import com.alibaba.fastjson.JSONObject;

import cn.java.demo.webmvc.http.converter.fastjson.FastJsonObject;

/**
 * @author zhouzhian
 * 
 * 使用Firefox - HttpRequester模拟插件测试
 * 
 */
@RequestMapping(path = { "/req-json-handler" }, method = { RequestMethod.GET, RequestMethod.POST })
public class ReqJsonHandler {

	
	/**
	 * /post-fast-json
	 */
	@RequestMapping(
			path = { "/post-fast-json" }, method = { RequestMethod.POST },
			consumes = {"application/fastjson+json;charset=UTF-8"}, // 接受请求头Content-Type为集合中的值的请求
			produces = {"application/fastjson+json;charset=UTF-8"}  // 接受请求头Accept为集合中的值的请求
	)
	public ModelAndView postFastJson(@RequestBody(required = true) @Validated() FastJsonObject fastJsonObject, //
			BindingResult result // Errors result, 必须紧跟在@ModelAttribute后面 - 存放校验结果
	) throws Exception {
		/*
		 	请求方式：
		 		POST
	 		请求头：
		 		Content-Type ： application/fastjson+json;charset=UTF-8
		 		Accept ： application/fastjson+json;charset=UTF-8
		 	请求地址：
		 		http://localhost:8080/springwebmvc/req-json-handler/post-fast-json
		 	请求数据：
		 		/*codec_is_fastjson* /{"message":"test message(postFastJson) client","status":"200"}
		 */
		System.out.println("------postFastJson----------");
		if (result.hasErrors()) {
			return null;
		}
		System.out.println(fastJsonObject);
		String status = fastJsonObject.getString("status");
		String message = fastJsonObject.getString("message");
		JSONObject data = fastJsonObject.getJSONObject("data");
		return null;
	}

	/**
	 * http://localhost:8080/springwebmvc/req-json-handler/post-jackson-json
	 * /post-jackson-json
	 */
	@RequestMapping(path = { "/post-jackson-json0" }, method = { RequestMethod.POST},
			consumes = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_JSON_UTF8_VALUE}, // 接受请求头Content-Type为集合中的值的请求
			produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_JSON_UTF8_VALUE}  // 接受请求头Accept为集合中的值的请求
			)
	public ModelAndView postJacksonJson0(@RequestBody(required = false) @Validated() Map map, //
			BindingResult result // Errors result, 必须紧跟在@ModelAttribute后面 - 存放校验结果
	) throws Exception {
		/*
		 	请求方式：
		 		POST
		 	请求头：
		 		Content-Type ： application/json;charset=UTF-8
		 		Accept ： application/json;charset=UTF-8
		 	请求地址：
		 		http://localhost:8080/springwebmvc/req-json-handler/post-jackson-json0
		 	请求数据：
		 		{"message":"test message(postJacksonJson0) client","status":"200"}
		 */
		System.out.println("------postJacksonJson0----------");
		if (result.hasErrors()) {
			return null;
		}
		System.out.println(map);
		return null;
	}

	/**
	 * /post-jackson-json2
	 */
	@RequestMapping(path = { "/post-jackson-json1" }, method = { RequestMethod.POST,RequestMethod.GET },
			consumes = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_JSON_UTF8_VALUE}, // 接受请求头Content-Type为集合中的值的请求
			produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_JSON_UTF8_VALUE}  // 接受请求头Accept为集合中的值的请求
			)
	public @ResponseBody Map postJacksonJson1(@RequestBody(required = false) @Validated() Map map, //
			BindingResult result // Errors result, 必须紧跟在@ModelAttribute后面 - 存放校验结果
	) throws Exception {
		/*
		 	请求方式：
		 		POST、GET
		 	请求头：
		 		Content-Type ： application/json;charset=UTF-8
		 		Accept ： application/json;charset=UTF-8
		 	请求地址：
		 		http://localhost:8080/springwebmvc/req-json-handler/post-jackson-json1
		 	请求数据：
		 		{"message":"test message(postJacksonJson1) client","status":"200"}
		 */
		System.out.println("------postJacksonJson1----------");
		if (result.hasErrors()) {
			return null;
		}
		if(map==null){
			map = new HashMap();
			map.put("status", "200");
			map.put("message", "test message(postJacksonJson1) server create");
		}
		// Jackson解析list类型的xml数据的时候有个bug，只会获取最后一项
		System.out.println(map);
		return map;
	}

	/**
	 * 
	 * @param binder
	 */
	@InitBinder(value = { "fastJsonObject","map" })
	public void initBinder(DataBinder binder) {
		if (binder instanceof ExtendedServletRequestDataBinder) {

		}
		if ("fastJsonObject".equals(binder.getObjectName())) {
			if (binder.getTarget() instanceof FastJsonObject) {
				FastJsonObject fastJsonObject = (FastJsonObject) binder.getTarget();
			}
		} else if ("map".equals(binder.getObjectName())) {
			
		}
	}

}
