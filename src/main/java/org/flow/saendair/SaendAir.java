package org.flow.saendair;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.flow.saendair.email.Email;
import org.flow.saendair.email.Mails;
import org.flow.saendair.email.TimedMail;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SaendAir {

    private static Properties mailServerProperties;
    private static Session getMailSession;
    private static MimeMessage generateMail;

    private static String host = "smtp.gmail.com";
    private static String senderUserName = "emailaddress";
    private static String senderPassword = "password";

    public static void main(String[] args) {
        new TimedMail(5);
    }

    public static void generateAndSendMail(Mails emailsFromJson) {
        Email[] emails = emailsFromJson.getMails();
        for (int i = 0; i < emails.length; i++) {

        // Setup Mailserver Properties
        mailServerProperties = System.getProperties();

        mailServerProperties.put("mail.smtp.starttls.enable", "true");

        mailServerProperties.put("mail.smtp.ssl.trust", host);
        mailServerProperties.put("mail.smtp.port", 587);
        mailServerProperties.put("mail.smtp.auth", "true");
        System.out.println("Mails server set up successfully.");

        // Get Mails Session
        getMailSession = Session.getDefaultInstance(mailServerProperties, null);
        generateMail = new MimeMessage(getMailSession);

            try {
                System.out.println(emails[i]);
                generateMail.addRecipient(Message.RecipientType.TO, new InternetAddress(emails[i].getEmailAddress()));
                generateMail.setSubject(emails[i].getSubject());
                generateMail.setContent(emails[i].getContent(), "text/html");

            } catch (AddressException ae) {
                ae.printStackTrace();
            } catch (MessagingException me) {
                me.printStackTrace();
            }
            System.out.println("Session created");

            // Get session and send mail
            Transport transport = null;
            try {
                transport = getMailSession.getTransport("smtp");
            } catch (NoSuchProviderException exc) {
                exc.printStackTrace();
            }

            // Gmail username and passwd
            try {
                transport.connect(host, senderUserName, senderPassword);
                transport.sendMessage(generateMail, generateMail.getAllRecipients());
                transport.close();
            } catch (MessagingException messex) {
                messex.printStackTrace();
            }
        }
        System.out.println("Message sent.");

    }

    public static Mails parseJson(String path) throws IOException {
        byte[] db = new byte[0];

        try {
            db = Files.readAllBytes(Paths.get(path));
        } catch (IOException e) {
            e.printStackTrace();
        }

        ObjectMapper objectMapper = new ObjectMapper();
        Mails mailsArray = null;

        try {
            mailsArray = objectMapper.readValue(db, Mails.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return mailsArray;
    }
}
