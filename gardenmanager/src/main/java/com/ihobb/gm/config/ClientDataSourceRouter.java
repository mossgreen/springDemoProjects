package com.ihobb.gm.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Component
public class ClientDataSourceRouter extends AbstractRoutingDataSource {

    private Map<String, HikariDataSource> tenants = new HashMap<>();

    @Override
    public void afterPropertiesSet() {
        // Nothing to do ..
    }

    @Override
    protected DataSource determineTargetDataSource() {
        String lookupKey = (String) determineCurrentLookupKey();

        return tenants.get(lookupKey);
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return DBContextHolder.getCurrentDb();
    }
}
