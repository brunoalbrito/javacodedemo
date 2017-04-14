package cn.java.thread.data_access_controller;

public class SynchronizedTest extends CommonTest{

	public static void main(String[] args) {
		testSynchronizedConcurrentCtrlRunnable();
		try {
			Thread.sleep(1000 * 20);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void testSynchronizedConcurrentCtrlRunnable() {
		ShareObject shareObject = new ShareObject();
		new Thread(new SynchronizedConcurrentCtrlRunnable("SynchronizedConcurrentCtrlRunnableA",shareObject)).start();
		new Thread(new SynchronizedConcurrentCtrlRunnable("SynchronizedConcurrentCtrlRunnableB",shareObject)).start();
		
	}
	
	public static class SynchronizedConcurrentCtrlRunnable extends CommonRunnable{
		public SynchronizedConcurrentCtrlRunnable(String threadName, ShareObject shareObject) {
			super(threadName, shareObject);
		}
		@Override
		protected void runInternal() {
			shareObject.methodSynchronized("threadName=" + threadName);
		}
	}
	


}
