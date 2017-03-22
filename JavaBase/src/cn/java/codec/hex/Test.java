package cn.java.codec.hex;

public class Test {

	public static void main(String[] args) {
		String str = "test";
		System.out.println("test string : "+str);

		String hexEncode = HexUtil.encode(str.getBytes());
		System.out.println("HexUtil.encode Result : "+hexEncode);

		byte[] bytes = HexUtil.decode(hexEncode);
		System.out.println("HexUtil.decode Result : "+new String(bytes));
		
	}

}

