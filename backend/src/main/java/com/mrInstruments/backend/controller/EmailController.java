package com.mrInstruments.backend.controller;

import com.mrInstruments.backend.dto.EmailMessageDto;
import com.mrInstruments.backend.dto.UserDto;
import com.mrInstruments.backend.entities.User;
import com.mrInstruments.backend.exception.BadRequestException;
import com.mrInstruments.backend.service.UserService;
import com.mrInstruments.backend.service.email.EmailTemplateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;

@RestController
@RequestMapping("/email")
public class EmailController {

    private final EmailTemplateService emailTemplateService;

    private final UserService userService;

    public EmailController(EmailTemplateService emailTemplateService, UserService userService) {
        this.emailTemplateService = emailTemplateService;
        this.userService = userService;
    }

    @PermitAll
    @PostMapping("/simple")
    public ResponseEntity<?> enviarMail(@RequestBody EmailMessageDto emailMessage) throws Exception {
//        emailService.sendEmail(emailMessage.getTo(), emailMessage.getSubject(), emailMessage.getText());
        return ResponseEntity.ok("Mensaje enviado");
    }

    @PermitAll
    @PostMapping("/reenvio")
    public ResponseEntity<?> reenviarMail(@RequestBody UserDto userDto) throws Exception {
        try{
            emailTemplateService.sendEmailFromTemplate(userDto.getNombre(), userDto.getEmail());
        } catch (Exception exception){
            throw new BadRequestException("Ocurrio un error en el envio de email");
        }
        return ResponseEntity.ok("Mensaje enviado");
    }
}
