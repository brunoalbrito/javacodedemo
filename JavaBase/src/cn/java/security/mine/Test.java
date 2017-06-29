package cn.java.security.mine;


import java.security.Key;
import java.util.Base64;


import cn.java.codec.hex.HexUtil;
import cn.java.security.mine.SecurityUtil.RsaUtil.RsaKeyPair;


public class Test {

	public static void main(String[] args)  throws Exception{
		System.out.println("-----------<<< testDigest >>>------------------");
		testDigest();
		System.out.println();
		
		System.out.println("-----------<<< testAes >>>------------------");
		testAes();
		System.out.println();
		
		System.out.println("-----------<<< testRsa >>>------------------");
		testRsa();
		System.out.println();
	}

	/**
	 * 对称加密算法
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	public static void testAes() throws Exception {
		{
			String content = "testAes";
			String secretKeyStr = SecurityUtil.AesUtil.genAesKey();
			System.out.println("-----------secretKeyStr------------------");
			System.out.println(secretKeyStr);
			System.out.println("-----------encryptStr------------------");
			String encryptStr = SecurityUtil.AesUtil.encrypt(content, secretKeyStr);
			System.out.println(encryptStr);
			System.out.println("-----------decryptStr------------------");
			String decryptStr = SecurityUtil.AesUtil.decrypt(encryptStr, secretKeyStr);
			System.out.println(decryptStr);
		}
		
		if(false){
			String content = "hello";
			String screctKey = "1234567890123456";
			String secretKeyStr = Base64.getEncoder().encodeToString(screctKey.getBytes());
			System.out.println("-----------secretKeyStr------------------");
			System.out.println(secretKeyStr);
			System.out.println("-----------encryptStr------------------");
			String encryptStr = SecurityUtil.AesUtil.encrypt(content, secretKeyStr);
			System.out.println(encryptStr);
			System.out.println("-----------decryptStr------------------");
			String decryptStr = SecurityUtil.AesUtil.decrypt(encryptStr, secretKeyStr);
			System.out.println(decryptStr);
		}
	}
	
	/**
	 * 非对称加密算法
	 * @throws Exception
	 */
	public static void testRsa() throws Exception {

		String content = "testRsa";
		// 生成秘钥对
		RsaKeyPair mRsaKeyPair = SecurityUtil.RsaUtil.genKeyPair();
		String privateKeyStr = mRsaKeyPair.getPrivateKey();
		privateKeyStr = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCk9cWlNGycaqSDh7R7vS+chj4A0n2zApMfjTO/cJyBFE+HP6FO1CrY0IGl3rq8Z16CWDk7y4IKShwCWF0DfLyeJlkhx+6PxfsJN/lFbHGYY/f8zhrILzBzhXJoJVK/2C8HO/gps+CjbKlPH/CdUX3tX72wZ32zVj0JKNKRdf9jpegTJCmIF3qyGro3LQI3KUquvqgttYLB+yOWyWdpADefkROI/qoNz+q2R0J4SSOFVYSQeQtM6vcRolSSoEjIQ26pEMBealECdM6gwP8HZKuPXTOvt5pODF/xCkEfcIZUeIyasuQGr9stvoknA3zk4aVHqHDISbMrg4vbMT4ZFUwpAgMBAAECggEAVhISvT9cSETwb9yX1FSDCiWVJnKtuYBnaXgQfxvv3P3O/R+7N9lGPfiVHQnY30MEMfh8bHGj+WBvut2GMrKb69zQdurN0+CWOlolSw7pOn02wIZ9vOO6YRxI9IQ85/KRnzGwK3o6hKrGsI1Wy47gqMFixS9KjA88K5JMRC+QQNpPmb49ETV24uTgitKHBpUJZk7HIzSUv7tHHaXVkyInjzysoH4k7Ha8R0TZ3K96SZUVi/ed0flytI6+vh7Cjvf2dbtTgqbLj2ft8D15D1lb4893l/T2G5E4qEAxGrp+MlQNEPvc+5HgwuomGN+Jbdq/BGzbwc+AcoNFdCakZQzBsQKBgQDybeEaC2vcPsnfpzeY8+dbYBHqN4YVZF838/+DECpI8KF8TizFJLTYps1NZ9dBtsSFKpXK9WRCHLV9+497ywbgOTQlvD2e3yn130994A1ECoROIJyYoMmJY4rvJvcB/afgBOp0QiXjjPrLIjw7/kcqAzQZlDbyzPIB3qaXmp4O/QKBgQCuMblyDty+llMJWuTcFuZUltm9ak9hPDgcpoCXGhBIw+GdUKUKIuGky5phVkxxQTn5UY+KBt9ViqTPAd8T9ohBpJsFTQspj4Nf3smLD0MEVFE3YCGWMITcERSsNpIM+eI4MU64w92+9uPDBuZVyP49YBxEtPGIMsKANLv/Q3D3nQKBgEl33TD8Zxqe+KTL00CKIgICEkvNude/zE9zpWp9uqLSMc4vDshMAHZrzmn33zcuAU5uAmk4hsK2WpYsZ2ZXt+S50UDiVDZSYS//FeMKxFpraX8XyP1ENO9q+E7TmyXCTl7Ifpju9vaH4fUvkduIit6DjAV4clKQo4LHi+sD04StAoGAF2wAL2HoNIgp6hwaoa1CkyUlad6dhcl/EfshVy/d+Kq971Ukm9RM2se1nljoNmI61VcuIbUC2pGlz1/FCv4fHBRx86iEU7ZER+PA3b3TmQVtVjDK01L6ykxRp0Y/JReEX2Hh394WNtMjlNmcpvQ7my0NH//kahfJJ0vv60a6LdECgYEAk7+qwa7s3vxDYSHDmmT09bC2KkB/nM6bOD1AstZ7/dFiIPVhcPR7nKGu75YUhVNF9xjF5M9V1oai5CQ5YrnPEQPVaQOlWk4I3q02CteiXd6Z/X2iHrHvkAM7CfWCrLmgQYpUT/B+BpMPafHS4/Xd9RiPC+SHL9U04QmbSUtSR98=";
		String publicKeyStr = mRsaKeyPair.getPublicKey();
		publicKeyStr = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApPXFpTRsnGqkg4e0e70vnIY+ANJ9swKTH40zv3CcgRRPhz+hTtQq2NCBpd66vGdeglg5O8uCCkocAlhdA3y8niZZIcfuj8X7CTf5RWxxmGP3/M4ayC8wc4VyaCVSv9gvBzv4KbPgo2ypTx/wnVF97V+9sGd9s1Y9CSjSkXX/Y6XoEyQpiBd6shq6Ny0CNylKrr6oLbWCwfsjlslnaQA3n5ETiP6qDc/qtkdCeEkjhVWEkHkLTOr3EaJUkqBIyENuqRDAXmpRAnTOoMD/B2Srj10zr7eaTgxf8QpBH3CGVHiMmrLkBq/bLb6JJwN85OGlR6hwyEmzK4OL2zE+GRVMKQIDAQAB";
		
		System.out.println("-----------privateKeyStr------------------");
		System.out.println(privateKeyStr);
		System.out.println("-----------publicKeyStr------------------");
		System.out.println(publicKeyStr);
		
		// test sign
		{
			String signStr = SecurityUtil.RsaUtil.sign(content, privateKeyStr,false);
			boolean isValid = SecurityUtil.RsaUtil.verify(content,signStr, publicKeyStr,false);
			System.out.println("-----------signStr------------------");
			System.out.println(signStr);
			System.out.println("-----------isValid------------------");
			System.out.println(isValid);
		}

		// test codec
		{
			Key privateKey = SecurityUtil.RsaUtil.genRsaPrivateKeyFromPKCS8Encoded(privateKeyStr);
			Key publicKey = SecurityUtil.RsaUtil.genRsaPublicKeyFromX509Encoded(publicKeyStr);

			// 私钥加密、公钥解密
			String encryptStr = SecurityUtil.RsaUtil.encrypt(content, privateKey);
			String decryptStr = SecurityUtil.RsaUtil.decrypt(encryptStr, publicKey);
//			Assert.assertEquals(content, decryptStr);
			System.out.println("-----------encryptStr------------------");
			System.out.println(encryptStr);
			System.out.println("-----------decryptStr------------------");
			System.out.println(decryptStr);

			// 公钥加密、私钥解密
			encryptStr = SecurityUtil.RsaUtil.encrypt(content, privateKey);
			decryptStr = SecurityUtil.RsaUtil.decrypt(encryptStr, publicKey);
//			Assert.assertEquals(content, decryptStr);
		}

	}

	/**
	 * 签名
	 */
	public static void testDigest() throws Exception {
		byte[] bytes = SecurityUtil.MessageDigestUtil.digest("test", true);
		String hexEncode = HexUtil.encode(bytes);
		System.out.println(hexEncode);

		byte[] hexDecode = HexUtil.decode(hexEncode);
		System.out.println(Base64.getEncoder().encodeToString(bytes));
		System.out.println(Base64.getEncoder().encodeToString(hexDecode));
	}

}

