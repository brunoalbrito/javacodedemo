package cn.java.debug.webmvc.other;

import org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter;

public class AnnotationMethodHandlerAdapterTest {

	public static void main(String[] args) {
		FooHandler handler = new FooHandler();
		AnnotationMethodHandlerAdapter annotationMethodHandlerAdapter = new AnnotationMethodHandlerAdapter();
		if(annotationMethodHandlerAdapter.supports(handler)){
			
		}
	}
	
	public static class FooHandler {
		
	}

}
