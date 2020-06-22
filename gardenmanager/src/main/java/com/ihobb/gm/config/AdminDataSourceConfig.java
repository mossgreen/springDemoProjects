package com.ihobb.gm.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
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
    basePackages = "com.ihobb.gm.admin",
    entityManagerFactoryRef = "adminEntityManager",
    transactionManagerRef = "adminTransactionManager"
)
public class AdminDataSourceConfig {

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.admin.datasource")
    public DataSource adminDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "adminEntityManager")
    @Primary
    public LocalContainerEntityManagerFactoryBean adminEntityManagerFactory() {

        LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        localContainerEntityManagerFactoryBean.setDataSource(adminDataSource());
        localContainerEntityManagerFactoryBean.setPackagesToScan(packagesToScan());
        localContainerEntityManagerFactoryBean.setPersistenceUnitName("adminPersistenceUnit"); // todo
        localContainerEntityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        localContainerEntityManagerFactoryBean.setJpaDialect(new HibernateJpaDialect());
        localContainerEntityManagerFactoryBean.setJpaPropertyMap(hibernateProperties());
        return localContainerEntityManagerFactoryBean;
    }

    @Bean(name = "adminTransactionManager")
    public PlatformTransactionManager anotherTransactionManager( LocalContainerEntityManagerFactoryBean adminEntityManager) {
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
            "com.ihobb.gm.admin"
        };
    }
}
