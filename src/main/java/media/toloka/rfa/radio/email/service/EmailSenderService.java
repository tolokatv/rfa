package media.toloka.rfa.radio.email.service;


//import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import media.toloka.rfa.radio.email.model.Mail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
//@RequiredArgsConstructor
public class EmailSenderService {
//    https://www.baeldung.com/spring-email

    Logger logger = LoggerFactory.getLogger(EmailSenderService.class);
    @Autowired
    private JavaMailSenderImpl emailSender;
//
    @Autowired
    private SpringTemplateEngine templateEngine;

    @Value("${media.toloka.rfa..mail.smtphost}")
    private String mailhost;
    @Value("${media.toloka.rfa.mail.smtpport}")
    private int mailport;
    @Value("${media.toloka.rfa.mail.protocol}")
    private String  mailprotocol;
    @Value("${media.toloka.rfa.mail.defaultencoding}")
    private String maildefaultencoding;

    public void sendEmail(Mail mail) throws MessagingException //, IOException
    {
        JavaMailSenderImpl emailSender = new JavaMailSenderImpl();
        emailSender.setHost(mailhost);
        emailSender.setPort(mailport);
        emailSender.setProtocol(mailprotocol);
        emailSender.setDefaultEncoding(maildefaultencoding);

//        emailSender.setJavaMailProperties();
        jakarta.mail.internet.MimeMessage message = emailSender.createMimeMessage();

        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
            String html = getHtmlContent(mail);
            helper.setTo(mail.getTo());
            helper.setFrom(mail.getFrom());
            helper.setSubject(mail.getSubject());
            helper.setText(html, true);
        } catch (MessagingException e) {
            logger.info("Щось пішло не так при створенні хелпера. :(");
        } catch (jakarta.mail.MessagingException e) {
            logger.info("Щось пішло не так при створенні хелпера. :(");
//            throw new RuntimeException(e);
        }

//        String html = getHtmlContent(mail);


//        helper.setTo(mail.getTo());
//        helper.setFrom(mail.getFrom());
//        helper.setSubject(mail.getSubject());
//        helper.setText(html, true);

        emailSender.send(message);
    }

    private String getHtmlContent(Mail mail) {
        Context context = new Context();
        context.setVariables(mail.getHtmlTemplate().getProps());
        return templateEngine.process(mail.getHtmlTemplate().getTemplate(), context);
    }


}