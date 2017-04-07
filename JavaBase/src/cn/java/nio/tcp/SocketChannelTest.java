package cn.java.nio.tcp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class SocketChannelTest {
	public static void main(String[] args) {
		testBlock();
		testNonBlock();
	}
	
	public static void testBlock(){
		try {
			SocketChannel socketChannel = SocketChannel.open();
			socketChannel.connect(new InetSocketAddress("http://jenkov.com", 80));
			read(socketChannel);
			write(socketChannel);
			socketChannel.close();
		} catch (Exception e) {
		}
	}
	
	public static void testNonBlock(){
		try {
			SocketChannel socketChannel = SocketChannel.open();
			socketChannel.configureBlocking(false); // 非阻塞模式
			socketChannel.connect(new InetSocketAddress("http://jenkov.com", 80));
			while(! socketChannel.finishConnect() ){ // 非阻塞模式,调用connect()，该方法可能在连接建立之前就返回了。为了确定连接是否建立，可以调用finishConnect()的方法
				read(socketChannel);
				write(socketChannel);
				socketChannel.close();
			}
		} catch (Exception e) {
		}
	}

	public static void read(SocketChannel channel) throws IOException {
		ByteBuffer buf = ByteBuffer.allocate(48);
		int bytesRead = channel.read(buf); // 将数据从SocketChannel 读到Buffer中
	}
	
	public static void write(SocketChannel channel) throws IOException {
		String newData = "New String to write to file..." + System.currentTimeMillis();
		ByteBuffer buf = ByteBuffer.allocate(48);
		buf.clear();
		buf.put(newData.getBytes());
		buf.flip();

		while(buf.hasRemaining()) { // write()方法在尚未写出任何内容时可能就返回了。所以需要在循环中调用write()
			channel.write(buf);
		}
	}


}
