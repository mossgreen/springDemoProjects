package com.ihobb.gm.utility;

import com.ihobb.gm.config.DbConfigProperties;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.log4j.Log4j2;

import javax.sql.DataSource;

@Log4j2
public final class DataSourceUtil {

    public static DataSource createAndConfigureDataSource(DbConfigProperties properties) {

        HikariDataSource ds = new HikariDataSource();
        ds.setUsername(properties.getUsername());
        ds.setPassword(properties.getPassword());
        ds.setJdbcUrl(properties.getJdbcUrl());
        ds.setDriverClassName(properties.getDriverClassName());
        ds.setConnectionTimeout(20000);
        ds.setMinimumIdle(3);
        ds.setMaximumPoolSize(500);
        ds.setIdleTimeout(300000);
        ds.setConnectionTimeout(20000);
        String tenantConnectionPoolName = properties.getDbName() + "-connection-pool";
        ds.setPoolName(tenantConnectionPoolName);
        log.info("Configured datasource:" + properties.getDbName() + ". Connection pool name:" + tenantConnectionPoolName);

        final DataSource dataSource = ds.getDataSource();
        log.info("----" + dataSource);
        return ds;
    }
}
