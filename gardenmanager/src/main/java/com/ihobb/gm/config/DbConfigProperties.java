package com.ihobb.gm.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(DbConfigProperties.PREFIX)
public class DbConfigProperties {

    public static final String PREFIX = "spring.datasource";

    private String jdbcUrl;
    private String dbName;
    private String username;
    private String password;
    private String driverClassName;
    private long connectionTimeout;
    private int maxPoolSize;
    private long idleTimeout;
    private int minIdle;
    private String poolName;

    public void setDbName(String dbName) {
        this.dbName = dbName;
        this.jdbcUrl = this.jdbcUrl + this.dbName;
    }
}
