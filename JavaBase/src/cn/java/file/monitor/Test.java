package cn.java.file.monitor;

import org.apache.commons.io.monitor.FileAlterationObserver;

public class Test {

	/**
	 * 机制：
	 * 		使用多线程的方式，定时读取目录下文件的信息进行对比，如果有改变，则进行相关的通知
	 * 
	 * http://www.cnblogs.com/Mr-kevin/p/5784443.html
	 * @param args
	 */
	public static void main(String[] args) {
		FileMonitorImpl fileMonitor = null;
		try {
			FileAlterationListenerAdaptorImpl fileAlterationListener = new FileAlterationListenerAdaptorImpl(); // 文件改变监听器
			FileFilter fileFilter = new FileFilterImpl("xml", "txt"); // 文件后缀过滤器
			FileAlterationObserver fileAlterationObserver = new FileAlterationObserverImpl("D:\\UploadDir", fileFilter,
					fileAlterationListener); // 文件观察器

			fileMonitor = new FileMonitorImpl(10000, fileAlterationObserver); // 文件监测器，监测时间间隔，单位：毫秒

			fileMonitor.start(); // 启动
		} catch (Exception e) {
			if (fileMonitor != null) {
				fileMonitor.stop(); // 停止
			}
		}
	}

}
