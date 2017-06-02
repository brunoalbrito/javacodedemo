package cn.java.demo.webmvc.http.converter.fastjson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

public class FastJsonObjectHttpMessageConverter implements HttpMessageConverter<FastJsonObject> {

	@Override
	public boolean canRead(Class<?> clazz, MediaType mediaType) {
		return false;
	}

	@Override
	public boolean canWrite(Class<?> clazz, MediaType mediaType) {
		if(FastJsonObject.class.isAssignableFrom(clazz)){
			return true;
		}
		return false;
	}

	@Override
	public List<MediaType> getSupportedMediaTypes() {
		List<MediaType> list = new ArrayList();
		list.add(MediaType.parseMediaType(MediaType.APPLICATION_JSON_UTF8_VALUE));
		return list;
	}

	@Override
	public FastJsonObject read(Class<? extends FastJsonObject> clazz, HttpInputMessage inputMessage)
			throws IOException, HttpMessageNotReadableException {
		if(FastJsonObject.class.isAssignableFrom(clazz)){
			
		}
		return (new FastJsonObject());
	}

	@Override
	public void write(FastJsonObject object, MediaType contentType, HttpOutputMessage outputMessage)
			throws IOException, HttpMessageNotWritableException {
		String jsonStr = object.toString();
		jsonStr = "/* codec is fastjson */" + jsonStr;
		outputMessage.getBody().write(jsonStr.getBytes());
	}


}
