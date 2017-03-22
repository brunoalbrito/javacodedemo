package cn.java.security.bc;

import java.util.Base64;

/**
 * http://www.myexception.cn/industry/1505040.html
 * http://www.bouncycastle.org/latest_releases.html
 * http://www.myexception.cn/program/679920.html
 * http://www.massapi.com/class/jc/JcaX509v3CertificateBuilder.html
 * http://blog.csdn.net/xtayfjpk/article/details/46402061
 * 
 * @author zhouzhian
 *
 */
public class Test {

	/**
	 * openssl req -new -nodes -sha256 -newkey rsa:2048 -keyout myprivate.key -out mydomain.csr
	 */
	public static void main(String[] args) throws Exception {
//		testMessageDigest();
		testGenX509v3Certificate();
	}
	
	public static void testMessageDigest() throws Exception {
		String str = "test";
		byte[] bytes = SecurityUtil.MessageDigestUtil.digestMd4(str);
		String base64Str = Base64.getEncoder().encodeToString(bytes);
		System.out.println(base64Str);
	}
	
	public static void testGenX509v3Certificate() throws Exception {
		SecurityUtil.CertificateUtil.genX509v3Certificate(true);
	}
	

}
