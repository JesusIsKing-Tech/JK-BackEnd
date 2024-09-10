package com.api.jesus_king_tech.cadastro_login.JavaMailRecuperacaoSenha;

public class EmailTemplate {

    public static String getPasswordResetEmailBody(String code) {
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
                ".code { font-size: 24px; font-weight: bold; color: #333; text-align: center; margin: 20px 0; }" +
                ".footer { text-align: center; padding: 10px 0; font-size: 12px; color: #aaa; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<div class='header'>" +
                "<h1>Recuperação de Senha</h1>" +
                "</div>" +
                "<div class='content'>" +
                "<p>Olá,</p>" +
                "<p>Você solicitou a recuperação de sua senha. Use o código abaixo para redefinir sua senha:</p>" +
                "<div class='code'>" + code + "</div>" +
                "<p>Se você não solicitou a recuperação de senha, por favor ignore este email.</p>" +
                "</div>" +
                "<div class='footer'>" +
                "<p>&copy; 2023 Jesus King Tech. Todos os direitos reservados.</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
    }
}
