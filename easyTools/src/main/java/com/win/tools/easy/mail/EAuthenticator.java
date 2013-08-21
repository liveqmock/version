package com.win.tools.easy.mail;

import javax.mail.*;

public class EAuthenticator extends Authenticator {
	String userName = null;
	String password = null;

	public EAuthenticator() {
	}

	public EAuthenticator(String username, String password) {
		this.userName = username;
		this.password = password;
	}

	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(userName, password);
	}

	public static void main(String[] args) {
		// 这个类主要是设置邮件
		MailSenderInfo mailInfo = new MailSenderInfo();
		mailInfo.setMailServerHost("172.16.126.76");
		mailInfo.setMailServerPort("25");
		mailInfo.setValidate(true);
		mailInfo.setUserName("no-reply");
		mailInfo.setPassword("SUN.japan0");// 您的邮箱密码
		// mailInfo.setUserName("no-reply@sy.liandisys.com.cn");
		// mailInfo.setPassword("sun.japan");// 您的邮箱密码

		mailInfo.setFromAddress("no-reply@sy.liandisys.com.cn");
		mailInfo.setToAddress("yuanxd@sy.liandisys.com.cn");
		mailInfo.setSubject("设置邮箱标题1");
		mailInfo.setContent("设置邮箱内容1");
		// 这个类主要来发送邮件
		MailSender.sendTextMail(mailInfo);// 发送文体格式
		MailSender.sendHtmlMail(mailInfo);// 发送html格式
	}
}