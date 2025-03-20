package com.foodygo.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("FoodyGo - Food delivery platform for Dormitory B - HCMC National University Dormitory"))
                .addServersItem(
                        new Server().url("https://foodygo.theanh0804.duckdns.org")
                )
                .addServersItem(
                        new Server().url("http://localhost:8080")
                )
                .addSecurityItem(new SecurityRequirement().addList("FoodyGo Authentication Service"))
                .components(new Components().addSecuritySchemes("FoodyGo Authentication Service", new SecurityScheme()
                        .name("Dormitory B - HCMC National University Dormitory").type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")));
    }
}
