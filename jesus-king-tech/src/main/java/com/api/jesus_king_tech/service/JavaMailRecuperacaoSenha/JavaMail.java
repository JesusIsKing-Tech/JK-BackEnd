package com.api.jesus_king_tech.service.JavaMailRecuperacaoSenha;

import com.api.jesus_king_tech.api.config.EnvironmentConfig;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.Random;

public class JavaMail {
    private static String code;
    public static void sendEmail(String to, String nomeUsuario){

        final String email = EnvironmentConfig.get("EMAIL_EMAIL");
        final String password = EnvironmentConfig.get("EMAIL_SENHA");

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
            generateVerificationCode();
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(email));
            message.setRecipient(Message.RecipientType.TO, InternetAddress.parse(to)[0]);
            message.setSubject("Seu codigo de Recuperação de senha");
            message.setContent(EmailTemplate.getPasswordResetEmailBody(code, nomeUsuario), "text/html; charset=utf-8");

            Transport.send(message);
            System.out.println("O codigo gerado foi: " + code);
            System.out.println("Email enviado com sucesso");
        }catch (MessagingException e){
            System.out.println("DEU ERRO");
            throw new RuntimeException(e);
        }
    }

    private static void generateVerificationCode(){
        Random random = new Random();

        int code = 100000 + random.nextInt(900000);

        setCode(String.valueOf(code));
    }

    public static String getCode() {
        return code;
    }

    public static void setCode(String code) {
        JavaMail.code = code;
    }
}
