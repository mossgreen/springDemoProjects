package com.ihobb.gm.admin.repository;

import com.ihobb.gm.auth.domain.Authority;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository extends CrudRepository<Authority, String> {
}
