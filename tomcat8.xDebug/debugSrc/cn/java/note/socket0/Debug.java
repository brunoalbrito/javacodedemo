package cn.java.note.socket0;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import org.apache.tomcat.util.net.NioEndpoint.SendfileData;

public class Debug {

	public static void main(String[] args) throws Exception {

	}
	public void test() throws Exception {
		bind();
		poller();
		acceptor();
	}

	private volatile int keyCount = 0;
	private ServerSocketChannel serverSock = null;

	public void bind() throws Exception {
		serverSock = ServerSocketChannel.open();
		ServerSocket socket = serverSock.socket(); // 阻塞接受socket
		socket.setReceiveBufferSize(1024);
		InetSocketAddress addr = new InetSocketAddress("127.0.0.1",8080);
		serverSock.socket().bind(addr,100); // 等待连接数 100
		serverSock.configureBlocking(true); //mimic APR behavior 是否是阻塞模式
		serverSock.socket().setSoTimeout(Integer.valueOf(20000));

	}

	private Selector selector;
	private long selectorTimeout = 1000;
	/**
	 * 连接处理者
	 * @throws Exception
	 */
	public void poller() throws Exception {
		if(selector==null){
			selector = Selector.open();
		}
		keyCount = selector.select(selectorTimeout);

		// 执行“事件队列”里面的事件，执行内容就是注册key
		pollerEvent();

		Iterator<SelectionKey> iterator =
				keyCount > 0 ? selector.selectedKeys().iterator() : null;
		while (iterator != null && iterator.hasNext()) {
			SelectionKey sk = iterator.next();
			NioSocketWrapper attachment = (NioSocketWrapper)sk.attachment();
			// Attachment may be null if another thread has called
			// cancelledKey()
			if (attachment == null) {
				iterator.remove();
			} else {
				iterator.remove();
				if ( sk.isValid() && attachment != null ) {
					if (sk.isReadable() || sk.isWritable() ) {

						if ( attachment.getSendfileData() != null ) {

						}else {
							int intops = sk.interestOps()& (~sk.readyOps());
							sk.interestOps(intops);
							attachment.interestOps(intops);

							boolean closeSocket = false;

							if (sk.isReadable()) { // 读

							}
							if (!closeSocket && sk.isWritable()) { // 写

							}
						}
					}
				}
			}
		}//while
	}

	private class NioSocketWrapper{
		private volatile SendfileData sendfileData = null;
		public SendfileData getSendfileData() { return this.sendfileData;}
		
		private int interestOps = 0;
		public int interestOps(int ops) { this.interestOps  = ops; return ops; }
		
		public final void write(boolean block, byte[] buf, int off, int len){

		}
		public boolean flush(boolean block) {
			return false;
		}
		public final  byte[] read(boolean block, long timeout, TimeUnit unit,ByteBuffer... dsts) {
			return null;
		}
	}
	
	/**
	 * “事件队列”里面的事件的“执行内容”
	 */
	public void pollerEvent() throws Exception {
		SocketChannel socket = null;
		socket.register(selector, SelectionKey.OP_READ,new NioSocketWrapper());
	}

	public void acceptor() throws Exception {
		while (true) {
			SocketChannel socket = null;
			socket = serverSock.accept(); // 阻塞接受socket

			socket.configureBlocking(false);
			Socket sock = socket.socket();
			setProperties(sock);

			//			ByteBuffer readBuffer = ByteBuffer.allocateDirect(readBufferSize);
			//			ByteBuffer writeBuffer = ByteBuffer.allocateDirect(writeBufferSize);

			// 轮询出一个poller，并把事件压入 poller 的“事件队列”里面
		}
	}

	public void setProperties(Socket socket) {
		//		socket.setReceiveBufferSize(rxBufSize.intValue());
		//		socket.setSendBufferSize(txBufSize.intValue());
		//		socket.setOOBInline(ooBInline.booleanValue());
		//		socket.setKeepAlive(soKeepAlive.booleanValue());
		//		socket.setPerformancePreferences(
		//				performanceConnectionTime.intValue(),
		//				performanceLatency.intValue(),
		//				performanceBandwidth.intValue());
		//		socket.setReuseAddress(soReuseAddress.booleanValue());
		//		socket.setSoLinger(soLingerOn.booleanValue(),
		//				soLingerTime.intValue());
		//		socket.setSoTimeout(soTimeout.intValue());
		//		socket.setTcpNoDelay(tcpNoDelay.booleanValue());
	}


}
