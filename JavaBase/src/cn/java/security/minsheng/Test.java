package cn.java.security.minsheng;

public class Test {
	public static void main(String[] args) {
		try {
			// 密钥
			byte[] key = "1234567887654321".getBytes();
			// 明文文件路径
			String plainFilePath = "e:/req_plain.txt";
			// 密文文件路径
			String secretFilePath = "e:/req_secret.txt";
			// 加密
			AesUtil.encodeAESFile(key, plainFilePath, secretFilePath);

			// 解密
			plainFilePath = "e:/res_plain.txt";
			secretFilePath = "e:/res_secret.txt";
			AesUtil.decodeAESFile(key, plainFilePath, secretFilePath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
