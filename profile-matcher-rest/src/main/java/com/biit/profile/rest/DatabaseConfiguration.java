package com.biit.profile.rest;

/*-
 * #%L
 * Profile Matcher (Rest)
 * %%
 * Copyright (C) 2024 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@EnableJpaRepositories(entityManagerFactoryRef = "profileMatcherFactory", transactionManagerRef = "profileMatcherTransactionManager",
        basePackages = {DatabaseConfiguration.PACKAGE})
public class DatabaseConfiguration {
    public static final String PACKAGE = "com.biit.profile.persistence";

    private final Environment environment;

    public DatabaseConfiguration(Environment environment) {
        this.environment = environment;
    }

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.profile.datasource")
    public DataSource profileMatcherDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean profileMatcherFactory(EntityManagerFactoryBuilder builder,
                                                                        @Qualifier("profileMatcherDataSource") DataSource dataSource) {
        final HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", environment.getProperty("spring.profile.datasource.hibernate.ddl-auto"));
        return builder.dataSource(dataSource).properties(properties).packages(PACKAGE).build();
    }

    @Bean
    @Primary
    public PlatformTransactionManager profileMatcherTransactionManager(
            @Qualifier("profileMatcherFactory") EntityManagerFactory profileMatcherFactory) {
        return new JpaTransactionManager(profileMatcherFactory);
    }
}
