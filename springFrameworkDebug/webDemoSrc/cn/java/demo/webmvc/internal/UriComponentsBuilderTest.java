package cn.java.demo.webmvc.internal;

import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

public class UriComponentsBuilderTest {

	public static void main(String[] args) {
		String targetUrl = "http://www.baidu.com/dir1/dir2?param0=value0#fragment0";
		UriComponents uriComponents = UriComponentsBuilder.fromUriString(targetUrl).build();
		System.out.println("uriComponents.getScheme() = " + uriComponents.getScheme());
		System.out.println("uriComponents.getHost() = " + uriComponents.getHost());
		System.out.println("uriComponents.getPort() = " + uriComponents.getPort());
		System.out.println("uriComponents.getPath() = " + uriComponents.getPath());
		System.out.println("uriComponents.getQueryParams() = " + uriComponents.getQueryParams());
		System.out.println("uriComponents.getFragment() = " + uriComponents.getFragment());
	}

}
