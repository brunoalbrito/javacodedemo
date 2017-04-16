package cn.java.note.nio.selector.mock;

import java.io.IOException;
import java.net.ProtocolFamily;
import java.nio.channels.DatagramChannel;
import java.nio.channels.Pipe;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.AbstractSelectableChannel;
import java.nio.channels.spi.AbstractSelector;
import java.nio.channels.spi.SelectorProvider;

/**
 * 自己的实现类
 * @author Administrator
 *
 */
public class SelectableChannelImpl extends AbstractSelectableChannel {


	protected SelectableChannelImpl(SelectorProvider provider) {
		super(provider);
	}

	@Override
	protected void implCloseSelectableChannel() throws IOException {

	}

	@Override
	protected void implConfigureBlocking(boolean block) throws IOException {

	}

	@Override
	public int validOps() {
		return 0;
	}

	public static class SelectorProviderImpl extends SelectorProvider{

		@Override
		public DatagramChannel openDatagramChannel() throws IOException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public DatagramChannel openDatagramChannel(ProtocolFamily family) throws IOException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Pipe openPipe() throws IOException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public AbstractSelector openSelector() throws IOException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public ServerSocketChannel openServerSocketChannel() throws IOException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public SocketChannel openSocketChannel() throws IOException {
			// TODO Auto-generated method stub
			return null;
		}

	}

}
