package cn.java.demo.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

public class WebUtilx {

	/**
	 * 获取上传文件的文件名 part.getSubmittedFileName() 在不同平台下有兼容性的问题
	 * 在jetty中没有实现part.getSubmittedFileName()，方法是part.getContentDispositionFilename()
	 * 在tomcat中有实现方法part.getSubmittedFileName()
	 */
	public static String getSubmittedFileName(Part part) {
		String disposition = part.getHeader("content-disposition");
		String filename = extractFilename(disposition, "filename="); // 文件名
		if (filename == null) {
			filename = extractFilename(disposition, "filename*=");
			if (filename == null) {
				return null;
			}
			int index = filename.indexOf("'");
			if (index != -1) {
				Charset charset = null;
				try {
					charset = Charset.forName(filename.substring(0, index));
				} catch (IllegalArgumentException ex) {
					// ignore
				}
				filename = filename.substring(index + 1);
				// Skip language information..
				index = filename.indexOf("'");
				if (index != -1) {
					filename = filename.substring(index + 1);
				}
				if (charset != null) {
					filename = new String(filename.getBytes(Charset.forName("us-ascii")), charset);
				}
			}
			return filename;
		}
		return filename;
	}

	private static String extractFilename(String contentDisposition, String key) {
		if (contentDisposition == null) {
			return null;
		}
		int startIndex = contentDisposition.indexOf(key); // key === "filename="
		if (startIndex == -1) {
			return null;
		}
		String filename = contentDisposition.substring(startIndex + key.length());
		if (filename.startsWith("\"")) {
			int endIndex = filename.indexOf("\"", 1);
			if (endIndex != -1) {
				return filename.substring(1, endIndex);
			}
		} else {
			int endIndex = filename.indexOf(";");
			if (endIndex != -1) {
				return filename.substring(0, endIndex);
			}
		}
		return filename;
	}

	public static int copy(InputStream in, OutputStream out) throws IOException {
		final int BUFFER_SIZE = 4096;
		int byteCount = 0;
		byte[] buffer = new byte[BUFFER_SIZE];
		int bytesRead = -1;
		while ((bytesRead = in.read(buffer)) != -1) {
			out.write(buffer, 0, bytesRead);
			byteCount += bytesRead;
		}
		out.flush();
		return byteCount;
	}

	/**
	 * 上传文件保存名
	 */
	public static String getSaveFileName(HttpServletRequest request, String originalFilename){
		return getSaveFileName(request, originalFilename, "/upload");
	}
	public static String getSaveFileName(HttpServletRequest request, String originalFilename, String uploadSubDir)
	{
		// 文件名
		String fileName = "";
		{
			fileName = UUID.randomUUID().toString();
			if (originalFilename.lastIndexOf(".") > 0) {
				String extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1,
						originalFilename.length());
				fileName = fileName + "." + extension;
			}
		}

		// 保存路径
		String saveDirRealpath = null;
		try {
			saveDirRealpath = request.getServletContext().getResource(uploadSubDir).getFile();
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
		File saveDirRealpathFile = new File(saveDirRealpath);
		if (saveDirRealpathFile.exists() && saveDirRealpathFile.isDirectory()) {
			return saveDirRealpath + "/" + fileName;
		} else {
			if (!saveDirRealpathFile.mkdirs()) {
				throw new RuntimeException(saveDirRealpath + " dir make failure.");
			}
			return saveDirRealpath + "/" + fileName;
		}
	}

}
