package com.ihobb.gm.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

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
public class ClientDataSourceConfig {

    //todo https://dzone.com/articles/dynamic-multi-tenancy-using-java-spring-boot-sprin

//    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource clientDataSource() {

        if (DynamicDataSourceContextHolder.getDataSourceContext() == null) {
//            return DataSourceBuilder.create()
//                .type(HikariDataSource.class)
//                .build();

            return null;
        } else {
            return DynamicDataSourceContextHolder.getDataSourceContext();
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
            "com.ihobb.gm.client"
        };
    }
}
