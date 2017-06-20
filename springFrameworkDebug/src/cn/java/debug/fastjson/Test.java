package cn.java.debug.fastjson;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;

public class Test {

	public static void main(String[] args) {
		// 转成json字符串
		Map map = new HashMap();
		map.put("status", "200");
		map.put("message", "success...");
		String jsonStr = JSON.toJSONString(map);
		System.out.println("jsonStr = " + jsonStr);
		
		// json字符串转成对象
		Map mapObj = JSON.parseObject(jsonStr,Map.class);
		System.out.println("mapObj = " + mapObj);
	}

}
