package cn.java.codec.xml.jackson;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLResolver;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import cn.java.codec.xml.jackson.Test2.StringInputStream;

public class Test {
	private static XMLInputFactory inputFactory = null;
	static {
		inputFactory = XMLInputFactory.newInstance();
		inputFactory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
		inputFactory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
		inputFactory.setXMLResolver(new XMLResolver() {
			@Override
			public Object resolveEntity(String publicID, String systemID, String base, String ns) {
				return new ByteArrayInputStream(new byte[0]);
			}
		});
	}
	
	public static void main(String[] args) throws Exception {
		String xmlStr = testToXmlStr();
		testToObject(xmlStr);
		testXmlToObject();
	}
	
	/**
	 * 转成json字符串
	 * @return
	 */
	private static String testToXmlStr() {

		System.out.println("-----------“Java对象”转成“xml字符串”--------------");
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
			data.put("list", list);

			// map类型
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("map_intValue", 201);
			map.put("map_stringValue", "key2_value");
			data.put("map", map);
		}
		params.put("data", data);

		StringWriter stringWriter = new StringWriter();
		{
			ObjectMapper objectMapper = new XmlMapper(inputFactory);

			try {
				JsonGenerator generator = objectMapper.getFactory().createGenerator(stringWriter);
				objectMapper.writeValue(generator, params);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		System.out.println(stringWriter);
		return stringWriter.toString();
	}

	/**
	 * 转成java对象
	 * 
	 * @param jsonStr
	 * @return
	 */
	private static Map<String, Object> testToObject(String xmlStr) {
		System.out.println("-----------“xml字符”串转成“Java对象”（有bug，list不能正确解析出来）--------------");
		Map<String, Object> maps;
		try {
			ObjectMapper objectMapper = new XmlMapper(inputFactory);
			maps = objectMapper.readValue(xmlStr, Map.class);
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
	
	public static void testXmlToObject() throws Exception {
		ObjectMapper objectMapper = null;
		{
			objectMapper = new XmlMapper(inputFactory);
		}
		
		JavaType javaType = null;
		{
			TypeFactory typeFactory = objectMapper.getTypeFactory();
			javaType = typeFactory.constructType(Map.class);
		}
		
		boolean canDeserialize = false;
		{
			AtomicReference<Throwable> causeRef = new AtomicReference<Throwable>();
			canDeserialize = objectMapper.canDeserialize(javaType, causeRef);
		}
		
		if(canDeserialize)
		{
			InputStream inputStream = new StringInputStream("<?xml version=\"1.0\" ?><xml><status>200</status><message>消息内容...</message></xml>");
			Object object = objectMapper.readValue(inputStream, javaType);
			System.out.println(object);
		}
	}

	public static class StringInputStream extends InputStream {
		protected int strOffset = 0;
		protected int charOffset = 0;
		protected int available;
		protected String str;

		public StringInputStream(String arg0) {
			this.str = arg0;
			this.available = arg0.length() * 2;
		}

		public int read() throws IOException {
			if (this.available == 0) {
				return -1;
			} else {
				--this.available;
				char arg0 = this.str.charAt(this.strOffset);
				if (this.charOffset == 0) {
					this.charOffset = 1;
					return (arg0 & '＀') >> 8;
				} else {
					this.charOffset = 0;
					++this.strOffset;
					return arg0 & 255;
				}
			}
		}

		public int available() throws IOException {
			return this.available;
		}
	}
}
