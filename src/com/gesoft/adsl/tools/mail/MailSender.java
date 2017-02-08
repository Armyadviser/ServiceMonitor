package com.gesoft.adsl.tools.mail;

import java.util.Properties;

import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailSender {

	private final Properties props = System.getProperties();
	private MailAuthenticator authenticator = null;
	private Session session = null;
	
	public MailSender(String hostName, String user, String pwd) {
		init(user, pwd, hostName);
	}
	
	public MailSender(String user, String pwd) {
		String hostName = "smtp." + user.split("@")[1];
		init(user, pwd, hostName);
	}
	
	private void init(String user, String pwd, String hostName) {
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.host", hostName);
		
		authenticator = new MailAuthenticator(user, pwd);
		session = Session.getInstance(props, authenticator);
	}
	
	/**
	 * 发送，单人
	 * @param recipient	收件人
	 * @param subject	主题
	 * @param content	内容
	 */
	public void send(String recipient, String subject, String content) throws MessagingException {
		final MimeMessage message = new MimeMessage(session);
		//设置发信人
		message.setFrom(new InternetAddress(authenticator.getUsername()));
		//设置收件人
		message.setRecipient(RecipientType.TO, new InternetAddress(recipient));
		//设置主题
		message.setSubject(subject);
		//设置邮件内容
		message.setContent(content, "text/html;charset=utf-8");
		
		Transport.send(message);
	}
	
	/**
	 * 发送，多人
	 * @param recipients	收件人
	 * @param subject	主题
	 * @param content	内容
	 */
	public void send(String[] recipients, String subject, String content) throws MessagingException {
		InternetAddress[] recipAddress = new InternetAddress[recipients.length];
		for (int i = 0; i < recipients.length; i++) {
			recipAddress[i] = new InternetAddress(recipients[i]);
		}
		final MimeMessage message = new MimeMessage(session);
		//设置发信人
		message.setFrom(new InternetAddress(authenticator.getUsername()));
		//设置收件人
		message.setRecipients(RecipientType.TO, recipAddress);
		//设置主题
		message.setSubject(subject);
		//设置邮件内容
		message.setContent(content, "text/html;charset=utf-8");
		
		Transport.send(message);
	}
}
