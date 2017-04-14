package cn.java.thread.data_access_controller;

public class CommonTest {

	public static abstract class CommonRunnable implements Runnable{
		protected ShareObject shareObject;
		protected String threadName;
		
		public CommonRunnable(String threadName,ShareObject shareObject){
			this.threadName = threadName;
			this.shareObject = shareObject;
		}

		@Override
		public void run() {
			runInternal();
		}
		protected abstract void runInternal();
	}
	
	protected static class ShareObject {
		private String objName = "";
		private ThreadLocal<String> strValueThreadLocal = new ThreadLocal<String>();
		private ThreadLocal<Foo> objValueThreadLocal = new ThreadLocal<Foo>();
		
		/**
		 * 没并发控制会出现，不可重复读的问题
		 * @param objName
		 */
		public void methodNon(String objName) {
			this.objName = objName; 
			try {
				Thread.sleep(1000*3);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("methodNon : " + this.objName);
		}
		
		/**
		 * synchronized 并发控制
		 * @param objName
		 */
		public synchronized void methodSynchronized(String objName) {
			this.objName = objName; 
			try {
				Thread.sleep(1000*3);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("methodSynchronized : " + this.objName);
		}
		
		/**
		 * ThreadLocal 本地线程的拷贝
		 * @param objName
		 */
		public void methodThreadLocalStrValue(String objName) {
			this.strValueThreadLocal.set(objName);; 
			try {
				Thread.sleep(1000*3); 
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("methodThreadLocalStrValue : " + this.strValueThreadLocal.get());
		}
		
		/**
		 * ThreadLocal 本地线程的拷贝
		 * 		指定如果对象，那么对该对象的信息的改变，在其他线程里面还是被改变了。
		 * @param objName
		 */
		public void methodThreadLocalObjecValue(Foo foo,String objName) {
			this.objValueThreadLocal.set(foo);
			Foo fooTemp = this.objValueThreadLocal.get();
			fooTemp.setStr(objName);
			try {
				Thread.sleep(1000*3);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("methodThreadLocalObjecValue : " + this.objValueThreadLocal.get());
		}
		
		public static class Foo{
			private String str;

			public String getStr() {
				return str;
			}

			public void setStr(String str) {
				this.str = str;
			}

			@Override
			public String toString() {
				return "Foo [str=" + str + "]";
			}
		}

	}
}
