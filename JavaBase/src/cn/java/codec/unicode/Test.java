package cn.java.codec.unicode;

public class Test {

	public static void main(String[] args) throws Exception {
		String str = "test中文";
		String unicodeEncode = UnicodeUtil.encode(str);
		System.out.println("UnicodeUtil.encode Result : " + unicodeEncode);
		
		str = UnicodeUtil.decode(unicodeEncode);
		System.out.println("UnicodeUtil.decode Result : " + str);
	}
}

