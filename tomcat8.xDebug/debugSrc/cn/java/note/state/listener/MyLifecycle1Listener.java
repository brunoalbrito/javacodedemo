package cn.java.note.state.listener;

public class MyLifecycle1Listener implements LifecycleListener {

	@Override
	public void lifecycleEvent(LifecycleEvent event) {
		MyLifecycle1 myLifecycle1 = (MyLifecycle1) event.getLifecycle();
//		System.out.println(myLifecycle1);
		if (event.getType().equals(Lifecycle.BEFORE_INIT_EVENT)) { 
			beforeInit();
		} else if (event.getType().equals(Lifecycle.AFTER_INIT_EVENT)) {
		} else if (event.getType().equals(Lifecycle.BEFORE_START_EVENT)) {
		} else if (event.getType().equals(Lifecycle.START_EVENT)) {
		} else if (event.getType().equals(Lifecycle.AFTER_START_EVENT)) {
		} else if (event.getType().equals(Lifecycle.BEFORE_STOP_EVENT)) {
		} else if (event.getType().equals(Lifecycle.STOP_EVENT)) {
		} else if (event.getType().equals(Lifecycle.AFTER_STOP_EVENT)) {
		} else if (event.getType().equals(Lifecycle.BEFORE_DESTROY_EVENT)) {
		} else if (event.getType().equals(Lifecycle.AFTER_DESTROY_EVENT)) {
		}
		System.out.println("trigger event : " + event.getType());
	}

	protected synchronized void beforeInit() {

	}

}