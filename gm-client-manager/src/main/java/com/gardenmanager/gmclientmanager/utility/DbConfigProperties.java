package com.gardenmanager.gmclientmanager.utility;

import com.gardenmanager.gmclientmanager.DataSourceConfig;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.stereotype.Component;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Component
public class DbConfigProperties {

    public static final String JDBC_URL = "jdbc:postgresql://127.0.0.1:5432/";

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

    public DbConfigProperties(DataSourceProperties properties) {
        this.jdbcUrl = properties.getUrl();
        this.username = properties.getUsername();
        this.password = properties.getPassword();
        this.driverClassName = properties.getDriverClassName();
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
        this.jdbcUrl = JDBC_URL + this.dbName + "?createDatabaseIfNotExist=true";
    }
}
