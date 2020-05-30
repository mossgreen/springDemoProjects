package com.mossj.springsecurity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeRepo extends JpaRepository<Employee, Long> {

    Optional<Employee> findByName(String name);

}
