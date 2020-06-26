package com.ihobb.gm.config;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Log4j2
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    basePackages = "com.ihobb.gm.admin",
    entityManagerFactoryRef = "adminEntityManagerFactory",
    transactionManagerRef = "adminTransactionManager"
)
public class AdminDataSourceConfig {

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource adminDataSource() {
        return DataSourceBuilder.create()
            .type(HikariDataSource.class)
            .build();
    }

    @Bean(name = "adminEntityManagerFactory")
    @Primary
    public LocalContainerEntityManagerFactoryBean adminEntityManagerFactory() {

        LocalContainerEntityManagerFactoryBean entityManagerFactor = new LocalContainerEntityManagerFactoryBean();

        final DatabaseConfigProperties db = DatabaseConfigProperties.builder()
            .dbName("admin")
            .url("jdbc:postgresql://127.0.0.1:5432/admin")
            .build();
        entityManagerFactor.setDataSource(adminDataSource());
        entityManagerFactor.setPackagesToScan(packagesToScan());
        entityManagerFactor.setPersistenceUnitName("admin-persistence-unit"); // todo
        entityManagerFactor.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        entityManagerFactor.setJpaDialect(new HibernateJpaDialect());
        entityManagerFactor.setJpaProperties(hibernateProperties());
        return entityManagerFactor;
    }

    @Bean(name = "adminTransactionManager")
    public JpaTransactionManager adminTransactionManager(@Qualifier("adminEntityManager") EntityManagerFactory emf) {

        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);
        return transactionManager;
    }

    @Bean // todo why we need this
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    protected String[] packagesToScan() {
        return new String[]{
            "com.ihobb.gm.admin"
        };
    }

    //Hibernate configuration properties
    private Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.put(org.hibernate.cfg.Environment.DIALECT, "org.hibernate.dialect.PostgreSQL94Dialect");
        properties.put(org.hibernate.cfg.Environment.SHOW_SQL, true);
        properties.put(org.hibernate.cfg.Environment.FORMAT_SQL, true);
        properties.put(org.hibernate.cfg.Environment.HBM2DDL_AUTO, "none");
        return properties;
    }
}
