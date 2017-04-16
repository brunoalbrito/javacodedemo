package cn.java.note.state.listener;

public class Test {

	public static void main(String[] args) throws LifecycleException {
		MyLifecycle1 myLifecycle = new MyLifecycle1();
		myLifecycle.addLifecycleListener(new MyLifecycle1Listener());
		myLifecycle.init();
		myLifecycle.start();
		myLifecycle.stop();
		myLifecycle.destroy();
	}

}
