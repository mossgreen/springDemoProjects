package com.ihobb.gm.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;

public class DynamicTenantAwareRoutingDataSource extends AbstractRoutingDataSource {

//    private final Map<String, HikariDataSource> tenants;

    @Override
    public void afterPropertiesSet() {
        // todo sth needs to do
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return TenantDataSourceContextHolder.getDataSourceContext();
    }

    @Override
    protected DataSource determineTargetDataSource() {
        return (DataSource)determineCurrentLookupKey();
    }


}
