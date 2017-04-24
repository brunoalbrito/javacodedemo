package cn.java.core.servlet;

import java.io.IOException;

import javax.servlet.ServletResponse;
import javax.servlet.jsp.JspWriter;

//JspWriterImpl
//JspWriter
public class MyJspWriterImpl extends JspWriter {

	private String htmlStrTemp = "";


	public MyJspWriterImpl() {
		super(0, false);
	}

	@Override
	public void clearBuffer() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void flush() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public int getRemaining() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void newLine() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void print(boolean flag) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void print(char c) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void print(int i) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void print(long l) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void print(float f) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void print(double d) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void print(char[] ac) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void print(String s) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void print(Object obj) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void println() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void println(boolean flag) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void println(char c) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void println(int i) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void println(long l) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void println(float f) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void println(double d) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void println(char[] ac) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void println(String s) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void println(Object obj) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void write(char[] cbuf, int off, int len) throws IOException {
		//		System.out.println(System.currentTimeMillis());
//		long timeStamp = System.currentTimeMillis();
//		System.out.println("-----START--------" + timeStamp);
//		System.out.println(new String(cbuf).trim());
//		System.out.println("-----END----------" + timeStamp);
		htmlStrTemp = htmlStrTemp + new String(cbuf).trim();
	}

	public String getHtmlStrTemp() {
		return htmlStrTemp;
	}

	@Override
	public void clear() throws IOException {
		// TODO Auto-generated method stub

	}

}
