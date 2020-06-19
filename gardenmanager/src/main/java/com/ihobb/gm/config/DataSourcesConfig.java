package com.ihobb.gm.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

//@Configuration
public class DataSourcesConfig {

    //todo create a database use jdbc?

    @Bean
    public HikariDataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:postgresql://127.0.0.1:5432/admin");
        dataSource.setUsername("postgres");
        dataSource.setPassword("ihobb");
        return dataSource;
    }


    @Primary
    @Bean(name = "adminDbConfig")
    @ConfigurationProperties("app.datasource.ds1")
    public DataSourceProperties adminDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Primary
    @Bean(name = "adminDataSource")
    @ConfigurationProperties("app.datasource.ds1")
    public HikariDataSource adminDataSource(@Qualifier("adminDbConfig") DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder()
            .type(HikariDataSource.class)
            .build();
    }

    @Bean(name = "clientDbConfig")
    @ConfigurationProperties("app.datasource.ds2")
    public DataSourceProperties clientDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "clientDataSource")
    @ConfigurationProperties("app.dtasource.ds")
    public HikariDataSource clientDataSource(@Qualifier("clientDbConfig") DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder()
            .type(HikariDataSource.class)
            .build();
    }

}
