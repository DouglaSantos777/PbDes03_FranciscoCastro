package com.desafio03.ms_event.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocOpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("REST API - MS EVENTS")
                                .description("API for event manager")
                                .version("v1")
                                .contact(new Contact()
                                        .name("Francisco Douglas")
                                        .email("francisco.castro.pb@compasso.com.br")
                                        .url("https://github.com/DouglaSantos777"))
                                .license(
                                        new License().name("Apache 2.0").url("https://www.apache.org/licenses/LICENSE-2.0")));
    }
}
