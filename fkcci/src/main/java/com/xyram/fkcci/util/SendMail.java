package com.xyram.fkcci.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.mail.internet.MimeMultipart;

public class SendMail {
	
	private static final String CONTENT_HTML = "text/html";
	
	public static void  sendEmail(String toAddress, String subject,String bodyText, String fileLocation) {
		try {
			Properties prop = new Properties();
		    InputStream input = null;
		 
		    String filename = "fkcci.properties";
	    	input = SendMail.class.getClassLoader().getResourceAsStream(filename);
	    	prop.load(input);
			
			
			MimeMessage message = new MimeMessage(getSession());
			InternetAddress addFrom = new InternetAddress(prop.getProperty("from_mail_id"));

			InternetAddress addTo = new InternetAddress(toAddress);
			message.setFrom(addFrom);
			message.setRecipient(RecipientType.TO, addTo);
			message.setSubject(subject);
			MimeBodyPart mimeBodyPart = new MimeBodyPart();
			mimeBodyPart.setText(bodyText);
			mimeBodyPart.setContent(bodyText, CONTENT_HTML);
			
			
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(mimeBodyPart);
			 MimeBodyPart attachPart = new MimeBodyPart();
			   
			  // code to add attachment...will be revealed later
			   if(fileLocation!=null){
			  DataSource source = new FileDataSource(fileLocation);
			  attachPart.setDataHandler(new DataHandler(source));
			  attachPart.setFileName(new File(fileLocation).getName());
			   
			  multipart.addBodyPart(attachPart);
			   }
			  // multipart.addBodyPart(messageBodyPart);
			   message.setContent(multipart);
			   
			
			Transport transport = getSession().getTransport(prop.getProperty("mail.transport.protocol"));
			
			transport.connect(prop.getProperty("mail.smtp.host"), prop.getProperty("from_mail_id"), prop.getProperty("from_mail_password"));
			transport.sendMessage(message, message.getAllRecipients());
			
			transport.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static Session getSession() throws IOException {
		Properties prop = new Properties();
	    InputStream input = null;
	 
	    String filename = "fkcci.properties";
    	input = SendMail.class.getClassLoader().getResourceAsStream(filename);
    	prop.load(input);
		
		
		Properties properties = new Properties();
        properties.put("mail.smtp.auth", prop.getProperty("mail.smtp.auth"));
		properties.put("mail.smtp.port", prop.getProperty("mail.smtp.port"));
		properties.put("mail.transport.protocol", prop.getProperty("mail.transport.protocol"));
		properties.put("mail.smtp.starttls.enable", prop.getProperty("mail.smtp.starttls.enable"));
		properties.put("mail.smtp.host", prop.getProperty("mail.smtp.host"));
		return Session.getDefaultInstance(properties, null);
	}

	@SuppressWarnings("unused")
	private static class Authenticator extends javax.mail.Authenticator {

		private PasswordAuthentication authentication;

		public Authenticator() throws IOException {
			
			Properties prop = new Properties();
		    InputStream input = null;
		 
		    String filename = "fkcci.properties";
	    	input = SendMail.class.getClassLoader().getResourceAsStream(filename);
	    	prop.load(input);
	    	
			authentication = new PasswordAuthentication(prop.getProperty("from_mail_id"), prop.getProperty("from_mail_password"));
		}

		@Override
		protected PasswordAuthentication getPasswordAuthentication() {
			return authentication;
		}
	}
	
}
