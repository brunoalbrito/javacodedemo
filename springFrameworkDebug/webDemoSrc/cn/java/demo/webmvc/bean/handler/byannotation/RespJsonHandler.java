package cn.java.demo.webmvc.bean.handler.byannotation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import cn.java.demo.webmvc.bean.handlermethodargumentresolver.GsonObject;

/**
 * 响应json
 * @author zhouzhian
 *
 */
@RequestMapping(
	path={"/resp-json-handler"},
	method={RequestMethod.GET,RequestMethod.POST}
)
public class RespJsonHandler {
	
	/**
	 * /resp-json-handler/method0
	 */
	@RequestMapping(path={"/method0"})
	public ModelAndView method0(HttpServletRequest request,HttpServletResponse response) throws Exception{ 
		ModelAndView modelAndView = new ModelAndView();
		
		// 渲染器
		{
			modelAndView.setView(new MappingJackson2JsonView()); // 使用Jackson渲染
		}
		
		// 数据
		{
			Map<String, Object> modelMap = new HashMap();
			{
				// 简单类型
				modelMap.put("intValue", 001);
				modelMap.put("stringValue", "stringValue");
				
				// list类型
				ArrayList list = new ArrayList();
				list.add("element1");
				list.add("element2");
				list.add("element3");
				modelMap.put("list", list);
				
				// map类型
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("mapKey0", 201);
				map.put("mapKey1", "key2_value");
				modelMap.put("map", map);
			}
			modelAndView.addAllObjects(modelMap);
		}
		
		return modelAndView;
	}
	
	/**
	 * /json-req-resp-handler/method1
	 */
	@RequestMapping(path={"/method1"},method={RequestMethod.POST})
	public ModelAndView method1(GsonObject gsonObject,HttpServletRequest request,HttpServletResponse response) throws Exception{ 
		// 获取json数据
		{
			System.out.println(gsonObject.get("code"));
			System.out.println(gsonObject.get("code").getClass());

			System.out.println(gsonObject.get("message"));
			System.out.println(gsonObject.get("message").getClass());

			Map<String, Object> resultData = (Map<String, Object>) gsonObject.get("data");
			System.out.println("---------intValue----------------");  
			System.out.println(resultData.get("intValue"));
			System.out.println(resultData.get("intValue").getClass());

			System.out.println("---------stringValue----------------");  
			System.out.println(resultData.get("stringValue"));
			System.out.println(resultData.get("stringValue").getClass());

			System.out.println("---------LinkedTreeMap----------------");
			System.out.println(gsonObject.get("data").getClass());

			System.out.println("---------ArrayList----------------");  
			ArrayList resultArrayList = (ArrayList) resultData.get("list1");
			Iterator iterator = resultArrayList.iterator();
			while (iterator.hasNext()) {
				System.out.println(iterator.next());
			}
			System.out.println(resultData.get("list1").getClass());
		}
		
		// 响应json数据
		{
			ModelAndView modelAndView = new ModelAndView();
			modelAndView.setView(new MappingJackson2JsonView()); // 使用Jackson渲染
			Map<String, Object> modelMap = new HashMap();
			modelMap.put("key0", "value0");
			modelAndView.addAllObjects(modelMap);
			return modelAndView;
		}
	}
}
