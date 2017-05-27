package cn.java.demo.webmvc.bean.handler.byannotation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * 处理上传
 * @author zhouzhian
 *
 */
@RequestMapping(
	path={"/download-handler"},
	method={RequestMethod.GET,RequestMethod.POST}
)
public class DownloadHandler {
	
	/**
	 * 修改
	 */
	@RequestMapping(path={"/*"},method={RequestMethod.POST})
	public ModelAndView upload(HttpServletRequest request,HttpServletResponse response) throws Exception{ 
		return null;
	}
	

}
