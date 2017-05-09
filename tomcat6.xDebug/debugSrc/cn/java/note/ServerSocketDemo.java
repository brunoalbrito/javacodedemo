package cn.java.note;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 
 * 1、创建N个Accept接受线程，默认一个 2、创建工作线程Worker，并放入工作线程栈WorkerStack，并进行唤醒
 * 
 * @author Administrator
 *
 */
public class ServerSocketDemo {

	public static void main(String[] args) throws IOException {

	}

	protected volatile boolean running = false;
	protected volatile boolean paused = false;
	protected int maxThreads = 200;
	protected WorkerStack workerStack = null;
	protected int curThreadsBusy = 0;
	protected int curThreads = 0;

	public void start() {
		if (!running) {
			running = true;
			paused = false;
			// 创建N个接受线程
			int acceptorThreadCount = 1;
			workerStack = new WorkerStack(200);// 工作者栈
			for (int i = 0; i < acceptorThreadCount; i++) {// 创建一个接受者
				Thread acceptorThread = new Thread(new Acceptor(), "Acceptor-"
						+ i);
				acceptorThread.setPriority(Thread.NORM_PRIORITY);// 线程权重
				acceptorThread.setDaemon(true);// true
				acceptorThread.start();
			}
		}
	}

	/**
	 * 从工作线程栈中取得一个工作线程
	 * 
	 * @return
	 */
	protected Worker getWorkerThread() {
		synchronized (workerStack) {
			Worker workerThread;
			while ((workerThread = createWorkerThread()) == null) {// 没有取得工作线程的话
				try {
					workerStack.wait();// 工作栈进入等待，需要等待其他线程唤醒
				} catch (InterruptedException e) {
					// Ignore
				}
			}
			return workerThread;
		}

	}

	/**
	 * 创建工作线程
	 * 
	 * @return
	 */
	protected Worker createWorkerThread() {
		synchronized (workerStack) {// 锁定工作线程栈
			if (workerStack.size() > 0) {
				curThreadsBusy++;
				return workerStack.pop();// 弹出一个工作线程
			}
			if ((maxThreads > 0) && (curThreads < maxThreads)) {// 如果工作线程还没有到达极限
				curThreadsBusy++;
				if (curThreadsBusy == maxThreads) {
					System.out.println("工作线程耗尽了，endpoint.info.maxThreads"
							+ "maxThreads");

				}
				return (newWorkerThread());// 创建一个工作线程并启动
			} else {
				if (maxThreads < 0) {// 如果没有做最大工作线程数量限制
					curThreadsBusy++;
					return (newWorkerThread());// 创建一个工作线程并启动
				} else {
					return (null);
				}
			}
		}
	}

	/**
	 * Create and return a new processor suitable for processing HTTP requests
	 * and returning the corresponding responses.
	 */
	protected Worker newWorkerThread() {

		Worker workerThread = new Worker();
		workerThread.start();
		return (workerThread);

	}

	/**
	 * 内部类，接受线程
	 * 
	 * @author Administrator
	 *
	 */
	class Acceptor implements Runnable {

		@Override
		public void run() {
			// 等待线程
			ServerSocket serverSocket;
			try {
				serverSocket = new ServerSocket();
				Socket socket = serverSocket.accept();

				boolean temp = false;
				getWorkerThread().assign(socket);// 取得工作线程，并进行唤醒。
				// 处理用户请求
				if (temp) {
					socket.close();
				}
			} catch (IOException e) {

				e.printStackTrace();
			}

		}

	}

	/**
	 * 内部类，处理请求线程
	 * 
	 * @author Administrator
	 *
	 */
	class Worker implements Runnable {
		protected Thread thread = null;
		protected boolean available = false;
		protected Socket socket = null;

		@Override
		public void run() {
			while (running) {// 启动中

				// Wait for the next socket to be assigned
				Socket socket = await();
				if (socket == null)// 如果没有socket，一直循环
					continue;

				try {
					InputStream inputStream = socket.getInputStream();//输入流
					OutputStream outputStream = socket.getOutputStream();//输出
					socket.setSoTimeout(-1);
					
					
					
					
					
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// // Process the request from this socket
				// if (!setSocketOptions(socket) || !handler.process(socket))
				// {//处理socket
				// // Close socket
				// try {
				// socket.close();
				// } catch (IOException e) {
				// }
				// }

				// Finish up this request
				socket = null;
				// recycleWorkerThread(this);

			}

		}

		/**
		 * 线程赋值socket，并启动
		 * 
		 * @param socket
		 */
		synchronized void assign(Socket socket) {

			// Wait for the Processor to get the previous Socket
			while (available) {// 如果线程在跑，进入等待
				try {
					wait();
				} catch (InterruptedException e) {
				}
			}

			// Store the newly available Socket and notify our thread
			this.socket = socket;
			available = true;// 可用
			notifyAll();// 唤醒其他调用wait()的线程

		}

		/**
		 * 线程进行等待
		 * 
		 * @return
		 */
		private synchronized Socket await() {

			// Wait for the Connector to provide a new Socket
			while (!available) {// 如果线程没在跑，进入等待
				try {
					wait();
				} catch (InterruptedException e) {
				}
			}

			// Notify the Connector that we have received this Socket
			Socket socket = this.socket;
			available = false;
			notifyAll();// 唤醒其他调用wait()的线程

			return (socket);

		}

		/**
		 * Start the background processing thread.
		 */
		public void start() {
			thread = new Thread(this);
			thread.setName("-" + (++curThreads));
			thread.setDaemon(true);
			thread.start();
		}
	}

	/**
	 * 工作线程站，存放解析用户请求的线程
	 * 
	 * @author Administrator
	 *
	 */
	class WorkerStack {
		protected Worker[] workers = null;
		protected int end = 0;

		public WorkerStack(int size) {
			workers = new Worker[size];
		}

		/**
		 * Put the worker into the queue. If the queue is full (for example if
		 * the queue has been reduced in size) the worker will be dropped.
		 * 
		 * @param worker
		 *            the worker to be appended to the queue (first element).
		 */
		public void push(Worker worker) {
			if (end < workers.length) {
				workers[end++] = worker;
			} else {
				curThreads--;
			}
		}

		/**
		 * Get the first object out of the queue. Return null if the queue is
		 * empty.
		 */
		public Worker pop() {
			if (end > 0) {
				return workers[--end];
			}
			return null;
		}

		/**
		 * Get the first object out of the queue, Return null if the queue is
		 * empty.
		 */
		public Worker peek() {
			return workers[end];
		}

		/**
		 * Is the queue empty?
		 */
		public boolean isEmpty() {
			return (end == 0);
		}

		/**
		 * How many elements are there in this queue?
		 */
		public int size() {
			return (end);
		}

		/**
		 * Resize the queue. If there are too many objects in the queue for the
		 * new size, drop the excess.
		 * 
		 * @param newSize
		 */
		public void resize(int newSize) {
			Worker[] newWorkers = new Worker[newSize];
			int len = workers.length;
			if (newSize < len) {
				len = newSize;
			}
			System.arraycopy(workers, 0, newWorkers, 0, len);
			workers = newWorkers;
		}
	}

}
