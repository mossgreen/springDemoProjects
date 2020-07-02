package com.ihobb.gm.config;

import org.hibernate.MultiTenancyStrategy;
import org.hibernate.cfg.Environment;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Configuration
@EnableJpaRepositories(
    basePackages = "com.ihobb.gm.client",
    entityManagerFactoryRef = "tenantEntityManagerFactory",
    transactionManagerRef = "tenantTransactionManager"
)
@EnableTransactionManagement
public class TenantDataSourceConfig {

    private final DataSourceProperties properties;

    public TenantDataSourceConfig(DataSourceProperties properties) {
        this.properties = properties;
    }

    @Bean(name = "multiTenantConnectionProvider")
    @ConditionalOnBean(name = "adminEntityManagerFactory")
    public MultiTenantConnectionProvider multiTenantConnectionProvider() {

        DbConfigProperties configProperties = new DbConfigProperties(properties);
        return new MultiTenantConnectionProviderImpl(configProperties);
    }

    @Bean(name = "currentTenantIdentifierResolver")
    public CurrentTenantIdentifierResolver currentTenantIdentifierResolver() {
        return new CurrentTenantIdentifierResolverImpl();
    }

    @Bean(name = "tenantEntityManagerFactory")
    @ConditionalOnBean(name = "multiTenantConnectionProvider")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
        @Qualifier("multiTenantConnectionProvider") MultiTenantConnectionProvider connectionProvider,
        @Qualifier("currentTenantIdentifierResolver") CurrentTenantIdentifierResolver tenantResolver) {

        LocalContainerEntityManagerFactoryBean emfBean = new LocalContainerEntityManagerFactoryBean();
        //All tenant related entities, repositories and service classes must be scanned
        emfBean.setPackagesToScan(packagesToScan());
        emfBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        emfBean.setPersistenceUnitName("tenant-persistence-unit");
        Map<String, Object> properties = new HashMap<>();
        properties.put(Environment.MULTI_TENANT, MultiTenancyStrategy.DATABASE);
        properties.put(Environment.MULTI_TENANT_CONNECTION_PROVIDER, connectionProvider);
        properties.put(Environment.MULTI_TENANT_IDENTIFIER_RESOLVER, tenantResolver);
        properties.put(org.hibernate.cfg.Environment.DIALECT, "org.hibernate.dialect.PostgreSQL94Dialect");
        properties.put(Environment.SHOW_SQL, true);
        properties.put(Environment.FORMAT_SQL, true);
        properties.put(Environment.HBM2DDL_AUTO, "create-drop"); // todo only for testing, see: https://docs.jboss.org/hibernate/orm/5.2/userguide/html_single/Hibernate_User_Guide.html#configurations-multi-tenancy
        emfBean.setJpaPropertyMap(properties);
        return emfBean;
    }

    @Bean(name = "tenantTransactionManager")
    public PlatformTransactionManager anotherTransactionManager(
        @Qualifier("tenantEntityManagerFactory")LocalContainerEntityManagerFactoryBean tenantEntityManagerFactory) {
        return new JpaTransactionManager(Objects.requireNonNull(tenantEntityManagerFactory.getObject())); // todo handle not null
    }

    protected String[] packagesToScan() {
        return new String[]{
            "com.ihobb.gm.client"
        };
    }
}
