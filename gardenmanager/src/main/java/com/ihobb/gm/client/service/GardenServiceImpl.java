package com.ihobb.gm.client.service;

import com.ihobb.gm.client.domain.Garden;
import com.ihobb.gm.client.repository.GardenRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GardenServiceImpl implements GardenService {

    private final GardenRepository gardenRepository;

    public GardenServiceImpl(GardenRepository gardenRepository) {
        this.gardenRepository = gardenRepository;
    }

    @Override
    public List<Garden> fetchAll() {
        return gardenRepository.findAll();
    }
}
