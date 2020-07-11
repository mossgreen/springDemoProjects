package com.ihobb.gm.admin.controller;


import com.ihobb.gm.admin.domain.Organization;
import com.ihobb.gm.admin.repository.OrganizationRepository;
import com.ihobb.gm.admin.service.OrganizationService;
import com.ihobb.gm.admin.service.UserService;
import com.ihobb.gm.admin.service.UserServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collector;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final OrganizationService organizationService;

    public AdminController(UserService userService, OrganizationService organizationService) {
        this.userService = userService;
        this.organizationService = organizationService;
    }

    @GetMapping
    List<Organization> fetchAllOrganizations() {
        return organizationService.fetchAll();
    }

//    @GetMapping
//    void setCurrentOrg(@PathVariable(name = "id") Long orgId) {
//        Organization organization = organizationRepository.findAll().get(0);
//
//        // dynamic datasource?
//
//    }
}
