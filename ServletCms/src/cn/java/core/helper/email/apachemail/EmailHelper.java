package cn.java.core.helper.email.apachemail;

import java.net.URL;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.ImageHtmlEmail;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.commons.mail.SimpleEmail;
import org.apache.commons.mail.resolver.DataSourceUrlResolver;

public class EmailHelper {

	/**
	 * 发送普通的邮件
	 */
	public static void testSendText() {
		EmailServerInfo mEmailServerInfo = new EmailHelper.EmailServerInfo();
		mEmailServerInfo.setHostName("smtp.googlemail.com");
		mEmailServerInfo.setPort(465);
		mEmailServerInfo.setUsername("username");
		mEmailServerInfo.setPassword("password");
		mEmailServerInfo.setSSL(true);
		mEmailServerInfo.setAuth(true);

		EmailMsg mEmailMsg = new EmailMsg();
		mEmailMsg.setTextMsg("消息内容...");
		mEmailMsg.setReceiverAddress("1742898231@qq.com");
		mEmailMsg.setReceiverName("小舟");
		mEmailMsg.setSenderAddress("297963025@qq.com");
		mEmailMsg.setSenderName("小周");
		mEmailMsg.setSubject("标题");
		EmailHelper.sendMail(mEmailServerInfo, mEmailMsg);
	}

	/**
	 *  发送带本地资源的邮件
	 */
	public static void testSendAttachPic() {
		EmailServerInfo mEmailServerInfo = new EmailHelper.EmailServerInfo();
		mEmailServerInfo.setHostName("smtp.googlemail.com");
		mEmailServerInfo.setPort(465);
		mEmailServerInfo.setUsername("username");
		mEmailServerInfo.setPassword("password");
		mEmailServerInfo.setSSL(true);
		mEmailServerInfo.setAuth(true);

		AttachInfo mAttachInfo = new EmailHelper.AttachInfo();
		mAttachInfo.setAttachDes("附件描述");
		mAttachInfo.setAttachName("附件名称");
		mAttachInfo.setAttachPath("mypictures/john.jpg");

		EmailMsg mEmailMsg = new EmailHelper.EmailMsg();
		mEmailMsg.setTextMsg("消息内容...");
		mEmailMsg.setReceiverAddress("1742898231@qq.com");
		mEmailMsg.setReceiverName("小舟");
		mEmailMsg.setSenderAddress("297963025@qq.com");
		mEmailMsg.setSenderName("小周");
		mEmailMsg.setSubject("标题");
		
		mEmailMsg.setAttachInfo(mAttachInfo);//设置附件
		EmailHelper.sendMail(mEmailServerInfo, mEmailMsg);
	}

	/**
	 * 发送带网络资源的邮件
	 */
	public static void testSendAttachPicUrl() {
		EmailServerInfo mEmailServerInfo = new EmailHelper.EmailServerInfo();
		mEmailServerInfo.setHostName("smtp.googlemail.com");
		mEmailServerInfo.setPort(465);
		mEmailServerInfo.setUsername("username");
		mEmailServerInfo.setPassword("password");
		mEmailServerInfo.setSSL(true);
		mEmailServerInfo.setAuth(true);

		AttachInfo mAttachInfo = new EmailHelper.AttachInfo();
		mAttachInfo.setAttachDes("附件描述");
		mAttachInfo.setAttachName("附件名称");
		mAttachInfo.setAttachPath("http://www.apache.org/images/asf_logo_wide.gif");

		EmailMsg mEmailMsg = new EmailHelper.EmailMsg();
		mEmailMsg.setTextMsg("消息内容...");
		mEmailMsg.setReceiverAddress("1742898231@qq.com");
		mEmailMsg.setReceiverName("小舟");
		mEmailMsg.setSenderAddress("297963025@qq.com");
		mEmailMsg.setSenderName("小周");
		mEmailMsg.setSubject("标题");
		
		mEmailMsg.setAttachInfo(mAttachInfo);//设置附件
		EmailHelper.sendMail(mEmailServerInfo, mEmailMsg);
	}
	
	/**
	 * 发送HTML格式的邮件
	 */
	public static void testSendHtmlMail() {
		EmailServerInfo mEmailServerInfo = new EmailHelper.EmailServerInfo();
		mEmailServerInfo.setHostName("smtp.googlemail.com");
		mEmailServerInfo.setPort(465);
		mEmailServerInfo.setUsername("username");
		mEmailServerInfo.setPassword("password");
		mEmailServerInfo.setSSL(true);
		mEmailServerInfo.setAuth(true);
		
		EmailMsg mEmailMsg = new EmailHelper.EmailMsg();
		mEmailMsg.setTextMsg("消息内容...");
		mEmailMsg.setReceiverAddress("1742898231@qq.com");
		mEmailMsg.setReceiverName("小舟");
		mEmailMsg.setSenderAddress("297963025@qq.com");
		mEmailMsg.setSenderName("小周");
		mEmailMsg.setSubject("标题");
		
		mEmailMsg.setBaseUrlResolve("http://www.apache.org");//要解析的基本资源的路径
		mEmailMsg.setHtmlMsg(".... <img src=\"http://www.apache.org/images/feather.gif\"> ....");
		
		EmailHelper.sendMail(mEmailServerInfo, mEmailMsg);
	}

	/**
	 * 设置通用信息
	 * 
	 * @param email
	 * @param emailServerInfo
	 * @param emailMsg
	 * @throws EmailException
	 */
	protected static void setCommonInfo(Email email, EmailServerInfo emailServerInfo, EmailMsg emailMsg) throws EmailException {
		email.setHostName(emailServerInfo.getHostName());//"mail.myserver.com"
		email.setSmtpPort(emailServerInfo.getPort());
		if (emailServerInfo.isAuth()) {
			if (emailServerInfo.isSSL()) {
				email.setAuthenticator(new DefaultAuthenticator(emailServerInfo.getUsername(), emailServerInfo.getPassword()));
				email.setSSLOnConnect(emailServerInfo.isSSL());
			} else {
				email.setAuthentication(emailServerInfo.getUsername(), emailServerInfo.getPassword());
			}
		}
		email.addTo(emailMsg.getReceiverAddress(), emailMsg.getReceiverName());//"jdoe@somewhere.org", "John Doe"
		email.setFrom(emailMsg.getSenderAddress(), emailMsg.getSenderName());//"me@apache.org", "Me"
		email.setSubject(emailMsg.getSubject());//"The picture"
	}
	
   
	/**
	 * 发送HTML格式的邮件
	 * @param emailServerInfo
	 * @param emailMsg
	 */
	public static void sendHtmlFormattedMail(EmailServerInfo emailServerInfo, EmailMsg emailMsg) {
		try {
			  // define you base URL to resolve relative resource locations
			  URL url = new URL(emailMsg.getBaseUrlResolve()); //"http://www.apache.org"

			  // create the email message
			  ImageHtmlEmail email = new ImageHtmlEmail();
			  email.setDataSourceResolver(new DataSourceUrlResolver(url));
			  
			  setCommonInfo(email, emailServerInfo, emailMsg);
			  
			  // set the html message
			  email.setHtmlMsg(emailMsg.getHtmlMsg());//".... <img src=\"http://www.apache.org/images/feather.gif\"> ...."

			  // set the alternative message
			  email.setTextMsg(emailMsg.getTextMsg());//"Your email client does not support HTML messages"

			  // send the email
			  email.send();
		} catch (Exception e) {
			e.printStackTrace();
		}
		 

	}

	public static void sendMail(EmailServerInfo emailServerInfo, EmailMsg emailMsg) {

		try {
			AttachInfo attachInfo = emailMsg.getAttachInfo();
			if (attachInfo != null) {//带附件发送
				// Create the email message
				MultiPartEmail email = new MultiPartEmail();

				//设置通用信息
				setCommonInfo(email, emailServerInfo, emailMsg);
				email.setMsg(emailMsg.getTextMsg());//"Here is the picture you wanted"
				
				// Create the attachment
				EmailAttachment attachment = new EmailAttachment();
				attachment.setPath(attachInfo.getAttachPath());//"mypictures/john.jpg"
				attachment.setDisposition(EmailAttachment.ATTACHMENT);
				attachment.setDescription(attachInfo.getAttachDes());//"Picture of John"
				attachment.setName(attachInfo.getAttachName());//"John"
				email.attach(attachment);

				// send the email
				email.send();
			} else {
				Email email = new SimpleEmail();
				setCommonInfo(email, emailServerInfo, emailMsg);
				email.setMsg(emailMsg.getTextMsg());//"Here is the picture you wanted"
				email.send();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * smtp信息
	 * 
	 * @author Administrator
	 *
	 */
	public static class EmailServerInfo {
		private String hostName;
		private String username;
		private String password;
		private boolean isAuth;
		private boolean isSSL;
		private int port;

		public int getPort() {
			return port;
		}

		public void setPort(int port) {
			this.port = port;
		}

		public String getHostName() {
			return hostName;
		}

		public void setHostName(String hostName) {
			this.hostName = hostName;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public boolean isAuth() {
			return isAuth;
		}

		public void setAuth(boolean isAuth) {
			this.isAuth = isAuth;
		}

		public boolean isSSL() {
			return isSSL;
		}

		public void setSSL(boolean isSSL) {
			this.isSSL = isSSL;
		}

	}

	/**
	 * 要发送的信息
	 * 
	 * @author Administrator
	 *
	 */
	public static class EmailMsg {
		private String senderAddress;
		private String senderName;
		private String receiverName;
		private String receiverAddress;
		private String subject;
		private String textMsg;
		private String htmlMsg;
		private String baseUrlResolve;
		private AttachInfo attachInfo;

		
		public String getBaseUrlResolve() {
			return baseUrlResolve;
		}

		public void setBaseUrlResolve(String baseUrlResolve) {
			this.baseUrlResolve = baseUrlResolve;
		}

		public String getSenderName() {
			return senderName;
		}

		public void setSenderName(String senderName) {
			this.senderName = senderName;
		}

		public String getReceiverName() {
			return receiverName;
		}

		public void setReceiverName(String receiverName) {
			this.receiverName = receiverName;
		}

		public String getSenderAddress() {
			return senderAddress;
		}

		public void setSenderAddress(String senderAddress) {
			this.senderAddress = senderAddress;
		}

		public String getReceiverAddress() {
			return receiverAddress;
		}

		public void setReceiverAddress(String receiverAddress) {
			this.receiverAddress = receiverAddress;
		}

		public String getSubject() {
			return subject;
		}

		public void setSubject(String subject) {
			this.subject = subject;
		}


		public AttachInfo getAttachInfo() {
			return attachInfo;
		}

		public void setAttachInfo(AttachInfo attachInfo) {
			this.attachInfo = attachInfo;
		}

		public String getHtmlMsg() {
			return htmlMsg;
		}

		public void setHtmlMsg(String htmlMsg) {
			this.htmlMsg = htmlMsg;
		}

		public String getTextMsg() {
			return textMsg;
		}

		public void setTextMsg(String textMsg) {
			this.textMsg = textMsg;
		}
		

	}

	public static class AttachInfo {
		private String attachPath;
		private String attachDes;
		private String attachName;

		public String getAttachPath() {
			return attachPath;
		}

		public void setAttachPath(String attachPath) {
			this.attachPath = attachPath;
		}

		public String getAttachDes() {
			return attachDes;
		}

		public void setAttachDes(String attachDes) {
			this.attachDes = attachDes;
		}

		public String getAttachName() {
			return attachName;
		}

		public void setAttachName(String attachName) {
			this.attachName = attachName;
		}

	}

}
