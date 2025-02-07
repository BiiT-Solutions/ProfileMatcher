package com.biit.profile.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ConfigurationPropertiesScan({"com.biit.profile"})
@EnableJpaRepositories({"com.biit.profile.persistence.repositories"})
@EntityScan({"com.biit.profile.persistence.entities"})
@ComponentScan({"com.biit.profile", "com.biit.usermanager.client", "com.biit.server.client", "com.biit.factmanager.client"})
public class Server {
    public static void main(String[] args) {
        SpringApplication.run(Server.class, args);
    }
}
