package cn.java.security.minsheng;

public class Test {
	public static void main(String[] args) {
		try {
			// 密钥
			byte[] key = "1234567887654321".getBytes();
			
			
			String plainFilePath = "e:/req_plain.txt"; // 存放 - 要加密的内容
			String secretFilePath = "e:/req_secret.txt"; // 存放 - 加密后的内容
			
			// 加密
			AesUtil.encodeAESFile(key, plainFilePath, secretFilePath);

			// 解密
			plainFilePath = "e:/res_plain.txt"; // 存放 - 解密后的内容
			secretFilePath = "e:/res_secret.txt"; // 存放 - 要解密的内容
			AesUtil.decodeAESFile(key, plainFilePath, secretFilePath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
