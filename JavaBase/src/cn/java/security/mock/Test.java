package cn.java.security.mock;

public class Test {

	public static void main(String[] args) {
		System.out.println("---------MD5加密-----------");
		System.out.println(Encrypter.encryptByMD5("123"));
		
		System.out.println("---------使用默认秘钥、加解密-----------");
		{
			String encryptStr = Encrypter.encryptByDES("hello");
			System.out.println(encryptStr);
			System.out.println(Encrypter.decodeByDES(encryptStr));
		}
		
		System.out.println("---------指定秘钥、加解密-----------");
		{
			String encryptStr = Encrypter.encryptByDES("19800820","hello");
			System.out.println(encryptStr);
			System.out.println(Encrypter.decodeByDES("19800820",encryptStr));
		}
	}

}
