package cn.java.jfinal.servlets;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UploadPrototypeServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		try {
			if (request.getContentLength() > 297) {
				InputStream in = request.getInputStream();
				File f = new File("d:/temp", "test.txt");
				FileOutputStream o = new FileOutputStream(f);
				byte b[] = new byte[1024];
				int n;
				while ((n = in.read(b)) != -1) {
					o.write(b, 0, n);
				}
				o.close();
				in.close();

				out.print("File upload success!");
			} else {
				out.print("No file!");
			}
		} catch (IOException e) {
			out.print("upload error.");
			e.printStackTrace();
		}
	}

	private void doPost2(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String tempFileName = (String) request.getSession().getId();
		// create the tempfile.
		File temp = new File("d:/temp", tempFileName);
		FileOutputStream o = new FileOutputStream(temp);
		if (request.getContentLength() > 297) {
			// write theupload content to the temp file.
			InputStream in = request.getInputStream();
			byte b[] = new byte[1024];
			int n;
			while ((n = in.read(b)) != -1) {
				o.write(b, 0, n);
			}
			o.close();
			in.close();
			// read the tempfile.
			RandomAccessFile random = new RandomAccessFile(temp, "r");
			// read Line2 tofind the name of the upload file.
			int second = 1;
			String secondLine = null;
			while (second <= 2) {
				secondLine = random.readLine();
				second++;
			}

			// get the lastlocation of the dir char.'\\'.
			int position = secondLine.lastIndexOf('\\');
			// get the nameof the upload file.
			String fileName = secondLine.substring(position + 1,
					secondLine.length() - 1);
			// relocate tothe head of file.
			random.seek(0);
			// get thelocation of the char.'Enter' in Line4.
			long forthEndPosition = 0;
			int forth = 1;
			while ((n = random.readByte()) != -1 && (forth <= 4)) {
				if (n == '\n') {
					forthEndPosition = random.getFilePointer();
					forth++;
				}
			}
			File realFile = new File("d:/temp", fileName);
			RandomAccessFile random2 = new RandomAccessFile(realFile, "rw");
			// locate the endposition of the content.Count backwards 6 lines.
			random.seek(random.length());
			long endPosition = random.getFilePointer();
			long mark = endPosition;
			int j = 1;
			while ((mark >= 0) && (j <= 6)) {
				mark--;
				random.seek(mark);
				n = random.readByte();
				if (n == '\n') {
					endPosition = random.getFilePointer();
					j++;
				}
			}
			// locate to thebegin of content.Count for 4 lines's end position.
			random.seek(forthEndPosition);
			long startPoint = random.getFilePointer();
			// read the realcontent and write it to the realFile.
			while (startPoint < endPosition - 1) {
				n = random.readByte();
				random2.write(n);
				startPoint = random.getFilePointer();
			}
			random2.close();
			random.close();
			// delete the tempfile.
			temp.delete();
			System.out.println("File uploadsuccess!");
		} else {
			System.out.println("Nofile!");
		}

	}
}
