package cn.java.debug.note;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public class JarFileIoTest {

	public static void main(String[] args) throws Exception {
		JarFile jarFile = null;
		try {
			jarFile = new JarFile( "d:/dir1/test.jar" );
			Enumeration jarEntries = jarFile.entries();
			while ( jarEntries.hasMoreElements() ) {
				ZipEntry ze = (ZipEntry) jarEntries.nextElement();
				if ( ze.getName().endsWith( ".hbm.xml" ) ) {
					InputStream xmlInputStream = jarFile.getInputStream( ze );
				}
			}
		}
		finally {
			try {
				if ( jarFile != null ) {
					jarFile.close();
				}
			}
			catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}

}
