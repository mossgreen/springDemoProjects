package com.gufeifei.demo.petclinic.owner;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/*Repository class for Pt domain objects ll method names are compliant with spring data naming conventions
* so this interface caneasily be estended for sring data*/
public interface PetRepository extends Repository<Pet, Integer> {

    /**
     * Retrieve all PetTypes from the data store.
     */
    @Query("SELECT ptype FROM PetType ptype ORDER BY ptype.name")
    @Transactional(readOnly = true)
    List<PetType> findPetTypes();

    /**
     * Retrieve a Pet from the data store by Id
     */
    @Transactional(readOnly = true)
    Pet findById(Integer id);

    /**
     * Save a Pet to the data store, either inserting or updating it
     */
    void save(Pet pet);
}
