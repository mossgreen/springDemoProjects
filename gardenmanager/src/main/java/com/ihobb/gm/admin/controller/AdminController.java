package com.ihobb.gm.admin.controller;


import com.ihobb.gm.admin.domain.Organization;
import com.ihobb.gm.admin.repository.OrganizationRepository;
import com.ihobb.gm.admin.service.AdminUserService;
import com.ihobb.gm.admin.service.AdminUserServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final AdminUserService adminUserService;
    private final OrganizationRepository organizationRepository;

    public AdminController(AdminUserServiceImpl userService, OrganizationRepository organizationRepository) {
        this.adminUserService = userService;
        this.organizationRepository = organizationRepository;
    }

    @GetMapping
    List<Organization> fetchAllOrganizations() {

        return organizationRepository.findAll();
    }

//    @GetMapping
//    void setCurrentOrg(@PathVariable(name = "id") Long orgId) {
//        Organization organization = organizationRepository.findAll().get(0);
//
//        // dynamic datasource?
//
//    }
}
