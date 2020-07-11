package com.ihobb.gm.client.controller;

import com.ihobb.gm.client.domain.Garden;
import com.ihobb.gm.client.repository.GardenRepository;
import com.ihobb.gm.client.service.GardenService;
import com.ihobb.gm.client.service.GardenServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/garden")
public class GardenController {

    private final GardenService gardenService;

    public GardenController(GardenService gardenService) {
        this.gardenService = gardenService;
    }

    @GetMapping
    public List<Garden> fetchGardens() {
        return gardenService.fetchAll();
    }
}
