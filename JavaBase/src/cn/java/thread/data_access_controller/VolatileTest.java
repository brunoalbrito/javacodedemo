package cn.java.thread.data_access_controller;

/**
 * https://www.ibm.com/developerworks/cn/java/j-jtp06197.html
 * @author zhouzhian
 */
public class VolatileTest {
	public static void main(String[] args) {
		Foo foo = new Foo();
		new Thread(new ReadRunnable(foo)).start();
		new Thread(new WriteRunnable(foo)).start();
	}

	private static class ReadRunnable implements Runnable {
		private Foo foo;

		public ReadRunnable(Foo foo) {
			this.foo = foo;
		}

		@Override
		public void run() {
			int i = 0;
			while (i < 10) {
				StringBuilder sb = new StringBuilder();
				sb.append("before Sleep: getStr = ");
				sb.append(this.foo.getStr());
				sb.append(" , ");
				try {
					Thread.sleep(1000 * 2);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				sb.append("after Sleep: getStr = ");
				sb.append(this.foo.getStr());
				System.out.println(sb);
				i++;
			}
		}
	}

	private static class WriteRunnable implements Runnable {
		private Foo foo;

		public WriteRunnable(Foo foo) {
			this.foo = foo;
		}

		@Override
		public void run() {
			int i = 0;
			while (i < 10) {
				try {
					Thread.sleep(1000 * 1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				this.foo.setStr("setStr i=" + i);
				i++;
			}
		}
	}

	private static class Foo {
		private volatile String str;

		public String getStr() {
			return str;
		}

		public synchronized void setStr(String str) {
			this.str = str;
		}

	}

}
