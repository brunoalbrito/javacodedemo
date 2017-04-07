package cn.java.nio.tcp;

import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class ServerSocketChannelTest {

	public static void main(String[] args) {

	}
	
	public static void testBlock() {
		try {
			ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
			serverSocketChannel.socket().bind(new InetSocketAddress(9999));
			while(true){
				SocketChannel socketChannel = serverSocketChannel.accept();
				// do something with socketChannel...
				serverSocketChannel.close();
			}
		} catch (Exception e) {
		}
	}
	
	public static void testNonBlock() {
		try {
			ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
			serverSocketChannel.socket().bind(new InetSocketAddress(9999));
			serverSocketChannel.configureBlocking(false); // 非阻塞模式
			while(true){
				SocketChannel socketChannel = serverSocketChannel.accept();
				if(socketChannel != null){ // 在非阻塞模式下，accept() 方法会立刻返回，如果还没有新进来的连接,返回的将是null。 因此，需要检查返回的SocketChannel是否是null.
					// do something with socketChannel...
					serverSocketChannel.close();
				}
			}
		} catch (Exception e) {
		}
	}
}
