package cn.java.demo.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

public class EchoTag extends TagSupport{
	
	private static final long serialVersionUID = 1L;
	private String str;

	public String getStr() {
		return str;
	}

	public void setStr(String str) {
		this.str = str;
	}

	@Override
	public int doStartTag() throws JspException {
		return super.doStartTag();
	}

	@Override
	public int doEndTag() throws JspException {
		JspWriter out = pageContext.getOut();
        try {
            out.println(str);
        } catch (IOException ex) {
            throw new JspTagException("IOException: " + ex.toString());
        }
		return super.doEndTag();
	}
	
	
	
}
