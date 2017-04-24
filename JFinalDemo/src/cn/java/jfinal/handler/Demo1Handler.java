package cn.java.jfinal.handler;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jfinal.handler.Handler;

public class Demo1Handler extends Handler {

	@Override
	public void handle(String target, HttpServletRequest request,
			HttpServletResponse response, boolean[] isHandled) {
			try {
//				request.setCharacterEncoding("UTF-8");//设置字符编码
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("Handler:  cn.java.jfinal.handler.Demo1Handler");
			this.nextHandler.handle(target, request, response, isHandled);
	}

}
