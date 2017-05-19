package cn.java.debug.webmvc.oldhandler;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Deprecated
@Controller
public class Foo1WithAnnotationHandler {
	
	/**
	 * 没有要传递到视图的数据
	 */
	@RequestMapping(path={"/foo1-with-annotation/method0","/foo1-with-annotation/method0-alias0"})
	public void method0(){
		return;
	}
	
	/**
	 * 有要传递到视图的数据通过ModelAndView指定
	 * @return
	 */
	@RequestMapping(path={"/foo1-with-annotation/method1","/foo1-with-annotation/method1-alias1"})
	public ModelAndView method1(){
		return null;
	}
}
