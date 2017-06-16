package cn.java.demo.beantag.lifecycle;

import org.springframework.context.Lifecycle;

/**
 * 不能自动启动
 * @author zhouzhian
 */
public class FooOneLifecycle implements Lifecycle {
	boolean isRunning = false;

	@Override
	public void start() {
		this.codeIn("start()");
		this.isRunning = true;
	}

	@Override
	public void stop() {
		this.codeIn("stop()");
		this.isRunning = false;
	}

	@Override
	public boolean isRunning() {
		return this.isRunning;
	}

	private void codeIn(String methodName) {
		System.out.println("code in : " + FooOneLifecycle.class.getName() + ":" + methodName);
	}
}
