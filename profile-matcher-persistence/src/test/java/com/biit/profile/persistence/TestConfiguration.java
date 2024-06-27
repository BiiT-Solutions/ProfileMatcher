package com.biit.profile.persistence;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.TestPropertySource;

@Configuration
@TestPropertySource("classpath:application.properties")
@ComponentScan({"com.biit.profile.persistence"})
public class TestConfiguration {

}
