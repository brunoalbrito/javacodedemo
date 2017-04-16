package cn.java.note.state.listener;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class LifecycleBase implements Lifecycle {
	private volatile LifecycleState state = LifecycleState.NEW;
	private final List<LifecycleListener> lifecycleListeners = new CopyOnWriteArrayList<>();

	/**
	 * 设置状态
	 */
	protected synchronized void setState(LifecycleState state)
			throws LifecycleException {
		setStateInternal(state, null, true);
	}

	/**
	 * 设置状态
	 */
	protected synchronized void setState(LifecycleState state, Object data) throws LifecycleException {
		setStateInternal(state, data, true);
	}

	/**
	 * 设置状态
	 */
	private synchronized void setStateInternal(LifecycleState state, Object data, boolean check) throws LifecycleException {
		this.state = state;
		String lifecycleEvent = state.getLifecycleEvent(); // 事件名称，如 “before_init” “start”
		if (lifecycleEvent != null) {
			fireLifecycleEvent(lifecycleEvent, data); // 触发“监听器”
		}
	}

	/**
	 * 触发事件监听器
	 * @param type
	 * @param data
	 */
	protected void fireLifecycleEvent(String type, Object data) {
		LifecycleEvent event = new LifecycleEvent(this, type, data);
		for (LifecycleListener listener : lifecycleListeners) {
			listener.lifecycleEvent(event); // 触发事件
		}
	}

	/**
	 * 添加事件监听器
	 */
	@Override
	public void addLifecycleListener(LifecycleListener listener) {
		lifecycleListeners.add(listener);
	}

	/**
	 * 查看事件监听器
	 */
	@Override
	public LifecycleListener[] findLifecycleListeners() {
		return lifecycleListeners.toArray(new LifecycleListener[0]);
	}

	/**
	 * 删除事件监听器
	 */
	@Override
	public void removeLifecycleListener(LifecycleListener listener) {
		lifecycleListeners.remove(listener);
	}

	/**
	 * 初始化
	 */
	@Override
	public final synchronized void init() throws LifecycleException {
		if (!state.equals(LifecycleState.NEW)) {
			// invalidTransition(Lifecycle.BEFORE_INIT_EVENT);
		}
		setStateInternal(LifecycleState.INITIALIZING, null, false); // 触发事件

		// initInternal(); //!!!!

		setStateInternal(LifecycleState.INITIALIZED, null, false); // 触发事件
	}

	/**
	 * 启动
	 */
	@Override
	public final synchronized void start() throws LifecycleException {

		if (LifecycleState.STARTING_PREP.equals(state) || LifecycleState.STARTING.equals(state) ||
				LifecycleState.STARTED.equals(state)) {
			return;
		}

		if (state.equals(LifecycleState.NEW)) {
			init();//!!!调用初始化
		} else if (state.equals(LifecycleState.FAILED)) {
			stop();
		} else if (!state.equals(LifecycleState.INITIALIZED) &&
				!state.equals(LifecycleState.STOPPED)) {
			// invalidTransition(Lifecycle.BEFORE_START_EVENT);
		}

		setStateInternal(LifecycleState.STARTING_PREP, null, false); // 触发事件

		// startInternal(); //触发事件

		if (state.equals(LifecycleState.FAILED)) {
			stop();
		} else if (!state.equals(LifecycleState.STARTING)) {
			// invalidTransition(Lifecycle.AFTER_START_EVENT);
		} else {
			setStateInternal(LifecycleState.STARTED, null, false);// 触发事件
		}
	}

	/**
	 * 停止
	 */
	@Override
	public final synchronized void stop() throws LifecycleException {
		if (LifecycleState.STOPPING_PREP.equals(state) || LifecycleState.STOPPING.equals(state) ||
				LifecycleState.STOPPED.equals(state)) {
			return;
		}

		if (state.equals(LifecycleState.NEW)) {
			state = LifecycleState.STOPPED;
			return;
		}

		if (!state.equals(LifecycleState.STARTED) && !state.equals(LifecycleState.FAILED)) {
			// invalidTransition(Lifecycle.BEFORE_STOP_EVENT);
		}

		if (state.equals(LifecycleState.FAILED)) {
			fireLifecycleEvent(BEFORE_STOP_EVENT, null);
		} else {
			setStateInternal(LifecycleState.STOPPING_PREP, null, false);
		}

		// stopInternal();

		if (!state.equals(LifecycleState.STOPPING) && !state.equals(LifecycleState.FAILED)) {
			// invalidTransition(Lifecycle.AFTER_STOP_EVENT);
		}

		setStateInternal(LifecycleState.STOPPED, null, false);
	}

	/**
	 * 销毁
	 */
	@Override
	public final synchronized void destroy() throws LifecycleException {
		if (LifecycleState.FAILED.equals(state)) {
			stop();
		}

		if (LifecycleState.DESTROYING.equals(state) ||
				LifecycleState.DESTROYED.equals(state)) {
			return;
		}
		if (!state.equals(LifecycleState.STOPPED) &&
				!state.equals(LifecycleState.FAILED) &&
				!state.equals(LifecycleState.NEW) &&
				!state.equals(LifecycleState.INITIALIZED)) {
			// invalidTransition(Lifecycle.BEFORE_DESTROY_EVENT);
		}
		setStateInternal(LifecycleState.DESTROYING, null, false);

		// destroyInternal();

		setStateInternal(LifecycleState.DESTROYED, null, false);
	}

	@Override
	public LifecycleState getState() {
		return state;
	}

	@Override
	public String getStateName() {
		return getState().toString();
	}
}
