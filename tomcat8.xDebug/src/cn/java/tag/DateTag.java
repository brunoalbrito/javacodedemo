package cn.java.tag;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

public class DateTag extends TagSupport{
	private long timestamp;
	private String timeZone = "PRC";
	private String format = "yyyy-MM-dd HH:mm:ss";
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	
	public String getTimeZone() {
		return timeZone;
	}
	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}
	
	@Override
	public int doStartTag() throws JspException {
		return super.doStartTag();
	}
	
	@Override
	public int doEndTag() throws JspException {
		JspWriter out = pageContext.getOut();
		SimpleDateFormat timestampFormat = new SimpleDateFormat(format);
		if("PRC".equals(this.getTimeZone())){
			timestampFormat.setTimeZone(TimeZone.getTimeZone("PRC"));
		}
		else{
			timestampFormat.setTimeZone(TimeZone.getTimeZone(this.getTimeZone()));
		}
        try {
        	if((timestamp+"").length()<11){ // 秒数的时间戳是：10位
        		out.print(timestampFormat.format(new Date(timestamp*1000)));
        	}
        	else{
        		out.print(timestampFormat.format(new Date(timestamp)));
        	}
		} catch (IOException e) {
			throw new JspTagException("IOException: " + e.toString());
		}
		return super.doEndTag();
	}
	
	
	
}
