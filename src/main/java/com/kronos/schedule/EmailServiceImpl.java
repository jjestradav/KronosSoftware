package com.kronos.schedule;

import org.springframework.stereotype.Component;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.awt.Color;
import java.io.File;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;

@Component
public class EmailServiceImpl implements EmailService {

	@Autowired 
	private JavaMailSender emailSender;
	
	@Value("${spring.mail.username}")
    private String from;
	

	public void sendSimpleMail(String to, String subject, String message ) {
		MimeMessage _message = emailSender.createMimeMessage();
		String msg= addColor(message, Color.RED);
		try {
           MimeMessageHelper helper= new MimeMessageHelper(_message,true);
            
            helper.setFrom(from);
            
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(msg,true);

            emailSender.send(_message);
        } catch (MessagingException exception) {
            exception.printStackTrace();
        }
	}
	
	@Override 
	public void sendMailWithAttachment(String to, String subject, String text, String pathToAttachment) {
		MimeMessage message = emailSender.createMimeMessage();
	      try {
	    MimeMessageHelper helper = new MimeMessageHelper(message, true);
	     
	    helper.setFrom(from);
	    helper.setTo(to);
	    helper.setSubject(subject);
	    helper.setText(text);
	         
	    FileSystemResource file 
	      = new FileSystemResource(new File(pathToAttachment));
	    helper.addAttachment("Acuerdo.pdf", file);
	    
	 
	    emailSender.send(message);
	      }catch(MessagingException e) {
	    	  e.printStackTrace();
	      }
	}
	
	public static String addColor(String message, Color color) {
		String hexColor = String.format("#%06X",  (0xFFFFFF & color.getRGB()));
	    String colorMsg = "<FONT COLOR=\"#" + hexColor + "\">" + message + "</FONT>";
	    return colorMsg;
	} 
}
