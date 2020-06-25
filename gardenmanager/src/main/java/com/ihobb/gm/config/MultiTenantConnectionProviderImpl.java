package com.ihobb.gm.config;

import org.hibernate.cfg.Environment;
import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.AbstractMultiTenantConnectionProvider;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class MultiTenantConnectionProviderImpl extends AbstractMultiTenantConnectionProvider {

    /**
     * The Hibernate ConnectionProvider is a factory of database connections,
     * hence each database catalog will have its own ConnectionProvider instance.
     */
    public static final MultiTenantConnectionProviderImpl INSTANCE = new MultiTenantConnectionProviderImpl();

    private final Map<String, ConnectionProvider> connectionProviderMap = new HashMap<>();

    Map<String, ConnectionProvider> getConnectionProviderMap() {
        return connectionProviderMap;
    }

    @Override
    protected ConnectionProvider getAnyConnectionProvider() {
        return connectionProviderMap.get(TenantContextHolder.DEFAULT_TENANT);
    }

    @Override
    protected ConnectionProvider selectConnectionProvider(String tenantIdentifier) {
        return connectionProviderMap.get(tenantIdentifier);
    }

    private void addTenantConnectionProvider(String tenantId, DataSource tenantDataSource, Properties properties) {

        DatasourceConnectionProviderImpl connectionProvider = new DatasourceConnectionProviderImpl();
        connectionProvider.setDataSource(tenantDataSource);
        connectionProvider.configure(properties);

        MultiTenantConnectionProviderImpl.INSTANCE.getConnectionProviderMap().put(tenantId, connectionProvider);
    }

//    private void addTenantConnectionProvider(String tenantId) {
//
//        DataSourceProvider dataSourceProvider = database()
//            .dataSourceProvider();
//
//        Properties properties = properties();
//
//        MysqlDataSource tenantDataSource = new MysqlDataSource();
//        tenantDataSource.setDatabaseName(tenantId);
//        tenantDataSource.setUser(dataSourceProvider.username());
//        tenantDataSource.setPassword(dataSourceProvider.password());
//
//        properties.put(Environment.DATASOURCE, dataSourceProxyType().dataSource(tenantDataSource));
//
//        addTenantConnectionProvider(tenantId, tenantDataSource, properties);
//    }

}
