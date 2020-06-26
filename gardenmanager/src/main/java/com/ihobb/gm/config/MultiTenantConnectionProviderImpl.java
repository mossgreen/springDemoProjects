package com.ihobb.gm.config;

import com.ihobb.gm.utility.DataSourceUtil;
import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.AbstractMultiTenantConnectionProvider;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
public class MultiTenantConnectionProviderImpl extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl {

    private static final long serialVersionUID = 1L;
    private final Map<String, DataSource> dataSources = new HashMap<>();

    @Override
    protected DataSource selectAnyDataSource() {
        if (dataSources.isEmpty()) {
            final DatabaseConfigProperties db = DatabaseConfigProperties.builder()
                .dbName("admin")
                .url("jdbc:postgresql://127.0.0.1:5432/" + DBContextHolder.DEFAULT_TENANT_ID)
                .build();
            dataSources.put(DBContextHolder.DEFAULT_TENANT_ID, DataSourceUtil.createAndConfigureDataSource(db));
        }
        return dataSources.get(DBContextHolder.DEFAULT_TENANT_ID);
    }

    @Override
    protected DataSource selectDataSource(String tenantIdentifier) {

        tenantIdentifier = initializeTenantIfLost(tenantIdentifier);
        if (!this.dataSources.containsKey(tenantIdentifier)) {

            final DatabaseConfigProperties db = DatabaseConfigProperties.builder()
                .dbName(tenantIdentifier)
                .url("jdbc:postgresql://127.0.0.1:5432/"+ tenantIdentifier)
                .build();
            dataSources.put(DBContextHolder.DEFAULT_TENANT_ID, DataSourceUtil.createAndConfigureDataSource(db));
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
