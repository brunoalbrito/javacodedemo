package cn.java.demo.webmvc.bean.handler.byannotation;

import java.util.ArrayList;
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

import groovyjarjarantlr.collections.List;

/**
 * @author zhouzhian
 * 
 * 使用Firefox - HttpRequester模拟插件测试
 * 
 * 普通参数
 * json参数
 * xml参数
 */
@RequestMapping(
	path={"/req-xml-handler"},
	method={RequestMethod.GET,RequestMethod.POST}
)
public class ReqXmlHandler {
	
	/**
	 * 	
	 * 
	 * /post-jackson-xml0
	 */
	@RequestMapping(path={"/post-jackson-xml0"},method={RequestMethod.POST},
			consumes = {MediaType.APPLICATION_XML_VALUE}, // 接受请求头Content-Type为集合中的值的请求
			produces = {MediaType.APPLICATION_XML_VALUE}  // 接受请求头Accept为集合中的值的请求
			)
	public ModelAndView postJacksonXml0(
			@RequestBody(required=false) @Validated() Map map, // 
			BindingResult result // Errors result, 必须紧跟在@ModelAttribute后面 - 存放校验结果
	) throws Exception{
		
		/*
		 	请求方式：
		 		POST
		 	请求头：
		 		Content-Type ： application/xml
		 		Accept ： application/xml
		 	请求地址：
		 		http://localhost:8080/springwebmvc/req-xml-handler/post-jackson-xml0
		 	请求数据：
		 		<Map><message>test message(postJacksonXml0)</message><status>200</status></Map>
		 */
		System.out.println("------postJacksonXml0----------");
		if(result.hasErrors()){
			return null;
		}
		System.out.println(map);
		return null;
	}
	
	/**
	 * /post-jackson-xml1
	 */
	@RequestMapping(path={"/post-jackson-xml1"},method={RequestMethod.POST},
			consumes = {MediaType.APPLICATION_XML_VALUE}, // 接受请求头Content-Type为集合中的值的请求
			produces = {MediaType.APPLICATION_XML_VALUE}  // 接受请求头Accept为集合中的值的请求
			)
	public @ResponseBody Map postJacksonXml1(
			@RequestBody(required=false) @Validated() Map map, // 
			BindingResult result // Errors result, 必须紧跟在@ModelAttribute后面 - 存放校验结果
			) throws Exception{
		
		/*
		 	请求方式：
		 		POST
		 	请求头：
		 		Content-Type ： application/xml
		 		Accept ： application/xml
		 	请求地址：
		 		http://localhost:8080/springwebmvc/req-xml-handler/post-jackson-xml1
		 	请求数据：
		 		<Map>
				<message>test message(postJacksonXml0)</message>
				<status>200</status>
				<data>
				<str>strValue</str>
				<list>listItem0</list>
				<list>listItem1</list>
				<map>
				<mapKey1>mapKey1Value</mapKey1>
				<mapKey0>mapKey0Value</mapKey0>
				</map>
				</data>
				</Map>
		 */
		System.out.println("------postJacksonXml1----------");
		if(result.hasErrors()){
			return null;
		}
		if(map==null){
			map = new HashMap();
			map.put("status", "200");
			map.put("message", "test message(postJacksonXml1) server create");
			Map data = new HashMap();
			{
				data.put("str", "strValue");
			}
			{
				ArrayList list = new ArrayList(); 
				list.add("listItem0"); 
				list.add("listItem1");
				data.put("list", list);
			}
			{
				Map mapData = new HashMap(); 
				mapData.put("mapKey0", "mapKey0Value");
				mapData.put("mapKey1", "mapKey1Value");
				data.put("map", mapData);
			}
			map.put("data", data);
		}
		System.out.println(map); // 
		return map;
	}
	
	/**
	 * 
	 * @param binder
	 */
	@InitBinder(value={"map"})
	public void initBinder(DataBinder binder) {
		if(binder instanceof ExtendedServletRequestDataBinder){
			
		}
		if("map".equals(binder.getObjectName())){
		}
	}

}
