package cn.java.nio.udp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class DatagramChannelTest {

	public static void main(String[] args) {
		server();
		client();
	}

	public static void client() {
		try {
			DatagramChannel channel = DatagramChannel.open();
			channel.connect(new InetSocketAddress("jenkov.com", 80));
			ByteBuffer buf = ByteBuffer.allocate(48);
			int bytesRead = channel.read(buf);

			buf.flip();
			int bytesWritten = channel.write(buf);	
		} catch (Exception e) {
		}
	}
	
	public static void server() {
		try {
			DatagramChannel channel = DatagramChannel.open();
			channel.socket().bind(new InetSocketAddress(9999)); // 在UDP端口9999上接收数据包
			write(channel);
			read(channel);
		} catch (Exception e) {
		}

	}

	/**
	 * 发送数据
	 * @param channel
	 * @throws IOException
	 */
	public static void write(DatagramChannel channel) throws IOException {
		String newData = "New String to write to file..." + System.currentTimeMillis();

		ByteBuffer buf = ByteBuffer.allocate(48);
		buf.clear();
		buf.put(newData.getBytes());
		buf.flip();

		int bytesSent = channel.send(buf, new InetSocketAddress("jenkov.com", 80));
	}

	/**
	 * 接收数据
	 * @param channel
	 * @throws IOException
	 */
	public static void read(DatagramChannel channel) throws IOException {
		ByteBuffer buf = ByteBuffer.allocate(48);
		buf.clear();
		channel.receive(buf);
	}
}
