package cn.java.file.monitor;

import java.io.File;

import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationObserver;

/**
 * 文件监听器
 */
public final class FileAlterationListenerAdaptorImpl extends FileAlterationListenerAdaptor {

	/**
	 * 文件创建时执行的动作
	 */
	@Override
	public void onFileCreate(File file) {
		// To do something
		System.out.println("Create file: "+file.getName());
	}

	/**
	 * 文件删除（转移）时执行的动作
	 */
	@Override
	public void onFileDelete(File file) {
		// To do something
		System.out.println("Delete file: "+file.getName());
	}

	/**
	 * 文件内容改变时执行的动作
	 */
	@Override
	public void onFileChange(File file) {
		// To do something
	}

	/**
	 * 开始执行监听时执行的动作
	 */
	@Override
	public void onStart(FileAlterationObserver observer) {
		// To do something
	}

	/**
	 * 停止监听时执行的动作
	 */
	@Override
	public void onStop(FileAlterationObserver observer) {
		// To do something
	}
}