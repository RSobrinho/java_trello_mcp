package com.sobrinho.java_trello_mcp.infra.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI trelloOpenAPI() {
    return new OpenAPI()
        .info(new Info()
            .title("API Trello")
            .description("API para integração com o Trello")
            .version("1.0")
            .contact(new Contact()
                .name("Sobrinho")
                .url("https://github.com/sobrinho")
                .email("contato@sobrinho.com"))
            .license(new License()
                .name("MIT License")
                .url("https://opensource.org/licenses/MIT")));
  }
}