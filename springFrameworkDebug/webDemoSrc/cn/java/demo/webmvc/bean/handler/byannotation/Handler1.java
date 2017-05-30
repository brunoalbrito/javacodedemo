package cn.java.demo.webmvc.bean.handler.byannotation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class Handler1 {
	/**
	 * 由于在类上面没有使用@RequestMapping注解，所以在这个的path路径要从根路径开始写
	 */
	@RequestMapping(name="Handler1_method0",
			path={"/handler1/method0","/handler1/method0_alias"},
			method={RequestMethod.GET,RequestMethod.POST},
			params={"param0=value0","param0!=value0"},
			headers={"content-type=text/*"},
			consumes={"text/plain","application/*"},
			produces={"text/plain", "application/*"}
			)
	public void method0(){
		
	}
}
