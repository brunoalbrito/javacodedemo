package cn.java.note.serviceloader;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class Test {

	public static void main(String[] args) throws IOException {
		WebappServiceLoader<ServletContainerInitializer>  loader = new WebappServiceLoader<>();
		List<ServletContainerInitializer> detectedScis = loader.load(ServletContainerInitializer.class);
		
		for (Iterator detectedSci = detectedScis.iterator(); detectedSci.hasNext();) {
			ServletContainerInitializer servletContainerInitializer = (ServletContainerInitializer) detectedSci.next();
			servletContainerInitializer.onStartup(null, null);
		}
	}

}
