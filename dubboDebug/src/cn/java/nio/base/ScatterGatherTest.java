package cn.java.nio.base;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class ScatterGatherTest {

	/*
	 	分散（scatter）从Channel中读取是指在读操作时将读取的数据写入多个buffer中。因此，Channel将从Channel中读取的数据“分散（scatter）”到多个Buffer中。
		聚集（gather）写入Channel是指在写操作时将多个buffer的数据写入同一个Channel，因此，Channel 将多个Buffer中的数据“聚集（gather）”后发送到Channel。
	 */
	public static void main(String[] args) {
		testGather();
		testScatter();
	}
	
	/*
	 	Gathering Writes是指数据从多个buffer写入到同一个channel。
	 	write()方法会按照buffer在数组中的顺序，将数据写入到channel，注意只有position和limit之间的数据才会被写入。
	 */
	public static void testGather() {
		try {
			RandomAccessFile aFile = new RandomAccessFile("data/nio-data.txt", "rw");
			FileChannel channel = aFile.getChannel();
			
			ByteBuffer header = ByteBuffer.allocate(128);
			ByteBuffer body   = ByteBuffer.allocate(1024);

			//write data into buffers

			ByteBuffer[] bufferArray = { header, body };

			channel.write(bufferArray);	
			aFile.close();
		} catch (Exception e) {
		}
		
	}
	
	/*
	 	Scattering Reads是指数据从一个channel读取到多个buffer中。
	 	
	 	read()方法按照buffer在数组中的顺序将从channel中读取的数据写入到buffer，当一个buffer被写满后，channel紧接着向另一个buffer中写。
	 	Scattering Reads在移动下一个buffer前，必须填满当前的buffer，这也意味着它不适用于动态消息(译者注：消息大小不固定)。
	 */
	public static void testScatter() {
		try {
			RandomAccessFile aFile = new RandomAccessFile("data/nio-data.txt", "rw");
			FileChannel channel = aFile.getChannel();
			
			ByteBuffer header = ByteBuffer.allocate(128);
			ByteBuffer body   = ByteBuffer.allocate(1024);

			ByteBuffer[] bufferArray = { header, body };
			channel.read(bufferArray);
			aFile.close();
		} catch (Exception e) {
		}
	}


}
