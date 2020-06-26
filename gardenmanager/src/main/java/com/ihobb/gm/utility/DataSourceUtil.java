package com.ihobb.gm.utility;

import com.ihobb.gm.config.DatabaseConfigProperties;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.log4j.Log4j2;

import javax.sql.DataSource;

@Log4j2
public final class DataSourceUtil {

    public static DataSource createAndConfigureDataSource(DatabaseConfigProperties property) {

        HikariDataSource ds = new HikariDataSource();
        ds.setUsername(property.getUsername());
        ds.setPassword(property.getPassword());
        ds.setJdbcUrl(property.getUrl() + property.getDbName());
        ds.setDriverClassName(property.getDriverClassName());
        // HikariCP settings - could come from the master_tenant table but
        // hardcoded here for brevity
        // Maximum waiting time for a connection from the pool
        ds.setConnectionTimeout(20000);
        // Minimum number of idle connections in the pool
        ds.setMinimumIdle(3);
        // Maximum number of actual connection in the pool
        ds.setMaximumPoolSize(500);
        // Maximum time that a connection is allowed to sit idle in the pool
        ds.setIdleTimeout(300000);
        ds.setConnectionTimeout(20000);
        // Setting up a pool name for each tenant datasource
        String tenantConnectionPoolName = property.getDbName() + "-connection-pool";
        ds.setPoolName(tenantConnectionPoolName);
        log.info("Configured datasource:" + property.getDbName() + ". Connection pool name:" + tenantConnectionPoolName);
        return ds;
    }
}
