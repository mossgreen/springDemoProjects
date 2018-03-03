package com.moss.demo.petclinic.repository;

import com.moss.demo.petclinic.model.Specialty;
import org.springframework.dao.DataAccessException;

import java.util.Collection;

public interface SpecialtyRepository {

    Specialty findById(int id) throws DataAccessException;

    Collection<Specialty> findAll() throws DataAccessException;

    void save(Specialty specialty) throws DataAccessException;

    void delete(Specialty specialty) throws DataAccessException;

}
