package com.api.jesus_king_tech;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.RequestMapping;


@EnableFeignClients
@SpringBootApplication
public class JesusKingTechApplication {

	public static void main(String[] args) {
		SpringApplication.run(JesusKingTechApplication.class, args);
	}

}
