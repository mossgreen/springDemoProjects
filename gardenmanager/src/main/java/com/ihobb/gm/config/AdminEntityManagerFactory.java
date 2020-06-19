package com.ihobb.gm.config;


import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

//@Configuration
//@EnableJpaRepositories(
//    entityManagerFactoryRef = "adminDataSourceEntityManagerFactory",
//    transactionManagerRef = "adminDataSourceTransactionManager",
//    basePackages = "com.ihobb.gm.admin" )
//@EnableTransactionManagement
public class AdminEntityManagerFactory {

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean adminDataSourceEntityManagerFactory(
        EntityManagerFactoryBuilder builder, @Qualifier("adminDataSource") DataSource dataSource ) {

        return builder
            .dataSource(dataSource)
            .packages(packagesToScan())
            .persistenceUnit("ds1-pu") // test
            .properties(hibernateProperties())
            .build();
    }

    @Bean
    @Primary
    public PlatformTransactionManager ds1TransactionManager( @Qualifier("adminDataSourceEntityManagerFactory") EntityManagerFactory adminDataSourceEntityManagerFactory) {
        return new JpaTransactionManager(adminDataSourceEntityManagerFactory);
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
