package com.api.jesus_king_tech.api.observer;


import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class JavaMailObserver {
    public static void sendEmail(String to, String nomeUsuario, String emailUsuario){

        final String email = System.getenv("EMAIL_EMAIL");
        final String password = System.getenv("EMAIL_SENHA");

        System.out.println(email);
        System.out.println(password);

        Properties props = new Properties();

        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator(){
                    protected PasswordAuthentication getPasswordAuthentication(){
                        return new PasswordAuthentication(email,password);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(email));
            message.setRecipient(Message.RecipientType.TO, InternetAddress.parse(to)[0]);
            message.setSubject("Novo usuario cadastrado");
            message.setContent(EmailTemplateObserver.getNewUserBody(nomeUsuario, emailUsuario), "text/html; charset=utf-8");

            Transport.send(message);
            System.out.println("Email enviado com sucesso");
        }catch (MessagingException e){
            System.out.println("DEU ERRO");
            throw new RuntimeException(e);
        }
    }
}
