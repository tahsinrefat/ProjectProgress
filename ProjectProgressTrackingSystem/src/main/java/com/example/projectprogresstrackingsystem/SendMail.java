package com.example.projectprogresstrackingsystem;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;
import java.util.Random;

public class SendMail {
    public void sendEmail(MimeMessage mimeMessage, Session newSession){
        System.out.println("Final Step...");
        String fromUser = "zacksteins99@gmail.com";
        String fromUserPass = "zcrzfvwkuygddlef";
        String emailHost = "smtp.gmail.com";
        try {
            Transport transport = newSession.getTransport("smtp");
            transport.connect(emailHost,fromUser, fromUserPass);
            transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
            transport.close();
            System.out.println("Email Sent!");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
    public int draftEmail(Session newSession, String mail, String subject) throws MessagingException {
        System.out.println("Creating Message...");
        Random random = new Random();
        int min = 100000;
        int max = 999999;
        int OTP = random.nextInt(max - min + 1) + min;
        String emailBody = "Hello,\nThe OTP for your desired task is: "+OTP+". Please do not share this with anyone in order to protect the privacy of your account and the company as well.\nYours Faithfully,\nProject Progress Tracking System Server";
        MimeMessage mimeMessage = new MimeMessage(newSession);
        mimeMessage.addRecipients(Message.RecipientType.TO, new InternetAddress[]{new InternetAddress(mail)});
        mimeMessage.setSubject(subject);

        MimeBodyPart bodyPart = new MimeBodyPart();
        bodyPart.setContent(emailBody,"text/plain");
        MimeMultipart multipart = new MimeMultipart();
        multipart.addBodyPart(bodyPart);
        mimeMessage.setContent(multipart);
        sendEmail(mimeMessage, newSession);
        return OTP;
    }
    public Session setupServerProperties(){
        System.out.println("Setting Up Server...");
        Properties properties = System.getProperties();
        properties.put("mail.smtp.port","587");
        properties.put("mail.smtp.auth","true");
        properties.put("mail.smtp.starttls.enable","true");
        return Session.getDefaultInstance(properties,null);
    }
}
