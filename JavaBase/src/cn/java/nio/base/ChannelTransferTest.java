package cn.java.nio.base;

import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

public class ChannelTransferTest {

	/*
	 	如果两个通道中有一个是FileChannel，那你可以直接将数据从一个channel传输到另外一个channel
	 */
	public static void main(String[] args) {

	}

	/**
	 * transferFrom()方法可以将数据从源通道传输到FileChannel中
	 * @param args
	 */
	public static void testTransferFrom(String[] args) {
		try {
			RandomAccessFile fromFile = new RandomAccessFile("fromFile.txt", "rw");
			FileChannel      fromChannel = fromFile.getChannel();

			RandomAccessFile toFile = new RandomAccessFile("toFile.txt", "rw");
			FileChannel      toChannel = toFile.getChannel();

			long position = 0;
			long count = fromChannel.size();

			toChannel.transferFrom(fromChannel,position, count);
		} catch (Exception e) {
		}

	}

	/**
	 * transferTo()方法将数据从FileChannel传输到其他的channel中
	 * @param args
	 */
	public static void testTransferTo(String[] args) {
		try {
			RandomAccessFile fromFile = new RandomAccessFile("fromFile.txt", "rw");
			FileChannel      fromChannel = fromFile.getChannel();

			RandomAccessFile toFile = new RandomAccessFile("toFile.txt", "rw");
			FileChannel      toChannel = toFile.getChannel();

			long position = 0;
			long count = fromChannel.size();

			fromChannel.transferTo(position, count, toChannel);
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

}
