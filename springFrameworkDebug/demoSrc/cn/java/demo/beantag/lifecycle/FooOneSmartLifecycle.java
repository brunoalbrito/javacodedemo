package cn.java.demo.beantag.lifecycle;

import org.springframework.context.SmartLifecycle;

/**
 * 自动启动的bean
 * @author zhouzhian
 */
public class FooOneSmartLifecycle implements SmartLifecycle {

	private Boolean isAutoStartup = true;
	private boolean isRunning = false;

	private void codeIn(String methodName) {
		System.out.println("code in : " + FooOneSmartLifecycle.class.getName() + ":" + methodName);
	}
	
	public void setIsAutoStartup(Boolean isAutoStartup) {
		this.isAutoStartup = isAutoStartup;
	}

	@Override
	public void start() {
		this.codeIn("start()");
		this.isRunning = true;
	}

	@Override
	public void stop() {
		this.codeIn("start()");
		this.isRunning = false;
	}

	@Override
	public boolean isRunning() {
		return this.isRunning;
	}

	@Override
	public int getPhase() {
		return 0;
	}

	@Override
	public boolean isAutoStartup() {
		return this.isAutoStartup;
	}

	@Override
	public void stop(Runnable callback) {
		this.isRunning = false;
		callback.run();
	}

}
