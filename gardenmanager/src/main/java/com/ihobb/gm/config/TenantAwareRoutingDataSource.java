package com.ihobb.gm.config;

import com.ihobb.gm.DbConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class TenantAwareRoutingDataSource extends AbstractRoutingDataSource {

//    private final Map<String, HikariDataSource> tenants;

    public TenantAwareRoutingDataSource() {
        // default constructor, use default data source
        this("admin");
    }

    public TenantAwareRoutingDataSource(String databaseName)  {
        try {
            final HikariDataSource dataSource = buildDataSource(databaseName);
            DynamicDataSourceContextHolder.setDataSourceContext(dataSource);
        } catch (Exception e) {

            try {
                // todo create database and run flyway, maybe I want a queue here, RabbitMQ?
                DbConfig dbConfig = new DbConfig();
                dbConfig.createDb(databaseName);
                final HikariDataSource dataSource = buildDataSource(databaseName);
                DynamicDataSourceContextHolder.setDataSourceContext(dataSource);
            } catch (Exception e1) {
                throw new RuntimeException("nonono"); //todo
            }
        }
    }

    public HikariDataSource buildDataSource(String dataBaseName) {

        Properties datasourceProperties = new Properties();
        datasourceProperties.put("url", "jdbc:postgresql://127.0.0.1:5432" + dataBaseName);
        datasourceProperties.put("user", "postgres");
        datasourceProperties.put("password", "ihobb");
        datasourceProperties.put("driverClassName", "org.postgresql.Driver");

        Properties hikariConfigProperties = new Properties();

        hikariConfigProperties.put("poolName","ProjectsDBPool");
        hikariConfigProperties.put("jdbcUrl", "jdbc:postgresql://127.0.0.1:5432/" + dataBaseName);
        hikariConfigProperties.put("driverClassName","org.postgresql.Driver");

        hikariConfigProperties.put("maximumPoolSize", 5);
        hikariConfigProperties.put("idleTimeout",     50000);
        hikariConfigProperties.put("dataSourceProperties", datasourceProperties);

        HikariConfig hc = new HikariConfig(hikariConfigProperties);
        return new HikariDataSource(hc);
    }

    @Override
    public void afterPropertiesSet() {
        // todo sth needs to do
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return DynamicDataSourceContextHolder.getDataSourceContext();
    }

    @Override
    protected DataSource determineTargetDataSource() {
        return (DataSource) determineCurrentLookupKey();
    }

    public void initDatasource() {
        Map<Object, Object> dataSourceMap = new HashMap<>();
        DataSource adminDataSource = buildDataSource("admin");
        dataSourceMap.put("admin", adminDataSource);
        this.setTargetDataSources(dataSourceMap);
        this.setDefaultTargetDataSource(adminDataSource);
    }
}
