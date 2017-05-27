package cn.java.demo.webmvc.bean.handler.byannotation;

import java.util.Map;
import java.util.concurrent.Callable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.WebAsyncTask;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

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
	 */
	@RequestMapping(path={"/method0"},method={RequestMethod.GET})
	public ModelAndView method0(HttpServletRequest request,HttpServletResponse response) throws Exception{ 
		return null;
	}
	
	@RequestMapping(path={"/method1"},method={RequestMethod.GET})
	public Model method1(HttpServletRequest request,HttpServletResponse response) throws Exception{ 
		return null;
	}
	
	@RequestMapping(path={"/method2"},method={RequestMethod.GET})
	public View method2(HttpServletRequest request,HttpServletResponse response) throws Exception{ 
		return null;
	}
	
	@RequestMapping(path={"/method3"},method={RequestMethod.GET})
	public ResponseEntity method3(HttpServletRequest request,HttpServletResponse response) throws Exception{ 
		return null;
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
