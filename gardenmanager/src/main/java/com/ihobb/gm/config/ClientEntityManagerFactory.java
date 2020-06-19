package com.ihobb.gm.config;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

//@Configuration
//@EnableJpaRepositories(
//    entityManagerFactoryRef = "clientDataSourceEntityManagerFactory",
//    transactionManagerRef = "clientDataSourceTransactionManager",
//    basePackages = "com.ihobb.gm.client")
public class ClientEntityManagerFactory {

    @Bean
    public LocalContainerEntityManagerFactoryBean clientDataSourceEntityManagerFactory(
        EntityManagerFactoryBuilder builder, @Qualifier("clientDataSource") DataSource dataSource) {

        return builder
            .dataSource(dataSource)
            .packages(packagesToScan())
            .persistenceUnit("ds2-pu") // test
            .properties(hibernateProperties())
            .build();
    }

    @Bean
    public PlatformTransactionManager ds2TransactionManager(@Qualifier("clientDataSourceEntityManagerFactory") EntityManagerFactory clientDataSourceEntityManagerFactory) {
        return new JpaTransactionManager(clientDataSourceEntityManagerFactory);
    }

    protected String[] packagesToScan() {
        return new String[]{
            "com.ihobb.gm.admin"
        };
    }

    protected Map<String, String> hibernateProperties() {
        return new HashMap<String, String>() {
            {
                put("hibernate.dialect", "org.hibernate.dialect.Postgres10Dialect");
                put("hibernate.hbm2ddl.auto", "create-drop");  // for testing
            }
        };
    }

}
