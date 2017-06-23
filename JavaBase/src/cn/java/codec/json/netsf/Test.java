package cn.java.codec.json.netsf;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

public class Test {

	public static void main(String[] args) {
		System.out.println("code in : "+Test.class.getName());
		String jsonStr = testToJsonStr();
		testToObject(jsonStr);
	}

	/**
	 * 转成json字符串
	 * @return
	 */
	private static String testToJsonStr() {  
		System.out.println("-----------“Java对象”转成“json字符串”--------------");  
		JSONObject jsonObject = new JSONObject();    
		jsonObject.put("code", 200);
		jsonObject.put("message", "消息内容");
		Map data = new HashMap();  
		{
			// 简单类型
			data.put("intValue", 201);
			data.put("stringValue", "key2_value");
			
			// list类型
			ArrayList list = new ArrayList();
			list.add("element1");
			list.add("element2");
			list.add("element3");
			data.put("list",list);
			
			// map类型
			Map<String, Object> map= new HashMap<String, Object>();
			map.put("map_intValue", 201);
			map.put("map_stringValue", "key2_value");
			data.put("map", map);
		}
		jsonObject.put("data", data);
		String jsonStr = jsonObject.toString();
		System.out.println(jsonStr);
		return jsonStr;
	}
	
	private static Map<String, Object> testToObject(String jsonStr){
		System.out.println("-----------“json字符”串转成“Java对象”--------------");
		JSONObject object = JSONObject.fromObject("/* commnet */"+jsonStr);
		System.out.println(object.getInt("code"));
		System.out.println(object.getString("message"));
		System.out.println(object.getJSONObject("data"));
		System.out.println(object.getJSONObject("data").getString("stringValue"));
		System.out.println(object);
		
		return null;  
	}
}
