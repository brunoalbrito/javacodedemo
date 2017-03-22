package cn.java.security;


import java.security.Key;
import java.util.Base64;
import org.junit.Assert;
import cn.java.codec.hex.HexUtil;
import cn.java.security.SecurityUtil.RsaUtil.RsaKeyPair;


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
	public static void testAes() throws Exception {
		String content = "testAes";
		String secretKeyStr = SecurityUtil.AesUtil.generaterKey();
		System.out.println("-----------secretKeyStr------------------");
		System.out.println(secretKeyStr);
		String encryptStr = SecurityUtil.AesUtil.encrypt(content, secretKeyStr);
		String decryptStr = SecurityUtil.AesUtil.decrypt(encryptStr, secretKeyStr);
		System.out.println("-----------encryptStr------------------");
		System.out.println(encryptStr);
		System.out.println("-----------decryptStr------------------");
		System.out.println(decryptStr);
	}
	
	/**
	 * 非对称加密算法
	 * @throws Exception
	 */
	public static void testRsa() throws Exception {

		String content = "testRsa";
		// 生成秘钥对
		RsaKeyPair mRsaKeyPair = SecurityUtil.RsaUtil.generaterKeyPair();
		String privateKeyStr = mRsaKeyPair.getPrivateKey();
		String publicKeyStr = mRsaKeyPair.getPublicKey();
		System.out.println("-----------privateKeyStr------------------");
		System.out.println(privateKeyStr);
		System.out.println("-----------publicKeyStr------------------");
		System.out.println(publicKeyStr);
		
		// test sign
		{
			String signStr = SecurityUtil.RsaUtil.sign(content, privateKeyStr,true);
			boolean isValid = SecurityUtil.RsaUtil.verify(content,signStr, publicKeyStr,true);
			System.out.println("-----------signStr------------------");
			System.out.println(signStr);
			System.out.println("-----------isValid------------------");
			System.out.println(isValid);
		}

		// test codec
		{
			Key privateKey = SecurityUtil.RsaUtil.getPrivateKey(privateKeyStr);
			Key publicKey = SecurityUtil.RsaUtil.getPublicKey(publicKeyStr);

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
			Assert.assertEquals(content, decryptStr);
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

