package cn.note;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JspServlet {

	public static void main(String[] args) {
		JspServlet jspServlet = new JspServlet();
		String javaFileFullPath = jspServlet.transfromJsp2Java();
		jspServlet.compliedJava2Class(javaFileFullPath);
	}

	public void invokeJsp() {
		this.transfromJsp2Java();
		
		this.invokeClass();
	}

	/**
	 * 把jsp文件转换成java文件
	 */
	private String transfromJsp2Java() {
		URL url = this.getClass().getClassLoader().getResource("");

		String dirFullPath = url.getPath().replaceFirst("/", "")
				.replace("/", File.separator);

		String packageName = this.getClass().getPackage().getName()
				.replace(".", File.separator);

		String jspName = "test.jsp";
		String javaName = "test.java";
		try {
			String jspFileFullPath = dirFullPath + packageName + File.separator
					+ jspName;
			String javaFileFullPath = dirFullPath + packageName
					+ File.separator + javaName;
			File jspFile = new File(jspFileFullPath);
			File javaFile = new File(javaFileFullPath);
			FileInputStream fileInputStream = new FileInputStream(jspFile);
			FileOutputStream fileOutputStream = new FileOutputStream(javaFile);
			byte[] byteArray = new byte[(int) jspFile.length()];
			byte byteTemp;
			int i = 0;
			while ((byteTemp = (byte) fileInputStream.read()) != -1) {
				byteArray[i] = byteTemp;
				i++;
			}
			fileInputStream.close();
			String jspContent = new String(byteArray);
//			System.out.println(jspContent);
			String javaContent = "";
			Matcher matcher = Pattern.compile("<%(.*)+%>").matcher(jspContent);
			if (matcher.matches()) {
				int matcherCount = matcher.groupCount();
				for (int j = 0; j < matcherCount; j++) {
					javaContent += matcher.group(j).replaceFirst("<%", "")
							.replaceFirst("%>", "").trim();
				}
			}
//			System.out.println(javaContent);
			byte[] writeCharTemp = javaContent.getBytes();
			for (int j = 0; j < writeCharTemp.length; j++) {
				fileOutputStream.write(writeCharTemp[j]);
			}
			fileOutputStream.flush();
			fileOutputStream.close();
			return javaFileFullPath;
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return "";
	}

	/**
	 * 把java文件编译成class文件
	 */
	public void compliedJava2Class(String javaFileFullPath) {
	}

	/**
	 * 加载class文件，执行
	 * 
	 */
	public void invokeClass() {
		String classPath = "";
		try {
			Class.forName(classPath);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
