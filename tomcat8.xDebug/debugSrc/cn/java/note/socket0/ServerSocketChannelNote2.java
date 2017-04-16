package cn.java.note.socket0;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import org.apache.tomcat.util.collections.SynchronizedStack;
import org.apache.tomcat.util.net.NioChannel;
import org.apache.tomcat.util.net.NioEndpoint.PollerEvent;
import org.apache.tomcat.util.net.SocketBufferHandler;
import org.apache.tomcat.util.net.SocketProcessorBase;

public class ServerSocketChannelNote2 {
	protected SynchronizedStack<SocketProcessorBase<S>> processorCache;
	private SynchronizedStack<PollerEvent> eventCache;
	private SynchronizedStack<NioChannel> nioChannels;
	public void test1() throws IOException {
		ServerSocketChannel serverSock = ServerSocketChannel.open();
		InetSocketAddress addr = (false ? new InetSocketAddress(InetAddress.getByName("127.0.0.1"),8080):new InetSocketAddress(8080));
		serverSock.socket().bind(addr,100);
		serverSock.configureBlocking(true);

		// 同步栈
//		processorCache = new SynchronizedStack<>(SynchronizedStack.DEFAULT_SIZE,
//				socketProperties.getProcessorCache());
//		eventCache = new SynchronizedStack<>(SynchronizedStack.DEFAULT_SIZE,
//				socketProperties.getEventCache());
//		nioChannels = new SynchronizedStack<>(SynchronizedStack.DEFAULT_SIZE,
//				socketProperties.getBufferPool());


		SocketChannel socket = null;
		socket = serverSock.accept(); // 接受任务
		socket.configureBlocking(false);
		Socket sock = socket.socket();

		NioChannel channel = null;
		SocketBufferHandler bufhandler = new SocketBufferHandler(8192,8192,false);
		channel = new NioChannel(socket, bufhandler);

//		 getPoller0().register(channel);//选个一个工作者，并对任务进行委托
		
		
		//..如果出现异常，就进行关闭
		//		socket.socket().close();
		//		socket.close();
	}

	public static void main(String[] args) {

	}


}
