package cn.java.util;

import java.util.Date;

public class FormatUtil {
	/**
	 * 格式时间
	 * @param timestamp
	 * @return
	 */
	public static String timestampFormat(String timestamp){
		return timestampFormat(timestamp, "yyyy-MM-dd HH:mm:ss");
	}
	
	/**
	 * 格式时间
	 * @param timestamp
	 * @return
	 */
	public static String timestampFormat(String timestamp,String pattern){
		if(timestamp==null || "".equals(timestamp) ){
			return "-";
		}
		long milliseconds = 0;
		if("1497423707039".length() == timestamp.length()){ //System.currentTimeMillis() 从1970到现在的微秒数
			milliseconds = Long.valueOf(timestamp);
		}
		else if("1229256648929836".length() == timestamp.length()){ // System.nanoTime() 不是有效的时间
			return "-";
		}
		else{
			milliseconds = Long.valueOf(timestamp) * 1000; // 从1970到现在的秒数
		}
		return (new java.text.SimpleDateFormat()).format(new Date(milliseconds));
	}
}
