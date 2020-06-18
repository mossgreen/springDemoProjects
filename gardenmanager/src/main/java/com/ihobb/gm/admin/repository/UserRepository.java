package com.ihobb.gm.admin.repository;

import com.ihobb.gm.admin.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findAllByEmailAndActivatedIsTrue(String email);
}
