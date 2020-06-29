package com.ihobb.gm.config;

import com.ihobb.gm.utility.DataSourceUtil;
import lombok.extern.log4j.Log4j2;
import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Qualifier;
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

    private final DbConfigProperties dataSourceProperties;

    public AdminDataSourceConfig(DbConfigProperties dataSourceProperties) {
        this.dataSourceProperties = dataSourceProperties;
    }

    @Bean
    @Primary
    public DataSource adminDataSource() {
        dataSourceProperties.setDbName("admin");
        return DataSourceUtil.createAndConfigureDataSource(dataSourceProperties);
    }

    @Bean(name = "adminEntityManagerFactory")
    @Primary
    public LocalContainerEntityManagerFactoryBean adminEntityManagerFactory() {

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setDataSource(adminDataSource());
        factory.setPackagesToScan(packagesToScan());
        factory.setPersistenceUnitName("admin-persistence-unit"); // todo
        factory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        factory.setJpaDialect(new HibernateJpaDialect());
        factory.setJpaProperties(hibernateProperties());
        return factory;
    }

    @Bean(name = "adminTransactionManager")
    public JpaTransactionManager adminTransactionManager(
        @Qualifier("adminEntityManagerFactory") EntityManagerFactory emf) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);
        return transactionManager;
    }

    @Bean
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
        properties.put(Environment.HBM2DDL_AUTO, "create-drop"); // todo moss only for testing,
        return properties;
    }
}
