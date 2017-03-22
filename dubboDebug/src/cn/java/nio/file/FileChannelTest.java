package cn.java.nio.file;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileChannelTest {

	public static void main(String[] args) {
		try {
			RandomAccessFile aFile = new RandomAccessFile("data/nio-data.txt", "rw");
			FileChannel channel = aFile.getChannel();
			long fileSize = channel.size();
			channel.truncate(1024); // 截取文件时，文件将中指定长度后面的部分将被删除
			read(channel);
			write(channel);
			channel.force(true); // 将通道里尚未写入磁盘的数据强制写到磁盘上
			channel.close();
		} catch (Exception e) {
		}
	}

	public static void read(FileChannel channel) throws IOException {
		ByteBuffer buf = ByteBuffer.allocate(48);
		int bytesRead = channel.read(buf);
	}

	public static void write(FileChannel channel) throws IOException {
		// 向FileChannel写数据
		String newData = "New String to write to file..." + System.currentTimeMillis();
		ByteBuffer buf = ByteBuffer.allocate(48);
		buf.clear();
		buf.put(newData.getBytes());

		buf.flip();

		while(buf.hasRemaining()) { // 无法保证write()方法一次能向FileChannel写入多少字节，因此需要重复调用write()方法
			channel.write(buf);
		}
	}

}
