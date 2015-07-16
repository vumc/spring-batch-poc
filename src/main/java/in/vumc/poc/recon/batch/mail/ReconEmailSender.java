package in.vumc.poc.recon.batch.mail;

import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class ReconEmailSender {

  public String sendEmailWithAttachments(String host,
                                         String port,
                                         final String userName,
                                         final String password,
                                         String toAddress,
                                         String subject,
                                         String message,
                                         String filePath) throws AddressException, MessagingException {

    String result = null;

    try {
      Properties properties = new Properties();
      properties.put("mail.smtp.host", host);
      properties.put("mail.smtp.port", port);
      Session session = Session.getDefaultInstance(properties, null);
      Message msg = new MimeMessage(session);

      msg.setFrom(new InternetAddress(userName));
      InternetAddress[] toAddresses = { new InternetAddress(toAddress) };
      msg.setRecipients(Message.RecipientType.TO, toAddresses);
      msg.setSubject(subject);
      msg.setSentDate(new Date());

      MimeBodyPart messageBodyPart = new MimeBodyPart();
      messageBodyPart.setContent(message, "text/html");

      Multipart multipart = new MimeMultipart();
      multipart.addBodyPart(messageBodyPart);

      MimeBodyPart attachPart = new MimeBodyPart();
      attachPart.attachFile(filePath);
      multipart.addBodyPart(attachPart);
      msg.setContent(multipart);

      Transport.send(msg);
      result = "success";
    } catch (IOException ioex) {
      result = "failure";
      ioex.printStackTrace();
    } catch (Exception ex) {
      result = "failure";
      ex.printStackTrace();
    }
    return result;
  }

}
