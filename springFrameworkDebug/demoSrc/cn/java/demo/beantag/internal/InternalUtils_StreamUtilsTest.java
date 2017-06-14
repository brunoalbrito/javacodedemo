package cn.java.demo.beantag.internal;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

import org.springframework.util.StreamUtils;

public class InternalUtils_StreamUtilsTest {
	public static void main(String[] args) throws Exception {
		byte[] bytes = null;
		InputStream inputStream = null;
		OutputStream outputStream = null;
		String str = "";
		
		// 字节数组和流的转换
		{
			StreamUtils.copy(bytes, outputStream);
			bytes = StreamUtils.copyToByteArray(inputStream);
		}
		
		// 流和流的转换
		{
			StreamUtils.copy(inputStream, outputStream);
		}
		
		// 字符串和流的转换
		{
			str = StreamUtils.copyToString(inputStream,Charset.forName("UTF-8"));
			StreamUtils.copy(str, Charset.forName("UTF-8"), outputStream);
		}
		
	}
}
