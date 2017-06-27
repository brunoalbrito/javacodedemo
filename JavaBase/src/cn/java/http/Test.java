package cn.java.http;

import java.util.LinkedList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class Test {
//	echo var_export($_POST,true);
//	echo 'this is index.';
//	die();
	public static void main(String[] args) {
		
		// 请求地址
		String serverUrl = "http://my.pangu.com/";
		
		// 请求参数
		List<NameValuePair> nvps = new LinkedList<NameValuePair>();
		nvps.add(new BasicNameValuePair("field0", "field0_value"));
		nvps.add(new BasicNameValuePair("field1", "field1_value"));
		
		// 执行请求
		String charset = "utf-8";
		try {
			byte[] b = HttpClient4Util.getInstance().doPost(serverUrl, null, nvps);
			String respStr = new String(b, charset);
			System.out.println(respStr);
		} catch (Exception exception) {
			System.out.println(exception);
		}
		
	}

}
