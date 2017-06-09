package cn.java.demo.webmvc.bean.handler.byannotation;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
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
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.context.request.async.WebAsyncTask;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import org.springframework.web.servlet.view.InternalResourceView;
import org.springframework.web.util.WebUtils;

import cn.java.demo.web.util.WebUtilx;
import net.sf.jasperreports.engine.JREmptyDataSource;

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
	 * 《jsp、jstl》
	 * 返回“模板+模板数据”
	 * 		有jsp模板，会直接使用
	 * 		赋值数据、并进行渲染
	 * 		localhost:8080/springwebmvc/resp-handler/return-model-and-view0
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
	 * 《freemark》
	 * 返回“模板+模板数据”
	 * 		没有jsp模板，会使用freemark的模板
	 * 		赋值数据、并进行渲染 - 
	 * 			/resp-handler/return-model-and-view1
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
	 * 《pdf文件》
	 * /resp-handler/return-model-and-view1
	 */
	@RequestMapping(path={"/return-model-and-view2"},method={RequestMethod.GET})
	public ModelAndView returnModelAndView2(HttpServletRequest request,HttpServletResponse response) throws Exception{ 
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("resp-handler/return-model-and-view2");
		ModelMap modelMap = new ModelMap();
		modelMap.put("parameter0", "parameter0value"); // 模板变量
		modelMap.put("parameter1", "parameter1value");
		modelMap.put("reportData", new JREmptyDataSource()); // 模板数据源
		modelAndView.addAllObjects(modelMap);
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
	
	@RequestMapping(path={"/returnResponseEntity"},method={RequestMethod.GET})
	public ResponseEntity returnResponseEntity(HttpServletRequest request,HttpServletResponse response) throws Exception{ 
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
	
	@RequestMapping(path={"/returnStreamingResponseBody"},method={RequestMethod.GET})
	public StreamingResponseBody returnStreamingResponseBody(HttpServletRequest request,HttpServletResponse response) throws Exception{ 
		
		class StreamingResponseBodyImpl implements StreamingResponseBody {

			@Override
			public void writeTo(OutputStream outputStream) throws IOException {
				outputStream.write("{status:200;message:'hello message...'}".getBytes());
			}
		}
		return (new StreamingResponseBodyImpl());
	}
	
	@RequestMapping(path={"/returnHttpEntity"},method={RequestMethod.GET})
	public HttpEntity returnHttpEntity(HttpServletRequest request,HttpServletResponse response) throws Exception{ 
		// 头
		MultiValueMap<String, String> headers = new LinkedMultiValueMap();
		headers.set("X-Sender-By", "JanChou");
		// 体部
		String body = "{status:200;message:'hello message...'}";
		return (new HttpEntity(body,headers));
	}
	
	@RequestMapping(path={"/returnHttpHeaders"},method={RequestMethod.GET})
	public HttpHeaders returnHttpHeaders(HttpServletRequest request,HttpServletResponse response) throws Exception{ 
		HttpHeaders httpHeaders = new HttpHeaders();
		List<String> list = new ArrayList();
		list.add("JanChou");
		httpHeaders.put("X-Sender-By", list);
		return httpHeaders;
	}
	
	@RequestMapping(path={"/returnCallable"},method={RequestMethod.GET})
	public Callable returnCallable(HttpServletRequest request,HttpServletResponse response) throws Exception{ 
		class StreamingResponseBodyTask implements Callable<Void> {

			private final HttpServletRequest request;
			private final HttpServletResponse response;

			public StreamingResponseBodyTask(HttpServletRequest request, HttpServletResponse response) {
				this.request = request;
				this.response = response;
			}

			@Override
			public Void call() throws Exception {
				this.response.getOutputStream().write("{status:200;message:'hello message...'}".getBytes());
				return null;
			}
		}
		return new StreamingResponseBodyTask(request,response);
	}
	
	@RequestMapping(path={"/retunSimpleDeferredResultAdapter"},method={RequestMethod.GET})
	public DeferredResult returnSimpleDeferredResultAdapter(HttpServletRequest request,HttpServletResponse response) throws Exception{ 
		DeferredResult deferredResult = new DeferredResult();
		deferredResult.setResult("{status:200;message:'hello message...'}");
		return deferredResult;
	}
	
	@RequestMapping(path={"/returnWebAsyncTask"},method={RequestMethod.GET})
	public WebAsyncTask returnWebAsyncTask(HttpServletRequest request,HttpServletResponse response) throws Exception{ 
		Long timeout = 3600L;
		AsyncTaskExecutor executor = new SimpleAsyncTaskExecutor(this.getClass().getSimpleName());
		class StreamingResponseBodyTask implements Callable<Void> {

			private final HttpServletRequest request;
			private final HttpServletResponse response;

			public StreamingResponseBodyTask(HttpServletRequest request, HttpServletResponse response) {
				this.request = request;
				this.response = response;
			}

			@Override
			public Void call() throws Exception {
				this.response.getOutputStream().write("{status:200;message:'hello message...'}".getBytes());
				return null;
			}
		}
		Callable callable = new StreamingResponseBodyTask(request,response);
		WebAsyncTask webAsyncTask = new WebAsyncTask(timeout,executor,callable);
		return webAsyncTask;
		
	}
	
	/**
	 * 只返回“单条模板数据”
	 * 		把返回值放入指定的键
	 */
	@RequestMapping(path={"/method8"},method={RequestMethod.GET})
	public @ModelAttribute(value="tplParam0") Object returnWithModelAttributeAnnotation(HttpServletRequest request,HttpServletResponse response) throws Exception{ 
		return "指定键tplParam0的值";
	}
	
	/**
	 * 查找消息转换器，使用消息转换器进行输出
	 */
	@RequestMapping(path={"/method9"},method={RequestMethod.GET})
	public @ResponseBody Object returnWithResponseBodyAnnotation(HttpServletRequest request,HttpServletResponse response) throws Exception{
		return "";
	}
	
	/**
	 * 只返回“模板名称”
	 */
	@RequestMapping(path={"/returnViewName0"},method={RequestMethod.GET})
	public String returnViewName0(HttpServletRequest request,HttpServletResponse response) throws Exception{ 
		return "resp-handler/return-model-and-view0";
	}
	
	/**
	 * 转发，只支持JSP类型的模板引擎
	 * 		/resp-handler/returnViewName1
	 * 		只返回“模板名称”
	 */
	@RequestMapping(path={"/returnViewName1"},method={RequestMethod.GET})
	public String returnViewName1(HttpServletRequest request,HttpServletResponse response) throws Exception{
		return "forward:" + WebUtilx.getContextUrl(WebUtilx.getPathToServlet(request) +"/resp-handler/return-model-and-view0",request,response); // 只支持JSP类型的模板引擎
	}
	
	/**
	 * 重定向
	 * 		/resp-handler/returnViewName2
	 * 		只返回“模板名称”
	 */
	@RequestMapping(path={"/returnViewName2"},method={RequestMethod.GET})
	public String returnViewName2(HttpServletRequest request,HttpServletResponse response) throws Exception{ 
		return "redirect:http://www.baidu.com"; // 重定向
		
	}
	
	/**
	 * 重定向
	 * 		/resp-handler/returnViewName3
	 * 		只返回“模板名称”
	 */
	@RequestMapping(path={"/returnViewName3"},method={RequestMethod.GET})
	public String returnViewName3(HttpServletRequest request,HttpServletResponse response) throws Exception{
		return "redirect:" + WebUtilx.getContextUrl(WebUtilx.getPathToServlet(request) +"/resp-handler/return-model-and-view0",request,response); // 只支持JSP类型的模板引擎
	}

	/**
	 * 只返回“模板数据”
	 */
	@RequestMapping(path={"/returnMap"},method={RequestMethod.GET})
	public Map returnMap(HttpServletRequest request,HttpServletResponse response) throws Exception{ 
		ModelMap modelMap = new ModelMap();
		modelMap.put("attr1", "value1");
		return modelMap;
	}
	
	/**
	 * /resp-handler/returnNull
	 */
	@RequestMapping(path={"/returnNull"},method={RequestMethod.GET})
	public Map returnNull(HttpServletRequest request,HttpServletResponse response) throws Exception{ 
		return null; // 返回null不会进行任何渲染
	}
	
	/**
	 *  /resp-handler/returnModelAndView
	 */
	@RequestMapping(path={"/returnModelAndView"},method={RequestMethod.GET})
	public ModelAndView returnModelAndView(HttpServletRequest request,HttpServletResponse response) throws Exception{ 
		return new ModelAndView(); // 返回一个空对象，会被自动填充视图，视图的名称和访问的地址相关，即 “resp-handler/returnModelAndView.jsp”
	}

}
