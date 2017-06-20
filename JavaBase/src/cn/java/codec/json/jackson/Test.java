package cn.java.codec.json.jackson;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.map.ObjectMapper;
public class Test {

	public static void main(String[] args) {
		String jsonStr = testToJsonStr();
		testToObject(jsonStr);
	}
	
	/**
	 * 转成json字符串
	 * @return
	 */
	private static String testToJsonStr() {  
		System.out.println("-----------“Java对象”转成“json字符串”--------------");  
		Map<String, Object> params = new HashMap<String, Object>();    
		params.put("code", 200);
		params.put("message", "消息内容");
		Map<String, Object> data = new HashMap<String, Object>();  
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
		params.put("data", data);

		StringWriter stringWriter = new StringWriter();
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			if(objectMapper.canSerialize(params.getClass())){
				objectMapper.writeValue(stringWriter, params);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		System.out.println(stringWriter);
		return stringWriter.toString();
	}
	
	/**
	 * 转成json对象
	 */
	private static Map<String, Object> testToObject(String jsonStr){
		System.out.println("-----------“json字符”串转成“Java对象”--------------");  
        Map<String, Object> maps;
		try {
			ObjectMapper objectMapper = new ObjectMapper();
//			if(objectMapper.canDeserialize(Map.class)){
				maps = objectMapper.readValue(jsonStr, Map.class);
//			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
        Set<String> key = maps.keySet();
        Iterator<String> iter = key.iterator();
        while (iter.hasNext()) {
            String field = iter.next();
            System.out.println(field + ":" + maps.get(field));
        }
        System.out.println(maps);
        return maps;
	}

}
