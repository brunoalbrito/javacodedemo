package cn.java.nio.pipe;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;

public class PipeTest {

	public static void read(Pipe pipe) throws IOException {
		Pipe.SourceChannel sourceChannel = pipe.source();
		ByteBuffer buf = ByteBuffer.allocate(48);

		int bytesRead = sourceChannel.read(buf);
	}
	
	public static void write(Pipe pipe) throws IOException {
		Pipe.SinkChannel sinkChannel = pipe.sink();
		String newData = "New String to write to file..." + System.currentTimeMillis();
		ByteBuffer buf = ByteBuffer.allocate(48);
		buf.clear();
		buf.put(newData.getBytes());

		buf.flip();

		while(buf.hasRemaining()) {
			sinkChannel.write(buf);
		}

	}
	
	public static void main(String[] args) {
		try {
			Pipe pipe = Pipe.open();
			read(pipe);
			write(pipe);
		} catch (Exception e) {
		}
		
	}

}
