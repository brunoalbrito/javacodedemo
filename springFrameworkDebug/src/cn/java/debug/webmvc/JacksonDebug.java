package cn.java.debug.webmvc;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class JacksonDebug {

	public static void main(String[] args) throws Exception {
		testXmlToObject();
	}
	
	public static void testXmlToObject() throws Exception {
		ObjectMapper objectMapper = null;
		{
			Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder().createXmlMapper(true);
			objectMapper = builder.build();
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
