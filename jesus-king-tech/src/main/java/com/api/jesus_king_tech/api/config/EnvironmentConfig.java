package com.api.jesus_king_tech.api.config;

import io.github.cdimascio.dotenv.Dotenv;

public class EnvironmentConfig {

//    Configutação necessaria para variaveis de ambiente

    private static final Dotenv dotenv = Dotenv.load();

    public static String get(String key) {
        return dotenv.get(key);
    }
}
