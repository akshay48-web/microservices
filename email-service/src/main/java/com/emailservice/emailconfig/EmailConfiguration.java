package com.emailservice.emailconfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import org.springframework.web.multipart.MultipartFile;

public class EmailConfiguration {
	
	public static Properties getProperty() {
		Properties props = new Properties();
		
		//Outlook
		/*props.put("mail.smtp.host", "smtp.office365.com");
		props.put("mail.smtp.socketFactory.port", "587");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "587");
		*/
		//Gmail
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "587");
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.smtp.port", "587");
	    props.put("mail.smtp.starttls.enable", "true");

		return props;
	}

	public static Session getSession(final String from, final String password) {
		Session session = Session.getDefaultInstance(getProperty(), new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(from, password);
			}
		});
		return session;

	}

	public static MimeMessage getMimeMsg(final String from, final String password) {
		Session session = getSession(from, password);
		MimeMessage message = new MimeMessage(session);
		try {
			message.setSender(new InternetAddress(from));

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
		return message;
	}

	public static void setToAddress(MimeMessage mimeMessage, String to) {

		if (to != null) {
			try {
				String[] toArry = to.split(",");
				for (int i = 0; i < toArry.length; i++) {
					mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(toArry[i]));

				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	private static void setCCAddress(MimeMessage mimeMessage, String cc) {

		if (cc != null) {
			try {
				String[] toArry = cc.split(",");
				for (int i = 0; i < toArry.length; i++) {
					mimeMessage.addRecipient(Message.RecipientType.CC, new InternetAddress(toArry[i]));

				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	private static void setMessage(MimeMessage mimeMessage, String message) {

		if (message != null) {
			try {
				mimeMessage.setText(message);
			} catch (MessagingException e) {
				e.printStackTrace();
			}

		}

	}

	private static void setSubject(MimeMessage mimeMessage, String subject) {

		if (subject != null) {

			try {
				mimeMessage.setSubject(subject);
			} catch (MessagingException e) {
				e.printStackTrace();
			}
		}

	}

	public static String getFileName(String filenPath) {

		String[] file = filenPath.split("/");
		String fileName = file[file.length - 1];

		return fileName;

	}


	private static void setAttachment(MimeMessage mimeMessage, MultipartFile[] files, String message) {
	    if (files != null && files.length > 0) {
	        try {
	            Multipart multipart = new MimeMultipart();

	            // Add message body
	            BodyPart messageBodyPart = new MimeBodyPart();
	            messageBodyPart.setText(message);
	            multipart.addBodyPart(messageBodyPart);

	            // Add attachments
	            for (MultipartFile file : files) {
	                MimeBodyPart attachPart = new MimeBodyPart();
	                DataSource source = new ByteArrayDataSource(file.getBytes(), file.getContentType());
	                attachPart.setDataHandler(new DataHandler(source));
	                attachPart.setFileName(file.getOriginalFilename());
	                multipart.addBodyPart(attachPart);
	            }

	            mimeMessage.setContent(multipart);
	        } catch (MessagingException | IOException e) {
	            e.printStackTrace();
	        }
	    }
	}


	public static String sendMail(final String from, final String password, String to, String subject, String message) {

		String successMessage = "";

		MimeMessage mimeMessage = getMimeMsg(from, password);
		try {
			setToAddress(mimeMessage, to);
			setSubject(mimeMessage, subject);
			setMessage(mimeMessage, message);
			Transport.send(mimeMessage);
			successMessage = "message sent successfully";
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
		return successMessage;

	}

	public static String sendMailWithCC(String from, String password, String to, String cc, String subject,
			String message) {
		String successMessage = "";

		MimeMessage mimeMessage = getMimeMsg(from, password);
		try {
			setToAddress(mimeMessage, to);
			setCCAddress(mimeMessage, cc);
			setSubject(mimeMessage, subject);
			setMessage(mimeMessage, message);
			Transport.send(mimeMessage);
			successMessage = "message sent successfully with cc";
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
		return successMessage;
	}

	public static String sendMailWithAttachment(String from, String password, String to, String cc, String subject,
			String message, MultipartFile[] files) {

		String successMessage = "";

		MimeMessage mimeMessage = getMimeMsg(from, password);
		try {
			setToAddress(mimeMessage, to);
			setCCAddress(mimeMessage, cc);
			setSubject(mimeMessage, subject);
			setAttachment(mimeMessage, files, message);
			Transport.send(mimeMessage);
			successMessage = "message sent successfully with Attachment";
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}

		return successMessage;
	}

}
