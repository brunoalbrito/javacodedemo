package cn.java.demo.webmvc.bean.handler.byannotation;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.xml.MappingJackson2XmlView;

/**
 * @author zhouzhian
 *
 */
@RequestMapping(
	path={"/resp-xml-handler"},
	method={RequestMethod.GET,RequestMethod.POST}
)
public class RespXmlHandler {
	
	/**
	 * /xml-handler/method0
	 */
	@RequestMapping(path={"/method0"})
	public ModelAndView method0(HttpServletRequest request,HttpServletResponse response) throws Exception{ 
		ModelAndView modelAndView = new ModelAndView();
		
		// 渲染器
		{
			modelAndView.setView(new MappingJackson2XmlView()); // 使用Jackson渲染
		}
		
		// 数据
		{
			Map<String, Object> modelMap = new HashMap();
			modelMap.put("key0", "value0");
			modelAndView.addAllObjects(modelMap);
		}
		
		return modelAndView;
	}
}
