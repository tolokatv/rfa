package media.toloka.rfa.radio.email.service;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

//import jakarta.mail.MessagingException;
//import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import media.toloka.rfa.radio.email.model.Mail;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import java.io.IOException;
import java.nio.charset.StandardCharsets;


@Service
@RequiredArgsConstructor
public class EmailSenderService {
//    https://www.baeldung.com/spring-email
    @Autowired
    private JavaMailSenderImpl emailSender1;

    @Autowired
    private SpringTemplateEngine templateEngine;

    public void sendEmail(Mail mail)
            throws MessagingException, IOException
    {

        JavaMailSenderImpl emailSender = new JavaMailSenderImpl();
        emailSender.setHost("gate.iw.com.ua");
        emailSender.setPort(25);
        emailSender.setProtocol("smtp");
        emailSender.setDefaultEncoding("UTF-8");

//        emailSender.setJavaMailProperties();
        MimeMessage message = emailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());

        String html = getHtmlContent(mail);


        helper.setTo(mail.getTo());
        helper.setFrom(mail.getFrom());
        helper.setSubject(mail.getSubject());
        helper.setText(html, true);

        emailSender.send(message);
    }

    private String getHtmlContent(Mail mail) {
        Context context = new Context();
        context.setVariables(mail.getHtmlTemplate().getProps());
        return templateEngine.process(mail.getHtmlTemplate().getTemplate(), context);
    }


}