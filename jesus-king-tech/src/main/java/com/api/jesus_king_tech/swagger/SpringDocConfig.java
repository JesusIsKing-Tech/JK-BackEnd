package com.api.jesus_king_tech.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Array;
import java.util.Arrays;

@Configuration
public class SpringDocConfig{

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Jesus King Tech")
                        .version("v1")
                        .description("API para Gerenciamento de doação de alimentos"))
                .tags(
                        Arrays.asList(
                                new Tag().name("Usuários").description("Gerencia os usuários"),
                                new Tag().name("Endereços").description("Gerencia os endereços"),
                                new Tag().name("Produtos").description("Gerencia os produtos")
                        )
                )
                ;
    }
}
