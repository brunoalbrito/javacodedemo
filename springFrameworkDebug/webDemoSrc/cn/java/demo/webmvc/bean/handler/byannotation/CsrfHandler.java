package cn.java.demo.webmvc.bean.handler.byannotation;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author zhouzhian
 *
 */
@RequestMapping(
	path={"/csrf-handler"},
	method={RequestMethod.GET,RequestMethod.POST}
)
public class CsrfHandler {
	
	/**
	 * /csrf-handler/method0
	 */
	@RequestMapping(path={"/method0"},method={RequestMethod.GET,RequestMethod.POST})
	public String method0(HttpServletRequest request,HttpServletResponse response,Locale locale) throws Exception{ 
		return "csrf-handler/method0";
	}
	
}
