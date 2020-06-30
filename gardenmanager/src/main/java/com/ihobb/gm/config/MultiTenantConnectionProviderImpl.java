package com.ihobb.gm.config;

import com.ihobb.gm.utility.DataSourceUtil;
import lombok.extern.log4j.Log4j2;
import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Log4j2
@Configuration
public class MultiTenantConnectionProviderImpl extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl {
    private static final long serialVersionUID = 1L;

    private final Map<String, DataSource> dataSources = new HashMap<>();

    @Autowired
    @Qualifier("adminDataSourceProperties")
    private DataSourceProperties properties;


    @Override
    public DataSource selectAnyDataSource() {
        if (dataSources.isEmpty()) {

            final DataSource ds = DataSourceUtil.createAndConfigureDataSource(properties);
            dataSources.put(DBContextHolder.DEFAULT_TENANT_ID, ds);
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
        if (!tenantIdentifier.equals(DBContextHolder.getCurrentDb())) {
            tenantIdentifier = DBContextHolder.getCurrentDb();
        }
        return tenantIdentifier;
    }
}
