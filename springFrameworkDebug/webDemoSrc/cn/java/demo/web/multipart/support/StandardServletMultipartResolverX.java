package cn.java.demo.web.multipart.support;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.Part;

public class StandardServletMultipartResolverX {
	
	
	public boolean isMultipart(HttpServletRequest request) {
		// Same check as in Commons FileUpload...
		if (!"post".equals(request.getMethod().toLowerCase())) {
			return false;
		}
		String contentType = request.getContentType();
		return (contentType != null && contentType.toLowerCase().startsWith("multipart/"));
	}
	
	public StandardMultipartHttpServletRequestX resolveMultipart(HttpServletRequest request) throws RuntimeException {
		return new StandardMultipartHttpServletRequestX(request);
	}
	
	/**
	 * Spring MultipartFile adapter, wrapping a Servlet 3.0 Part object.
	 */
	@SuppressWarnings("serial")
	public static class StandardMultipartHttpServletRequestX extends HttpServletRequestWrapper {
		
		public StandardMultipartHttpServletRequestX(HttpServletRequest request) {
			super(request);
			if(true){
				parseRequest(request);
			}
		}

		private Set<String> multipartParameterNames;
		private LinkedHashMap<String, List<StandardMultipartFileX>> multipartFiles;
		
		private static final String CONTENT_DISPOSITION = "content-disposition";
		private static final String FILENAME_KEY = "filename=";
		private static final String FILENAME_WITH_CHARSET_KEY = "filename*=";
		private static final Charset US_ASCII = Charset.forName("us-ascii");
		
		private void parseRequest(HttpServletRequest request) {
			try {
				Collection<Part> parts = request.getParts();
				this.multipartParameterNames = new LinkedHashSet<String>(parts.size());
				LinkedHashMap<String, List<StandardMultipartFileX>> files = new LinkedHashMap<String, List<StandardMultipartFileX>>(parts.size());
				for (Part part : parts) {
					String disposition = part.getHeader(CONTENT_DISPOSITION);
					String filename = extractFilename(disposition); // 文件名
					if (filename == null) {
						filename = extractFilenameWithCharset(disposition);
					}
					if (filename != null) {
						List<StandardMultipartFileX> values = files.get(filename);
						if (values == null) {
							values = new LinkedList<StandardMultipartFileX>();
							files.put(filename, values);
						}
						values.add(new StandardMultipartFileX(part, filename));
					}
					else {
						this.multipartParameterNames.add(part.getName()); // 普通类型
					}
				}
				setMultipartFiles(files);
			}
			catch (Throwable ex) {
				throw new RuntimeException("Could not parse multipart servlet request", ex);
			}
		}
		
		@Override
		public HttpServletRequest getRequest() {
			return (HttpServletRequest) super.getRequest();
		}
		
		protected void initializeMultipart() {
			parseRequest(getRequest());
		}
		
		private Enumeration<String> getParameterNamesInternal() {
			return getRequest().getParameterNames();
		}
		
		public Enumeration<String> getParameterNames() {
			if (this.multipartParameterNames == null) {
				initializeMultipart();
			}
			if (this.multipartParameterNames.isEmpty()) {
				return this.getParameterNamesInternal();
			}

			// Servlet 3.0 getParameterNames() not guaranteed to include multipart form items
			// (e.g. on WebLogic 12) -> need to merge them here to be on the safe side
			Set<String> paramNames = new LinkedHashSet<String>();
			Enumeration<String> paramEnum = this.getParameterNamesInternal();
			while (paramEnum.hasMoreElements()) {
				paramNames.add(paramEnum.nextElement());
			}
			paramNames.addAll(this.multipartParameterNames);
			return Collections.enumeration(paramNames);
		}
		
		private Map<String, String[]> getParameterMapInternal() {
			return getRequest().getParameterMap();
		}
		
		private String[] getParameterValuesInternal(String name) {
			return getRequest().getParameterValues(name);
		}
		
		public Map<String, String[]> getParameterMap() {
			if (this.multipartParameterNames == null) {
				initializeMultipart();
			}
			if (this.multipartParameterNames.isEmpty()) {
				return this.getParameterMapInternal();
			}

			// Servlet 3.0 getParameterMap() not guaranteed to include multipart form items
			// (e.g. on WebLogic 12) -> need to merge them here to be on the safe side
			Map<String, String[]> paramMap = new LinkedHashMap<String, String[]>();
			paramMap.putAll(this.getParameterMapInternal());
			for (String paramName : this.multipartParameterNames) { // 普通类型
				if (!paramMap.containsKey(paramName)) {
					paramMap.put(paramName, getParameterValuesInternal(paramName));
				}
			}
			return paramMap;
		}

		/**
		 * 指定字段的类型
		 */
		public String getMultipartContentType(String paramOrFileName) {
			try {
				Part part = getPart(paramOrFileName);
				return (part != null ? part.getContentType() : null);
			}
			catch (Throwable ex) {
				throw new RuntimeException("Could not access multipart servlet request", ex);
			}
		}

		/**
		 * 获取头
		 */
		public Map<String, List<String>> getMultipartHeaders(String paramOrFileName) {
			try {
				Part part = getPart(paramOrFileName);
				if (part != null) {
					Map<String, List<String>> headers = new LinkedHashMap<String, List<String>>(8);
					for (String headerName : part.getHeaderNames()) {
						headers.put(headerName, new ArrayList<String>(part.getHeaders(headerName)));
					}
					return headers;
				}
				else {
					return null;
				}
			}
			catch (Throwable ex) {
				throw new RuntimeException("Could not access multipart servlet request", ex);
			}
		}
		
		/**
		 * Set a Map with parameter names as keys and list of MultipartFile objects as values.
		 * To be invoked by subclasses on initialization.
		 */
		protected final void setMultipartFiles(LinkedHashMap<String, List<StandardMultipartFileX>> multipartFiles) {
			this.multipartFiles =
					new LinkedHashMap<String, List<StandardMultipartFileX>>(Collections.unmodifiableMap(multipartFiles));
		}
		
		protected Map<String, List<StandardMultipartFileX>> getMultipartFilesInternal() {
			if (this.multipartFiles == null) {
				initializeMultipart();
			}
			return this.multipartFiles;
		}
		
		/**
		 * 获取文件map列表
		 * @return
		 */
		public Map<String, StandardMultipartFileX> getFileMap() {
			Map<String, List<StandardMultipartFileX>> multipartFilesTemp = getMultipartFilesInternal();
			LinkedHashMap<String, StandardMultipartFileX> singleValueMap = new LinkedHashMap<String,StandardMultipartFileX>(multipartFilesTemp.size());
			for (Entry<String, List<StandardMultipartFileX>> entry : multipartFilesTemp.entrySet()) {
				singleValueMap.put(entry.getKey(), entry.getValue().get(0));
			}
			return singleValueMap;
		}
		
		/**
		 * 获取文件map列表
		 */
		public Map<String, List<StandardMultipartFileX>> getMultiFileMap() {
			return getMultipartFilesInternal();
		}
		
		private String extractFilename(String contentDisposition) {
			return extractFilename(contentDisposition, FILENAME_KEY);
		}

		private String extractFilenameWithCharset(String contentDisposition) {
			String filename = extractFilename(contentDisposition, FILENAME_WITH_CHARSET_KEY);
			if (filename == null) {
				return null;
			}
			int index = filename.indexOf("'");
			if (index != -1) {
				Charset charset = null;
				try {
					charset = Charset.forName(filename.substring(0, index));
				}
				catch (IllegalArgumentException ex) {
					// ignore
				}
				filename = filename.substring(index + 1);
				// Skip language information..
				index = filename.indexOf("'");
				if (index != -1) {
					filename = filename.substring(index + 1);
				}
				if (charset != null) {
					filename = new String(filename.getBytes(US_ASCII), charset);
				}
			}
			return filename;
		}
		
		private String extractFilename(String contentDisposition, String key) {
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
			}
			else {
				int endIndex = filename.indexOf(";");
				if (endIndex != -1) {
					return filename.substring(0, endIndex);
				}
			}
			return filename;
		}
		
		
	}
	
	public static class StandardMultipartFileX implements Serializable {
		private final Part part;

		private final String filename;

		public StandardMultipartFileX(Part part, String filename) {
			this.part = part;
			this.filename = filename;
		}

		public String getName() {
			return this.part.getName();
		}

		public String getOriginalFilename() {
			return this.filename;
		}

		public String getContentType() {
			return this.part.getContentType();
		}

		public boolean isEmpty() {
			return (this.part.getSize() == 0);
		}

		public long getSize() {
			return this.part.getSize();
		}

		public byte[] getBytes() throws IOException {
			// FileCopyUtils.copyToByteArray(this.part.getInputStream());
			return copyToByteArray(this.part.getInputStream());
		}

		public InputStream getInputStream() throws IOException {
			return this.part.getInputStream();
		}

		public void transferTo(File dest) throws IOException, IllegalStateException {
			this.part.write(dest.getPath());
		}
	}
	
	public static final int BUFFER_SIZE = 4096;
	private static byte[] copyToByteArray(InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream(BUFFER_SIZE);
		copy(in, out);
		return out.toByteArray();
	}
	
	private static int copy(InputStream in, OutputStream out) throws IOException {
		try {
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
		finally {
			try {
				in.close();
			}
			catch (IOException ex) {
			}
			try {
				out.close();
			}
			catch (IOException ex) {
			}
		}
	}
}
