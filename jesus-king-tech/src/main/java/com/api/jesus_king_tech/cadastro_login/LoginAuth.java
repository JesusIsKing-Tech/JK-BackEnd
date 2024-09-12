package com.api.jesus_king_tech.cadastro_login;

public class LoginAuth {

    private String email;
    private String senha;

    public LoginAuth() {
    }

    public LoginAuth(String email, String senha) {
        this.email = email;
        this.senha = senha;
    }

    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }
}
