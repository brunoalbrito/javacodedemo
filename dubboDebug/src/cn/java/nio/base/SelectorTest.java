package cn.java.nio.base;

import java.net.InetSocketAddress;
import java.nio.channels.Channel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

public class SelectorTest {

	/*
	 	Selector（选择器）是Java NIO中能够检测一到多个NIO通道，并能够知晓通道是否为诸如读写事件做好准备的组件。这样，一个单独的线程可以管理多个channel，从而管理多个网络连接。

		与Selector一起使用时，Channel必须处于非阻塞模式下。这意味着不能将FileChannel与Selector一起使用，因为FileChannel不能切换到非阻塞模式。而套接字通道都可以。
		通道触发了一个事件意思是该事件已经就绪。所以，某个channel成功连接到另一个服务器称为“连接就绪”。一个server socket channel准备好接收新进入的连接称为“接收就绪”。一个有数据可读的通道可以说是“读就绪”。等待写数据的通道可以说是“写就绪”。
	 */
	public static void main(String[] args) {
		Selector selector = Selector.open();

		try {
			ServerSocketChannel channel = ServerSocketChannel.open();
			channel.socket().bind(new InetSocketAddress(9999));

			channel.configureBlocking(false);
			int interestSet = SelectionKey.OP_READ | SelectionKey.OP_WRITE;

			// register()方法会返回一个SelectionKey 对象。这个对象代表了注册到该Selector的通道
			SelectionKey selectionKey = channel.register(selector,interestSet); // 向Selector注册通道

			int interestSetTemp = selectionKey.interestOps(); // interest集合是你所选择的感兴趣的事件集合。
			int readySet = selectionKey.readyOps(); // ready 集合是通道已经准备就绪的操作的集合

			Channel  channelTemp  = selectionKey.channel();
			Selector selectorTemp = selectionKey.selector();

			// 附加的对象
			selectionKey.attach(new Object());
			Object attachedObj = selectionKey.attachment();

			while(true){
				int readyChannels = selector.select(); // 阻塞到至少有一个通道在你注册的事件上就绪了
				if(readyChannels == 0) {
					continue;
				}

				Set selectedKeys = selector.selectedKeys();
				Iterator keyIterator = selectedKeys.iterator();
				while(keyIterator.hasNext()) { // 遍历已选择键集中的每个键，并检测各个键所对应的通道的就绪事件
					SelectionKey key = (SelectionKey) keyIterator.next();
					if(key.isAcceptable()) {
						// a connection was accepted by a ServerSocketChannel.
					} else if (key.isConnectable()) {
						// a connection was established with a remote server.
					} else if (key.isReadable()) {
						// a channel is ready for reading
					} else if (key.isWritable()) {
						// a channel is ready for writing
					}
					/*
					 	每次迭代末尾的keyIterator.remove()调用。Selector不会自己从已选择键集中移除SelectionKey实例。
					 	必须在处理完通道时自己移除。下次该通道变成就绪时，Selector会再次将其放入已选择键集中。
					 */
					keyIterator.remove();
				}
			}



		} catch (Exception e) {
		}

	}

}
