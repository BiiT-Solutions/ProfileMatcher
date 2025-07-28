package com.biit.profile.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableCaching
@ConfigurationPropertiesScan({"com.biit.profile"})
@EntityScan({"com.biit.profile.persistence.entities"})
@ComponentScan({"com.biit.profile", "com.biit.usermanager.client", "com.biit.server.client", "com.biit.kafka"})
public class Server {
    public static void main(String[] args) {
        SpringApplication.run(Server.class, args);
    }
}
