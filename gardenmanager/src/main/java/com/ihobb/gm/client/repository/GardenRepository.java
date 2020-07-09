package com.ihobb.gm.client.repository;

import com.ihobb.gm.client.domain.Garden;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GardenRepository extends JpaRepository<Garden, Long> {
}
