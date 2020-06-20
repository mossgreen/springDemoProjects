package com.ihobb.gm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    basePackages = "com.example.multidb",
    entityManagerFactoryRef = "multiEntityManager",
    transactionManagerRef = "multiTransactionManager"
)
public class PersistenceConfig {







    @Bean
    public DataSource dataSource() {
        return new DynamicTenantAwareRoutingDataSource();
    }
}
