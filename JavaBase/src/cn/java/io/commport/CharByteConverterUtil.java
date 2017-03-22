package cn.java.io.commport;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.util.Arrays;

public class CharByteConverterUtil {
	
	public void test(){
		string2ByteArray("Hello","UTF8");
	}

	public static int char2Integer(char c ){
		return Integer.valueOf(c);
	}
	
	public static char Integer2Char(int i){
		return (char)i;
	}
	
	public static char byte2Char(byte b,String charsetName){
		byte[] bx = {b};
		char[] c = byteArray2CharArray(bx,charsetName);
		return c[0];
	}
	
	public static byte char2Byte(char c,String charsetName){
		char[] cx = {c};
		byte[] b = charArray2ByteArray(cx, charsetName);
		return b[0];
	}
	
	
	/**
	 * 字符串转成字节数组
	 */
	public static byte[] string2ByteArray(String str,String charsetName){
		char[] charArray = str.toCharArray();
		return charArray2ByteArray(charArray,charsetName);
	}

	/**
	 * 字节数组转成字符数组
	 */
	public static char[] byteArray2CharArray(byte[] byteArray,String charsetName){
		Charset charset = Charset.forName(charsetName);
		CharsetDecoder decoder = charset.newDecoder();
		CharsetDecoder charsetDecoder = decoder.reset();
		
		int len = (int) (byteArray.length * charsetDecoder.maxCharsPerByte());
		char[] charArray = new char[len];
		if (len == 0)
			return charArray;
		
		ByteBuffer byteBuffer = ByteBuffer.wrap(byteArray); // 要转换的数据
		CharBuffer charBuffer = CharBuffer.wrap(charArray); // 要存放的容器
		
		CoderResult coderResult = charsetDecoder.decode(byteBuffer, charBuffer,true);
		if (!coderResult.isUnderflow())
			throw new IllegalArgumentException(coderResult.toString());
		coderResult = charsetDecoder.flush(charBuffer);
		if (!coderResult.isUnderflow())
			throw new IllegalArgumentException(coderResult.toString());
		
		if (charBuffer.position() == charArray.length)  //
			return charArray;
		else
			return Arrays.copyOf(charArray, charBuffer.position()); // 拷贝进charArray 
	}
	
	/**
	 * 字符数组转成字节数组
	 */
	public static byte[] charArray2ByteArray(char[] charArray,String charsetName){
		Charset charset = Charset.forName(charsetName);
		CharsetEncoder encoder = charset.newEncoder();
		CharsetEncoder charsetEncoder = encoder.reset();
		int len = (int)(charArray.length * charsetEncoder.maxBytesPerChar());
		byte[] byteArray = new byte[len];
		if (len == 0)
			return byteArray;

		ByteBuffer byteBuffer = ByteBuffer.wrap(byteArray); // 要存放的容器
		CharBuffer charBuffer = CharBuffer.wrap(charArray); // 要转换的数据
		CoderResult coderResult = charsetEncoder.encode(charBuffer, byteBuffer, true); // 转码charBuffer(charArray)放入byteBuffer(byteArray)
		if (!coderResult.isUnderflow())
			throw new IllegalArgumentException(coderResult.toString());
		coderResult = charsetEncoder.flush(byteBuffer);
		if (!coderResult.isUnderflow())
			throw new IllegalArgumentException(coderResult.toString());
		if (byteBuffer.position() == byteArray.length)  // defensive copy?
			return byteArray;
		else
			return Arrays.copyOf(byteArray, byteBuffer.position()); // 拷贝进byteArray 
	}

	
}