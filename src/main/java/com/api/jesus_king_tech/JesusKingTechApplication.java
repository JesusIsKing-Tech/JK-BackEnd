package com.api.jesus_king_tech;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
//import org.springframework.cloud.openfeign.EnableFeignClients;


@EnableFeignClients
@SpringBootApplication
public class JesusKingTechApplication {

	public static void main(String[] args) {
//		Dotenv dotenv = Dotenv.configure().load();
//		System.setProperty("DB_USERNAME", dotenv.get("DB_USERNAME"));
//		System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
//		System.setProperty("DB_HOST", dotenv.get("DB_HOST"));
//		System.setProperty("EMAIL_EMAIL", dotenv.get("EMAIL_EMAIL"));
//		System.setProperty("EMAIL_SENHA", dotenv.get("EMAIL_SENHA"));

		System.setProperty("DB_USERNAME", System.getenv("DB_USERNAME"));
		System.setProperty("DB_PASSWORD", System.getenv("DB_PASSWORD"));
		System.setProperty("DB_HOST", System.getenv("DB_HOST"));
		System.setProperty("EMAIL_EMAIL", System.getenv("EMAIL_EMAIL"));
		System.setProperty("EMAIL_SENHA", System.getenv("EMAIL_SENHA"));

		SpringApplication.run(JesusKingTechApplication.class, args);
	}

}
