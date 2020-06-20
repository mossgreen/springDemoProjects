package com.ihobb.gm.config;

import com.zaxxer.hikari.HikariDataSource;


public class TenantDataSource {

    public TenantDataSource() {
        this("admin");
    }

    public TenantDataSource(String dataBaseName) {
        final HikariDataSource dataSource = buildDataSource(dataBaseName);
        TenantDataSourceContextHolder.setDataSourceContext(dataSource);
    }

    private HikariDataSource buildDataSource(String dataBaseName) {
        HikariDataSource dataSource = new HikariDataSource();

        dataSource.setInitializationFailTimeout(0);
        dataSource.setIdleTimeout(50000L);
        dataSource.setMaximumPoolSize(5);
        dataSource.setDataSourceClassName(dataBaseName); // todo what is source class name?
        dataSource.addDataSourceProperty("url", "demo url");
        dataSource.addDataSourceProperty("user", "user from application.properteis");
        dataSource.addDataSourceProperty("password", "password from properteis?");

        return dataSource;
    }
}
