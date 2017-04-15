package cn.java.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class FastJsonTest {

	public static void main(String[] args) {
		jsonEncode();
		jsonDecode();
	}
	

	/**
	 * Json转换成字符串
	 */
	protected static void jsonEncode() {
		JSONObject mJSONObject = new JSONObject();
		JSONArray mJSONArray = new JSONArray();
		mJSONArray.add("1");
		mJSONArray.add(2);
		mJSONArray.add("3");
		mJSONObject.put("status", 1);
		mJSONObject.put("info", "成功！");
		mJSONObject.put("html", "<html><head></head><body><div class=\"divClas\">1111</div></body></html>");
		mJSONObject.put("data", mJSONArray);
		System.out.println(mJSONObject.toJSONString());
	}
	
	/**
	 * 字符串转成为Json
	 */
	protected static void jsonDecode() {
		String jsonStr = "{\"status\":1,\"data\":[\"1\",\"2\",\"3\"],\"info\":\"成功！\"}";
		JSONObject mJSONObject =  (JSONObject) JSON.parse(jsonStr);
		System.out.println(mJSONObject.get("status"));
		System.out.println(mJSONObject.get("info"));
		JSONArray mJSONArray = (JSONArray) mJSONObject.get("data");
		for (Object str : mJSONArray) {
			System.out.println(str);
		}
	}

}
