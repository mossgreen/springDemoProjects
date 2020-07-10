package com.ihobb.gm.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ihobb.gm.utility.DataSourceUtil;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

public class ClientDataSourceRouter extends AbstractRoutingDataSource {

    private Map<String, DataSource> tenants = new HashMap<>();

    private final DataSourceProperties dataSourceProperties;

    public ClientDataSourceRouter(@Qualifier("adminDataSourceProperties")DataSourceProperties properties) {
        this.dataSourceProperties = properties;
    }

    @Override
    public void afterPropertiesSet() {
        // Nothing to do ..
    }

    @Override
    protected DataSource determineTargetDataSource() {
        final String currentDb = DBContextHolder.getCurrentDb();

        if (null != currentDb) {
            final DbConfigProperties configProperties = new DbConfigProperties(dataSourceProperties);
            configProperties.setDbName(currentDb);
            final DataSource dataSource = DataSourceUtil.createAndConfigureDataSource(configProperties);
            tenants.put(currentDb, dataSource);
            return dataSource;
        }

        return null;
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return DBContextHolder.getCurrentDb();
    }
}
