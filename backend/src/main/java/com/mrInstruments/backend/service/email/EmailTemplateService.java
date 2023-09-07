package com.mrInstruments.backend.service.email;

import com.mrInstruments.backend.exception.BadRequestException;
import com.mrInstruments.backend.exception.MailSenderException;
import lombok.extern.log4j.Log4j2;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import static com.mrInstruments.backend.utils.UtilsFileManager.fileTextToString;

@Service
@Log4j2
@EnableAsync
public class EmailTemplateService {

    private final JavaMailSender mailSender;

    public EmailTemplateService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void sendEmailFromTemplate(String nombre, String email) throws MessagingException, BadRequestException, MailSenderException {
        MimeMessage message = mailSender.createMimeMessage();

        message.setFrom(new InternetAddress("mrinstrumentscorporation@gmail.com"));
        message.setRecipients(MimeMessage.RecipientType.TO, email);
        message.setSubject("Confirmación de registro exitoso en MrInstruments");

        // Read the HTML template into a String variable
        String nombreFichero = "src/main/resources/static/template.html";
        String htmlTemplate = fileTextToString(nombreFichero);

        htmlTemplate = htmlTemplate.replace("${name}", nombre);
        htmlTemplate = htmlTemplate.replace("${email_usuario}", email);

        // Set the email's content to be the HTML template
        message.setContent(htmlTemplate, "text/html; charset=utf-8");

        log.info("Begin sendMail");
        try {
            mailSender.send(message);
            log.info("End sendMail");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new MailSenderException(e.getMessage());
        }
    }
}