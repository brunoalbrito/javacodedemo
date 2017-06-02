package cn.java.demo.webmvc.bean.handler.byannotation;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.WebAsyncTask;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import org.springframework.web.servlet.view.InternalResourceView;

/**
 * @author zhouzhian
 *
 */
@RequestMapping(
	path={"/resp-handler"},
	method={RequestMethod.GET,RequestMethod.POST}
)
public class RespHandler {
	
	/**
	 * 有jsp模板，会直接使用
	 * 赋值数据、并进行渲染
	 * localhost:8080/springwebmvc/resp-handler/return-model-and-view0
	 */
	@RequestMapping(path={"/return-model-and-view0"},method={RequestMethod.GET})
	public ModelAndView returnModelAndView0(HttpServletRequest request,HttpServletResponse response) throws Exception{ 
		request.setAttribute("attr0","value0");
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("resp-handler/return-model-and-view0");
		ModelMap modelMap = new ModelMap();
		modelMap.put("attr1", "value1");
		modelAndView.addAllObjects(modelMap);
		return modelAndView;
	}
	
	/**
	 * 没有jsp模板，会使用freemark的模板
	 * 赋值数据、并进行渲染 - 
	 * localhost:8080/springwebmvc/resp-handler/return-model-and-view1
	 */
	@RequestMapping(path={"/return-model-and-view1"},method={RequestMethod.GET})
	public ModelAndView returnModelAndView1(HttpServletRequest request,HttpServletResponse response) throws Exception{ 
		request.setAttribute("attr0","value0");
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("resp-handler/return-model-and-view1");
		ModelMap modelMap = new ModelMap();
		modelMap.put("attr1", "value1");
		modelAndView.addAllObjects(modelMap);
		/*
			模板中数据的访问是通过：freemarker.ext.servlet.AllHttpScopesHashModel
				访问顺序是：
					用户赋值的 -- > request中的attributes--- > Session中的attributes --- > Application中的attributes
				其中还注入的对象：
					JspTaglibs、Application(访问的是attributes)、Session(访问的是attributes)、Request(访问的是attributes)、RequestParameters(访问的是attributes)

		 */
		return modelAndView;
	}
	
	/**
	 * /resp-handler/return-model
	 */
	@RequestMapping(path={"/return-model"},method={RequestMethod.GET})
	public Model returnModel(HttpServletRequest request,HttpServletResponse response) throws Exception{ 
		class FooModel implements Model{
			private ModelMap modelMap = new ModelMap();
			
			@Override
			public Model mergeAttributes(Map<String, ?> attributes) {
				modelMap.mergeAttributes(attributes);
				return this;
			}
			
			@Override
			public boolean containsAttribute(String attributeName) {
				return modelMap.containsKey(attributeName);
			}
			
			@Override
			public Map<String, Object> asMap() {
				return modelMap;
			}
			
			@Override
			public Model addAttribute(Object attributeValue) {
				modelMap.addAttribute(attributeValue);
				return this;
			}
			
			@Override
			public Model addAttribute(String attributeName, Object attributeValue) {
				modelMap.addAttribute(attributeName,attributeValue);
				return this;
			}
			
			@Override
			public Model addAllAttributes(Map<String, ?> attributes) {
				modelMap.addAllAttributes(attributes);
				return this;
			}
			
			@Override
			public Model addAllAttributes(Collection<?> attributeValues) {
				modelMap.addAllAttributes(attributeValues);
				return this;
			}
		}
		FooModel model = new FooModel();
		return model;
	}
	
	@RequestMapping(path={"/return-view0"},method={RequestMethod.GET})
	public View returnView0(HttpServletRequest request,HttpServletResponse response) throws Exception{ 
		{
			InternalResourceView internalResourceView = new InternalResourceView(); // 使用jsp解析引擎
			internalResourceView.setContentType("text/html; charset=UTF-8");
			internalResourceView.setUrl("/WEB-INF/templates/resp-handler/return-view0.jsp");
		}
		return null;
	}
	
	@RequestMapping(path={"/method3"},method={RequestMethod.GET})
	public ResponseEntity method3(HttpServletRequest request,HttpServletResponse response) throws Exception{ 
		// 头
		MultiValueMap<String, String> headers = new LinkedMultiValueMap();
		headers.set("X-Sender-By", "JanChou");
		// 体部
		ResponseBodyEmitter responseBodyEmitter = new ResponseBodyEmitter(3600L);
		responseBodyEmitter.send("{status:200;message:'hello message...'}",MediaType.parseMediaType(MediaType.APPLICATION_JSON_UTF8_VALUE)); // "application/json;charset=UTF-8"
		// 状态
		HttpStatus status = HttpStatus.OK;
		// 返回实体
		return new ResponseEntity(responseBodyEmitter,headers,status);
	}
	
	@RequestMapping(path={"/method4"},method={RequestMethod.GET})
	public StreamingResponseBody method4(HttpServletRequest request,HttpServletResponse response) throws Exception{ 
		return null;
	}
	
	@RequestMapping(path={"/method5"},method={RequestMethod.GET})
	public HttpEntity method5(HttpServletRequest request,HttpServletResponse response) throws Exception{ 
		return null;
	}
	
	@RequestMapping(path={"/method6"},method={RequestMethod.GET})
	public Callable method6(HttpServletRequest request,HttpServletResponse response) throws Exception{ 
		return null;
	}
	
	@RequestMapping(path={"/method7"},method={RequestMethod.GET})
	public WebAsyncTask method7(HttpServletRequest request,HttpServletResponse response) throws Exception{ 
		return null;
	}
	
	@RequestMapping(path={"/method8"},method={RequestMethod.GET})
	public @ModelAttribute Object method8(HttpServletRequest request,HttpServletResponse response) throws Exception{ 
		return null;
	}
	
	@RequestMapping(path={"/method9"},method={RequestMethod.GET})
	public @ResponseBody Object method9(HttpServletRequest request,HttpServletResponse response) throws Exception{ 
		return null;
	}
	
	@RequestMapping(path={"/method10"},method={RequestMethod.GET})
	public String method10(HttpServletRequest request,HttpServletResponse response) throws Exception{ 
		return null;
	}
	
	@RequestMapping(path={"/method11"},method={RequestMethod.GET})
	public Map method11(HttpServletRequest request,HttpServletResponse response) throws Exception{ 
		return null;
	}
	
	@RequestMapping(path={"/method12"},method={RequestMethod.GET})
	public String method12(HttpServletRequest request,HttpServletResponse response) throws Exception{ 
		return "redirect:http://www.baidu.com";
		
	}
	
	@RequestMapping(path={"/method13"},method={RequestMethod.GET})
	public String method13(HttpServletRequest request,HttpServletResponse response) throws Exception{ 
		return "forward:/resp-handler/method13";
	}


}
