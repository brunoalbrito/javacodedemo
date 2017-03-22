package cn.java.nio.base;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class BufferTest {

	/*
	 	http://ifeve.com/buffers/
	 	
	 	Java NIO中的Buffer用于和NIO通道进行交互。如你所知，数据是从通道读入缓冲区，从缓冲区写入到通道中的。
		缓冲区本质上是一块可以写入数据，然后可以从中读取数据的内存。这块内存被包装成NIO Buffer对象，并提供了一组方法，用来方便的访问该块内存。
		当向buffer写入数据时，buffer会记录下写了多少数据。一旦要读取数据，需要通过flip()方法将Buffer从写模式切换到读模式。在读模式下，可以读取之前写入到buffer的所有数据。
		一旦读完了所有的数据，就需要清空缓冲区，让它可以再次被写入。有两种方式能清空缓冲区：调用clear()或compact()方法。clear()方法会清空整个缓冲区。compact()方法只会清除已经读过的数据。任何未读的数据都被移到缓冲区的起始处，新写入的数据将放到缓冲区未读数据的后面。
	 	******
	 	Buffer.rewind()将position设回0，所以你可以重读Buffer中的所有数据
		 一旦读完Buffer中的数据，需要让Buffer准备好再次被写入。可以通过clear()或compact()方法来完成。
		 通过调用Buffer.mark()方法，可以标记Buffer中的一个特定position。之后可以通过调用Buffer.reset()方法恢复到这个position。
	 */
	public static void main(String[] args)  {
		try {
			RandomAccessFile aFile = new RandomAccessFile("data/nio-data.txt", "rw");
			FileChannel inChannel = aFile.getChannel();

			ByteBuffer buf = ByteBuffer.allocate(48); // 创建缓冲区

			int bytesRead = inChannel.read(buf); // 读取数据放入缓冲区
			while (bytesRead != -1) {

				System.out.println("Read " + bytesRead);
				buf.flip(); // Buffer切换到读模式

				while(buf.hasRemaining()){
					System.out.print((char) buf.get()); // 读取一个字节
				}

				buf.clear(); // 清空缓冲区
				bytesRead = inChannel.read(buf);
			}
			aFile.close();
		} catch (Exception e) {
		}
		
	}
	public static void test()  {
		try {
			RandomAccessFile aFile = new RandomAccessFile("data/nio-data.txt", "rw");
			FileChannel inChannel = aFile.getChannel();
			ByteBuffer buf = ByteBuffer.allocate(48); // 创建缓冲区
			
			// 向Buffer中写数据
			int bytesRead = inChannel.read(buf); // 从Channel写到Buffer
			buf.put((byte) 127); // 通过put方法写Buffer
			
			// 从Buffer中读取数据
			int bytesWritten = inChannel.write(buf); // 从Buffer读取数据到Channel
			byte aByte = buf.get(); // 使用get()方法从Buffer中读取数据
			
			/*
				 
			 */
			
		} catch (Exception e) {
		}
		
	}

}
