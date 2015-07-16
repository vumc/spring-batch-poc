package in.vumc.poc.recon.batch.mail;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
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

import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailParseException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.velocity.VelocityEngineUtils;

public class ReconEmailManager {

  private static Logger LOG = Logger.getLogger(ReconEmailManager.class);

  private JavaMailSender mailSender;
  private SimpleMailMessage simpleMailMessage;
  private String attachmentFilePath;
  private String emailTemplateLocation;
  private String batchJobName;
  private VelocityEngine velocityEngine;
  Properties prop = new Properties();
  InputStream input = null;
  

  public void setVelocityEngine(VelocityEngine velocityEngine) {
    this.velocityEngine = velocityEngine;
  }

  public String getAttachmentFilePath() {
    return attachmentFilePath;
  }

  public void setAttachmentFilePath(String attachmentFilePath) {
    this.attachmentFilePath = attachmentFilePath;
  }

  public String getEmailTemplateLocation() {
    return emailTemplateLocation;
  }

  public void setEmailTemplateLocation(String emailTemplateLocation) {
    this.emailTemplateLocation = emailTemplateLocation;
  }

  public String getBatchJobName() {
    return batchJobName;
  }

  public void setBatchJobName(String batchJobName) {
    this.batchJobName = batchJobName;
  }

  public void setSimpleMailMessage(SimpleMailMessage simpleMailMessage) {
    this.simpleMailMessage = simpleMailMessage;
  }

  public void setMailSender(JavaMailSender mailSender) {
    this.mailSender = mailSender;
  }

  public void sendMail() {
    try {
      LOG.debug("sendMail :: simpleMailMessage: " + simpleMailMessage);

      MimeMessage message = mailSender.createMimeMessage();
      MimeMessageHelper reconDocuMimeMessage = new MimeMessageHelper(message, true);
      reconDocuMimeMessage.setFrom(simpleMailMessage.getFrom());
      reconDocuMimeMessage.setTo(simpleMailMessage.getTo());
      reconDocuMimeMessage.setSubject(simpleMailMessage.getSubject());

      Map<String, Object> model = new HashMap<String, Object>();
      model.put("batchJobName", this.getBatchJobName());

      String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine,
                                                                this.getEmailTemplateLocation(),
                                                                "utf-8",
                                                                model);

      reconDocuMimeMessage.setText(text, true);
      FileSystemResource file = new FileSystemResource(this.getAttachmentFilePath());
      reconDocuMimeMessage.addAttachment(file.getFilename(), file);

      mailSender.send(message);

    } catch (MessagingException e) {
      throw new MailParseException(e);
    }

  }

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
