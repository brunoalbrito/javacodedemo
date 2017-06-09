package cn.java.demo.beantag.internal;

import java.text.MessageFormat;
import java.util.Locale;

public class MessageFormatTest {

	public static void main(String[] args) {
		{
			String msgFormat = "hello";
			Locale localeFormat = null;
			Object[] argsFormat = null;
			MessageFormat messageFormat = new MessageFormat((msgFormat != null ? msgFormat : ""), localeFormat);
			System.out.println(messageFormat.format(args));
		}

		{
			int fileCount = 1273;
			String diskName = "MyDisk";
			Object[] testArgs = { new Long(fileCount), diskName };
			MessageFormat messageFormat = new MessageFormat("The disk \"{1}\" contains {0} file(s).");
			System.out.println(messageFormat.format(testArgs));
		}
	}

}
