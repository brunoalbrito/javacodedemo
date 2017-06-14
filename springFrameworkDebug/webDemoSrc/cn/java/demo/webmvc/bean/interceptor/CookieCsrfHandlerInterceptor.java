package cn.java.demo.webmvc.bean.interceptor;

import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Random;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.CookieGenerator;
import org.springframework.web.util.WebUtils;

public class CookieCsrfHandlerInterceptor extends CookieGenerator implements CsrfHandlerInterceptor {
	private static final String DEFAULT_COOKIE_NAME = "_csrf";
	private static final String CSRF_HEADER = "X-CSRF-Token";
	private static final String DEFAULT_PRIVATE_KEY_STRING = "MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkEAocbCrurZGbC5GArEHKlAfDSZi7gFBnd4yxOt0rwTqKBFzGyhtQLu5PRKjEiOXVa95aeIIBJ6OhC2f8FjqFUpawIDAQABAkAPejKaBYHrwUqUEEOe8lpnB6lBAsQIUFnQI/vXU4MV+MhIzW0BLVZCiarIQqUXeOhThVWXKFt8GxCykrrUsQ6BAiEA4vMVxEHBovz1di3aozzFvSMdsjTcYRRo82hS5Ru2/OECIQC2fAPoXixVTVY7bNMeuxCP4954ZkXp7fEPDINCjcQDywIgcc8XLkkPcs3Jxk7uYofaXaPbg39wuJpEmzPIxi3k0OECIGubmdpOnin3HuCP/bbjbJLNNoUdGiEmFL5hDI4UdwAdAiEAtcAwbm08bKN7pwwvyqaCBC//VnEWaq39DCzxr+Z2EIk=";
	private static final String DEFAULT_PUBLIC_KEY_STRING = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAKHGwq7q2RmwuRgKxBypQHw0mYu4BQZ3eMsTrdK8E6igRcxsobUC7uT0SoxIjl1WveWniCASejoQtn/BY6hVKWsCAwEAAQ==";
	private static final String ENCRYPT_ALGORITHM = "RSA";
	private static final String ENCRYPT_DEFAULT_CHARSET = "UTF-8";

	private String publicKey = DEFAULT_PUBLIC_KEY_STRING;
	private String privateKey = DEFAULT_PRIVATE_KEY_STRING;
	private String pageCsrfTokenKeyName = DEFAULT_COOKIE_NAME;
	public CookieCsrfHandlerInterceptor() {
		setCookieName(DEFAULT_COOKIE_NAME);
	}

	public String getPageCsrfTokenKeyName() {
		return pageCsrfTokenKeyName;
	}

	public void setPageCsrfTokenKeyName(String pageCsrfTokenKeyName) {
		this.pageCsrfTokenKeyName = pageCsrfTokenKeyName;
	}


	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	/**
	 * 获取客户端的“明值”
	 * @param request
	 * @return
	 */
	private String getUserCsrfTokenValue(HttpServletRequest request){
		String valueTemp = request.getParameter(pageCsrfTokenKeyName); // 从页面获取
		if(valueTemp==null){
			valueTemp = request.getHeader(CSRF_HEADER); // 从头部获取
		}
		return valueTemp;
	}

	/**
	 * 创建csrf值
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	private void buildCsrfTokenInfoInRequestAttribute(HttpServletRequest request,HttpServletResponse response) throws Exception{
		RSAPublicKey rsaPublicKey = EncryptUtil.getPublicKey(publicKey);
		String csrfTokenValue = RandomUtil.randomUUIDWithoutDelimiter();
		String csrfTokenValueEncrypt = EncryptUtil.encrypt(csrfTokenValue, rsaPublicKey); // 公钥加密新的
		request.setAttribute(CSRF_TOKEN_KEYNAME_REQUEST_ATTRIBUTE_NAME,pageCsrfTokenKeyName);
		request.setAttribute(CSRF_TOKEN_VALUE_REQUEST_ATTRIBUTE_NAME,csrfTokenValue);
		addCookie(response, csrfTokenValueEncrypt); // 发送密值
	}
	
	/**
	 * 真实token值，加密放入cookie
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		// only validate CSRF token on non-"safe" methods http://www.w3.org/Protocols/rfc2616/rfc2616-sec9.html#sec9.1.1
		String method = request.getMethod().toUpperCase();
		if("GET".equals(method) || "HEAD".equals(method) || "OPTIONS".equals(method)){
			buildCsrfTokenInfoInRequestAttribute(request,response);
			return true;
		}
		
		// Retrieve cookie value from request.
		Cookie cookie = WebUtils.getCookie(request,getCookieName());
		if (cookie != null) {
			String trueCsrfTokenValue = cookie.getValue();
			String requestCsrfTokenValue = getUserCsrfTokenValue(request); // 获取客户端的“明值”
			if(StringUtils.isEmpty(trueCsrfTokenValue) || StringUtils.isEmpty(requestCsrfTokenValue)){
				return false;
			}
			RSAPrivateKey rsaPrivateKey = EncryptUtil.getPrivateKey(privateKey);
			trueCsrfTokenValue = EncryptUtil.decrypt(trueCsrfTokenValue, rsaPrivateKey); // 私钥解密出来内容
			if(trueCsrfTokenValue.equals(requestCsrfTokenValue)){
				System.out.println("校验通过了...");
				buildCsrfTokenInfoInRequestAttribute(request,response);
				return true;
			}
			else{
				return false;
			}
		}
		return false;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

	}

	@SuppressWarnings("unused")
	private static class RandomUtil {
		public static String randomUUIDWithoutDelimiter() {
			UUID uuid = UUID.randomUUID();
			String randomUUIDStr = uuid.toString();
			// 去掉"-"符号
			return randomUUIDStr.substring(0, 8) + randomUUIDStr.substring(9, 13) + randomUUIDStr.substring(14, 18) + randomUUIDStr.substring(19, 23) + randomUUIDStr.substring(24);
		}

		public static String getRandomString(int length){  
			String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";  
			Random random = new Random();  
			StringBuffer sb = new StringBuffer();  

			for(int i = 0 ; i < length; ++i){  
				int number = random.nextInt(62);//[0,62)  
				sb.append(str.charAt(number));  
			}  
			return sb.toString();  
		}  
	}
	
	/**
	 * 加密工具
	 */
	private static class EncryptUtil {
		/**
		 * 获取公钥
		 * @param publicKey
		 * @return
		 * @throws Exception
		 */
		public static RSAPublicKey getPublicKey(String publicKey) throws Exception{  
			byte[] keyBytes = Base64.getDecoder().decode(publicKey);  
			X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);  
			KeyFactory keyFactory = KeyFactory.getInstance(ENCRYPT_ALGORITHM);  
			return (RSAPublicKey) keyFactory.generatePublic(spec);  
		}

		/**
		 * 获取私钥
		 * @param privateKey
		 * @return
		 * @throws NoSuchAlgorithmException 
		 * @throws InvalidKeySpecException 
		 * @throws Exception
		 */
		public static RSAPrivateKey getPrivateKey(String privateKey) throws Exception{  
			byte[] keyBytes = Base64.getDecoder().decode(privateKey);  
			PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);  
			KeyFactory keyFactory = KeyFactory.getInstance(ENCRYPT_ALGORITHM);  
			return (RSAPrivateKey) keyFactory.generatePrivate(spec);  
		}


		/**
		 * 加密
		 * @param input
		 * @param pubOrPrikey
		 * @return
		 */
		public static String encrypt(String content, Key pubOrPrikey) throws Exception{
			Cipher cipher = null;
			cipher = Cipher.getInstance(ENCRYPT_ALGORITHM); 
			cipher.init(Cipher.ENCRYPT_MODE, pubOrPrikey);
			byte[] result = cipher.doFinal(content.getBytes(ENCRYPT_DEFAULT_CHARSET));
			return Base64.getEncoder().encodeToString(result);
		}

		/**
		 * 解密
		 * @param input
		 * @param pubOrPrikey
		 * @return
		 */
		public static String decrypt(String content, Key pubOrPrikey) throws Exception {
			Cipher cipher = null;
			cipher = Cipher.getInstance(ENCRYPT_ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, pubOrPrikey);  
			byte[] result = cipher.doFinal(Base64.getDecoder().decode(content));
			return new String(result);
		}
	}

}
