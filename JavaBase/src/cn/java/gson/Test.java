package cn.java.gson;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Test {

	public static void main(String[] args) {
		dataType();
		dataObj();
		dataArray();
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

}
