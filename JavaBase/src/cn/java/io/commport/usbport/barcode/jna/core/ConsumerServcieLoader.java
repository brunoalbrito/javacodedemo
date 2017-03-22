package cn.java.io.commport.usbport.barcode.jna.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ConsumerServcieLoader  {
	/**
	 * 从类路径下的barcode.save.services文件中获取保存服务类名
	 * 
	 * @return 多个保存服务实现类名
	 */
	public List<String> getBarcodeConsumerServcieImplClasses() {
		List result = new ArrayList();
		BufferedReader reader = null;
		InputStream in = null;
		try {
			// 放在 WEB-INF/classes下的 "barcode.save.services" 会覆盖jar包中的 "cn.java.io.commport.usbport.barcode.consumerservices"
			URL url = Thread.currentThread().getContextClassLoader().getResource("cn.java.io.commport.usbport.barcode.consumerservices");
			System.out.println("url:" + url.getPath());
			in = url.openStream();
			if (in == null) {
				System.out.println("没有在类路径下找到条码保存服务配置文件：barcode.save.services");
				return result;
			}
			reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
			String line = reader.readLine();
			while (line != null) {
				// 忽略空行和以#号开始的注释行
				if (!"".equals(line.trim()) && !line.trim().startsWith("#")) {
					result.add(line);
				}
				line = reader.readLine();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}
}
