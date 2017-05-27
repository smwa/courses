package me.smwa.courses.utilities;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import java.util.Properties;

public class Mailer {
    public static boolean send(String to, String subject, String body, String filename, String fileAttachment) {
        final String username = ""; //Insert full gmail address here (example@gmail.com)
        final String password = ""; //Insert gmail password here

        if (to.isEmpty() || subject.isEmpty() || body.isEmpty() || username.isEmpty() || password.isEmpty()) {
            return false;
        }

        Properties props = new Properties();
        props.put("mail.smtp.auth","true"); //Enable Authentication
        props.put("mail.smtp.starttls.enable","true"); //Enable STARTTLS to an encrypted connection
        props.put("mail.smtp.host","smtp.gmail.com"); //Host name of the SMTP mail server
        props.put("mail.smtp.port", "587"); //TLS Port on which the mail server is listening

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            //Authentication object
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username,password);
            }
        });

        try {
            Address mailSender = new InternetAddress(username);
            String[] recipeintList = to.split(",");
            InternetAddress[] recipientAddress = new InternetAddress[recipeintList.length];
            int counter = 0;
            for (String recipient : recipeintList) {
                recipient = recipient.trim();
                if (recipient.equals("")) continue;
                recipientAddress[counter] = new InternetAddress(recipient.trim());
                counter++;
            }
            //Create MimeMessage
            Message message = new MimeMessage(session);

            //Creates body part for the message
            Multipart multipart = new MimeMultipart();

            //Message Text
            MimeBodyPart messageTextBodyPart = new MimeBodyPart();
            messageTextBodyPart.setText(body);
            multipart.addBodyPart(messageTextBodyPart);

            //Attachment
            DataSource source = new ByteArrayDataSource(fileAttachment,"text/html");
            MimeBodyPart messageAttachmentBodyPart = new MimeBodyPart();
            messageAttachmentBodyPart.setDataHandler(new DataHandler(source));
            messageAttachmentBodyPart.setFileName(filename);
            multipart.addBodyPart(messageAttachmentBodyPart);

            //Message settings
            message.setContent(multipart);
            message.setFrom(mailSender);
            message.setRecipients(Message.RecipientType.TO, recipientAddress);
            message.setSubject(subject);

            //Send email
            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        } catch (java.io.IOException e ) {
            throw new RuntimeException(e);
        }
        return true;
    }
}