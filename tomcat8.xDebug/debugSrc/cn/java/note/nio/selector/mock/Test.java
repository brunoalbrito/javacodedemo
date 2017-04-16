package cn.java.note.nio.selector.mock;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Test {

	public static void main(String[] args) {
		try {
			new NioEndpoint().start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static class NioEndpoint {

		// 接受连接
		protected AcceptRunnable[] acceptors;

		// 处理连接
		private PollerRunnable[] pollers = null;
		private AtomicInteger pollerRotater = new AtomicInteger(0);

		/**
		 * 轮询的方式获取 Poller
		 * @return
		 */
		public PollerRunnable getPoller0() {
			int idx = Math.abs(pollerRotater.incrementAndGet()) % pollers.length; // 轮询机制分配给后端
			return pollers[idx];
		}

		public void start() throws IOException {

			// 启动pollers
			pollers = new PollerRunnable[10]; // 启动十个
			for (int i = 0; i < pollers.length; i++) {
				pollers[i] = new PollerRunnable();
				Thread pollerThread = new Thread(pollers[i], "ClientPoller-" + i);
				pollerThread.setPriority(Thread.NORM_PRIORITY);
				pollerThread.setDaemon(true);
				pollerThread.start();
			}

			// 启动 acceptors
			int count = 1;
			acceptors = new AcceptRunnable[count]; // 启动一个
			for (int i = 0; i < count; i++) {
				acceptors[i] = new AcceptRunnable();
				String threadName = "Acceptor-" + i;
				Thread t = new Thread(acceptors[i], threadName);
				t.setPriority(Thread.NORM_PRIORITY);
				t.setDaemon(true);
				t.start();
			}
		}

		/**
		 * SelectionKey 携带的附件
		 * 
		 * @author Administrator
		 */
		private class SelectionKeyAttachment {
			private String attachmentName;

			public String getAttachmentName() {
				return attachmentName;
			}

			public void setAttachmentName(String attachmentName) {
				this.attachmentName = attachmentName;
			}

			public SelectionKeyAttachment(String attachmentName) {
				super();
				this.attachmentName = attachmentName;
			}
		}

		/**
		 * 事件
		 * 
		 * @author Administrator
		 *
		 */
		private class EventRunnable implements Runnable {
			private RunnableHolder runnableHolder;
			private SelectionKeyAttachment selectionKeyAttachment;

			public EventRunnable(RunnableHolder runnableHolder, SelectionKeyAttachment selectionKeyAttachment) {
				this.runnableHolder = runnableHolder;
				this.selectionKeyAttachment = selectionKeyAttachment;
			}

			/**
			 * 把SelectableChannelImpl注册到Selector
			 */
			@Override
			public void run() {
				try {
					runnableHolder.getSelectableChannelImpl().register(runnableHolder.getPoller().getSelector(), SelectionKey.OP_READ, selectionKeyAttachment);
				} catch (ClosedChannelException e) {
					e.printStackTrace();
				}
			}
		}

		/**
		 * 对象容器
		 * @author Administrator
		 *
		 */
		private class RunnableHolder /*implements ByteChannel*/ {
			private AcceptRunnable accept;
			private PollerRunnable poller;
			private SelectableChannelImpl selectableChannelImpl;

			public RunnableHolder(AcceptRunnable accept, SelectableChannelImpl selectableChannelImpl) {
				super();
				this.accept = accept;
				this.selectableChannelImpl = selectableChannelImpl;
			}

			public void setPoller(PollerRunnable poller) {
				this.poller = poller;
			}

			public AcceptRunnable getAccept() {
				return accept;
			}

			public PollerRunnable getPoller() {
				return poller;
			}

			public SelectableChannelImpl getSelectableChannelImpl() {
				return selectableChannelImpl;
			}


		}

		/**
		 * Accept专门接受连接 ，然后以事件的方式转给 Poller
		 */
		private class AcceptRunnable implements Runnable {
			@Override
			public void run() {
				int i = 10;
				while (true) {
					SelectableChannelImpl selectableChannelImpl = new SelectableChannelImpl(new SelectableChannelImpl.SelectorProviderImpl());
					getPoller0().register(new RunnableHolder(this,selectableChannelImpl), "AcceptRunnable - attachmentName" + i); // 把事件放入 Poller的队列中
					break;
				}
			}
		}

		/**
		 * 处理连接
		 * 
		 * @author Administrator
		 *
		 */
		private class PollerRunnable implements Runnable {
			private final SynchronousQueue<EventRunnable> events = new SynchronousQueue<>(); // 队列
			private Selector selector;

			public PollerRunnable() throws IOException {
				this.selector = Selector.open();
			}

			public Selector getSelector() {
				return selector;
			}

			@Override
			public void run() {
				events(); // 执行事件
				int keyCount = 0;
				try {
					keyCount = selector.select(1000);
				} catch (IOException e) {
					e.printStackTrace();
				}
				Iterator<SelectionKey> iterator = keyCount > 0 ? selector.selectedKeys().iterator() : null;
				while (iterator != null && iterator.hasNext()) {
					SelectionKey sk = iterator.next();
				}
			}

			/**
			 * 执行事件
			 * 
			 * @return
			 */
			public boolean events() {
				boolean result = false;
				EventRunnable pe = null;
				while ((pe = events.poll()) != null) { // 从队列中取得事件
					result = true;
					try {
						pe.run(); // 启动事件
					} catch (Throwable x) {
					}
				}
				return result;
			}

			/**
			 * 注册事件
			 * 
			 * @param runnableHolder
			 */
			public void register(final RunnableHolder runnableHolder, String attachmentName) {
				runnableHolder.setPoller(this);
				events.add(new EventRunnable(runnableHolder, new SelectionKeyAttachment(attachmentName)));
			}
		}
	}

}
