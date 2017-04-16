package cn.java.note;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.catalina.startup.Constants;

public class ParseWar {

	public static void main(String[] args) {
		File file = new File("webapps");
		String appBase = "d:/a/b/c";
		if (!file.isAbsolute()) {
			file = new File(appBase, file.getPath());
		}
		try {
			file = file.getCanonicalFile();
		} catch (IOException ioe) {
		}
		String[] files = file.list();
		for (int i = 0; i < files.length; i++) {

			if (files[i].equalsIgnoreCase("META-INF"))
				continue;
			if (files[i].equalsIgnoreCase("WEB-INF"))
				continue;
			File war = new File(appBase, files[i]);
			if (files[i].toLowerCase(Locale.ENGLISH).endsWith(".war") &&
					war.isFile()) {

			}
		}
		// ---------------------
		String war = "d:/a/b/c/test.war";
		boolean xmlInWar = false;
		try (JarFile jar = new JarFile(war)) {
			JarEntry entry = jar.getJarEntry("META-INF/context.xml");
			if (entry != null) {
				xmlInWar = true;
			}
		} catch (IOException e) {
			/* Ignore */
		}
	}
	
	public static void temp(){
		File xml = new File("d:/a/b/tomcat/conf/Catalina/localhost", "context1.xml");
        try (JarFile jar = new JarFile("d:/a/b/c/context1.war")) {
            JarEntry entry = jar.getJarEntry("META-INF/context.xml");
            try (InputStream istream = jar.getInputStream(entry);
                    FileOutputStream fos = new FileOutputStream(xml);
                    BufferedOutputStream ostream = new BufferedOutputStream(fos, 1024)) {
                byte buffer[] = new byte[1024];
                while (true) {
                    int n = istream.read(buffer);
                    if (n < 0) {
                        break;
                    }
                    ostream.write(buffer, 0, n);
                }
                ostream.flush();
            }
        } catch (IOException e) {
            /* Ignore */
        }
	}

}
