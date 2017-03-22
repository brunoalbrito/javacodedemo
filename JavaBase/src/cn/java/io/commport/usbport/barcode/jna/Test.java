package cn.java.io.commport.usbport.barcode.jna;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * http://www.cnblogs.com/softidea/p/5862484.html
 * http://blog.csdn.net/liujiahan629629/article/details/41780611
 */
public class Test {
	private BarcodeProducter barcodeProducter;
	private BarcodeConsumer barcodeConsumer;

	/**
	 * 可以在此文件中运行测试
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		BarcodeProducter producter = new BarcodeProducter();
		BarcodeConsumer consumer = new BarcodeConsumer();
		producter.startProduct();
		consumer.startConsume();
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("输入 '' 退出程序");
		String line = reader.readLine();
		while (line != null) {
			if ("exit".equals(line)) {
				producter.stopProduct();
				consumer.stopConsume();
				System.exit(0);
			}
			line = reader.readLine();
		}
	}
	
	/**
	 * 启动
	 * 
	 * @param sce
	 */
	public void contextInitialized() {
		barcodeProducter = new BarcodeProducter();
		barcodeProducter.startProduct();
		barcodeConsumer = new BarcodeConsumer();
		barcodeConsumer.startConsume();
	}

	/**
	 * 关闭
	 * 
	 * @param sce
	 */
	public void contextDestroyed() {
		barcodeProducter.stopProduct();
		barcodeConsumer.stopConsume();
	}

	
}