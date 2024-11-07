package com.api.jesus_king_tech.api.observer;

public class EmailTemplateObserver {

    public static String getNewUserBody(String nomeUsuario, String emailUsuario) {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; }" +
                ".container { max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #ddd; border-radius: 10px; }" +
                ".header { text-align: center; padding: 10px 0; }" +
                ".header h1 { color: #333; }" +
                ".content { padding: 20px; }" +
                ".content p { font-size: 16px; color: #555; }" +
                ".user-info { font-size: 18px; font-weight: bold; color: #333; text-align: center; margin: 20px 0; }" +
                ".footer { text-align: center; padding: 10px 0; font-size: 12px; color: #aaa; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<div class='header'>" +
                "<h1>Novo Usuário Cadastrado</h1>" +
                "</div>" +
                "<div class='content'>" +
                "<p>Olá,</p>" +
                "<p>Um novo usuário foi cadastrado no sistema. Aqui estão os detalhes:</p>" +
                "<div class='user-info'>" +
                "<p>Nome: " + nomeUsuario + "</p>" +
                "<p>Email: " + emailUsuario + "</p>" +
                "</div>" +
                "</div>" +
                "<div class='footer'>" +
                "<p>&copy; 2024 Jesus King Tech. Todos os direitos reservados.</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
    }
}
