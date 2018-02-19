package com.gufeifei.demo.petclinic.owner;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;


/*
* Repository class for Owner.
* All method names are compliant with spring data naming conventions
* so this interface can easily be extended for spring data
* */
public interface OwnerRepository extends Repository<Owner, Integer>{


    /*
    * Retrieve Owners from data store by last name, returning all owners
    * whose last name starts with the given name
    * */
    @Query("SELECT DISTINCT owner FROM Owner owner left join fetch owner.pets WHERE owner.lastName LIKE :lastname%")
    @Transactional(readOnly = true)
    Collection<Owner> findByLastName(@Param("lastName") String lastName);


    /*
    * Retrieve an Owner from the data store by id
    * */
    @Query("SELECT owner FROM Owner owner left join fetch owner.pets WHERE owner.id =:id")
    @Transactional(readOnly = true)
    Owner findById(@Param("id") Integer id);

    /*
    * Save an Owner to the data store, either inserting or updating it
    * */
    void save(Owner owner);

}
