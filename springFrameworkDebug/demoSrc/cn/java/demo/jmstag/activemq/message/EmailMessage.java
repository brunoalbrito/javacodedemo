package cn.java.demo.jmstag.activemq.message;

import java.io.Serializable;

@SuppressWarnings("serial")
public class EmailMessage implements Serializable{

    private String to;
    private String body;

    public EmailMessage() {
    }

    public EmailMessage(String to, String body) {
        this.to = to;
        this.body = body;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

	@Override
	public String toString() {
		return "EmailMessage [to=" + to + ", body=" + body + "]";
	}


}