package cn.java.demo.webmvc.bean.handler.byannotation;

import java.util.ArrayList;
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
	 * http://localhost:8080/springwebmvc/resp-xml-handler/method0
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
			Map<String, Object> hashMap = new HashMap();
			{
				// 简单类型
				hashMap.put("intValue", 001);
				hashMap.put("stringValue", "stringValue");
				
				// list类型
				ArrayList list = new ArrayList();
				list.add("element1");
				list.add("element2");
				list.add("element3");
				hashMap.put("list", list);
				
				// map类型
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("mapKey0", 201);
				map.put("mapKey1", "key2_value");
				hashMap.put("map", map);
			}
			
			Map<String, Object> modelMap = new HashMap();	
			modelMap.put("modelMap", hashMap);
			modelAndView.addAllObjects(modelMap);
		}
		
		return modelAndView;
	}
}
