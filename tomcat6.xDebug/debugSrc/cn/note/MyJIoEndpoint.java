package cn.note;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.tomcat.util.net.ServerSocketFactory;


/**
 * 一个接受线程，多个worker线程
 * 
 * @author Administrator
 *
 */
public class MyJIoEndpoint {
	boolean running = false;
	boolean paused = true;
	boolean daemon = true;
	int threadPriority = 5;
	int acceptorThreadCount = 1;
	int maxThreads = 200;
	ServerSocket serverSocket = null;
	Object executor  =  null;
	WorkerStack  workers;
	
	public void start() throws Exception {
		/**
         * 	工作线程最大数量 200
         */
        if (executor == null) {
        	workers = new WorkerStack(maxThreads);//工作者堆栈
        }
		serverSocket = new ServerSocket();
        if (!running) {
            running = true;
            paused = false;
            /**
             * 开始接受线程
             * Acceptor是内部类
             * 
             * acceptorThreadCount = 1;
             */
            for (int i = 0; i < acceptorThreadCount; i++) {//
                Thread acceptorThread = new Thread(new Acceptor(), "TP-Acceptor-" + i);
                acceptorThread.setPriority(threadPriority);//线程权重
                acceptorThread.setDaemon(daemon);//true
                acceptorThread.start();
            }
        }
	}
	
	/**
	 * 内部类
	 * @author Administrator
	 *
	 */
	protected class Acceptor implements Runnable {

		@Override
		public void run() {
			 // Loop until we receive a shutdown command
            while (running) {

                // Loop if endpoint is paused
                while (paused) {
                    try {
                        Thread.sleep(1000);//线程睡眠
                    } catch (InterruptedException e) {
                        // Ignore
                    }
                }

                // Accept the next incoming connection from the server socket
                try {
                	/**
                	 * org.apache.tomcat.util.net.DefaultServerSocketFactory.acceptSocket(serverSocket);
                	 * 		return serverSocket.accept();
                	 */
                	ServerSocketFactory serverSocketFactory = ServerSocketFactory.getDefault();;
                	
                    Socket socket = serverSocketFactory.acceptSocket(serverSocket);//====等价serverSocket.accept(); 会阻塞住
                    /**
                     * org.apache.tomcat.util.net.DefaultServerSocketFactory.initSocket(socket);   
                     * 		
                     */
                    serverSocketFactory.initSocket(socket);//没有执行内容
                 
                    // Hand this socket off to an appropriate processor
                    /**
                     * 	processSocket 这边
                     *  接入 socket，进行处理
                     */
                    if (!processSocket(socket)) {//
                        // Close socket right away
                        try {
                            socket.close();
                        } catch (IOException e) {
                            // Ignore
                        }
                    }
                    
                }catch ( IOException x ) {
                	System.out.println("endpoint.accept.fail");
                } 
                //处理完成后，会重复回收
                // The processor will recycle itself when it finishes

            }
		}
	}
	
	
	/**
	 * 处理进程
	 * @param socket
	 * @return
	 */
	public boolean processSocket(Socket socket)
	{
		return true;
	}
	
	/**
	 * 内部类
	 * @author Administrator
	 *
	 */
	public class WorkerStack {

		public WorkerStack(int maxThreads) {
			// TODO Auto-generated constructor stub
		}
     
    }
	
	/**
	 * 内部类
	 * @author Administrator
	 *
	 */
	protected class Worker implements Runnable {

		@Override
		public void run() {
		    // Process requests until we receive a shutdown signal
//            while (running) {
//
//                // Wait for the next socket to be assigned
//                Socket socket = await();
//                if (socket == null)
//                    continue;
//
//                // Process the request from this socket
//                /**
//                 * 
//                 * org.apache.coyote.http11.Http11Protocol.Http11ConnectionHandler
//                 * 
//                 * handler.process(socket)
//                 */
//                //org.apache.coyote.http11.Http11Protocol.Http11ConnectionHandler
//                if (!setSocketOptions(socket) || !handler.process(socket)) {
//                    // Close socket
//                    try {
//                        socket.close();
//                    } catch (IOException e) {
//                    }
//                }
//
//                // Finish up this request
//                socket = null;
//                recycleWorkerThread(this);
//
//            }
		}
		
	}
}
