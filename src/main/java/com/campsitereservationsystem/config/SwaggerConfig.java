package com.campsitereservationsystem.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI myOpenAPI() {
        Server devServer = new Server();
        devServer.setUrl("http://localhost:8080");
        devServer.setDescription("Server URL in Development environment");

        Contact contact = new Contact();
        contact.setEmail("m.vascelli@outlook.com");
        contact.setName("Mike Vascelli");
        contact.setUrl("https://github.com/mike-vascelli");

        License mitLicense = new License().name("MIT License").url("https://choosealicense.com/licenses/mit/");

        Info info = new Info()
                .title("Campsite Reservation System")
                .version("1.0")
                .contact(contact)
                .description("A system that provides APIs to make reservations for the campsite")
                .license(mitLicense);

        return new OpenAPI().info(info).servers(List.of(devServer));
    }
}