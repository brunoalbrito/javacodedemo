package cn.java.codec.string;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class StringUtil {
	
	/**
	 * 字节数组转成字符串
	 * @param bytes
	 * @return
	 */
	public static String byteArrayToString(byte[] bytes) { 
		return new String(bytes);
	}
	
	/**
	 * 字符串转成字节数组
	 * @param bytes
	 * @return
	 */
	public static byte[] stringToByteArray(String str) { 
		return str.getBytes();
	}
	
	/**
	 * 字符串转成输入流
	 * @param str
	 * @return
	 */
	public static InputStream stringToInputStream(String str) { 
		return new ByteArrayInputStream(str.getBytes());
	}
	
	/**
	 * 输入流转成字符串
	 * @param inputStream
	 * @return
	 */
	public static String inputStreamToString(InputStream inputStream) { 
		/*  
		 * To convert the InputStream to String we use the BufferedReader.readLine()  
		 * method. We iterate until the BufferedReader return null which means  
		 * there's no more data to read. Each line will appended to a StringBuilder  
		 * and returned as String.  
		 */     
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));      
		StringBuilder sb = new StringBuilder();      

		String line = null;      
		try {      
			while ((line = reader.readLine()) != null) {      
				sb.append(line);      
			}      
		} catch (IOException e) {      
			e.printStackTrace();      
		}  

		return sb.toString();      
	}

}
