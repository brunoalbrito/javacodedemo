package cn.java.socket.minshengbank;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 代理的客户端
 * @author zhouzhian
 *
 */
public class ProxyClientWithTcp {

	public static void main(String[] args) {
		for (int i = 0; i < 1; i++) {
			int sleepSeconds = ThreadLocalRandom.current().nextInt(1, 5)*1000;
			try {
				Thread.sleep(sleepSeconds);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			sendOneData(sleepSeconds);
		}
		System.out.println("-------ProxyClientWithTcp end--------------");
	}
	public static void sendOneData(int sleepSeconds) {
		Socket socket = null;
		DataInputStream inputStream = null;
		DataOutputStream outputStream = null;
		try {
			socket =  new Socket("127.0.0.1", 9998);
			socket.setSoTimeout(30 * 1000); // 设置超时
			inputStream = new DataInputStream(socket.getInputStream());
			outputStream = new DataOutputStream(socket.getOutputStream());
			StringBuilder  stringBuilder = new StringBuilder();
			stringBuilder.append("i am proxy client,this is proxy-client data ");
			stringBuilder.append(System.currentTimeMillis());
			stringBuilder.append(" , sleep=");
			stringBuilder.append(sleepSeconds);
			outputStream.write(stringBuilder.toString().getBytes());
			System.out.println(ConvertUtil.inputStreamToString(inputStream));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				 if (inputStream != null) {
					 inputStream.close();
	             }
	             if (outputStream != null) {
	            	 outputStream.close();
	             }
	             if (socket != null) {
	                 socket.close();
	             }
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	
	
}
