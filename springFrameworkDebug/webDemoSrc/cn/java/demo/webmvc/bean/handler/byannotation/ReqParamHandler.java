package cn.java.demo.webmvc.bean.handler.byannotation;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.security.Principal;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.SmartValidator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.MatrixVariable;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.ValueConstants;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.WebUtils;

import cn.java.demo.webmvc.form.UserLoginForm;
import cn.java.demo.webmvc.validator.UserLoginFormValidator;

/**
 * @author zhouzhian
 *
 */
@RequestMapping(
	path={"/req-param-handler"},
	method={RequestMethod.GET,RequestMethod.POST}
)
public class ReqParamHandler {
	
	/**
	 * 
	 * @param binder
	 */
	@InitBinder(value={"userLognForm","userLognForm0"})
	public void initBinder(DataBinder binder) {
		binder.setValidator(new UserLoginFormValidator()); // 添加校验器
	}

	
	/**
	 * http://localhost:8080/springwebmvc/req-param-handler/get
	 */
	@RequestMapping(path={"/get"},method={RequestMethod.GET})
	public ModelAndView get(
			@RequestParam(name="userId",required=false)String userId, // get、post
			@RequestParam(name="",required=false)Map requestParamMap, 
			@PathVariable(name="urlTplParam0",required=false)String urlTplParam0, // 地址中的模板变量
			@PathVariable(name="",required=false)Map pathVariableMap,
			@MatrixVariable(name="matrixVariableParam0",pathVar="pathVar0",required=false)String matrixVariableParam0,
			@MatrixVariable(name="",pathVar="pathVar0",required=false)Map matrixVariableParamMap,
			@ModelAttribute(name="username") String username, 
			@ModelAttribute(name="userLognForm") @Validated() UserLoginForm userLogin, // 
			BindingResult result, // Errors result, 必须紧跟在@ModelAttribute后面 - 存放校验结果
			@RequestBody(required=false)String requestBody0, // !!! 读取http中body的数据，要首先配置转换器
			@RequestPart(name="",required=false)String requestPart,  //  !!! 读取http中multipart的数据，要首先配置转换器
			@RequestHeader(name=HttpHeaders.USER_AGENT,required=false)String userAgent, // header
			@RequestHeader(name="",required=false)Map requestHeaderMap,
			@CookieValue(name="cookieValue0",required=false)Cookie cookieValue0,  // cookie
			@Value(value="value值")String value0, // 常量
			@SessionAttribute(name="sessionAttribute0",required=false)String sessionAttribute0, // session
			@RequestAttribute(name="requestAttribute0",required=false)String requestAttribute0, 
			WebRequest webRequest,  // 包装对象
			HttpServletRequest request, // 原生对象
			// MultipartRequest multipartRequest,
			HttpSession httpSession,
			Principal principal,
			Locale locale,
			TimeZone timeZone,
			java.time.ZoneId zoneId,
			InputStream inputStream, // InputStream 和 Reader 不能同时使用
			// Reader reader,
			HttpMethod httpMethod,
			HttpServletResponse response, // 原生对象
			// OutputStream outputStream, // OutputStream 和 Writer 不能同时使用
			Writer writer,
			HttpEntity httpEntity, // !!!
			RequestEntity requestEntity, // !!!
			RedirectAttributes redirectAttributes,// !!!
			Model model,
			Map modelMap,
			SessionStatus sessionStatus,
			UriComponentsBuilder uriComponentsBuilder,
			ServletUriComponentsBuilder servletUriComponentsBuilder
	) throws Exception{ 
		//  @RequestParam
		{
			// 获取方式1 -- 通过request获取
			{
				String[] values = request.getParameterValues("userId");
				if ((values != null) && values.length > 0) {
					System.out.println(values[0]);
				}
			}
			
			// 获取方式2 -- 直接映射好
			{
				System.out.println(userId);
			}
			
			// 获取方式3  -- 通过map获取
			{
				System.out.println(requestParamMap.get("userId"));
			}
			
		}
		
		// @PathVariable 地址中的模板变量
		{
			// 获取方式1 -- 通过request获取
			{
				Map<String, String> uriTemplateVars = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
				// uriTemplateVars = (Map<String, String>) webRequest.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);
				System.out.println((uriTemplateVars != null ? uriTemplateVars.get("urlTplParam0") : null));
			}
			
			// 获取方式2 -- 直接映射好
			{
				System.out.println(urlTplParam0);
			}
			
			// 获取方式3  -- 通过map获取
			{
				System.out.println(pathVariableMap.get("urlTplParam0"));
			}
		}
		
		// @MatrixVariable 多维数组
		{
			// 获取方式1 -- 通过request获取
			{
				Map<String, MultiValueMap<String, String>> pathParameters = (Map<String, MultiValueMap<String, String>>) request.getAttribute(HandlerMapping.MATRIX_VARIABLES_ATTRIBUTE);
				// pathParameters = (Map<String, MultiValueMap<String, String>>)webRequest.getAttribute(HandlerMapping.MATRIX_VARIABLES_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);
				String pathVar = "pathVar0";
				if (!pathVar.equals(ValueConstants.DEFAULT_NONE)) {
					if ((pathParameters!=null) && pathParameters.containsKey(pathVar)) {
						System.out.println(pathParameters.get(pathVar).get("matrixVariableParam0"));
					}
				}
			}
			
			// 获取方式2 -- 直接映射好
			{
				System.out.println(matrixVariableParam0);
			}
			
			// 获取方式3  -- 通过map获取
			{
				System.out.println(matrixVariableParamMap.get("matrixVariableParam0"));
			}
			
		}
		
		// @ModelAttribute
		{
			// 1.尝试从url模板中获取，
			// 2.尝试request.getParameter(attributeName)获取
			// 3.尝试创建对象，自动注入
			if(result instanceof BeanPropertyBindingResult){
				
			}
		}
		
		// @RequestBody
		{
			
		}
		
		// @RequestPart
		{
			
		}
		
		// @RequestHeader
		{
			// 获取方式1 -- 通过request获取
			{
				// webRequest.getHeaderValues(HttpHeaders.USER_AGENT)[0];
				System.out.println(request.getHeader(HttpHeaders.USER_AGENT));
			}
			
			// 获取方式2 -- 直接映射好
			{
				System.out.println(userAgent);
			}
			
			// 获取方式3  -- 通过map获取
			{
				System.out.println(requestHeaderMap.get(HttpHeaders.USER_AGENT));
			}
		}
		
		// @CookieValue
		{
			// 获取方式1 -- 通过request获取
			{
				System.out.println(WebUtils.getCookie(request, "cookieValue0"));
			}
			
			// 获取方式2 -- 直接映射好
			{
				System.out.println(cookieValue0);
			}
		}
		
		// @SessionAttribute
		{
			// 获取方式1 -- 通过request获取
			{
				System.out.println(webRequest.getAttribute("sessionAttribute0", RequestAttributes.SCOPE_SESSION));
			}

			// 获取方式2 -- 直接映射好
			{
				System.out.println(sessionAttribute0);
			}
		}
		
		// @RequestAttribute
		{
			// 获取方式1 -- 通过request获取
			{
				System.out.println(webRequest.getAttribute("requestAttribute0", RequestAttributes.SCOPE_REQUEST));
			}
			
			// 获取方式2 -- 直接映射好
			{
				System.out.println(requestAttribute0);
			}
		}
		
		// WebRequest webRequest
		if(webRequest instanceof NativeWebRequest){ // webRequest = org.springframework.web.context.request.ServletWebRequest
			NativeWebRequest nativeWebRequest = (NativeWebRequest) webRequest;
			HttpServletRequest httpServletRequest = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
			HttpServletResponse httpServletResponse = nativeWebRequest.getNativeResponse(HttpServletResponse.class);
			
			// input
			{
				// header
				httpServletRequest.getHeader("key0");
				// get/post
				httpServletRequest.getParameter("key0");
				// session 
				httpServletRequest.getSession().getAttribute("key0");
				httpServletRequest.getSession().setAttribute("key0", "value0");
				// cookie
				httpServletRequest.getCookies();
				// attribute
				httpServletRequest.getAttribute("key0");
				httpServletRequest.setAttribute("key0", "value0");
				// InputStream
				httpServletRequest.getInputStream();
				// forward
				// httpServletRequest.getRequestDispatcher("/index.html").forward(httpServletRequest, httpServletResponse);
			}
			
			// output
			{
				// CharacterEncoding
				httpServletResponse.setCharacterEncoding("utf-8");
				// header
				httpServletResponse.addHeader("key0", "value0");
				// cookie
				Cookie cookie = new Cookie("cookieName0", "cookieValue0");
				httpServletResponse.addCookie(cookie);
				// Writer
				// httpServletResponse.getWriter().write("{json:json}");
			}
		}
		
		// HttpServletRequest request
		{
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
		writer.write("hello , code in "+this.getClass().getName());
		return null;
	}

}
