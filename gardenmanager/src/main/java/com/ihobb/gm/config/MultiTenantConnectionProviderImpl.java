package com.ihobb.gm.config;

import com.ihobb.gm.utility.DataSourceUtil;
import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class MultiTenantConnectionProviderImpl extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl {
    private static final long serialVersionUID = 1L;

    public final DbConfigProperties dbConfigProperties;
    private final Map<String, DataSource> dataSources = new HashMap<>();

    public MultiTenantConnectionProviderImpl(DbConfigProperties dataSourceProperties) {
        this.dbConfigProperties = dataSourceProperties;
    }

    @Override
    protected DataSource selectAnyDataSource() {
        if (dataSources.isEmpty()) {
            dataSources.put(DBContextHolder.DEFAULT_TENANT_ID, DataSourceUtil.createAndConfigureDataSource(dbConfigProperties));
        }
        return dataSources.get(DBContextHolder.DEFAULT_TENANT_ID);
    }

    @Override
    protected DataSource selectDataSource(String tenantIdentifier) {

        tenantIdentifier = initializeTenantIfLost(tenantIdentifier);
        if (!this.dataSources.containsKey(tenantIdentifier)) {

            dbConfigProperties.setDbName(tenantIdentifier);
            dataSources.put(DBContextHolder.DEFAULT_TENANT_ID, DataSourceUtil.createAndConfigureDataSource(dbConfigProperties));
        }

        if (!this.dataSources.containsKey(tenantIdentifier)) {

        } else {
            throw new RuntimeException("db not found exception"); //todo
        }

        return dataSources.get(tenantIdentifier);
    }

    private String initializeTenantIfLost(String tenantIdentifier) {
        if (tenantIdentifier != DBContextHolder.getCurrentDb()) {
            tenantIdentifier = DBContextHolder.getCurrentDb();
        }
        return tenantIdentifier;
    }
}
