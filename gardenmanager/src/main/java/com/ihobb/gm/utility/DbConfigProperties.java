package com.ihobb.gm.utility;

import lombok.Data;

@Data
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

    public void setDbName(String dbName) {
        this.dbName = dbName;
        this.jdbcUrl = this.jdbcUrl + this.dbName;
    }
}
