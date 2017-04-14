package cn.java.thread;

public class SynchronizedTest {

	/**
	 * http://www.cnblogs.com/qifengshi/p/6254607.html
	 * @param args
	 */
	public static void main(String[] args) {
		ShareObject shareObject = new ShareObject();
        SynchronizedMethodThread a = new SynchronizedMethodThread(shareObject);
        a.setName("A");
        SynchronizedMethodThread b = new SynchronizedMethodThread(shareObject);
        b.setName("B");

        a.start();
        b.start();

        try {
			Thread.sleep(1000 * 15);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static class SynchronizedMethodThread extends Thread{
		private ShareObject shareObject;
		public SynchronizedMethodThread(ShareObject shareObject){
			this.shareObject = shareObject;
		}

		@Override
		public void run() {
			super.run();
			if(Thread.currentThread().getName().equals("A")){
				shareObject.methodA();
			}else{
				shareObject.methodB();
			}
		}
	}

	private static class ShareObject {

		public synchronized void methodA(){
			try{
				System.out.println("begin methodA threadName=" + Thread.currentThread().getName()+
						" begin time =" + System.currentTimeMillis());
				Thread.sleep(5000);
				System.out.println("end");
			}catch (InterruptedException e){
				e.printStackTrace();
			}
		}

		public synchronized void methodB(){
			try{
				System.out.println("begin methodB threadName=" + Thread.currentThread().getName() +
						" begin time =" + System.currentTimeMillis());
				Thread.sleep(1000);
				System.out.println("end");
			}catch (InterruptedException e){
				e.printStackTrace();
			}
		}
	}

}
