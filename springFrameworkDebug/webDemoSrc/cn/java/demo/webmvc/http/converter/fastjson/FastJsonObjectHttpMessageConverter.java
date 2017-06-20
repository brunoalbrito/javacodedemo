package cn.java.demo.webmvc.http.converter.fastjson;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;

public class FastJsonObjectHttpMessageConverter implements HttpMessageConverter<FastJsonObject> {

	private static final String FAST_JSON_FLAG = "/*codec_is_fastjson*/"; // 标记位
	private List<MediaType> supportedMediaTypes = Collections.emptyList();
	
	
	public FastJsonObjectHttpMessageConverter() {
		super();
		this.setSupportedMediaTypes(Arrays.asList(new MediaType[]{
				MediaType.parseMediaType(MediaType.APPLICATION_JSON_UTF8_VALUE),
				new MediaType("application", "fastjson+json")}));
	}

	protected boolean canRead(MediaType mediaType) {
		if (mediaType == null) {
			return true;
		}
		for (MediaType supportedMediaType : getSupportedMediaTypes()) {
			if (supportedMediaType.includes(mediaType)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean canRead(Class<?> clazz, MediaType mediaType) {
		if (canRead(mediaType) && (FastJsonObject.class.isAssignableFrom(clazz))) {
			return true;
		}
		return false;
	}

	@Override
	public boolean canWrite(Class<?> clazz, MediaType mediaType) {
		if (canRead(mediaType) && (FastJsonObject.class.isAssignableFrom(clazz))) {
			return true;
		}
		return false;
	}

	/**
	 */
	public void setSupportedMediaTypes(List<MediaType> supportedMediaTypes) {
		Assert.notEmpty(supportedMediaTypes, "'supportedMediaTypes' must not be empty");
		this.supportedMediaTypes = new ArrayList<MediaType>(supportedMediaTypes);
	}
	
	@Override
	public List<MediaType> getSupportedMediaTypes() {
		return Collections.unmodifiableList(this.supportedMediaTypes);
	}

	@Override
	public FastJsonObject read(Class<? extends FastJsonObject> clazz, HttpInputMessage inputMessage)
			throws IOException, HttpMessageNotReadableException {
		if (!FastJsonObject.class.isAssignableFrom(clazz)) {
			throw new HttpMessageNotReadableException("clazz is not assignable from FastJsonObject.");
		}
		InputStream inputStream = inputMessage.getBody();
		HttpHeaders httpHeaders = inputMessage.getHeaders();
		byte[] buffer = new byte[1024];
		long inputByteLen = 0;
		StringBuilder stringBuilder = new StringBuilder();
		if (inputStream.read(buffer) != -1) {
			stringBuilder.append(new String(buffer));
			inputByteLen += buffer.length;
			if (inputByteLen > (1024 * 1024 * 20)) {
				throw new HttpMessageNotReadableException("inputStream is over " + inputByteLen + " bytes.");
			}
		}
		String jsonStr = stringBuilder.toString().trim();
		if (!jsonStr.startsWith(FAST_JSON_FLAG)) {
			throw new HttpMessageNotReadableException("inputStream is not starts with \"/* codec is fastjson */\" .");
		}
		return JSON.parseObject(jsonStr, FastJsonObject.class);
	}
	
	@Override
	public void write(FastJsonObject jsonObject, MediaType contentType, HttpOutputMessage outputMessage)
			throws IOException, HttpMessageNotWritableException {
		{
			outputMessage.getHeaders().setContentType(MediaType.APPLICATION_JSON_UTF8);
		}

		{
			String jsonStr = jsonObject.toJSONString();
			jsonStr = FAST_JSON_FLAG + jsonStr;
			outputMessage.getBody().write(jsonStr.getBytes());
		}
	}

}
