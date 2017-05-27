package cn.java.codec.json.gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

public class Test {

	public static void main(String[] args) {
		dataType();
		dataObj();
		dataArray();
		testEncodeDecode();
	}

	public static void dataType() {
		String jsonStr = "{\"code\":200,\"message\":\"message\",\"data\":[]}";
		JsonObject jsonObject = (JsonObject) new JsonParser().parse(jsonStr);
		System.out.println(jsonObject.get("code"));
		System.out.println(jsonObject.get("message").toString());
		System.out.println(jsonObject.get("message").getAsString());
		String dataStr = jsonObject.get("data").toString();
		if ( "{}".equals(dataStr) || "[]".equals(dataStr) || ("" == dataStr)) {
			System.out.println("data is empty..");
		}
	}

	public static void dataObj() {
		String jsonStr = "{\"code\":2,\"message\":2,\"data\":{}}";
		JsonObject jsonObject = (JsonObject) new JsonParser().parse(jsonStr);
		for (java.util.Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
			System.out.print(entry.getKey() + "=" + entry.getValue() + "\t");
		}
		System.out.println();
	}

	public static void dataArray() {
		String jsonStr = "{\"code\":2,\"message\":2,\"data\":[]}";
		JsonObject jsonObject = (JsonObject) new JsonParser().parse(jsonStr);
		for (java.util.Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
			System.out.print(entry.getKey() + "=" + entry.getValue() + "\t");
		}
		System.out.println();
	}

	private static void testEncodeDecode(){  
		String jsonStr = testToJsonStr();
		Map<String, Object> result = testToJsonObject(jsonStr);
		// 缩进打印对象树  
        System.out.println(GsonObjectFormatUtil.indentShowMap(result,0));  
	}

	/**
	 * 转成json字符串
	 * @return
	 */
	private static String testToJsonStr(){  
		System.out.println("-----------toJson result，Java对象编码成 json字符串--------------");  
		Map<String, Object> params = new HashMap<String, Object>();    
		params.put("code", 200);    
		params.put("message", "消息内容");  

		Map<String, Object> data = new HashMap<String, Object>();  
		data.put("intValue", 201);    
		data.put("stringValue", "key2_value");  

		ArrayList list1 = new ArrayList();  
		list1.add("element1");  
		list1.add("element2");  
		list1.add("element3");  
		data.put("list1",list1);  

		Map<String, Object> map2= new HashMap<String, Object>();  
		map2.put("map2_intValue", 201);    
		map2.put("map2_stringValue", "key2_value");  
		data.put("map2", map2);  

		params.put("data", data);  

		String jsonStr = new Gson().toJson(params);  
		System.out.println(jsonStr);  
		return jsonStr;
	}
	
	/**
	 * 转成json对象
	 * @param jsonStr
	 * @return
	 */
	private static Map<String, Object> testToJsonObject(String jsonStr){
		System.out.println("-----------fromJson result，json字符串解码成Java对象--------------");  
		Map<String, Object> result = new Gson().fromJson(jsonStr,new TypeToken<Map<String,Object>>(){}.getType());  
		System.out.println(result.get("code"));  
		System.out.println(result.get("code").getClass());

		System.out.println(result.get("message"));  
		System.out.println(result.get("message").getClass());  

		Map<String, Object> resultData = (Map<String, Object>) result.get("data");  
		System.out.println("---------intValue----------------");  
		System.out.println(resultData.get("intValue"));  
		System.out.println(resultData.get("intValue").getClass());  

		System.out.println("---------stringValue----------------");  
		System.out.println(resultData.get("stringValue"));  
		System.out.println(resultData.get("stringValue").getClass());  

		System.out.println("---------LinkedTreeMap----------------");  
		System.out.println(result.get("data").getClass());  

		System.out.println("---------ArrayList----------------");  
		ArrayList resultArrayList = (ArrayList) resultData.get("list1");  
		Iterator iterator = resultArrayList.iterator();  
		while (iterator.hasNext()) {  
			System.out.println(iterator.next());  
		}  
		System.out.println(resultData.get("list1").getClass());  
		return result;
	}


}
