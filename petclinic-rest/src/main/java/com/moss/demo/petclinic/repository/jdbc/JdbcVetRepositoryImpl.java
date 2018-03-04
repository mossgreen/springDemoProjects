package com.moss.demo.petclinic.repository.jdbc;


import com.moss.demo.petclinic.model.Specialty;
import com.moss.demo.petclinic.model.Vet;
import com.moss.demo.petclinic.repository.VetRepository;
import com.moss.demo.petclinic.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


@Repository
@Profile("jdbc")
public class JdbcVetRepositoryImpl implements VetRepository {

    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private SimpleJdbcInsert insertVet;

    @Autowired
    public JdbcVetRepositoryImpl(DataSource dataSource, JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertVet = new SimpleJdbcInsert(dataSource).withTableName("vets").usingGeneratedKeyColumns("id");
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    /**
     * Refresh the cache of Vets that the ClinicService is holding.
     */
    @Override
    public Collection<Vet> findAll() throws DataAccessException {
        List<Vet> vets = new ArrayList<>();
        // Retrieve the list of all vets.
        vets.addAll(this.jdbcTemplate.query(
                "SELECT id, first_name, last_name FROM vets ORDER BY last_name,first_name",
                BeanPropertyRowMapper.newInstance(Vet.class)));

        // Retrieve the list of all possible specialties.
        final List<Specialty> specialties = this.jdbcTemplate.query(
                "SELECT id, name FROM specialties",
                BeanPropertyRowMapper.newInstance(Specialty.class));

        // Build each vet's list of specialties.
        for (Vet vet : vets) {
            final List<Integer> vetSpecialtiesIds = this.jdbcTemplate.query(
                    "SELECT specialty_id FROM vet_specialties WHERE vet_id=?",
                    new BeanPropertyRowMapper<Integer>() {
                        @Override
                        public Integer mapRow(ResultSet rs, int row) throws SQLException {
                            return rs.getInt(1);
                        }
                    },
                    vet.getId());
            for (int specialtyId : vetSpecialtiesIds) {
                Specialty specialty = EntityUtils.getById(specialties, Specialty.class, specialtyId);
                vet.addSpecialty(specialty);
            }
        }
        return vets;
    }

    @Override
    public Vet findById(int id) throws DataAccessException {
        Vet vet;
        try {
            Map<String, Object> vet_params = new HashMap<>();
            vet_params.put("id", id);
            vet = this.namedParameterJdbcTemplate.queryForObject(
                    "SELECT id, first_name, last_name FROM vets WHERE id= :id",
                    vet_params,
                    BeanPropertyRowMapper.newInstance(Vet.class));

            final List<Specialty> specialties = this.namedParameterJdbcTemplate.query(
                    "SELECT id, name FROM specialties", vet_params, BeanPropertyRowMapper.newInstance(Specialty.class));

            final List<Integer> vetSpecialtiesIds = this.namedParameterJdbcTemplate.query(
                    "SELECT specialty_id FROM vet_specialties WHERE vet_id=:id",
                    vet_params,
                    new BeanPropertyRowMapper<Integer>() {
                        @Override
                        public Integer mapRow(ResultSet rs, int row) throws SQLException {
                            return rs.getInt(1);
                        }
                    });
            for (int specialtyId : vetSpecialtiesIds) {
                Specialty specialty = EntityUtils.getById(specialties, Specialty.class, specialtyId);
                vet.addSpecialty(specialty);
            }

        } catch (EmptyResultDataAccessException ex) {
            throw new ObjectRetrievalFailureException(Vet.class, id);
        }
        return vet;
    }

    @Override
    public void save(Vet vet) throws DataAccessException {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(vet);
        if (vet.isNew()) {
            Number newKey = this.insertVet.executeAndReturnKey(parameterSource);
            vet.setId(newKey.intValue());
        } else {
            this.namedParameterJdbcTemplate
                    .update("UPDATE vets SET first_name=:firstName, last_name=:lastName WHERE id=:id", parameterSource);
        }
    }

    @Override
    public void delete(Vet vet) throws DataAccessException {
        Map<String, Object> params = new HashMap<>();
        params.put("id", vet.getId());
        this.namedParameterJdbcTemplate.update("DELETE FROM vets WHERE id=:id", params);
    }
}
