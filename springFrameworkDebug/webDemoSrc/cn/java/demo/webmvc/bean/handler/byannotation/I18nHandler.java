package cn.java.demo.webmvc.bean.handler.byannotation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import cn.java.demo.webmvc.bean.handlermethodargumentresolver.GsonObject;

/**
 * @author zhouzhian
 *
 */
@RequestMapping(
	path={"/i18n-handler"},
	method={RequestMethod.GET,RequestMethod.POST}
)
public class I18nHandler {
	
	/**
	 * /i18n-handler/method0
	 */
	@RequestMapping(path={"/method0"},method={RequestMethod.GET})
	public String method0(HttpServletRequest request,HttpServletResponse response,Locale locale) throws Exception{ 
		// org.springframework.web.context.support.XmlWebApplicationContext
		// org.springframework.context.support.AbstractApplicationContext.getMessage(...)
		WebApplicationContext webApplicationContext = (WebApplicationContext) request.getAttribute(DispatcherServlet.WEB_APPLICATION_CONTEXT_ATTRIBUTE); // 子上下文
		Object[] args = { new Long(1273), "DiskOne" };
		
		// 多语言
		System.out.println("locale = " + locale.toString());
//		String message = webApplicationContext.getMessage("module0.ctrl0.action0", args, "硬盘\"{1}\"有{0}个文件。", Locale.CHINESE);
		String message = webApplicationContext.getMessage("module0.ctrl0.action0", args, "硬盘\"{1}\"有{0}个文件。", locale);
		System.out.println(message);
		// 会扫描指定语言的所有语言包（根据语言来选择加载），即module0_zh_CN.properties、module1_zh_CN.xml都会被加载。
		System.out.println(webApplicationContext.getMessage("module0.controller0.method0.message0", args, "默认消息...", locale));
		System.out.println(webApplicationContext.getMessage("module1.controller0.method0.message0", args, "默认消息...", locale));
		return "i18n-handler/method0";
	}
	
}
