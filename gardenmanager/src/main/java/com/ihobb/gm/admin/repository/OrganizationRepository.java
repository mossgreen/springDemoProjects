package com.ihobb.gm.admin.repository;

import com.ihobb.gm.admin.domain.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {
}
