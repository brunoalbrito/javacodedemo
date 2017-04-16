package cn.java.note.nio.selector;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import org.apache.tomcat.util.net.NioEndpoint.SendfileData;

public class SocketSelectorTest {

	public static void main(String[] args) throws Exception {
		Selector selector  = Selector.open();
		
		// 接受连接
		ServerSocketChannel serverSock = ServerSocketChannel.open();
		ServerSocket socket = serverSock.socket(); // 阻塞接受socket
		socket.setReceiveBufferSize(1024);
		InetSocketAddress addr = new InetSocketAddress("127.0.0.1",8080);
		serverSock.socket().bind(addr,100); // 等待连接数 100
		serverSock.configureBlocking(true); //mimic APR behavior 是否是阻塞模式
		serverSock.socket().setSoTimeout(Integer.valueOf(20000));
		
		// 接受线程，接受连接
		SocketChannel socketChannel = null;
		socketChannel = serverSock.accept(); // 阻塞接受socket
		socketChannel.configureBlocking(false);
		Socket sock = socketChannel.socket();
		sock.setReceiveBufferSize(1024);
		sock.setSendBufferSize(1024);
		sock.setSoTimeout(3600);
		sock.setTcpNoDelay(true);
		
		// 把“处理连接任务”放入 Selector
		socketChannel.register(selector, SelectionKey.OP_READ,new NioSocketWrapper());
		
		// 处理线程  ， 从Selector中检查是否存在“待处理的连接任务”
		int keyCount = selector.select(3600); 
		Iterator<SelectionKey> iterator = keyCount > 0 ? selector.selectedKeys().iterator() : null;
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
	
	private static class NioSocketWrapper{
		private volatile SendfileData sendfileData = null;
		public SendfileData getSendfileData() { return this.sendfileData;}
		
		private int interestOps = 0;
		public int interestOps(int ops) { this.interestOps  = ops; return ops; }
		
	}

}
