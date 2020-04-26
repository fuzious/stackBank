package com.stack.bank.actions;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JavaMailUtil {
	public static void sendEmail (String recipient, String passbook) {
		System.out.println("Preparing to send");
		Properties properties = new Properties();
		properties.put("mail.smtp.auth","true");
		properties.put("mail.smtp.starttls.enable","true");
		properties.put("mail.smtp.host","smtp.gmail.com");
		properties.put("mail.smtp.port","587");
		properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");


		String myAccountEmail= "*******"; //add sender email
		String password= "********";  // add sender password

		Session session = Session.getInstance(properties, new Authenticator() {

			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(myAccountEmail,password);
			}
		});

		Message message = prepareMessage(session,myAccountEmail,recipient,passbook);
		try {
			Transport.send(message);
			System.out.println("Sent message");
		}
		catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	private static Message prepareMessage(Session session, String myAccountEmail, String recipient , String passbook) {
		Message message = new MimeMessage(session);
		try {
			message.setFrom(new InternetAddress(myAccountEmail));
			message.setRecipient(Message.RecipientType.TO,new InternetAddress(recipient));
			message.setSubject("passbook");
			message.setText(passbook);
			return message;
		}
		catch (MessagingException e) {
			Logger.getLogger(JavaMailUtil.class.getName()).log(Level.SEVERE,null, e);
			e.printStackTrace();
		}
		return null;
	}
}
