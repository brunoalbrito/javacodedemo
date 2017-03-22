package cn.java.codec.base64;

public class Test {

	public static void main(String[] args) {
		String str = "hello world";
		System.out.println("test Str : " + str);
		
		byte[] binaryData = str.getBytes();
		String encodeStr = Base64.encode(binaryData);
		System.out.println("Base64.encode Result : " + encodeStr);
		
		byte[] decodeBytes = Base64.decode(encodeStr);
		String decodeStr = new String(decodeBytes);
		System.out.println("Base64.decode Result : " + decodeStr);
	}

}
