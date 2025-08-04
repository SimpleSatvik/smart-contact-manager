package com.smart.service;

import java.io.File;
import java.util.Properties;

import org.springframework.stereotype.Service;

import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

@Service
public class emailService 
{
	
	public boolean sendEmail(String subject, String msg, String to)
	{
		//rest of the code
		boolean flag = false; 
		
		String from = "newvalorantacc2134@gmail.com";
		//Variable for gmail host
    	String host = "smtp.gmail.com";
    	
    	//Get the system properties
    	Properties properties = System.getProperties();
    	System.out.println("PROPERTIES : " + properties);
    	
    	//Setting important information to properties object
    	//Host setting
    	properties.put("mail.smtp.host", host);
    	properties.put("mail.smtp.port", 465);
    	properties.put("mail.smtp.ssl.enable", "true");
    	properties.put("mail.smtp.auth", "true");
    	
    	//Step 1 : to get the session object
    	Session session = Session.getInstance(properties, new jakarta.mail.Authenticator() {

			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				// TODO Auto-generated method stub
				return new PasswordAuthentication("contacttmanager@gmail.com", "uclp cxzt bsrl hgiv"
						+ "");
			}
    		 
    	});
    	
    	
    	session.setDebug(true);
    	//Step 2 : Compose the msg [text, multimedia]
    	MimeMessage m = new MimeMessage(session);
    	
    	//from Email	
    	try
    	{
    		m.setFrom(from);
    		
    		//Adding recipient to message
    		m.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
    		
    		//Adding subject to message
    		m.setSubject(subject);
    		
    		//Attachment 
    		//File path
    		//String path = "G:\\Previous Downloads\\Gojo.jpg";
    		
    		//containts text and file
    		//MimeMultipart multiPart =  new MimeMultipart();
    		
    		//MimeBodyPart textMime = new MimeBodyPart();
    		//MimeBodyPart fileMime = new MimeBodyPart();
    		
    		m.setContent(msg, "text/html");
    		
    		//textMime.setText(msg);
    		//textMime.setContent(multiPart);
    		
    		//File file = new File(path);
    		//fileMime.attachFile(file);
    		
    		
    		//multiPart.addBodyPart(textMime);
    		//multiPart.addBodyPart(fileMime);
    		
    		//m.setContent(multiPart);
    		//Step 3 : Send the MEssage using Transport class
    		Transport.send(m);
    		System.out.println("Sent Successfully");
    		flag = true;
    		
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    	
		return flag;
	}
}
