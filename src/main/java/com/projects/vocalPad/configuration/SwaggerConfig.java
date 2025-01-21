package com.projects.vocalPad.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI swaggerOpenAPI(){
        return new OpenAPI().info(
                new Info().title("Vocal Pad")
                        .description("By - Pratik Chandkavathe")
                )
                .servers(List.of(new Server().url("http://localhost:8080").description("Local"),
                    new Server().url("heroku").description("Deployed app url")))
                .tags(List.of(
                        new Tag().name("Login - Signup APIs"),
                        new Tag().name("User Account Management APIs"),
                        new Tag().name("Notes Management APIs"),
                        new Tag().name("Notes to Voice Conversion APIs"),
                        new Tag().name("Admin controlled APIs")
                ));
    }
}