package com.mrInstruments.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Properties;

@SpringBootApplication
public class MrInstrumentsApplication {

    public static void main(String[] args) {
        SpringApplication.run(MrInstrumentsApplication.class, args);
    }

    @Configuration
    public class Config {

        @Bean
        public JavaMailSender javaMailSender() {
            JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
            mailSender.setHost("smtp.gmail.com");
            mailSender.setPort(587);

            mailSender.setUsername("mrinstrumentscorporation@gmail.com");
            mailSender.setPassword("xiadizgmazmqdvpz");

            Properties props = mailSender.getJavaMailProperties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.debug", "true");

            return mailSender;
        }

//        @Bean
//        public WebMvcConfigurer corsConfigurer() {
//            return new WebMvcConfigurer(){
//                @Override
//                public void addCorsMappings(CorsRegistry registry) {
//                    registry.addMapping("/**")
//                            .allowedOrigins("/**")
//                            .allowedMethods("GET","POST","PUT","DELETE").allowedHeaders("*")
//                            .allowCredentials(true).maxAge(3600);
//////                    registry.addMapping("/**")
//////                            .allowedOrigins("http://localhost:3000")
//////                            .allowedMethods("GET","POST","PUT","DELETE")
//////                            .allowCredentials(true).maxAge(3600);
//////                    registry.addMapping("/**")
//////                            .allowedOrigins("http://localhost:5173")
//////                            .allowedMethods("GET","POST","PUT","DELETE")
//////                            .allowCredentials(true).maxAge(3600);
//                }
//            };
//        }
    }
}