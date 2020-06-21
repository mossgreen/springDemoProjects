package com.ihobb.gm.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    basePackages = "com.ihobb.gm",
    entityManagerFactoryRef = "entityManager",
    transactionManagerRef = "transactionManager"
)
public class DataSourceConfig {


    @Bean
    @Primary
    public DataSource dataSource() {
        DynamicTenantAwareRoutingDataSource routingDataSource = new DynamicTenantAwareRoutingDataSource();
        routingDataSource.initDatasource();
        return routingDataSource;
    }

    @Bean(name = "entityManager")
    @Primary
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(EntityManagerFactoryBuilder builder) {

        return builder
            .dataSource(dataSource())
            .packages(packagesToScan())
            .persistenceUnit("ds1-pu") // test
            .properties(hibernateProperties())
            .build();
    }

    @Bean(name = "transactionManager")
    @Primary
    public JpaTransactionManager transactionManager(
        @Qualifier("entityManager") LocalContainerEntityManagerFactoryBean entityManagerFactoryBean) {
        return new JpaTransactionManager(entityManagerFactoryBean.getObject()); //todo might be null
    }

    protected Map<String, String> hibernateProperties() {
        return new HashMap<String, String>() {
            {
                put("hibernate.dialect", "org.hibernate.dialect.PostgreSQL94Dialect");
                put("hibernate.hbm2ddl.auto", "create-drop");  // for testing
            }
        };
    }

    protected String[] packagesToScan() {
        return new String[]{
            "com.ihobb.gm.admin"
        };
    }
}
