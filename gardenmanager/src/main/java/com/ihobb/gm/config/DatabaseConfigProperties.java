package com.ihobb.gm.config;

import lombok.Builder;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Builder
//@ConfigurationProperties("spring.datasource")
public class DatabaseConfigProperties {

    private String url = "jdbc:postgresql://127.0.0.1:5432";
    private String dbName;
    private String username = "postgres";
    private String password = "ihobb";
    private String driverClassName = "org.postgresql.Driver";
    private long connectionTimeout;
    private int maxPoolSize;
    private long idleTimeout;
    private int minIdle;
    private String poolName;

}
