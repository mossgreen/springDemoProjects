package com.moss.demo.petclinic.repository.jdbc;


import com.moss.demo.petclinic.model.Owner;
import com.moss.demo.petclinic.model.Pet;
import com.moss.demo.petclinic.model.PetType;
import com.moss.demo.petclinic.model.Visit;
import com.moss.demo.petclinic.repository.OwnerRepository;
import com.moss.demo.petclinic.util.EntityUtils;
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
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Profile("jdbc")
public class JdbcOwnerRepositoryImpl implements OwnerRepository {

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private SimpleJdbcInsert insertOwner;


    @Autowired
    public JdbcOwnerRepositoryImpl(DataSource dataSource) {

        this.insertOwner = new SimpleJdbcInsert(dataSource)
                .withTableName("owners")
                .usingGeneratedKeyColumns("id");

        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public Collection<Owner> findByLastName(String lastName) throws DataAccessException {
        Map<String, Object> params = new HashMap<>();
        params.put("lastName", lastName+"%");
        List<Owner> owners = this.namedParameterJdbcTemplate.query(
                "SELECT id, first_name, last_name, address, city, telephone" +
                        "FROM owners" +
                        "WHERE last_name like :lastName",
                params,
                BeanPropertyRowMapper.newInstance(Owner.class)
        );
        loadOwnersPetsAndVisits(owners);
        return owners;
    }

    @Override
    public Owner findById(int id) throws DataAccessException {
        Owner owner;

        try {
            Map<String, Object> params = new HashMap<>();
            params.put("id", id);
            owner = this.namedParameterJdbcTemplate.queryForObject(
                    "SELECT id, first_name, last_name, address, city, telephone" +
                            "FROM owners" +
                            "WHERE id= :id",
                    params,
                    BeanPropertyRowMapper.newInstance(Owner.class)
            );

        } catch (EmptyResultDataAccessException e) {
            throw new ObjectRetrievalFailureException(Owner.class, id);
        }
        loadPetsAndVisits(owner);
        return owner;
    }

    @Override
    public void save(Owner owner) throws DataAccessException {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(owner);
        if (owner.isNew()) {
            Number newKey = this.insertOwner.executeAndReturnKey(parameterSource);
            owner.setId(newKey.intValue());
        } else {
            this.namedParameterJdbcTemplate.update(
                    "UPDATE owners"+
                            "SET first_name=:firstName, last_name=:lastName, address=:address, city=:city, telephone=:telphone"+
                            "WHERE id=:id",
                    parameterSource
            );
        }
    }

    @Override
    public Collection<Owner> findAll() throws DataAccessException {
        List<Owner> owners = this.namedParameterJdbcTemplate.query(
                "SELECT id, first_name, last_name, address, city, telephone" +
                        "FROM owners",
                new HashMap<String, Object>(),
                BeanPropertyRowMapper.newInstance(Owner.class));

        for (Owner owner : owners) {
            loadPetsAndVisits(owner);
        }

        return owners;
    }

    @Override
    @Transactional
    public void delete(Owner owner) throws DataAccessException {
        Map<String, Object> owner_params = new HashMap<>();
        owner_params.put("id", owner.getId());
        List<Pet> pets = owner.getPets();

        for (Pet pet : pets) {
            Map<String, Object> pet_params = new HashMap<>();
            pet_params.put("id", pet.getId());
            List<Visit> visits = pet.getVisits();
            for (Visit visit : visits) {
                Map<String, Object> visit_params = new HashMap<>();
                visit_params.put("id", visit.getId());
                this.namedParameterJdbcTemplate.update(
                        "DELETE FROM visits" +
                                "WHERE id=:id",
                        visit_params
                );
            }
            this.namedParameterJdbcTemplate.update(
                    "DELETE FROM pets WHERE id=:id",
                    pet_params
            );
        }
        this.namedParameterJdbcTemplate.update(
                "DELETE FROM owners " +
                        "WHERE id=:id",
                owner_params);
    }


    private void loadOwnersPetsAndVisits(List<Owner> owners) {
        for (Owner owner : owners) {
            loadPetsAndVisits(owner);
        }
    }

    public void loadPetsAndVisits(final Owner owner) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", owner.getId());
        final List<JdbcPet> pets = this.namedParameterJdbcTemplate.query(
                "SELECT pets.id, name, birth_date, type_id, owner_id, visits.id as visit_id, visit_date, description, pet_id FROM pets LEFT OUTER JOIN visits ON  pets.id = pet_id WHERE owner_id=:id",
                params,
                new JdbcPetVisitExtractor()
        );
        Collection<PetType> petTypes = getPetTypes();
        for (JdbcPet pet : pets) {
            pet.setType(EntityUtils.getById(petTypes, PetType.class, pet.getTypeId()));
            owner.addPet(pet);
        }
    }

    public Collection<PetType> getPetTypes() throws DataAccessException {
        return this.namedParameterJdbcTemplate.query(
                "SELECT id, name FROM types ORDER BY name", new HashMap<String, Object>(),
                BeanPropertyRowMapper.newInstance(PetType.class));
    }
}
