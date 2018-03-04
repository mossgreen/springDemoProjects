package com.moss.demo.petclinic.repository.jdbc;

import com.moss.demo.petclinic.model.Specialty;
import com.moss.demo.petclinic.repository.SpecialtyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Repository
@Profile("jdbc")
public class JdbcSpecialtyRepositoryImpl implements SpecialtyRepository{

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private SimpleJdbcInsert insertSpecialty;

    @Autowired
    public JdbcSpecialtyRepositoryImpl(DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.insertSpecialty = new SimpleJdbcInsert(dataSource)
                .withTableName("specialities")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Specialty findById(int id) throws DataAccessException {
        Specialty specialty;
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("id", id);
            specialty = this.namedParameterJdbcTemplate.queryForObject(
                    "SELECT id, name FROM specialities " +
                            "WHERE id=:id",
                    params,
                    BeanPropertyRowMapper.newInstance(Specialty.class)
            );
        } catch (EmptyResultDataAccessException ex) {
            throw new ObjectRetrievalFailureException(Specialty.class, id);
        }
        return specialty;
    }

    @Override
    public Collection<Specialty> findAll() throws DataAccessException {
        Map<String, Object> params = new HashMap<>();
        return this.namedParameterJdbcTemplate.query(
                "SELECT id, name" +
                        "FROM specialties",
                params,
                BeanPropertyRowMapper.newInstance(Specialty.class)
        );
    }

    @Override
    public void save(Specialty specialty) throws DataAccessException {

        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(specialty);
        if (specialty.isNew()) {
            Number newKey = this.insertSpecialty.executeAndReturnKey(parameterSource);
            specialty.setId(newKey.intValue());
        } else {
            this.namedParameterJdbcTemplate.update(
                    "UPDATE specialties " +
                            "SET name=:name" +
                            "WHERE id=:id",
                    parameterSource
            );
        }
    }

    @Override
    public void delete(Specialty specialty) throws DataAccessException {
        Map<String, Object> params = new HashMap<>();
        params.put("id", specialty.getId());
        this.namedParameterJdbcTemplate.update("DELETE FROM vet_specialties WHERE specialty_id=:id", params);
        this.namedParameterJdbcTemplate.update("DELETE FROM specialties WHERE id=:id", params);
    }
}
