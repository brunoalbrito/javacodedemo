package cn.java.socket.minshengbank;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 银行端
 * @author zhouzhian
 *
 */
public class BankServerWithTcp {

	public static void main(String[] args) {
		new Thread(new BankServerSocketAccepter("127.0.0.1",9999)).start();
	}

	/**
	 * 接受代理的连接
	 * @author zhouzhian
	 *
	 */
	private static class BankServerSocketAccepter implements Runnable {
		private String listenHost = "127.0.0.1";
		private int listenPort = 9108;
		public BankServerSocketAccepter(String listenHost,int listenPort){
			this.listenHost = listenHost;
			this.listenPort = listenPort;
		}
		
		@Override
		public void run() {
			ServerSocket server;// 定义ServerSocket变量
			try {
				System.out.println("............ i am bank , daemon start success ............");
//				server = new ServerSocket(listenPort,50,InetAddress.getByName(listenHost)); 
				server = new ServerSocket(listenPort); 
				while (true) {
					Socket socket = server.accept();// 等待客户端连接
					new Thread(new BankServerSocketWorker(socket)).start(); // 有客户端连接进来，则开启SocketServer线程，并处理这个TCP连接
				}
			} catch (IOException e) {
				e.printStackTrace();
			}			
		}
	}
	
	/**
	 * 处理代理的任务
	 * @author zhouzhian
	 *
	 */
	private static class BankServerSocketWorker implements Runnable {
		private Socket client = null;

		public BankServerSocketWorker(Socket client) {
			this.client = client;
		}

		@Override
		public void run() {
			DataInputStream inputStream = null;
			DataOutputStream outputStream = null;
			try {
				client.setSoTimeout(0); // 0 表示永不超时
				inputStream = new DataInputStream(client.getInputStream());
				outputStream = new DataOutputStream(client.getOutputStream());
				
				byte[] byteBuffer = null;
				int bufferLen = -1;
				while (true) {
					byteBuffer = new byte[1024];
					bufferLen = inputStream.read(byteBuffer); 
					if (bufferLen == -1){ // 长连接，休眠一下进行读取数据
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						continue;
					}
					System.out.println("[i am bank ,this is proxy data] : "+ ConvertUtil.bytesToString(byteBuffer));
				}
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
		             if (client != null) {
		            	 client.close();
		             }
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				
			}

		}
	}

}
