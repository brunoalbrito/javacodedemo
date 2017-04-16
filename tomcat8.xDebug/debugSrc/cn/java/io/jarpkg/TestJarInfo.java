package cn.java.io.jarpkg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;

public class TestJarInfo {

	public static void main(String[] args) {

	}

	public void test() throws IOException{
		ClassLoader mClassLoader = this.getClass().getClassLoader();
		Enumeration<URL> resources = mClassLoader.getResources("META-INF/services/javax.servlet.ServletContainerInitializer");
		while (resources.hasMoreElements()) {
			URL url = resources.nextElement();
			try (InputStream is = url.openStream();
					InputStreamReader in = new InputStreamReader(is, StandardCharsets.UTF_8);
					BufferedReader reader = new BufferedReader(in);
				) {
				String line;
				while ((line = reader.readLine()) != null) {
					int i = line.indexOf('#');
					if (i >= 0) {
						line = line.substring(0, i);
					}
					line = line.trim();
					if (line.length() == 0) {
						continue;
					}
					System.out.println("行: "+line);
				}
			}
		}
	}

}
