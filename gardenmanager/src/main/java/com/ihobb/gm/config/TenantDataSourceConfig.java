package com.ihobb.gm.config;

import org.hibernate.MultiTenancyStrategy;
import org.hibernate.cfg.Environment;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.hibernate.engine.jdbc.connections.spi.DataSourceBasedMultiTenantConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Configuration
@EnableJpaRepositories(
    basePackages = "com.ihobb.gm.client",
    entityManagerFactoryRef = "clientEntityManager",
    transactionManagerRef = "clientTransactionManager"
)
@EnableTransactionManagement
public class TenantDataSourceConfig {

    @Bean(name = "tenantTransactionManager")
    public JpaTransactionManager transactionManager(@Qualifier("tenantEntityManagerFactory") EntityManagerFactory tenantEntityManager) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(tenantEntityManager);
        return transactionManager;
    }

    @Bean(name = "datasourceBasedMultitenantConnectionProvider")
    @ConditionalOnBean(name = "masterEntityManagerFactory")
    public MultiTenantConnectionProvider multiTenantConnectionProvider() {
        return new DataSourceBasedMultiTenantConnectionProviderImpl();
    }

    @Bean(name = "currentTenantIdentifierResolver")
    public CurrentTenantIdentifierResolver currentTenantIdentifierResolver() {
        return new CurrentTenantIdentifierResolverImpl();
    }

    @Bean(name = "tenantEntityManagerFactory")
    @ConditionalOnBean(name = "datasourceBasedMultitenantConnectionProvider")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
        @Qualifier("datasourceBasedMultitenantConnectionProvider") MultiTenantConnectionProvider connectionProvider,
        @Qualifier("currentTenantIdentifierResolver") CurrentTenantIdentifierResolver tenantResolver) {

        LocalContainerEntityManagerFactoryBean emfBean = new LocalContainerEntityManagerFactoryBean();
        //All tenant related entities, repositories and service classes must be scanned
        emfBean.setPackagesToScan("com.amran.dynamic.multitenant");
        emfBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        emfBean.setPersistenceUnitName("tenantdb-persistence-unit");
        Map<String, Object> properties = new HashMap<>();
        properties.put(Environment.MULTI_TENANT, MultiTenancyStrategy.DATABASE);
        properties.put(Environment.MULTI_TENANT_CONNECTION_PROVIDER, connectionProvider);
        properties.put(Environment.MULTI_TENANT_IDENTIFIER_RESOLVER, tenantResolver);
        properties.put(Environment.DIALECT, "org.hibernate.dialect.MySQL5Dialect");
        properties.put(Environment.SHOW_SQL, true);
        properties.put(Environment.FORMAT_SQL, true);
        properties.put(Environment.HBM2DDL_AUTO, "none");
        emfBean.setJpaPropertyMap(properties);
        return emfBean;
    }


    //    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource clientDataSource() {

        if (TenantContextHolder.getDataSourceContext() == null) {
//            return DataSourceBuilder.create()
//                .type(HikariDataSource.class)
//                .build();

            return null;
        } else {
            return TenantContextHolder.getDataSourceContext();
        }
    }

    @Bean(name = "clientEntityManager")
    public LocalContainerEntityManagerFactoryBean adminEntityManagerFactory() {

        LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        localContainerEntityManagerFactoryBean.setDataSource(clientDataSource());
        localContainerEntityManagerFactoryBean.setPackagesToScan(packagesToScan());
        localContainerEntityManagerFactoryBean.setPersistenceUnitName("clientPersistenceUnit"); // todo
        localContainerEntityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        localContainerEntityManagerFactoryBean.setJpaDialect(new HibernateJpaDialect());
        localContainerEntityManagerFactoryBean.setJpaPropertyMap(hibernateProperties());
        return localContainerEntityManagerFactoryBean;
    }

    @Bean(name = "clientTransactionManager")
    public PlatformTransactionManager anotherTransactionManager(LocalContainerEntityManagerFactoryBean adminEntityManager) {
        return new JpaTransactionManager(Objects.requireNonNull(adminEntityManager.getObject())); // todo handle not null
    }


    protected Map<String, String> hibernateProperties() {
        return new HashMap<String, String>() {
            {
                put("hibernate.dialect", "org.hibernate.dialect.PostgreSQL94Dialect");
                put("hibernate.hbm2ddl.auto", "create-drop");  // todo for testing
                put("hibernate.temp.use_jdbc_metadata_defaults", "false");
                put("hibernate.jdbc.lob.non_contextual_creation", "true");
            }
        };
    }

    protected String[] packagesToScan() {
        return new String[]{
            "com.ihobb.gm.client"
        };
    }
}
