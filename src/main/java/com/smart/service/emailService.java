package com.smart.service;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@Service
public class emailService 
{
	@Value("${MAIL_USERNAME:newvalorantacc2134@gmail.com}")
	private String from;

	@Value("${MAIL_PASSWORD:aioyokvanpqfqqgb}")
	private String password;
	
	public boolean sendEmail(String subject, String msg, String to)
	{
		boolean flag = false; 

		/* =========================================================
		   OLD SMTP CODE (Commented out because Render blocks port 587)
		   =========================================================
		//Variable for gmail host
    	String host = "smtp.gmail.com";
    	
    	//Get the system properties
    	Properties properties = System.getProperties();
    	System.out.println("PROPERTIES : " + properties);
    	
    	//Setting important information to properties object
    	properties.put("mail.smtp.host", host);
    	properties.put("mail.smtp.port", 587);
    	properties.put("mail.smtp.starttls.enable", "true");
    	properties.put("mail.smtp.auth", "true");
    	properties.put("mail.smtp.user", from);
    	
    	//Step 1 : to get the session object
		Session session = Session.getInstance(properties, new jakarta.mail.Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(from, password);
			}
    	});
    	
    	session.setDebug(true);
    	//Step 2 : Compose the msg [text, multimedia]
    	MimeMessage m = new MimeMessage(session);
    	
    	try {
    		m.setFrom(from);
    		m.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
    		m.setSubject(subject);
    		m.setContent(msg, "text/html");
    		
    		//Step 3 : Send the MEssage using Transport class
    		Transport.send(m);
    		System.out.println("Sent Successfully");
    		flag = true;
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
		========================================================= */

		// =========================================================
		// NEW HTTP API CODE (Bypasses Render's SMTP block)
		// Example using SendGrid (You will need to create a free SendGrid account)
		// =========================================================
		try {
			// Retrieve API Key from environment variables (fallback is empty so it won't crash locally)
			String apiKey = System.getenv("SENDGRID_API_KEY");
			if (apiKey == null || apiKey.isEmpty()) {
				System.err.println("SENDGRID_API_KEY environment variable is missing!");
				return false;
			}

			// Clean strings for JSON
			String safeSubject = subject.replace("\"", "\\\"");
			String safeMsg = msg.replace("\"", "\\\"");
			String safeTo = to.replace("\"", "\\\"");
			String safeFrom = from.replace("\"", "\\\"");

			// Build SendGrid JSON payload manually
			String jsonPayload = "{"
					+ "\"personalizations\": [{\"to\": [{\"email\": \"" + safeTo + "\"}], \"subject\": \"" + safeSubject + "\"}],"
					+ "\"from\": {\"email\": \"" + safeFrom + "\"},"
					+ "\"content\": [{\"type\": \"text/html\", \"value\": \"" + safeMsg + "\"}]"
					+ "}";

			// Use Java 11+ built-in HttpClient to make the REST request on port 443
			java.net.http.HttpClient client = java.net.http.HttpClient.newHttpClient();
			java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
					.uri(java.net.URI.create("https://api.sendgrid.com/v3/mail/send"))
					.header("Authorization", "Bearer " + apiKey)
					.header("Content-Type", "application/json")
					.POST(java.net.http.HttpRequest.BodyPublishers.ofString(jsonPayload))
					.build();

			java.net.http.HttpResponse<String> response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());

			if (response.statusCode() >= 200 && response.statusCode() < 300) {
				System.out.println("Sent Successfully using SendGrid API!");
				flag = true;
			} else {
				System.err.println("Failed to send email via API. Status code: " + response.statusCode());
				System.err.println("Response: " + response.body());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return flag;
	}
}
