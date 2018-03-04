package com.moss.demo.petclinic.repository.jdbc;

import com.moss.demo.petclinic.model.Owner;
import com.moss.demo.petclinic.model.Pet;
import com.moss.demo.petclinic.model.PetType;
import com.moss.demo.petclinic.model.Visit;
import com.moss.demo.petclinic.repository.OwnerRepository;
import com.moss.demo.petclinic.repository.PetRepository;
import com.moss.demo.petclinic.repository.VisitRepository;
import com.moss.demo.petclinic.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.*;


@Repository
@Profile("jdbc")
public class JdbcPetRepositoryImpl implements PetRepository{


    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SimpleJdbcInsert insertPet;
    private final OwnerRepository ownerRepository;
    private final VisitRepository visitRepository;

    @Autowired
    public JdbcPetRepositoryImpl(DataSource dataSource,
                                 OwnerRepository ownerRepository,
                                 VisitRepository visitRepository) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);

        this.insertPet = new SimpleJdbcInsert(dataSource)
                .withTableName("pets")
                .usingGeneratedKeyColumns("id");

        this.ownerRepository = ownerRepository;
        this.visitRepository = visitRepository;
    }


    @Override
    public List<PetType> findPetTypes() throws DataAccessException {
        Map<String, Object> params = new HashMap<>();
        return this.namedParameterJdbcTemplate.query(
                "SELECT id, name FROM types ORDER BY name",
                params,
                BeanPropertyRowMapper.newInstance(PetType.class));
    }

    @Override
    public Pet findById(int id) throws DataAccessException {
        Integer ownerId;

        try {
            Map<String, Object> params = new HashMap<>();
            params.put("id", id);
            ownerId = this.namedParameterJdbcTemplate.queryForObject(
                    "SELECT owner_id FROM pets WHERE id=:id",
                    params,
                    Integer.class);
        } catch (EmptyResultDataAccessException ex) {
            throw new ObjectRetrievalFailureException(Pet.class, id);
        }

        Owner owner = this.ownerRepository.findById(ownerId);
        return EntityUtils.getById(owner.getPets(), Pet.class, id);
    }

    @Override
    public void save(Pet pet) throws DataAccessException {
        if (pet.isNew()) {
            Number newKey = this.insertPet.executeAndReturnKey(createPetParameterSource(pet));
            pet.setId(newKey.intValue());
        } else {
            this.namedParameterJdbcTemplate.update(
                    "UPDATE pets " +
                            "SET name=:name, birth_date=:birth_date, type_id=:type_id, owner_id=:owner_id" +
                            "WHERE id=:id",
                    createPetParameterSource(pet)
            );

        }
    }

    private MapSqlParameterSource createPetParameterSource(Pet pet) {
        return new MapSqlParameterSource()
                .addValue("id", pet.getId())
                .addValue("name", pet.getName())
                .addValue("birth_date", pet.getBirthDate())
                .addValue("type_id", pet.getType().getId())
                .addValue("owner_id", pet.getOwner().getId());

    }

    @Override
    public Collection<Pet> findAll() throws DataAccessException {

        Map<String, Object> params = new HashMap<>();
        Collection<Pet> pets = new ArrayList<>();
        Collection<JdbcPet> jdbcPets = new ArrayList<>();

        jdbcPets = this.namedParameterJdbcTemplate.query(
                "SELECT petid, name, birth_date, type_id, ownerId " +
                        "FROM pets",
                params,
                new JdbcPetRowMapper());

        Collection<PetType> petTypes = this.namedParameterJdbcTemplate.query(
                "SELECT id, name" +
                        "FROM types" +
                        "ORDER BY name",
                new HashMap<String, Object>(),
                BeanPropertyRowMapper.newInstance(PetType.class));

        Collection<Owner> owners = this.namedParameterJdbcTemplate.query(
                "SELECT id, first_name, last_name, address, city, telephone " +
                        "FROM owners " +
                        "ORDER BY last_name",
                new HashMap<String, Object>(),
                BeanPropertyRowMapper.newInstance(Owner.class));

        for (JdbcPet jdbcPet : jdbcPets) {
            jdbcPet.setType(EntityUtils.getById(petTypes, PetType.class, jdbcPet.getTypeId()));
            jdbcPet.setOwner(EntityUtils.getById(owners, Owner.class, jdbcPet.getOwnerId()));
        }

        return pets;
    }

    @Override
    public void delete(Pet pet) throws DataAccessException {
        Map<String, Object> pet_params = new HashMap<>();
        pet_params.put("id", pet.getId());
        List<Visit> visits = pet.getVisits();
        // cascade delete visits
        for (Visit visit : visits) {
            Map<String, Object> visit_params = new HashMap<>();
            visit_params.put("id", visit.getId());
            this.namedParameterJdbcTemplate.update("DELETE FROM visits WHERE id=:id", visit_params);
        }
        this.namedParameterJdbcTemplate.update("DELETE FROM pets WHERE id=:id", pet_params);
    }
}
