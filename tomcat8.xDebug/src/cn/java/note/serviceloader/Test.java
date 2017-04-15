package cn.java.note.serviceloader;

import java.io.IOException;
import java.util.List;

public class Test {

	public static void main(String[] args) throws IOException {
		WebappServiceLoader<ServletContainerInitializer>  loader = new WebappServiceLoader<>();
		List<ServletContainerInitializer> detectedScis = loader.load(ServletContainerInitializer.class);
	}

}
