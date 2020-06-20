package com.ihobb.gm.admin.controller;


import com.ihobb.gm.admin.domain.Organization;
import com.ihobb.gm.admin.domain.User;
import com.ihobb.gm.admin.repository.OrganizationRepository;
import com.ihobb.gm.admin.repository.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AdminController {
    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;

    public AdminController(UserRepository userRepository, OrganizationRepository organizationRepository) {
        this.userRepository = userRepository;
        this.organizationRepository = organizationRepository;
    }

    @GetMapping
    List<User> fetchAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping
    void setCurrentOrg(@PathVariable(name = "id") Long orgId) {
        Organization organization = organizationRepository.findAll().get(0);

        // dynamic datasource?

    }
}
