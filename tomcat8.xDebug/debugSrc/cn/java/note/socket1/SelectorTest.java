package cn.java.note.socket1;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;

import org.apache.tomcat.util.net.NioEndpoint.NioSocketWrapper;

public class SelectorTest {
	public static void main() {

	}

	private Selector selector;
	private volatile int keyCount = 0;

	public void test() throws IOException {
		this.selector = Selector.open();
		long selectorTimeout = 1000;
		keyCount = selector.selectNow();
		keyCount = selector.select(selectorTimeout);
		// 迭代Select键列表
		Iterator<SelectionKey> iterator = keyCount > 0 ? selector.selectedKeys().iterator() : null;
		while (iterator != null && iterator.hasNext()) {
			SelectionKey sk = iterator.next();
			NioSocketWrapper attachment = (NioSocketWrapper) sk.attachment();
			if (attachment == null) {
				iterator.remove();
			} else {
				iterator.remove();
//				processKey(sk, attachment); // !!!!处理用户的socket请求
			}
		}//while
	}
}
