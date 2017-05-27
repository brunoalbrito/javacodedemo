package cn.java.demo.webmvc.internal;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.util.UriUtils;
import org.springframework.web.util.UrlPathHelper;
import org.springframework.web.util.WebUtils;

public class UrlPathHelperTest {

	public static void main(String[] args) throws Exception {
		
		// 编码/解码
		{
			System.out.println(UriUtils.encode("paramvalue", WebUtils.DEFAULT_CHARACTER_ENCODING));
			System.out.println(UriUtils.decode("paramvalue", WebUtils.DEFAULT_CHARACTER_ENCODING));
			
		}
		
		// 编码/解码
		{
			String strTemp = "中国";
			strTemp = UriUtils.encode(strTemp, "UTF-8");
			System.out.println(strTemp);
			System.out.println(UriUtils.decode(strTemp, "UTF-8"));
		}
	}

	public static void test() {
		HttpServletRequest request = null;
		Map<String, String> uriVariables = null;
		
		UrlPathHelper urlPathHelper = new UrlPathHelper();
		Map<String, String> decodedUriVariables = urlPathHelper.decodePathVariables(request, uriVariables);
	}
	
	
	

}
