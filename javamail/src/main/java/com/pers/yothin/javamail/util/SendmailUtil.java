package com.pers.yothin.javamail.util;

import java.io.File;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class SendmailUtil {
	// 设置服务器
	private static String KEY_SMTP = "mail.smtp.host";
	private static String VALUE_SMTP = "smtp.163.com";
	// 服务器验证
	private static String KEY_PROPS = "mail.smtp.auth";
	private static boolean VALUE_PROPS = true;
	// 发件人用户名、密码
	private String SEND_USER = "yothinhuang@163.com";
	private String SEND_UNAME = "yothinhuang@163.com";
	private String SEND_PWD = "456qwe123zxc";
	// 建立会话
	private MimeMessage message;
	private Session s;

	/*
	 * 初始化方法
	 */
	public SendmailUtil() {
		Properties props = System.getProperties();
		props.setProperty(KEY_SMTP, VALUE_SMTP);
		props.put(KEY_PROPS, VALUE_PROPS);
		// props.put("mail.smtp.auth", "true");
		s = Session.getInstance(props, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(SEND_UNAME, SEND_PWD);
			}
		});
		s.setDebug(true);
		message = new MimeMessage(s);
	}

	/**
	 * 发送邮件
	 * 
	 * @param headName
	 *            邮件头文件名
	 * @param sendHtml
	 *            邮件内容
	 * @param receiveUser
	 *            收件人地址
	 */
	public void doSendHtmlEmail(String headName, String content, String receiveUser) {
		try {
			// 发件人
			InternetAddress from = new InternetAddress(SEND_USER);
			message.setFrom(from);
			// 收件人
			InternetAddress to = new InternetAddress(receiveUser);
			message.setRecipient(Message.RecipientType.TO, to);
			// 邮件标题
			message.setSubject(headName);

			// 邮件内容,也可以使用纯文本"text/plain"
			message.setContent(content, "text/html;charset=utf-8");
			message.saveChanges();
			Transport transport = s.getTransport("smtp");
			// smtp验证，就是你用来发邮件的邮箱用户名密码
			transport.connect(VALUE_SMTP, SEND_UNAME, SEND_PWD);
			// 发送
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
			System.out.println("send success!");
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	public boolean sendMail(String subject, String toMail, String content, String... files) {
		boolean isFlag = false;
		try {
			// 发件人
			InternetAddress from = new InternetAddress(SEND_USER);
			message.setFrom(from);
			
			message.setRecipient(Message.RecipientType.TO, new InternetAddress(toMail));
			message.setSubject(subject);
			message.addHeader("charset", "UTF-8");

			/* 添加正文内容 */
			Multipart multipart = new MimeMultipart();
			BodyPart contentPart = new MimeBodyPart();
			contentPart.setText(content);

			contentPart.setHeader("Content-Type", "text/html; charset=utf-8");
			multipart.addBodyPart(contentPart);

			if(files!=null && files.length!=0){
				/* 添加附件 */
				for (String file : files) {
					File usFile = new File(file);
					MimeBodyPart fileBody = new MimeBodyPart();
					DataSource source = new FileDataSource(file);
					fileBody.setDataHandler(new DataHandler(source));
					sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();
					fileBody.setFileName("=?GBK?B?" + enc.encode(usFile.getName().getBytes()) + "?=");
					multipart.addBodyPart(fileBody);
				}
			}
			

			message.setContent(multipart);
			message.setSentDate(new Date());
			message.saveChanges();
			Transport transport = s.getTransport("smtp");

			transport.connect(VALUE_SMTP, SEND_UNAME, SEND_PWD);
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
			isFlag = true;
		} catch (Exception e) {
			isFlag = false;
		}
		return isFlag;

	}

	public static void main(String[] args) {
		SendmailUtil se = new SendmailUtil();
//		se.doSendHtmlEmail("邮件头文件名", "邮件内容", "931900857@qq.com");
		se.sendMail("你好", "931900857@qq.com", "朋友好久不见", "F:/1.jpg");
	}
}