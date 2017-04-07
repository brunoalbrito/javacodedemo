package cn.java.socket.minshengbank;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;

public class ConvertUtil {

	/**
	 * 字节数组转成字符串
	 * @param bytes
	 * @return
	 */
	public static String bytesToString(byte[] bytes) { 
		return new String(bytes);
	}

	/**
	 * int 转成  bytes
	 * @param a
	 * @return
	 */
	public static byte[] intToBytes(int a) {  
		return new byte[] {  
				(byte) ((a >> 24) & 0xFF),  
				(byte) ((a >> 16) & 0xFF),     
				(byte) ((a >> 8) & 0xFF),     
				(byte) (a & 0xFF)  
		};  
	}  
	
	/**
	 * bytes 转成 int
	 * @param b
	 * @return
	 */
	public static int bytesToInt(byte[] b) {  
		return   b[3] & 0xFF |  
				(b[2] & 0xFF) << 8 |  
				(b[1] & 0xFF) << 16 |  
				(b[0] & 0xFF) << 24;  
	}  

	/**
	 * long 转成 bytes
	 * @param x
	 * @return
	 */
	public static byte[] longToBytes(long x) {  
		ByteBuffer buffer = ByteBuffer.allocate(8);   
		buffer.putLong(0, x);  
		return buffer.array();  
	}  

	/**
	 * bytes 转成long
	 * @param bytes
	 * @return
	 */
	public static long bytesToLong(byte[] bytes) {  
		ByteBuffer buffer = ByteBuffer.allocate(8);   
		buffer.put(bytes, 0, bytes.length);  
		buffer.flip();//need flip   
		return buffer.getLong();  
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
