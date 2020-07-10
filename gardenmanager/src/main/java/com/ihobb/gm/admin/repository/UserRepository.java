package com.ihobb.gm.admin.repository;

import com.ihobb.gm.auth.domain.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<User, Long> {

    @EntityGraph(value = "user-authorities-organizations-graph", type = EntityGraph.EntityGraphType.FETCH)
    List<User> findAllByEmailAndActivatedIsTrue(String email);

    @EntityGraph(value = "user-authorities-organizations-graph", type = EntityGraph.EntityGraphType.FETCH)
    List<User> findAllByNameAndActivatedIsTrue(String username);

}
