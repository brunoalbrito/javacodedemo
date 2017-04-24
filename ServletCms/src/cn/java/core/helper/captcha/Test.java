package cn.java.core.helper.captcha;

import java.io.IOException;
import java.util.Date;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Captcha vCode = new Captcha(120, 40, 5, 100);
		try {
			String path = "D:/t/" + new Date().getTime() + ".png";
			System.out.println(vCode.getCode() + " >" + path);
			vCode.write(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
//	@Override  
//    protected void doGet(HttpServletRequest reqeust,  
//            HttpServletResponse response) throws ServletException, IOException {  
//        // 设置响应的类型格式为图片格式  
//        response.setContentType("image/jpeg");  
//        //禁止图像缓存。  
//        response.setHeader("Pragma", "no-cache");  
//        response.setHeader("Cache-Control", "no-cache");  
//        response.setDateHeader("Expires", 0);  
//          
//        HttpSession session = reqeust.getSession();  
//          
//        Captcha captcha = new Captcha(120,40,5,100);  
//        session.setAttribute("code", captcha.getCode());  
//        captcha.write(response.getOutputStream());  
//    }  

}
