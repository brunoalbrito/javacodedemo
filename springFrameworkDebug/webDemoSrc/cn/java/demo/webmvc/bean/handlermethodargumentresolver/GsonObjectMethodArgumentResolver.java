package cn.java.demo.webmvc.bean.handlermethodargumentresolver;

import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.ServletRequestMethodArgumentResolver;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 输入流转成json对象
 * @author zhouzhian
 */
public class GsonObjectMethodArgumentResolver extends ServletRequestMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		Class<?> paramType = parameter.getParameterType(); // 参数类型
		return (GsonObject.class.isAssignableFrom(paramType));
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		Class<?> paramType = parameter.getParameterType();
		if (WebRequest.class.isAssignableFrom(paramType)) {
			return webRequest;
		}
		HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
		InputStream inputStream = request.getInputStream();
		byte[] byteBuffer = new byte[1024];
		StringBuilder stringBuilder = new StringBuilder();
		while (inputStream.read(byteBuffer)>0) {
			stringBuilder.append(new String(byteBuffer));
			byteBuffer = new byte[1024];
		}
		String jsonStr = stringBuilder.toString().trim();
		return new Gson().fromJson(jsonStr,new TypeToken<GsonObject<String,Object>>(){}.getType());
	}
}
