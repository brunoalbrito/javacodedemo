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
 * @author zhouzhian
 *
 */
@RequestMapping(
	path={"/rest-handler"},
	method={RequestMethod.GET,RequestMethod.POST}
)
public class RestHandler {
	
	/**
	 * 添加
	 */
	@RequestMapping(path={"/*"},method={RequestMethod.PUT})
	public ModelAndView put(HttpServletRequest request,HttpServletResponse response) throws Exception{ 
		return null;
	}
	
	/**
	 * 读取
	 */
	@RequestMapping(path={"/{fooid}"},method={RequestMethod.GET})
	public ModelAndView get(HttpServletRequest request,HttpServletResponse response) throws Exception{ 
		return null;
	}
	
	/**
	 * 修改
	 */
	@RequestMapping(path={"/*"},method={RequestMethod.POST})
	public ModelAndView post(HttpServletRequest request,HttpServletResponse response) throws Exception{ 
		return null;
	}
	
	/**
	 * 删除
	 */
	@RequestMapping(path={"/{fooid}"},method={RequestMethod.DELETE})
	public ModelAndView delete(HttpServletRequest request,HttpServletResponse response) throws Exception{ 
		return null;
	}

}
