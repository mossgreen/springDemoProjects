package com.ihobb.gm.config;

import org.hibernate.MultiTenancyStrategy;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Environment;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class MultiTenantConfig {

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
        DataSource dataSource,
        MultiTenantConnectionProvider multiTenantConnectionProviderImpl,
        CurrentTenantIdentifierResolver currentTenantIdentifierResolverImpl,
        JpaProperties jpaProperties) {

        Map<String, Object> properties = new HashMap<>(jpaProperties.getProperties()); // todo check
//        properties.put(Environment.MULTI_TENANT, MultiTenancyStrategy.SCHEMA); //todo this is for schemas
        properties.put(Environment.MULTI_TENANT, MultiTenancyStrategy.DATABASE);
        properties.put(Environment.MULTI_TENANT_CONNECTION_PROVIDER, multiTenantConnectionProviderImpl);
        properties.put(Environment.MULTI_TENANT_IDENTIFIER_RESOLVER, currentTenantIdentifierResolverImpl);

        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setPersistenceUnitName("TENTANT_ID"); // todo check
        em.setDataSource(dataSource);
        em.setPackagesToScan(packagesToScan());
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        em.setJpaPropertyMap(properties);

        // to read and construct database
//        org.hibernate.cfg.Configuration config = new org.hibernate.cfg.Configuration().configure("/hibernate.cfg.xml");
//        SessionFactory sessionFactory = config.buildSessionFactory();
        return em;
    }

    protected String[] packagesToScan() {
        return new String[]{
            "com.ihobb.gm"
        };
    }

}
