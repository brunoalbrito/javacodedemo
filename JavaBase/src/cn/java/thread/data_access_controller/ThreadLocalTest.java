package cn.java.thread.data_access_controller;

import cn.java.thread.data_access_controller.CommonTest.CommonRunnable;
import cn.java.thread.data_access_controller.CommonTest.ShareObject;

/**
 * @author zhouzhian
 *
 */
public class ThreadLocalTest {

	/**
	 * 当使用ThreadLocal维护变量时，ThreadLocal为每个使用该变量的线程提供独立的变量副本，
	 * 所以每一个线程都可以独立地改变自己的副本，而不会影响其它线程所对应的副本
	 */
	public static void main(String[] args) {
		testNonConcurrentCtrlRunnable();
		testThreadLocalConcurrentCtrlRunnable();
		testObjecValueThreadLocalConcurrentCtrlRunnable();
		try {
			Thread.sleep(1000 * 20);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void testNonConcurrentCtrlRunnable() {
		ShareObject shareObject = new ShareObject();
		new Thread(new NonConcurrentCtrlRunnable("NonConcurrentCtrlRunnableA",shareObject)).start();
		new Thread(new NonConcurrentCtrlRunnable("NonConcurrentCtrlRunnableB",shareObject)).start();
	}
	
	
	public static void testThreadLocalConcurrentCtrlRunnable() {
		ShareObject shareObject = new ShareObject();
		new Thread(new ThreadLocalConcurrentCtrlRunnable("ThreadLocalConcurrentCtrlRunnableA",shareObject)).start();
		new Thread(new ThreadLocalConcurrentCtrlRunnable("ThreadLocalConcurrentCtrlRunnableB",shareObject)).start();
	}
	
	public static void testObjecValueThreadLocalConcurrentCtrlRunnable() {
		ShareObject shareObject = new ShareObject();
		ShareObject.Foo foo = new ShareObject.Foo();
		new Thread(new ObjecValueThreadLocalConcurrentCtrlRunnable("ObjecValueThreadLocalConcurrentCtrlRunnableA",foo,shareObject)).start();
		new Thread(new ObjecValueThreadLocalConcurrentCtrlRunnable("ObjecValueThreadLocalConcurrentCtrlRunnableB",foo,shareObject)).start();
	}
	
	public static class NonConcurrentCtrlRunnable extends CommonRunnable{
		public NonConcurrentCtrlRunnable(String threadName, ShareObject shareObject) {
			super(threadName, shareObject);
		}
		@Override
		protected void runInternal() {
			shareObject.methodNon("threadName=" + threadName);
		}
	}

	
	public static class ObjecValueThreadLocalConcurrentCtrlRunnable extends CommonRunnable{
		private ShareObject.Foo foo;
		public ObjecValueThreadLocalConcurrentCtrlRunnable(String threadName,ShareObject.Foo foo, ShareObject shareObject) {
			super(threadName, shareObject);
			this.foo = foo;
		}
		
		@Override
		protected void runInternal() {
			shareObject.methodThreadLocalObjecValue(foo,"threadName=" + threadName);
		}
	}

	public static class ThreadLocalConcurrentCtrlRunnable extends CommonRunnable{
		public ThreadLocalConcurrentCtrlRunnable(String threadName, ShareObject shareObject) {
			super(threadName, shareObject);
		}
		@Override
		protected void runInternal() {
			shareObject.methodThreadLocalStrValue("threadName=" + threadName);
		}
	}


}
