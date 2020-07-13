package com.ihobb.gm.admin.service;

import com.ihobb.gm.admin.domain.Organization;
import com.ihobb.gm.admin.repository.OrganizationRepository;
import com.ihobb.gm.config.DbConfigProperties;
import com.ihobb.gm.utility.DataSourceUtil;
import lombok.extern.log4j.Log4j2;
import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@Log4j2
@Service
public class OrganizationServiceImpl implements OrganizationService {

    private final JdbcTemplate jdbcTemplate;
    private final DataSourceProperties properties;
    private final OrganizationRepository organizationRepository;

    public OrganizationServiceImpl(JdbcTemplate jdbcTemplate, DataSourceProperties properties, OrganizationRepository organizationRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.properties = properties;
        this.organizationRepository = organizationRepository;
    }

    @Override
    public Organization fetchOrganizationByCode(String orgCode) {
        return organizationRepository.findByCode(orgCode).orElseThrow(RuntimeException::new);
    }

    @Override
    public List<Organization> fetchAll() {
        return organizationRepository.findAll();
    }

    @Override
    public Organization addOrganization(String description) {

        final Organization org = Organization.builder()
            .name("orgFour")
            .code("ihobdb04")
            .description(description)
            .createdBy(1L)
            .build();

        return organizationRepository.save(org);
    }

    @Override
    public String createOrganization(String orgCode) throws SQLException {

        String url = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection().getMetaData().getURL();
        log.info("jdbc url: {}", url);

        final Integer integer = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM pg_database WHERE datname= '" + orgCode + "'", Integer.class);

        if (Objects.equals(integer, 1)) {
            throw new RuntimeException("db name exists");
        }

        jdbcTemplate.execute("Create DATABASE " + orgCode + " TEMPLATE template0");

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
