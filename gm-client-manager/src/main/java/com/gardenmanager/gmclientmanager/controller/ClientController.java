package com.gardenmanager.gmclientmanager.controller;

import com.gardenmanager.gmclientmanager.DataSourceConfig;
import com.gardenmanager.gmclientmanager.dto.OrganizationDTO;
import com.gardenmanager.gmclientmanager.utility.DataSourceUtil;
import com.gardenmanager.gmclientmanager.utility.DbConfigProperties;
import lombok.extern.log4j.Log4j2;
import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import javax.validation.constraints.NotNull;
import java.sql.SQLException;
import java.util.Objects;

@Log4j2
@RestController
@RequestMapping("/client")
public class ClientController {

    private final JdbcTemplate jdbcTemplate;
    private final DataSourceProperties properties;

    public ClientController(JdbcTemplate jdbcTemplate, DataSourceProperties properties) {
        this.jdbcTemplate = jdbcTemplate;
        this.properties = properties;
    }

    @PostMapping
    public String addClient(@RequestBody @NotNull OrganizationDTO organizationDTO) throws SQLException {

         String url = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection().getMetaData().getURL();
        log.info("jdbc url: {}", url);
        final String orgCode = organizationDTO.getOrgCode();
        final Integer integer = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM pg_database WHERE datname= '" + orgCode + "'", Integer.class);

        if (Objects.equals(integer, 1)) {
            throw new RuntimeException("db name exists");
        }


        jdbcTemplate.execute("Create DATABASE " + orgCode + " TEMPLATE template0");

        jdbcTemplate.setDatabaseProductName(orgCode);

        url = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection().getMetaData().getURL();
        log.info("jdbc url: {}", url);

        final DbConfigProperties dbConfigProperties = new DbConfigProperties(properties);
        dbConfigProperties.setDbName(orgCode);

        final DataSource dataSource = DataSourceUtil.createAndConfigureDataSource(dbConfigProperties);

        jdbcTemplate.setDataSource(dataSource);
        url = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection().getMetaData().getURL();
        log.info("jdbc url: {}", url);

        Flyway flyway = Flyway.configure().dataSource(dataSource).load();
        flyway.migrate();

        return url;
    }
}
