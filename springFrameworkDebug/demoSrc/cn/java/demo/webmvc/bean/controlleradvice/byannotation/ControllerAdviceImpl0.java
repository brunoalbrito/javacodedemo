package cn.java.demo.webmvc.bean.controlleradvice.byannotation;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice(basePackages={"cn.java.controller0","cn.java.controller1."},assignableTypes={},annotations={})
public class ControllerAdviceImpl0 {
	
	@InitBinder
	public void initBinder0(){
		
	}
	
	@InitBinder
	public void initBinder1(){
		
	}
	
	@ModelAttribute
	public void modelAttribute0(){
		
	}
	
	@ModelAttribute
	public void modelAttribute1(){
		
	}
}
