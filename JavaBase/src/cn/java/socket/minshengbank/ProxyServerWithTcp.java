package cn.java.socket.minshengbank;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * 代理服务器
 * 
 * @author zhouzhian
 *
 */
public class ProxyServerWithTcp {

	public static void main(String[] args) {
		Queue queue = new ConcurrentLinkedQueue<byte[]>();
		// Queue queue = new ProxyDataQueue();
		int heartbeatsSeconds = 10;
//		BankSocketClient.start("127.0.0.1", 9999, queue, heartbeatsSeconds);
		ProxyServerSocket.start("127.0.0.1", 9998, queue);
	}

	/**
	 * 连接银行的客户端
	 * 
	 * @author zhouzhian
	 *
	 */
	private static class BankSocketClient {
		public static void start(String serverHost, int serverPort, Queue queue, int heartbeatsSeconds) {
			new Thread(new BankSocketClientWorker(serverHost, serverPort, queue, heartbeatsSeconds)).start();
		}

		private static class BankSocketClientWorker implements Runnable {
			private Queue queue = null;
			private int heartbeatsSeconds = -1;
			private String serverHost = "127.0.0.1";
			private int serverPort = 9108;

			public BankSocketClientWorker(String serverHost, int serverPort, Queue queue, int heartbeatsSeconds) {
				this.serverHost = serverHost;
				this.serverPort = serverPort;
				this.queue = queue;
				this.heartbeatsSeconds = heartbeatsSeconds;
			}

			@Override
			public void run() {
				Socket socket = null;
				try {
					System.out.println("............ i am bank client , connect bank success ............");
					socket = new Socket(serverHost, serverPort);
					socket.setSoTimeout(0); // 0 表示永不超时
					new Thread(new InputStream4BankSocketClientWorker(socket)).start();
					new Thread(new OutputStream4BankSocketClientWorker(socket, queue, heartbeatsSeconds)).start();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			/**
			 * 从银行获取数据
			 * 
			 * @author zhouzhian
			 */
			private static class InputStream4BankSocketClientWorker implements Runnable {
				private DataInputStream inputStream = null;
				private Socket socket = null;

				public InputStream4BankSocketClientWorker(Socket socket) throws IOException {
					this.socket = socket;
					this.inputStream = new DataInputStream(socket.getInputStream());
				}

				@Override
				public void run() {
					try {
						byte[] byteBuffer = null;
						int bufferLen = -1;
						while (true) {
							byteBuffer = new byte[1024];
							bufferLen = inputStream.read(byteBuffer);
							if (bufferLen == -1) { // 长连接，休眠一下进行读取数据
								try {
									Thread.sleep(100);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								continue;
							} else {
								// http请求进行回调
								System.out.println(byteBuffer);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						try {
							if (inputStream != null) {
								inputStream.close();
							}
							if (socket != null) {
								socket.close();
							}
						} catch (IOException e) {
							e.printStackTrace();
						}

					}

				}
			}

			/**
			 * 发送数据给银行
			 * 
			 * @author zhouzhian
			 */
			private static class OutputStream4BankSocketClientWorker implements Runnable {
				private DataOutputStream outputStream = null;
				private Queue queue = null;
				private int heartbeatsSeconds = -1;
				private Socket socket = null;
				private AtomicInteger atomicInteger = new AtomicInteger(0);
				public OutputStream4BankSocketClientWorker(Socket socket, Queue queue, int heartbeatsSeconds)
						throws IOException {
					this.socket = socket;
					this.outputStream = new DataOutputStream(socket.getOutputStream());
					this.queue = queue;
					this.heartbeatsSeconds = heartbeatsSeconds;
				}

				public void run() {
					
					// 发送心跳
					if (heartbeatsSeconds > 0) {
						final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
						Runnable runnable = new Runnable() {
							public void run() {
								try {
									if((atomicInteger.get()*2)>heartbeatsSeconds){ // 超过多少秒，发送一次心跳
										synchronized (outputStream) {
											atomicInteger.set(0); // 发送了，重置计数器
											outputStream.write("00000000".getBytes());
										}
									}
									else{
										atomicInteger.getAndAdd(1); //  没超过，计数器 + 1
									}
								} catch (IOException e) {
									e.printStackTrace();
									if (scheduledExecutorService != null) { // 关闭定时器
										scheduledExecutorService.shutdown();
									}
								}
							}
						};
						// 第二个参数为首次执行的延时时间，第三个参数为定时执行的间隔时间
						scheduledExecutorService.scheduleAtFixedRate(runnable, 2, 2, TimeUnit.SECONDS); // 没两秒执行一次
					}

					// 写出数据
					byte[] byteBuffer = null;
					try {
						while (true) {
							byteBuffer = (byte[]) queue.poll(); // 从队列中获取数据
							if (byteBuffer != null) {
								synchronized (outputStream) {
									atomicInteger.set(0); // 发送了，重置计数器
									outputStream.write(byteBuffer);
								}
							} else {
								Thread.sleep(100);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						try {
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

		}

	}

	/**
	 * 接口web端的连接请求
	 * 
	 * @author zhouzhian
	 *
	 */
	private static class ProxyServerSocket {
		public static void start(String listenHost, int listenPort, Queue queue) {
			new Thread(new ProxyServerSocket.ProxyServerSocketAccepter(listenHost, listenPort, queue)).start();
		}

		private static class ProxyServerSocketAccepter implements Runnable {
			private Queue queue = null;
			private String listenHost = "127.0.0.1";
			private int listenPort = 8080;

			public ProxyServerSocketAccepter(String listenHost, int listenPort, Queue queue) {
				this.listenHost = listenHost;
				this.listenPort = listenPort;
				this.queue = queue;
			}

			@Override
			public void run() {
				ServerSocket server; // 定义ServerSocket变量
				try {
					System.out.println("............ i am proxy , daemon start success ............");
					server = new ServerSocket(listenPort, 50, InetAddress.getByName(listenHost));
					while (true) {
						Socket client = server.accept(); // 等待客户端连接
						new Thread(new ProxyServerSocketWorker(client, queue)).start(); // 有客户端连接进来，则开启SocketServer线程，并处理这个TCP连接
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		/**
		 * 处理web端的请求
		 * 
		 * @author zhouzhian
		 */
		private static class ProxyServerSocketWorker implements Runnable {
			private Socket client = null;
			private Queue queue = null;

			public ProxyServerSocketWorker(Socket client, Queue queue) {
				this.client = client;
				this.queue = queue;
			}

			/**
			 * 客户端断言
			 */
			public void assertClient() {
				InetSocketAddress socketAddress = (InetSocketAddress) client.getRemoteSocketAddress();
				StringBuilder stringBuilder = new StringBuilder();
				stringBuilder.append("remote hostname : ");
				stringBuilder.append(socketAddress.getHostName());
				stringBuilder.append("\nremote port : ");
				stringBuilder.append(socketAddress.getPort());
				stringBuilder.append("\nremote address : ");
				stringBuilder.append(socketAddress.getAddress().getHostAddress());
				stringBuilder.append("\nremote hostname : ");
				stringBuilder.append(socketAddress.getAddress().getHostName());
				System.out.println(stringBuilder.toString());
				if(!socketAddress.getAddress().getHostAddress().equals("127.0.0.1")){
					throw new RuntimeException("ip地址不被允许");
				}
			}
			
			@Override
			public void run() {
				DataInputStream inputStream = null;
				DataOutputStream outputStream = null;
				try {
					assertClient();
					client.setSoTimeout(30 * 1000); // 设置超时
					inputStream = new DataInputStream(client.getInputStream());
					outputStream = new DataOutputStream(client.getOutputStream());
					byte[] byteBuffer = new byte[1024];
					int bufferLen = -1;
					while (true) {
						bufferLen = inputStream.read(byteBuffer);
						if (bufferLen == -1) { // 短连接，立即关闭连接
							break;
						} else {
							outputStream.write("[i am proxy, i accept the data] : ".getBytes());
							outputStream.write(byteBuffer);
							queue.add(byteBuffer); // 把数据压入队列
							break;
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
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

}
