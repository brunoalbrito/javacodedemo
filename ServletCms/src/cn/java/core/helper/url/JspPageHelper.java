package cn.java.core.helper.url;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspWriter;

public class JspPageHelper {

	/**
	 * 输出request中的变量
	 * @param keyName
	 * @param request
	 * @param out
	 * @throws IOException
	 */
	public static void write(String keyName, HttpServletRequest request, JspWriter out) throws IOException {
		if (request.getAttribute(keyName) != null && !"".equals(request.getAttribute(keyName))) {
			out.print(request.getAttribute(keyName));
		}
	}

	/**
	 * 取得request中的变量
	 * @param keyName
	 * @param request
	 * @return
	 */
	public static String getString(String keyName, HttpServletRequest request) {
		if (request.getAttribute(keyName) != null && !"".equals(request.getAttribute(keyName))) {
			return (String) request.getAttribute(keyName);
		}
		return "";
	}
	
	/**
	 * 取得request中的变量
	 * @param keyName
	 * @param request
	 * @return
	 */
	public static HashMap getHashMap(String keyName, HttpServletRequest request) {
		if (request.getAttribute(keyName) != null && !"".equals(request.getAttribute(keyName))) {
			return (HashMap) request.getAttribute(keyName);
		}
		return null;
	}

	/**
	 * 检查request中是否存在指定key
	 * @param keyName
	 * @param request
	 * @return
	 */
	public static boolean isEmpty(String keyName, HttpServletRequest request) {
		if (request.getAttribute(keyName) != null && !"".equals(request.getAttribute(keyName))) {
			return false;
		}
		return true;
	}
	
	/**
	 * 取得hashmap中指定key的值
	 * @param keyName
	 * @param hashMap
	 * @return
	 */
	public static String getString(String keyName, HashMap hashMap) {
		if(hashMap==null)
			return "";
		if (keyName.indexOf(".") != -1) {
			String[] strSplit = keyName.split("\\.");
			if (hashMap != null && hashMap.containsKey(strSplit[0])) {
				HashMap hashMapTemp = (HashMap) hashMap.get(strSplit[0]);
				if (hashMapTemp.containsKey(strSplit[1]))
					return hashMapTemp.get(strSplit[1]).toString();
			} else {
				return "";
			}
		} else {
			if (hashMap != null && hashMap.containsKey(keyName)) {
				if(hashMap.get(keyName)==null)
					return "";
				return hashMap.get(keyName).toString();
			}
		}
		return "";
	}

	/**
	 * 检查HashMap中是否存在指定key的记录
	 * @param keyName
	 * @param hashMap
	 * @return
	 */
	public static boolean isEmpty(String keyName, HashMap hashMap) {
		if (keyName.indexOf(".") != -1) {
			String[] strSplit = keyName.split("\\.");
			if (hashMap != null && hashMap.containsKey(strSplit[0])) {
				HashMap hashMapTemp = (HashMap) hashMap.get(strSplit[0]);
				if (hashMapTemp.containsKey(strSplit[1]))
					return false;
			}
		}
		else{
			if (hashMap != null && hashMap.containsKey(keyName)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 格式时间
	 * @param timestamp
	 * @return
	 */
	public static String timestampFormat(String timestamp){
		if(timestamp==null || timestamp == ""){
			return "-";
		}
		long microTime = Long.valueOf(timestamp) * 1000;
		return (new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date(microTime));
	}
	
	
	/**
	 * 图片缩略图
	 * @param timestamp
	 * @return
	 */
	public static String thumbImgAuto(String imageStr){
		if(imageStr==null || imageStr == ""){
			return "";
		}
		return imageStr;
	}
}
